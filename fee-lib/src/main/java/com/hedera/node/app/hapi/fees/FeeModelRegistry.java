package com.hedera.node.app.hapi.fees;

import com.hedera.node.app.hapi.fees.apis.common.AssociateOrDissociate;
import com.hedera.node.app.hapi.fees.apis.common.EntityUpdate;
import com.hedera.node.app.hapi.fees.apis.common.NoParametersAPI;
import com.hedera.node.app.hapi.fees.apis.consensus.HCSCreate;
import com.hedera.node.app.hapi.fees.apis.consensus.HCSSubmit;
import com.hedera.node.app.hapi.fees.apis.contract.ContractBasedOnGas;
import com.hedera.node.app.hapi.fees.apis.contract.ContractCreate;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoAllowance;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoCreate;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoTransfer;
import com.hedera.node.app.hapi.fees.apis.token.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class FeeModelRegistry {
    public static final Map<String, AbstractFeeModel> registry = new LinkedHashMap<>();

    static {
        // Crypto
        registry.put("CryptoCreate", new CryptoCreate());
        registry.put("CryptoTransfer", new CryptoTransfer("Crypto"));
        registry.put("CryptoUpdate", new EntityUpdate("Crypto", "CryptoUpdate", "Updates an existing account", 1));
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
        registry.put("ConsensusUpdateTopic", new EntityUpdate("Consensus", "ConsensusUpdateTopic", "Update an existing topic", 1));
        registry.put("ConsensusDeleteTopic", new NoParametersAPI("Consensus", "ConsensusDeleteTopic", "Delete an existing topic"));
        registry.put("ConsensusSubmitMessage", new HCSSubmit());
        registry.put("ConsensusGetTopicInfo", new NoParametersAPI("Consensus", "ConsensusGetTopicInfo", "Retrieve a topic’s metadata"));

        // Token
        registry.put("TokenCreate", new TokenCreate());
        registry.put("TokenUpdate", new EntityUpdate("Token", "TokenUpdate", "Update an existing token-type", 7));
        registry.put("TokenTransfer", new CryptoTransfer("Token"));
        registry.put("TokenDelete", new NoParametersAPI("Token", "TokenDelete", "Delete an existing token"));
        registry.put("TokenMint", new TokenMint());
        registry.put("TokenBurn", new TokenBurn());
        registry.put("TokenPause", new NoParametersAPI("Token", "TokenPause", "Pauses a token"));
        registry.put("TokenUnpause", new NoParametersAPI("Token", "TokenUnpause", "Unpauses a token"));
        registry.put("TokenFeeScheduleUpdate", new NoParametersAPI("Token", "TokenFeeScheduleUpdate", "Updates the custom fee schedule for a token"));
        registry.put("TokenAssociateToAccount", new TokenAssociateDissociate(AssociateOrDissociate.Associate));
        registry.put("TokenDissociateFromAccount", new TokenAssociateDissociate(AssociateOrDissociate.Dissociate));
        registry.put("TokenGrantKycToAccount", new NoParametersAPI("Token", "TokenGrantKycToAccount", "Grant KYC to an account from a particular token"));
        registry.put("TokenRevokeKycFromAccount", new NoParametersAPI("Token", "TokenRevokeKycFromAccount", "Revoke KYC from an account for a particular token"));
        registry.put("TokenFreezeAccount", new NoParametersAPI("Token", "TokenFreezeAccount", "Freeze an account for a particular token"));
        registry.put("TokenUnfreezeAccount", new NoParametersAPI("Token", "TokenUnfreezeAccount", "Unfreeze an account for a particular token"));
        registry.put("TokenAccountWipe", new TokenWipe());
        registry.put("TokenGetInfo", new NoParametersAPI("Token", "TokenGetInfo", "Retrieve a token’s metadata"));
        registry.put("TokenGetNftInfo", new NoParametersAPI("Token", "TokenGetNftInfo", "Retrieve an NFT's information"));

        // Smart Contracts
        registry.put("ContractCreate", new ContractCreate("ContractCreate", "Create a new Smart Contract", true));
        registry.put("ContractUpdate", new EntityUpdate("Smart Contract", "ContractUpdate", "Update an existing Smart Contract", 1));
        registry.put("ContractDelete", new NoParametersAPI("Smart Contract", "ContractDelete", "Delete an existing smart contract"));
        registry.put("ContractCall", new ContractBasedOnGas("ContractCall", "Execute a smart contract call", false));
        registry.put("EthereumTransaction", new ContractBasedOnGas("EthereumTransaction", "Submits a wrapped Ethereum Transaction per HIP-410", false));
        registry.put("ContractGetInfo", new NoParametersAPI("Smart Contract", "ContractGetInfo", "Retrieve a smart contract’s metadata"));
        registry.put("ContractCallLocal", new NoParametersAPI("Smart Contract", "ContractCallLocal", "Execute a smart contract call on a single node"));
        registry.put("ContractGetBytecode", new NoParametersAPI("Smart Contract", "ContractGetBytecode", "Retrieve a smart contract’s bytecode"));
//        registry.put("GetBySolidityID", new NoParametersAPI("Smart Contract", "GetBySolidityID", "Retrieve a smart contract by solidity identifier"));
//        registry.put("ContractGetRecords", new NoParametersAPI("Smart Contract", "ContractGetRecords", "Retrieve the records for a smart contract"));

    }
}
