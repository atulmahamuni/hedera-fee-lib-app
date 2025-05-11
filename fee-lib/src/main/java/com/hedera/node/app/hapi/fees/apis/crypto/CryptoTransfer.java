package com.hedera.node.app.hapi.fees.apis.crypto;

import com.hedera.node.app.hapi.fees.*;

import java.util.List;
import java.util.Map;

public class CryptoTransfer extends AbstractFeeModel {

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numAccountsInvolved", "number", null, 2, 0, 20, "Number of Accounts involved"),
            new ParameterDefinition("numFTNoCustomFeeEntries", "number", null, 0, 0, 10, "Fungible token entries without custom fee"),
            new ParameterDefinition("numNFTNoCustomFeeEntries", "number", null, 0, 0, 10, "Non-Fungible token entries without custom fee"),
            new ParameterDefinition("numFTWithCustomFeeEntries", "number", null, 0, 0, 10, "Fungible token entries with custom fee"),
            new ParameterDefinition("numNFTWithCustomFeeEntries", "number", null, 0, 0, 10, "Non-Fungible token entries with custom fee"),
            new ParameterDefinition("numAutoAssociationsCreated", "number", null, 0, 0, 10, "Auto-created token associations"),
            new ParameterDefinition("numAutoAccountsCreated", "number", null, 0, 0, 20, "Auto-created accounts")
    );

    @Override
    public String getService() {
        return "Crypto";
    }

    @Override
    public String getDescription() {
        return "Transfers a combination of hbars and tokens.";
    }

    @Override
    protected List<ParameterDefinition> apiSpecificParams() {
        return params;
    }

    @Override
    public FeeCheckResult checkParameters(Map<String, Object> values) {
        FeeCheckResult base = super.checkParameters(values);
        if (!base.result) return base;

        if ( (int) values.get("numAccountsInvolved") < 2 ||
                ((int) values.get("numFTNoCustomFeeEntries") < 2 &&
                (int) values.get("numNFTNoCustomFeeEntries") < 2 &&
                (int) values.get("numNFTWithCustomFeeEntries") < 2)) {
            return FeeCheckResult.failure("There must be at least 2 entries of hbar or token transfers.");
        }

        return FeeCheckResult.success();
    }

    @Override
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();

        fee.addDetail("Base fee", 1, BaseFeeRegistry.getBaseFee("CryptoTransfer"));

        if (values.get("numAccountsInvolved") instanceof Integer num && num > 2)
            fee.addDetail("Accounts involved", num, (num - 2) * BaseFeeRegistry.getBaseFee("PerCryptoTransferAccount"));

        if (values.get("numFTNoCustomFeeEntries") instanceof Integer num && num > 0)
            fee.addDetail("FT no custom fee", num, num * BaseFeeRegistry.getBaseFee("TokenTransferNoCustomFee"));
        if (values.get("numNFTNoCustomFeeEntries") instanceof Integer num && num > 0)
            fee.addDetail("NFT no custom fee", num,  num * BaseFeeRegistry.getBaseFee("TokenTransferNoCustomFee"));
        if (values.get("numFTWithCustomFeeEntries") instanceof Integer num && num > 0)
            fee.addDetail("FT with custom fee", num, num * BaseFeeRegistry.getBaseFee("TokenTransferWithCustomFee"));
        if (values.get("numNFTWithCustomFeeEntries") instanceof Integer num && num > 0)
            fee.addDetail("NFT with custom fee", num, num * BaseFeeRegistry.getBaseFee("TokenTransferWithCustomFee"));
        if (values.get("numAutoAssociationsCreated") instanceof Integer num && num > 0)
            fee.addDetail("Auto token associations", num, num * BaseFeeRegistry.getBaseFee("TokenAssociateToAccount"));
        if (values.get("numAutoAccountsCreated") instanceof Integer num && num > 0)
            fee.addDetail("Auto account creations", num, num * BaseFeeRegistry.getBaseFee("CryptoCreate"));

        return fee;
    }
}
