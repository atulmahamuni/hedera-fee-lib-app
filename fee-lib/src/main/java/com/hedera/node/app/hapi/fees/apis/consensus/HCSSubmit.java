package com.hedera.node.app.hapi.fees.apis.consensus;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;

import java.util.List;
import java.util.Map;

public class HCSSubmit extends AbstractFeeModel {
    final int NUM_FREE_HCS_BYTES = 128;

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("hasCustomFee", "boolean", false, 0, 0, "Does this topic have custom fee"),
            new ParameterDefinition("numBytes", "number", 128, 1, 1024, "Size of the message (bytes)")
    );

    @Override
    public String getDescription() {
        return "Submit a message to an existing topic";
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
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("ConsensusSubmitMessage"));
        } else {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("ConsensusSubmitMessageWithCustomFee"));
        }
        int numBytes = (int) values.get("numBytes");
        if (numBytes > NUM_FREE_HCS_BYTES) {
            fee.addDetail("Fee based on message size", numBytes, (numBytes - NUM_FREE_HCS_BYTES) * BaseFeeRegistry.getBaseFee("PerHCSByte"));
        }
        return fee;
    }
}
