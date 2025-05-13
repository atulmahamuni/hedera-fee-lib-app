package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;
import com.hedera.node.app.hapi.fees.apis.common.YesOrNo;

import java.util.List;
import java.util.Map;

import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.*;

public class TokenCreate extends AbstractFeeModel {
    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numKeys", "number", null, FREE_KEYS_TOKEN, MIN_KEYS, MAX_KEYS, "Number of keys"),
            new ParameterDefinition("hasCustomFee", "list", new Object[] {YesOrNo.YES, YesOrNo.NO}, YesOrNo.NO, 0, 0, "Does this token have custom fee")
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

        YesOrNo hasCustomFee = (YesOrNo) values.get("hasCustomFee");
        if (hasCustomFee == YesOrNo.NO) {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("TokenCreate"));
        } else {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("TokenCreateWithCustomFee"));
        }

        int numKeys = (int) values.get("numKeys");
        // First 7 keys are included in the base fee: adminKey, kycKey, freezeKey, wipeKey, supplyKey, feeScheduleKey, pauseKey
        if (numKeys > FREE_KEYS_TOKEN) {
            fee.addDetail("Additional keys", numKeys - FREE_KEYS_TOKEN, (numKeys - FREE_KEYS_TOKEN) * BaseFeeRegistry.getBaseFee("PerKey"));
        }

        return fee;
    }
}
