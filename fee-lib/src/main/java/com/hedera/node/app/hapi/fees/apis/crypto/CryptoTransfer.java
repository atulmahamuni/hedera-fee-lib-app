package com.hedera.node.app.hapi.fees.apis.crypto;

import com.hedera.node.app.hapi.fees.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.TOKEN_FREE_TRANSFERS;

public class CryptoTransfer extends AbstractFeeModel {
    String service;
    String api;

    private final List<ParameterDefinition> params = List.of(
            new ParameterDefinition("numAccountsInvolved", "number", null, 2, 0, 20, "Number of Accounts involved"),
            new ParameterDefinition("numFTNoCustomFeeEntries", "number", null, 0, 0, 10, "Fungible token entries without custom fee"),
            new ParameterDefinition("numNFTNoCustomFeeEntries", "number", null, 0, 0, 10, "NFT entries without custom fee"),
            new ParameterDefinition("numFTWithCustomFeeEntries", "number", null, 0, 0, 10, "Fungible token entries with custom fee"),
            new ParameterDefinition("numNFTWithCustomFeeEntries", "number", null, 0, 0, 10, "NFT entries with custom fee"),
            new ParameterDefinition("numAutoAssociationsCreated", "number", null, 0, 0, 10, "Auto-created token associations"),
            new ParameterDefinition("numAutoAccountsCreated", "number", null, 0, 0, 20, "Auto-created accounts")
    );
    private final List<ParameterDefinition> airdropParams = List.of(
            new ParameterDefinition("numAirdropsExecutedAsTokenTransfers", "number", null, 2, 0, 20, "Number of Airdrops executed as TokenTransfers")
    );

    public CryptoTransfer(String service, String api) {
        this.service = service;
        this.api = api;
    }

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
        if (api.equals("TokenAirdrop")) {
            return Stream.concat(params.stream(), airdropParams.stream()).collect(Collectors.toList());
        } else {
            return params;
        }
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

    // This API is complex. This same code can be called for
    // 1. CryptoTransfer: just hbar transfers. The base fee here includes 1 hbar transfer entry between 2 accounts
    // 2. TokenTransfer: fungible and/or non-fungible tokens and/or hbars. These tokens could have custom fee enabled. The base fee here includes 1 FT/NFT token transfer without custom fee
    // 3. TokenAirdrop: fungible and/or non-fungible tokens. These tokens could have custom fee enabled. The base fee here includes 1 FT/NFT token transfer without custom fee
    protected FeeResult computeApiSpecificFee(Map<String, Object> values) {
        FeeResult fee = new FeeResult();

        // Extract values
        int ftNoCustom = getInt(values.get("numFTNoCustomFeeEntries"));
        int nftNoCustom = getInt(values.get("numNFTNoCustomFeeEntries"));
        int ftWithCustom = getInt(values.get("numFTWithCustomFeeEntries"));
        int nftWithCustom = getInt(values.get("numNFTWithCustomFeeEntries"));
        int numAirdropsExecutedAsTokenTransfers = getInt(values.get("numAirdropsExecutedAsTokenTransfers"));

        int numCustomTokens = ftWithCustom + nftWithCustom;
        int numTokenTransfers = ftNoCustom + nftNoCustom + numCustomTokens;

        if (numAirdropsExecutedAsTokenTransfers > numTokenTransfers) {
            throw new IllegalArgumentException("Number of airdrops executed as token transfers cannot exceed the number of tokens");
        }


        // Find out the effective API: We allow a mix of hbar transfers and token transfers in a single API. Also, if the user calls a TokenAirdrop API
        // and if the receiver is already associated with the token, then it becomes a normal TokenTransfer instead of an airdrop
        // The API is effectively Airdrop only if there was at least one token transfer that went pending (i.e. didn't execute as a normal tokenTransfer)
        // Otherwise, if there were token transfers present in the API call, then the API is effectively a TokenTransfer
        // Otherwise, it's just a normal cryptoTransfer
        String effectiveApi; // Either CryptoTransfer, TokenTransfer, or TokenAirdrop
        int numEffectiveAirdrops = 0; // How many entries in the tokenTransferList really resulted in pending Airdrops (as against straight token-transfers)
        int numEffectiveTokenTransfers = numTokenTransfers; // How many entries in the tokenTransferList were effectively normal token transfers
        if (api.equals("TokenAirdrop") && numTokenTransfers > numAirdropsExecutedAsTokenTransfers) {
            effectiveApi = "TokenAirdrop";
            numEffectiveAirdrops = numTokenTransfers - numAirdropsExecutedAsTokenTransfers;
            numEffectiveTokenTransfers = numAirdropsExecutedAsTokenTransfers;
        } else if (numTokenTransfers > 0) {
            effectiveApi = "TokenTransfer";
        } else {
            effectiveApi = "CryptoTransfer";
        }

        fee.addDetail("Base fee", 1,  BaseFeeRegistry.getBaseFee(effectiveApi));

        // Overage for number of tokens transferred and airdropped. Remmeber that the first transfer is already included in the call, so don't double-count that.
        int includedTokenTransfers = TOKEN_FREE_TRANSFERS;
        if (numEffectiveAirdrops > includedTokenTransfers) {
            fee.addDetail( "Airdrops", (numEffectiveAirdrops - includedTokenTransfers), (numEffectiveAirdrops - includedTokenTransfers) * BaseFeeRegistry.getBaseFee("TokenAirdrop"));
            includedTokenTransfers = 0;
        }
        if (numEffectiveTokenTransfers  > includedTokenTransfers) {
            fee.addDetail( "Token Transfers", (numEffectiveTokenTransfers - includedTokenTransfers), (numEffectiveTokenTransfers - includedTokenTransfers) * BaseFeeRegistry.getBaseFee("TokenTransfer"));
        }

        // Overage for the tokens with custom fee
        if (numCustomTokens > 0) {
            fee.addDetail("Custom fee", numCustomTokens, numCustomTokens * BaseFeeRegistry.getBaseFee("TokenTransferCustomFeeSurcharge"));
        }

        // Overage for the number of accounts that we need to update for handling this transaction
        if (values.get("numAccountsInvolved") instanceof Integer num && num > 2) {
            fee.addDetail("Accounts involved", (num - 2), (num - 2) * BaseFeeRegistry.getBaseFee("PerCryptoTransferAccount"));
        }

        // Overage for the number of entities created automatically (associations/accounts) during handling this transaction
        if (values.get("numAutoAssociationsCreated") instanceof Integer num && num > 0)
            fee.addDetail("Auto token associations", num, num * BaseFeeRegistry.getBaseFee("TokenAssociateToAccount"));
        if (values.get("numAutoAccountsCreated") instanceof Integer num && num > 0)
            fee.addDetail("Auto account creations", num, num * BaseFeeRegistry.getBaseFee("CryptoCreate"));

        return fee;
    }
}
