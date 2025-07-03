package com.hedera.node.app.hapi.fees.apis.common;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.MAX_KEYS;
import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.MIN_KEYS;

public class EntityCreate extends AbstractFeeModel {
    private final String service;
    private final FeeApi api;
    private final String description;
    private final int numFreeKeys;
    private final boolean customFeeCapable;

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numKeys", "number", null, MIN_KEYS, MIN_KEYS, MAX_KEYS, "Number of keys")
    );
    private final List<ParameterDefinition> customFeeParams = List.of(
            new ParameterDefinition("hasCustomFee", "list", new Object[] {YesOrNo.YES, YesOrNo.NO}, YesOrNo.NO, 0, 0, "Enable custom fee?")
    );

    public EntityCreate(String service, FeeApi api, String description, int numFreeKeys, boolean customFeeCapable) {
        this.service = service;
        this.api = api;
        this.description = description;
        this.numFreeKeys = numFreeKeys;
        this.customFeeCapable = customFeeCapable;
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        if (customFeeCapable) {
            return Stream.concat(params.stream(), customFeeParams.stream()).collect(Collectors.toList());
        }
        return params;
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        super.setNumFreeSignatures(numFreeKeys + 1); // The user needs to sign each of the keys to verify that they have the corresponding private key

        FeeResult fee = new FeeResult();

        fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee(api));

        if (customFeeCapable && values.get("hasCustomFee") == YesOrNo.YES) {
            FeeApi customApi = switch(api) {
                case ConsensusCreateTopic -> FeeApi.ConsensusCreateTopicCustomFeeSurcharge;
                case ConsensusSubmitMessage -> FeeApi.ConsensusSubmitMessageCustomFeeSurcharge;
                case TokenCreate -> FeeApi.TokenCreateCustomFeeSurcharge;
                case TokenTransfer -> FeeApi.TokenTransferCustomFeeSurcharge;
                case TokenAirdrop -> FeeApi.TokenAirdropCustomFeeSurcharge;
                default -> throw new RuntimeException("Custom fee not specified for API: " + api);
            };
            fee.addDetail("Custom fee", 1, BaseFeeRegistry.getBaseFee(customApi));
        }

        int numKeys = (int) values.get("numKeys");

        if (numKeys > numFreeKeys) {
            fee.addDetail("Additional keys", numKeys - numFreeKeys, (numKeys - numFreeKeys) * BaseFeeRegistry.getBaseFee(FeeApi.PerKey));
        }
        return fee;
    }
}

