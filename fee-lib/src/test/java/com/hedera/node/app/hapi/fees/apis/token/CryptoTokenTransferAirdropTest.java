package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.BaseFeeRegistry;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoTransfer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CryptoTokenTransferAirdropTest {
    double tokenTransferWithCustomFee = BaseFeeRegistry.getBaseFee("TokenTransfer") + BaseFeeRegistry.getBaseFee("TokenTransferCustomFeeSurcharge");
    double tokenAirdropWithCustomFee = BaseFeeRegistry.getBaseFee("TokenAirdrop") + BaseFeeRegistry.getBaseFee("TokenTransferCustomFeeSurcharge");

    void testPredefinedScenarios(List<TransferTestScenario> scenarios) {
        for (var scenario : scenarios) {
            CryptoTransfer transfer = switch (scenario.api) {
                case "CryptoTransfer" -> new CryptoTransfer("Crypto", "CryptoTransfer");
                case "TokenTransfer" -> new CryptoTransfer("Token", "TokenTransfer");
                case "TokenAirdrop" -> new CryptoTransfer("Crypto", "TokenAirdrop");
                default -> throw new IllegalStateException("Unexpected value: " + scenario.api);
            };

            Map<String, Object> params = new HashMap<>();
            params.put("numSignatures", scenario.numSignatures);
            params.put("numAccountsInvolved", scenario.numAccountsInvolved);
            params.put("numFTNoCustomFeeEntries", scenario.numFTNoCustomFeeEntries);
            params.put("numNFTNoCustomFeeEntries", scenario.numNFTNoCustomFeeEntries);
            params.put("numFTWithCustomFeeEntries", scenario.numFTWithCustomFeeEntries);
            params.put("numNFTWithCustomFeeEntries", scenario.numNFTWithCustomFeeEntries);
            params.put("numAutoAssociationsCreated", scenario.numAutoAssociationsCreated);
            params.put("numAutoAccountsCreated", scenario.numAutoAccountsCreated);
            params.put("numAirdropsExecutedAsTokenTransfers", scenario.numAirdropsExecutedAsTokenTransfers);

            FeeResult fee = transfer.computeFee(params);
            System.out.println(fee);
            assertEquals(scenario.expectedFee, fee.fee, 1e-9, "hbar/token/airdrop test: " + scenario);
        }
    }

    @Test
    void testPureHbarsTrasnfers() {
        List<TransferTestScenario> scenarios = List.of(
            // Any API (CryptoTransfer, TokenTransfer, TokenAirdrop) with no tokens should default to CryptoTransfer price
            new TransferTestScenario("CryptoTransfer", 1, 2, 0, 0, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("CryptoTransfer")),
            new TransferTestScenario("TokenTransfer", 1, 2, 0, 0, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("CryptoTransfer")),
            new TransferTestScenario("TokenAirdrop", 1, 2, 0, 0, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("CryptoTransfer"))
        );
        testPredefinedScenarios(scenarios);
    }

    @Test
    void testSimpleTokenTrasnfers() {
        List<TransferTestScenario> scenarios = List.of(
            // Any API (CryptoTransfer, TokenTransfer, TokenAirdrop) with tokens that are transferred normally (i.e. not pending) should default to TokenTransfer price
            new TransferTestScenario("CryptoTransfer", 1, 2, 1, 0, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("TokenTransfer")),
            new TransferTestScenario("TokenTransfer", 1, 2, 1, 0, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("TokenTransfer")),
            new TransferTestScenario("TokenAirdrop", 1, 2, 1, 0, 0, 0, 0, 0, 1, BaseFeeRegistry.getBaseFee("TokenTransfer"))
        );
        testPredefinedScenarios(scenarios);
    }

    @Test
    void testTokenTrasnfersNoCustomFee() {
        List<TransferTestScenario> scenarios = List.of(
            // Tokens (FT/NFT) without custom fees
            new TransferTestScenario("CryptoTransfer", 1, 2, 2, 0, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("TokenTransfer") + BaseFeeRegistry.getBaseFee("TokenTransfer")),
            new TransferTestScenario("CryptoTransfer", 1, 2, 0, 2, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("TokenTransfer") + BaseFeeRegistry.getBaseFee("TokenTransfer")),

            new TransferTestScenario("TokenTransfer", 1, 2, 10, 0, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("TokenTransfer") + 9 * BaseFeeRegistry.getBaseFee("TokenTransfer")),
            new TransferTestScenario("TokenTransfer", 1, 2, 0, 10, 0, 0, 0, 0, 0, BaseFeeRegistry.getBaseFee("TokenTransfer") + 9 * BaseFeeRegistry.getBaseFee("TokenTransfer")),

            new TransferTestScenario("TokenAirdrop", 1, 2, 5, 5, 0, 0, 0, 0, 10, BaseFeeRegistry.getBaseFee("TokenTransfer") + 9 * BaseFeeRegistry.getBaseFee("TokenTransfer")),
            new TransferTestScenario("TokenAirdrop", 1, 2, 0, 10, 0, 0, 0, 0, 10, BaseFeeRegistry.getBaseFee("TokenTransfer") + 9 * BaseFeeRegistry.getBaseFee("TokenTransfer"))
        );
        testPredefinedScenarios(scenarios);
    }

    @Test
    void testTokenTrasnfersIncludesAFreeTransfer() {
        List<TransferTestScenario> scenarios = List.of(
            // Any API involving one token with custom fees should include that in the base price
            new TransferTestScenario("CryptoTransfer", 1, 2, 0, 0, 1, 0, 0, 0, 0, tokenTransferWithCustomFee),
            new TransferTestScenario("TokenTransfer", 1, 2, 0, 0, 0, 1, 0, 0, 0, tokenTransferWithCustomFee),
            new TransferTestScenario("TokenAirdrop", 1, 2, 0, 0, 1, 0, 0, 0, 0, tokenAirdropWithCustomFee)
        );
        testPredefinedScenarios(scenarios);
    }

    @Test
    void testTokenTrasnfersWithCustomFee() {
        List<TransferTestScenario> scenarios = List.of(
            // Any API involving more than one token with custom fees should charge for custom fees
            new TransferTestScenario("CryptoTransfer", 1, 2, 1, 1, 2, 4, 0, 0, 0, tokenTransferWithCustomFee + 2 * BaseFeeRegistry.getBaseFee("TokenTransfer") + 5 * tokenTransferWithCustomFee),
            new TransferTestScenario("TokenTransfer", 1, 2, 1, 1, 2, 4, 0, 0, 0, tokenTransferWithCustomFee + 2 * BaseFeeRegistry.getBaseFee("TokenTransfer") + 5 * tokenTransferWithCustomFee),
            new TransferTestScenario("TokenAirdrop", 1, 2, 1, 1, 2, 4, 0, 0, 0, BaseFeeRegistry.getBaseFee("TokenAirdrop") + 7 * BaseFeeRegistry.getBaseFee("TokenAirdrop") + 6 * BaseFeeRegistry.getBaseFee("TokenTransferCustomFeeSurcharge"))
        );
        testPredefinedScenarios(scenarios);
    }

    @Test
    void testTokenTrasnfersWithOtherOverages() {
        List<TransferTestScenario> scenarios = List.of(
            // Any API should charge overages for signatures, accounts, auto-associations, and auto-account-creations
            new TransferTestScenario("CryptoTransfer", 5, 6, 1, 0, 0, 0, 3, 4, 0, BaseFeeRegistry.getBaseFee("TokenTransfer") + 4 * BaseFeeRegistry.getBaseFee("PerSignature") + 4 * BaseFeeRegistry.getBaseFee("PerCryptoTransferAccount") + 3 * BaseFeeRegistry.getBaseFee("TokenAssociateToAccount") + 4 * BaseFeeRegistry.getBaseFee("CryptoCreate")),
            new TransferTestScenario("TokenTransfer", 5, 6, 1, 0, 0, 0, 3, 4, 0, BaseFeeRegistry.getBaseFee("TokenTransfer") + 4 * BaseFeeRegistry.getBaseFee("PerSignature") + 4 * BaseFeeRegistry.getBaseFee("PerCryptoTransferAccount") + 3 * BaseFeeRegistry.getBaseFee("TokenAssociateToAccount") + 4 * BaseFeeRegistry.getBaseFee("CryptoCreate")),
            new TransferTestScenario("TokenAirdrop", 5, 6, 1, 0, 0, 0, 3, 4, 0, BaseFeeRegistry.getBaseFee("TokenAirdrop") + 4 * BaseFeeRegistry.getBaseFee("PerSignature") + 4 * BaseFeeRegistry.getBaseFee("PerCryptoTransferAccount") + 3 * BaseFeeRegistry.getBaseFee("TokenAssociateToAccount") + 4 * BaseFeeRegistry.getBaseFee("CryptoCreate"))
        );
        testPredefinedScenarios(scenarios);
    }

    @Test
    void shouldThrowExceptionWhenAirdropsExceedTokenTransfers() {
        CryptoTransfer transfer = new CryptoTransfer("Crypto", "CryptoTransfer");
        Map<String, Object> params = new HashMap<>();

        params.put("numNFTNoCustomFeeEntries", 5);
        params.put("numAirdropsExecutedAsTokenTransfers", 6);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transfer.computeFee(params));
        assertEquals("Number of airdrops executed as token transfers cannot exceed the number of tokens", exception.getMessage()
        );
    }
}
