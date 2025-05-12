package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.AbstractFeeModel;
import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.ParameterDefinition;
import com.hedera.node.app.hapi.fees.apis.AssociateOrDissociate;

import java.util.List;
import java.util.Map;

public class TokenAssociateDissociate extends AbstractFeeModel {
    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("associateOrDissociate", "list", new Object[] {AssociateOrDissociate.Associate, AssociateOrDissociate.Dissociate}, AssociateOrDissociate.Associate, 0, 0, "Associate or dissociate operation"),
            new ParameterDefinition("numTokenTypes", "number", null, 1, 1, 10, "Number of token-types to be associate/dissociated")
    );

    @Override
    public String getService() {
        return "Token";
    }

    @Override
    public String getDescription() {
        return "Associate or Dissociate tokens-types to/from accounts";
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        return params;
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();

        double baseFee = ((AssociateOrDissociate)values.get("associateOrDissociate") == AssociateOrDissociate.Associate) ?
                BaseFeeRegistry.getBaseFee("TokenAssociateToAccount") : BaseFeeRegistry.getBaseFee("TokenDissociateFromAccount");

        fee.addDetail("Base fee", 1, baseFee);

        int numTokenTypes = (int) values.get("numTokenTypes");
        final int numFreeTokenTypes = 1;
        if (numTokenTypes > numFreeTokenTypes) {
            fee.addDetail("Additional NFTs", numTokenTypes - numFreeTokenTypes, (numTokenTypes - numFreeTokenTypes) * baseFee);
        }

        return fee;
    }
}
