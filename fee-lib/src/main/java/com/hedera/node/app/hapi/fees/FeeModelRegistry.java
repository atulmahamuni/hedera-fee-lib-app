package com.hedera.node.app.hapi.fees;

import com.hedera.node.app.hapi.fees.apis.NoParametersAPI;
import com.hedera.node.app.hapi.fees.apis.consensus.HCSCreate;
import com.hedera.node.app.hapi.fees.apis.consensus.HCSSubmit;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoAllowance;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoCreate;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoTransfer;
import com.hedera.node.app.hapi.fees.apis.token.TokenCreate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FeeModelRegistry {
    public static final Map<String, AbstractFeeModel> registry = new LinkedHashMap<>();

    static {
        // Crypto
        registry.put("CryptoCreate", new CryptoCreate());
        registry.put("CryptoTransfer", new CryptoTransfer());
        registry.put("CryptoUpdate", new NoParametersAPI("Crypto", "CryptoUpdate", "Updates an existing account"));
        registry.put("CryptoDelete", new NoParametersAPI("Crypto", "CryptoDelete", "Deletes an existing account"));
        registry.put("CryptoGetAccountRecords", new NoParametersAPI("Crypto", "CryptoGetAccountRecords", "Retrieves records for an account"));
        registry.put("CryptoGetAccountBalance", new NoParametersAPI("Crypto", "CryptoGetAccountBalance", "Retrieves an account’s balance"));
        registry.put("CryptoGetInfo", new NoParametersAPI("Crypto", "CryptoGetInfo", "Retrieves an account’s information"));
        registry.put("CryptoGetStakers", new NoParametersAPI("Crypto", "CryptoGetStakers", "Retrieves the list of proxy stakers for a node"));
        registry.put("CryptoApproveAllowance", new CryptoAllowance( "CryptoApproveAllowance", "Allows a third-party to transfer on behalf of a delegating account (HIP-336)"));
        registry.put("CryptoAdjustAllowance", new CryptoAllowance("CryptoAdjustAllowance", "Adjusts the allowance assigned to a third party (HIP-336)"));
        registry.put("CryptoDeleteAllowance", new CryptoAllowance("CryptoDeleteAllowance", "Deletes non-fungible approved allowances from an owner's account"));

        // HCS
        registry.put("ConsensusCreateTopic", new HCSCreate());
        registry.put("ConsensusUpdateTopic", new NoParametersAPI("Consensus", "ConsensusUpdateTopic", "Update an existing topic"));
        registry.put("ConsensusDeleteTopic", new NoParametersAPI("Consensus", "ConsensusDeleteTopic", "Delete an existing topic"));
        registry.put("ConsensusSubmitMessage", new HCSSubmit());
        registry.put("ConsensusGetTopicInfo", new NoParametersAPI("Consensus", "ConsensusGetTopicInfo", "Retrieve a topic’s metadata"));

        // Token
        registry.put("TokenCreate", new TokenCreate());
        registry.put("TokenUpdate", new NoParametersAPI("Token", "TokenUpdate", "Update an existing token"));
        registry.put("TokenTransfer", new CryptoTransfer());
        registry.put("TokenDelete", new NoParametersAPI("Token", "TokenDelete", "Delete an existing token"));
        registry.put("TokenMint", new NoParametersAPI("Token", "TokenMint", "Mint new token(s)"));
        registry.put("TokenBurn", new NoParametersAPI("Token", "TokenBurn", "Burn existing token(s)"));
        registry.put("TokenPause", new NoParametersAPI("Token", "TokenPause", "Pauses a token"));
        registry.put("TokenUnpause", new NoParametersAPI("Token", "TokenUnpause", "Unpauses a token"));
        registry.put("TokenFeeScheduleUpdate", new NoParametersAPI("Token", "TokenFeeScheduleUpdate", "Updates the custom fee schedule for a token"));
        registry.put("TokenAssociateToAccount", new NoParametersAPI("Token", "TokenAssociateToAccount", "Associates token(s) to an account"));
        registry.put("TokenDissociateFromAccount", new NoParametersAPI("Token", "TokenDissociateFromAccount", "Dissociates token(s) from an account"));
        registry.put("TokenGrantKycToAccount", new NoParametersAPI("Token", "TokenGrantKycToAccount", "Grant KYC to an account from a particular token"));
        registry.put("TokenRevokeKycFromAccount", new NoParametersAPI("Token", "TokenRevokeKycFromAccount", "Revoke KYC from an account for a particular token"));
        registry.put("TokenFreezeAccount", new NoParametersAPI("Token", "TokenFreezeAccount", "Freeze an account for a particular token"));
        registry.put("TokenUnfreezeAccount", new NoParametersAPI("Token", "TokenUnfreezeAccount", "Unfreeze an account for a particular token"));
        registry.put("TokenAccountWipe", new NoParametersAPI("Token", "TokenAccountWipe", "Wipe tokens from an account"));
        registry.put("TokenGetInfo", new NoParametersAPI("Token", "TokenGetInfo", "Retrieve a token’s metadata"));
        registry.put("TokenGetNftInfo", new NoParametersAPI("Token", "TokenGetNftInfo", "Retrieve an NFT's information"));

    }
}
