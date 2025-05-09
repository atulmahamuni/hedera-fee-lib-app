package com.hedera.node.app.hapi.fees;

import com.hedera.node.app.hapi.fees.apis.NoParametersAPI;
import com.hedera.node.app.hapi.fees.apis.consensus.HCSCreate;
import com.hedera.node.app.hapi.fees.apis.consensus.HCSSubmit;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoAllowance;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoCreate;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoTransfer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FeeModelRegistry {
    public static final Map<String, AbstractFeeModel> registry = new LinkedHashMap<>();

    static {
        // Crypto
        registry.put("CryptoCreate", new CryptoCreate());
        registry.put("CryptoTransfer", new CryptoTransfer());
        registry.put("CryptoUpdate", new NoParametersAPI("CryptoUpdate", "Updates an existing account"));
        registry.put("CryptoDelete", new NoParametersAPI("CryptoDelete", "Deletes an existing account"));
        registry.put("CryptoGetAccountRecords", new NoParametersAPI("CryptoGetAccountRecords", "Retrieves records for an account"));
        registry.put("CryptoGetAccountBalance", new NoParametersAPI("CryptoGetAccountBalance", "Retrieves an account’s balance"));
        registry.put("CryptoGetInfo", new NoParametersAPI("CryptoGetInfo", "Retrieves an account’s information"));
        registry.put("CryptoGetStakers", new NoParametersAPI("CryptoGetStakers", "Retrieves the list of proxy stakers for a node"));
        registry.put("CryptoApproveAllowance", new CryptoAllowance("CryptoApproveAllowance", "Allows a third-party to transfer on behalf of a delegating account (HIP-336)"));
        registry.put("CryptoAdjustAllowance", new CryptoAllowance("CryptoAdjustAllowance", "Adjusts the allowance assigned to a third party (HIP-336)"));
        registry.put("CryptoDeleteAllowance", new CryptoAllowance("CryptoDeleteAllowance", "Deletes non-fungible approved allowances from an owner's account"));

        // HCS
        registry.put("ConsensusCreateTopic", new HCSCreate());
        registry.put("ConsensusUpdateTopic", new CryptoAllowance("ConsensusUpdateTopic", "Update an existing topic"));
        registry.put("ConsensusDeleteTopic", new CryptoAllowance("ConsensusDeleteTopic", "Delete an existing topic"));
        registry.put("ConsensusSubmitMessage", new HCSSubmit());
        registry.put("ConsensusGetTopicInfo", new CryptoAllowance("ConsensusGetTopicInfo", "Retrieve a topic’s metadata"));



    }
}
