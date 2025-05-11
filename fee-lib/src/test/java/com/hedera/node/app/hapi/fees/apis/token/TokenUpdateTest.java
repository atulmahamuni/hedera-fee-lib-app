package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.FeeResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenUpdateTest {

    @Test
    void testTokenUpdate() {
        TokenUpdate topic = new TokenUpdate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numKeys", 7);

        FeeResult fee = topic.computeFee(params);
        assertEquals(0.001, fee.fee, "Token update");
    }


    @Test
    void testTokenUpdateWithMultipleSignatures() {
        TokenUpdate topic = new TokenUpdate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 6);
        params.put("numKeys", 7);
        FeeResult fee = topic.computeFee(params);
        assertEquals(0.0015, fee.fee, "Token update with multiple signatures");
    }

    @Test
    void testTokenUpdateWithMultipleKeys() {
        TokenUpdate topic = new TokenUpdate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numKeys", 10);
        params.put("hasCustomFee", false);
        FeeResult fee = topic.computeFee(params);
        assertEquals(0.031, fee.fee, "Token update with multiple keys");
    }
}
