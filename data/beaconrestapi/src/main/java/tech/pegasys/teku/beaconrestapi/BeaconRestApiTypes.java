/*
 * Copyright 2022 ConsenSys AG.
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

package tech.pegasys.teku.beaconrestapi;

import static tech.pegasys.teku.infrastructure.http.RestApiConstants.COMMITTEE_INDEX;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.COMMITTEE_INDEX_QUERY_DESCRIPTION;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.PARAM_BLOCK_ID;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.PARAM_BLOCK_ID_DESCRIPTION;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.PARAM_STATE_ID;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.PARAM_STATE_ID_DESCRIPTION;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.PARAM_VALIDATOR_DESCRIPTION;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.PARAM_VALIDATOR_ID;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.PARENT_ROOT;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.SLOT;
import static tech.pegasys.teku.infrastructure.http.RestApiConstants.SLOT_QUERY_DESCRIPTION;

import org.apache.tuweni.bytes.Bytes32;
import tech.pegasys.teku.infrastructure.json.types.CoreTypes;
import tech.pegasys.teku.infrastructure.restapi.endpoints.ParameterMetadata;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;

public class BeaconRestApiTypes {
  public static final ParameterMetadata<String> PARAMETER_STATE_ID =
      new ParameterMetadata<>(PARAM_STATE_ID, CoreTypes.string(PARAM_STATE_ID_DESCRIPTION, "head"));

  public static final ParameterMetadata<String> PARAMETER_VALIDATOR_ID =
      new ParameterMetadata<>(
          PARAM_VALIDATOR_ID, CoreTypes.string(PARAM_VALIDATOR_DESCRIPTION, "1"));

  public static final ParameterMetadata<String> PARAMETER_BLOCK_ID =
      new ParameterMetadata<>(PARAM_BLOCK_ID, CoreTypes.string(PARAM_BLOCK_ID_DESCRIPTION, "head"));

  public static final ParameterMetadata<UInt64> SLOT_PARAMETER =
      new ParameterMetadata<>(SLOT, CoreTypes.UINT64_TYPE.withDescription(SLOT_QUERY_DESCRIPTION));

  public static final ParameterMetadata<UInt64> COMMITTEE_INDEX_PARAMETER =
      new ParameterMetadata<>(
          COMMITTEE_INDEX,
          CoreTypes.UINT64_TYPE.withDescription(COMMITTEE_INDEX_QUERY_DESCRIPTION));

  public static final ParameterMetadata<Bytes32> PARENT_ROOT_PARAMETER =
      new ParameterMetadata<>(
          PARENT_ROOT, CoreTypes.BYTES32_TYPE.withDescription("Not currently supported."));
}