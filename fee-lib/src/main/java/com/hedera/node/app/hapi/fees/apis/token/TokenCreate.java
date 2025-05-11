package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;

import java.util.List;
import java.util.Map;

public class TokenCreate extends AbstractFeeModel {
    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numKeys", "number", 1, 1, 50, "Number of keys"),
            new ParameterDefinition("hasCustomFee", "boolean", false, 0, 0, "Does this token have custom fee")
    );

    @Override
    public String getService() {
        return "Token";
    }

    @Override
    public String getDescription() {
        return "Create a new token type";
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
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("TokenCreate"));
        } else {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("TokenCreateWithCustomFee"));
        }

        int numKeys = (int) values.get("numKeys");
        // First 7 keys are included in the base fee: adminKey, kycKey, freezeKey, wipeKey, supplyKey, feeScheduleKey, pauseKey
        final int numFreeKeys = 7;
        if (numKeys > numFreeKeys) {
            fee.addDetail("Additional keys", numKeys - numFreeKeys, (numKeys - numFreeKeys) * BaseFeeRegistry.getBaseFee("PerKey"));
        }

        return fee;
    }
}
