package com.hedera.node.app.hapi.fees.apis.consensus;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;
import com.hedera.node.app.hapi.fees.apis.common.YesOrNo;

import java.util.List;
import java.util.Map;

import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.MAX_KEYS;
import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.MIN_KEYS;

public class HCSCreate extends AbstractFeeModel {

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numKeys", "number", null,MIN_KEYS, MIN_KEYS, MAX_KEYS, "Number of keys"),
            new ParameterDefinition("hasCustomFee", "list", new Object[] { YesOrNo.YES, YesOrNo.NO }, YesOrNo.NO, 0, 0, "Does this topic have custom fee")
    );

    @Override
    public String getService() {
        return "Consensus";
    }

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

        YesOrNo hasCustomFee = (YesOrNo) values.get("hasCustomFee");
        if (hasCustomFee == YesOrNo.NO) {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("ConsensusCreateTopic"));
        } else {
            fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("ConsensusCreateTopicWithCustomFee"));
        }
        return fee;
    }
}
