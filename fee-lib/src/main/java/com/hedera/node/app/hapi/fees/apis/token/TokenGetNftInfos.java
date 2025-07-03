package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;
import com.hedera.node.app.hapi.fees.apis.common.FeeApi;

import java.util.List;
import java.util.Map;

// Handles tokenGetNFTInfo, tokenGetNFTInfo
public class TokenGetNftInfos extends AbstractFeeModel {
    FeeApi api;
    String desciption;
    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numTokens", "number", null, 1, 1, 10, "Number of NFT serial numbers")
    );

    public TokenGetNftInfos(FeeApi api, String desciption) {
        this.api = api;
        this.desciption = desciption;
    }

    @Override
    public String getService() {
        return "Token";
    }

    @Override
    public String getDescription() {
        return desciption;
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        return params;
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();

        double baseFee = switch (api) {
            case TokenGetNftInfo -> BaseFeeRegistry.getBaseFee(FeeApi.TokenGetNftInfo);
            case TokenGetNftInfos -> BaseFeeRegistry.getBaseFee(FeeApi.TokenGetNftInfos);
            default -> throw new IllegalStateException("Unexpected value: " + api);
        };
        fee.addDetail("Base fee", 1, baseFee);

        int numTokens = (int) values.get("numTokens");
        final int numFreeTokenTypes = 1;
        if (numTokens > numFreeTokenTypes) {
            fee.addDetail("Additional token-types", numTokens - numFreeTokenTypes, (numTokens - numFreeTokenTypes) * baseFee);
        }

        return fee;
    }
}
