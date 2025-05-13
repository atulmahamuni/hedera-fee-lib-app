package com.hedera.node.app.hapi.fees.apis.crypto;

import com.hedera.node.app.hapi.fees.*;

import java.util.List;
import java.util.Map;

public class CryptoTransfer extends AbstractFeeModel {
    String service;

    public CryptoTransfer(String service) {
        this.service = service;
    }

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numAccountsInvolved", "number", null, 2, 0, 20, "Number of Accounts involved"),
            new ParameterDefinition("numFTNoCustomFeeEntries", "number", null, 0, 0, 10, "Fungible token entries without custom fee"),
            new ParameterDefinition("numNFTNoCustomFeeEntries", "number", null, 0, 0, 10, "NFT entries without custom fee"),
            new ParameterDefinition("numFTWithCustomFeeEntries", "number", null, 0, 0, 10, "Fungible token entries with custom fee"),
            new ParameterDefinition("numNFTWithCustomFeeEntries", "number", null, 0, 0, 10, "NFT entries with custom fee"),
            new ParameterDefinition("numAutoAssociationsCreated", "number", null, 0, 0, 10, "Auto-created token associations"),
            new ParameterDefinition("numAutoAccountsCreated", "number", null, 0, 0, 20, "Auto-created accounts")
    );

    @Override
    public String getService() {
        return service;
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

    private int getInt(Object value) {
        return (value instanceof Integer) ? (Integer) value : 0;
    }

    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();

        // Extract values
        int ftNoCustom = getInt(values.get("numFTNoCustomFeeEntries"));
        int nftNoCustom = getInt(values.get("numNFTNoCustomFeeEntries"));
        int ftWithCustom = getInt(values.get("numFTWithCustomFeeEntries"));
        int nftWithCustom = getInt(values.get("numNFTWithCustomFeeEntries"));

        boolean noTokenTransfers = (ftNoCustom + nftNoCustom + ftWithCustom + nftWithCustom) == 0;
        if (noTokenTransfers) {
            fee.addDetail("Base fee", 1, service.equals("Crypto") ? BaseFeeRegistry.getBaseFee("CryptoTransfer") : BaseFeeRegistry.getBaseFee("TokenTransferNoCustomFee"));
        }

        if (values.get("numAccountsInvolved") instanceof Integer num && num > 2)
            fee.addDetail("Accounts involved", (num - 2), (num - 2) * BaseFeeRegistry.getBaseFee("PerCryptoTransferAccount"));

        if (ftNoCustom > 0)
            fee.addDetail("FT no custom fee", ftNoCustom, ftNoCustom * BaseFeeRegistry.getBaseFee("TokenTransferNoCustomFee"));
        if (nftNoCustom > 0)
            fee.addDetail("NFT no custom fee", nftNoCustom, nftNoCustom * BaseFeeRegistry.getBaseFee("TokenTransferNoCustomFee"));
        if (ftWithCustom > 0)
            fee.addDetail("FT with custom fee", ftWithCustom, ftWithCustom * BaseFeeRegistry.getBaseFee("TokenTransferWithCustomFee"));
        if (nftWithCustom > 0)
            fee.addDetail("NFT with custom fee", nftWithCustom, nftWithCustom * BaseFeeRegistry.getBaseFee("TokenTransferWithCustomFee"));

        if (values.get("numAutoAssociationsCreated") instanceof Integer num && num > 0)
            fee.addDetail("Auto token associations", num, num * BaseFeeRegistry.getBaseFee("TokenAssociateToAccount"));
        if (values.get("numAutoAccountsCreated") instanceof Integer num && num > 0)
            fee.addDetail("Auto account creations", num, num * BaseFeeRegistry.getBaseFee("CryptoCreate"));

        return fee;
    }
}
