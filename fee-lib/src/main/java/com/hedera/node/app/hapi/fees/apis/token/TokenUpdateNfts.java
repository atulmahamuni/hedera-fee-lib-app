package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;
import com.hedera.node.app.hapi.fees.apis.common.FeeApi;

import java.util.List;
import java.util.Map;

public class TokenUpdateNfts extends AbstractFeeModel {
    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numNftSerials", "number", null, 1, 1, 10, "Number of NFT serials updated")
    );

    @Override
    public String getService() {
        return "Token";
    }

    @Override
    public String getDescription() {
        return "Update NFT metadata";
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        return params;
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();

        fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee(FeeApi.TokenUpdateNfts));

        int numNftSerials = (int) values.get("numNftSerials");
        final int numFreeSerials = 1;
        if (numNftSerials > numFreeSerials) {
            fee.addDetail("Additional NFTs", numNftSerials - numFreeSerials, (numNftSerials - numFreeSerials) * BaseFeeRegistry.getBaseFee(FeeApi.TokenUpdateNfts));
        }

        return fee;
    }
}
