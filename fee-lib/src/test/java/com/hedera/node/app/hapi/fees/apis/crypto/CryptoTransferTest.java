package com.hedera.node.app.hapi.fees.apis.crypto;

import com.hedera.node.app.hapi.fees.FeeCheckResult;
import com.hedera.node.app.hapi.fees.FeeResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CryptoTransferTest {

    @Test
    void testSimpleHbarTransfer() {
        CryptoTransfer transfer = new CryptoTransfer();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);

        params.put("numHbarEntries", 2);
        FeeResult fee = transfer.computeFee(params);
        assertEquals(fee.fee, 0.0001, "Simple hbar transfer");
    }

    @Test
    void testMultipleHbarTransfer() {
        CryptoTransfer transfer = new CryptoTransfer();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);

        params.put("numHbarEntries", 10);
        FeeResult fee = transfer.computeFee(params);
        assertEquals(fee.fee, 0.0001, "Multiple hbar transfer");
    }

    @Test
    void testTokenTransfer() {
        CryptoTransfer transfer = new CryptoTransfer();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);

        params.put("numFTNoCustomFeeEntries", 1);

        FeeResult fee = transfer.computeFee(params);
        assertEquals(fee.fee, 0.001, "Simple token transfer");
    }

    @Test
    void testMultipleTokenTransfer() {
        CryptoTransfer transfer = new CryptoTransfer();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);

        params.put("numFTNoCustomFeeEntries", 5);

        FeeResult fee = transfer.computeFee(params);
        assertEquals(fee.fee, 0.005, "Multiple token transfers");
    }

    @Test
    void testMultipleHbarAndMultipleTokenTransfer() {
        CryptoTransfer transfer = new CryptoTransfer();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);

        params.put("numHbarEntries", 10);
        params.put("numFTNoCustomFeeEntries", 5);

        FeeResult fee = transfer.computeFee(params);
        assertEquals(fee.fee, 0.0051, "Multiple hbar and token transfers");
    }

    @Test
    void testInvalidParamsFailCheck() {
        CryptoTransfer transfer = new CryptoTransfer();
        Map<String, Object> params = new HashMap<>();
        params.put("numHbarEntries", 0);
        params.put("numFTNoCustomFeeEntries", 0);
        params.put("numNFTNoCustomFeeEntries", 0);
        params.put("numFTWithCustomFeeEntries", 0);
        params.put("numNFTWithCustomFeeEntries", 0);
        params.put("numAutoAssociationsCreated", 0);
        params.put("numAutoAccountsCreated", 0);
        params.put("numSignatures", 1);

        FeeCheckResult result = transfer.checkParameters(params);
        assertFalse(result.result, "Expected parameters to fail due to minimum transfer entry rule");
    }
}
