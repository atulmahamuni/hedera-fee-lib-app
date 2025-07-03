package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.FeeResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenUpdateNftsTest {

    @Test
    void testUpdateNftOne() {
        TokenUpdateNfts transaction = new TokenUpdateNfts();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numNftSerials", 1);

        FeeResult fee = transaction.computeFee(params);
        assertEquals(0.001, fee.fee, "Update 1 serial");
    }

    @Test
    void testUpdateNftsFive() {
        TokenUpdateNfts transaction = new TokenUpdateNfts();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numNftSerials", 5);

        FeeResult fee = transaction.computeFee(params);
        assertEquals(0.005, fee.fee, "Update 5 serials");
    }

    @Test
    void testTokenUpdateNftsWithMultipleSignatures() {
        TokenUpdateNfts transaction = new TokenUpdateNfts();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 10);
        params.put("numNftSerials", 10);

        FeeResult fee = transaction.computeFee(params);
        assertEquals(0.001*10+0.0001*9, fee.fee, "Update multiple serials with multiple signatures");
    }
}
