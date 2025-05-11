package com.hedera.node.app.hapi.fees.apis.consensus;

import com.hedera.node.app.hapi.fees.FeeCheckResult;
import com.hedera.node.app.hapi.fees.FeeResult;
import com.hedera.node.app.hapi.fees.apis.YesOrNo;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoTransfer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class HCSCreateTest {

    @Test
    void testHCSCreateNoCustomFee() {
        HCSCreate topic = new HCSCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);

        params.put("hasCustomFee", YesOrNo.NO);
        FeeResult fee = topic.computeFee(params);
        assertEquals(0.01, fee.fee, "HCS topic create without custom fee");
    }

    @Test
    void testHCSCreateWithCustomFee() {
        HCSCreate topic = new HCSCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 1);

        params.put("hasCustomFee", YesOrNo.YES);
        FeeResult fee = topic.computeFee(params);
        assertEquals(2.0, fee.fee, "HCS topic create with custom fee");
    }

    @Test
    void testHCSCreateNoCustomFeeWithMultipleSignatures() {
        HCSCreate topic = new HCSCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 11);

        params.put("hasCustomFee", YesOrNo.NO);
        FeeResult fee = topic.computeFee(params);
        assertEquals(0.011, fee.fee, "HCS topic create without custom fee with multiple signatures");
    }

    @Test
    void testHCSCreateWithCustomFeeWithMultipleSignatures() {
        HCSCreate topic = new HCSCreate();
        Map<String, Object> params = new HashMap<>();
        params.put("numSignatures", 11);

        params.put("hasCustomFee", YesOrNo.YES);
        FeeResult fee = topic.computeFee(params);
        assertEquals(2.001, fee.fee,"HCS topic create with custom fee with multiple signatures");
    }
}
