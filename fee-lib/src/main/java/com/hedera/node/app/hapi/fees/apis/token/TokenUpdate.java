package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;

import java.util.List;
import java.util.Map;

public class TokenUpdate extends AbstractFeeModel {
    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numKeys", "number", 1, 1, 50, "Number of keys")
    );

    @Override
    public String getService() {
        return "Token";
    }

    @Override
    public String getDescription() {
        return "Update an existing token type";
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        return params;
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();


        fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("TokenUpdate"));

        int numKeys = (int) values.get("numKeys");
        // First 7 keys are included in the base fee: adminKey, kycKey, freezeKey, wipeKey, supplyKey, feeScheduleKey, pauseKey
        final int numFreeKeys = 7;
        if (numKeys > numFreeKeys) {
            fee.addDetail("Additional keys", numKeys - numFreeKeys, (numKeys - numFreeKeys) * BaseFeeRegistry.getBaseFee("PerKey"));
        }

        return fee;
    }
}
