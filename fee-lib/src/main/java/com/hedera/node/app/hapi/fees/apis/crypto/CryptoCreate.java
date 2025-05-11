package com.hedera.node.app.hapi.fees.apis.crypto;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;

import java.util.List;
import java.util.Map;

public class CryptoCreate extends AbstractFeeModel {
    private String service = "Crypto";

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numKeys", "number", null, 1, 1, 50, "Number of keys")
    );

    @Override
    public String getService() {
        return "Crypto";
    }

    @Override
    public String getDescription() {
        return "Create a crypto new account";
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        return params;
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();
        fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("CryptoCreate"));

        int numKeys = (int) values.get("numKeys");
        if (numKeys > 1) {
            fee.addDetail("Additional keys", numKeys - 1, (numKeys - 1) * BaseFeeRegistry.getBaseFee("PerKey"));
        }
        return fee;
    }
}
