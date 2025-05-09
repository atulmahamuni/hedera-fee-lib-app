package com.hedera.node.app.hapi.fees.apis.consensus;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;

import java.util.List;
import java.util.Map;

public class HCSCreate extends AbstractFeeModel {

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("hasCustomFee", "boolean", false, 0, 0, "Does this topic have custom fee")
    );

    @Override
    public String getDescription() {
        return "Create a new HCS topic";
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        return params;
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();

        boolean hasCustomFee = (boolean) values.get("hasCustomFee");
        if (hasCustomFee == false) {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("ConsensusCreateTopic"));
        } else {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("ConsensusCreateTopicWithCustomFee"));
        }
        return fee;
    }
}
