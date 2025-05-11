package com.hedera.node.app.hapi.fees.apis.crypto;

import com.hedera.node.app.hapi.fees.FeeCheckResult;
import com.hedera.node.app.hapi.fees.FeeResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CryptoCreateTest {

    @Test
    void testCryptoCreate() {
        CryptoCreate transfer = new CryptoCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numKeys", 1);
        FeeResult fee = transfer.computeFee(params);
        assertEquals(0.05, fee.fee, "Crypto create");
    }

    @Test
    void testCryptoCreateMultipleKeys() {
        CryptoCreate transfer = new CryptoCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numKeys", 3);
        FeeResult fee = transfer.computeFee(params);
        assertEquals(0.05 + 2 * 0.01, fee.fee, "Crypto create with multiple keys");
    }

    @Test
    void testCryptoCreateMultipleKeysAndSignatures() {
        CryptoCreate transfer = new CryptoCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 4);
        params.put("numKeys", 3);
        FeeResult fee = transfer.computeFee(params);
        assertEquals(0.05 + 3 * 0.0001 + 2 * 0.01, fee.fee, "Crypto create with multiple keys and signatures");
    }


}
