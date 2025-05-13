package com.hedera.node.app.hapi.fees.apis.token;

import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.apis.common.YesOrNo;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenCreateTest {

    @Test
    void testTokenCreateNoCustomFee() {
        TokenCreate topic = new TokenCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numKeys", 7);


        params.put("hasCustomFee", YesOrNo.NO);
        FeeResult fee = topic.computeFee(params);
        assertEquals(1.00, fee.fee, "Token create without custom fee");
    }

    @Test
    void testTokenCreateWithCustomFee() {
        TokenCreate topic = new TokenCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numKeys", 7);

        params.put("hasCustomFee", YesOrNo.YES);
        FeeResult fee = topic.computeFee(params);
        assertEquals(2.0, fee.fee, "Token create with custom fee");
    }

    @Test
    void testTokenCreateNoCustomFeeWithMultipleSignatures() {
        TokenCreate topic = new TokenCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 11);
        params.put("numKeys", 7);
        params.put("hasCustomFee", YesOrNo.NO);
        FeeResult fee = topic.computeFee(params);
        assertEquals(1.001, fee.fee, "Token topic create without custom fee with multiple signatures");
    }

    @Test
    void testTokenCreateWithCustomFeeWithMultipleSignatures() {
        TokenCreate topic = new TokenCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 11);
        params.put("numKeys", 7);
        params.put("hasCustomFee", YesOrNo.YES);
        FeeResult fee = topic.computeFee(params);
        assertEquals(2.001, fee.fee,"Token topic create with custom fee with multiple signatures");
    }

    @Test
    void testTokenCreateNoCustomFeeWithMultipleKeys() {
        TokenCreate topic = new TokenCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);
        params.put("numKeys", 10);
        params.put("hasCustomFee", YesOrNo.NO);
        FeeResult fee = topic.computeFee(params);
        assertEquals(1.03, fee.fee, "Token topic create without custom fee with multiple keys");
    }
}
