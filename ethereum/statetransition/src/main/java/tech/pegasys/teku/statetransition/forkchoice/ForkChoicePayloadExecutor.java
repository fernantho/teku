/*
 * Copyright 2021 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.statetransition.forkchoice;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.pegasys.teku.infrastructure.async.SafeFuture;
import tech.pegasys.teku.spec.Spec;
import tech.pegasys.teku.spec.datastructures.blocks.SignedBeaconBlock;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayload;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayloadHeader;
import tech.pegasys.teku.spec.executionengine.ExecutionEngineChannel;
import tech.pegasys.teku.spec.executionengine.PayloadStatus;
import tech.pegasys.teku.spec.logic.versions.bellatrix.block.OptimisticExecutionPayloadExecutor;
import tech.pegasys.teku.storage.client.RecentChainData;

class ForkChoicePayloadExecutor implements OptimisticExecutionPayloadExecutor {
  private static final Logger LOG = LogManager.getLogger();

  private final ExecutionEngineChannel executionEngine;
  private final MergeTransitionBlockValidator transitionBlockValidator;
  private Optional<SafeFuture<PayloadStatus>> result = Optional.empty();

  ForkChoicePayloadExecutor(
      final ExecutionEngineChannel executionEngine,
      final MergeTransitionBlockValidator transitionBlockValidator) {
    this.transitionBlockValidator = transitionBlockValidator;
    this.executionEngine = executionEngine;
  }

  public static ForkChoicePayloadExecutor create(
      final Spec spec,
      final RecentChainData recentChainData,
      final SignedBeaconBlock block,
      final ExecutionEngineChannel executionEngine) {
    return new ForkChoicePayloadExecutor(
        executionEngine,
        new MergeTransitionBlockValidator(spec, recentChainData, executionEngine, block));
  }

  public SafeFuture<PayloadStatus> getExecutionResult() {
    return result.orElse(SafeFuture.completedFuture(PayloadStatus.VALID));
  }

  @Override
  public boolean optimisticallyExecute(
      final ExecutionPayloadHeader latestExecutionPayloadHeader,
      final ExecutionPayload executionPayload) {
    if (executionPayload.isDefault()) {
      // We're still pre-merge so no payload to execute
      // Note that the BlockProcessor will have already failed if this is default and shouldn't be
      // because it checks the parentRoot matches
      return true;
    }

    result =
        Optional.of(
            executionEngine
                .newPayload(executionPayload)
                .thenCompose(
                    result -> {
                      if (result.hasValidStatus()) {
                        return transitionBlockValidator.verifyTransitionBlock(
                            latestExecutionPayloadHeader, executionPayload);
                      } else {
                        return SafeFuture.completedFuture(result);
                      }
                    })
                .exceptionally(
                    error -> {
                      LOG.error("Error while validating payload", error);
                      return PayloadStatus.failedExecution(error);
                    }));

    return true;
  }
}