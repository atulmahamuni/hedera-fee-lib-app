package com.hedera.node.app.hapi.fees;

import com.hedera.node.app.hapi.fees.apis.common.*;
import com.hedera.node.app.hapi.fees.apis.consensus.HCSSubmit;
import com.hedera.node.app.hapi.fees.apis.contract.ContractBasedOnGas;
import com.hedera.node.app.hapi.fees.apis.contract.ContractCreate;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoAllowance;
import com.hedera.node.app.hapi.fees.apis.crypto.CryptoTransfer;
import com.hedera.node.app.hapi.fees.apis.file.FileOperations;
import com.hedera.node.app.hapi.fees.apis.token.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.FREE_KEYS_DEFAULT;
import static com.hedera.node.app.hapi.fees.apis.common.FeeConstants.FREE_KEYS_TOKEN;

public class FeeModelRegistry {
    public static final Map<FeeApi, AbstractFeeModel> registry = new LinkedHashMap<>();

    static {
        // Crypto
        registry.put(FeeApi.CryptoCreate, new EntityCreate("Crypto", FeeApi.CryptoCreate, "Create a new Account", FREE_KEYS_DEFAULT, false));
        registry.put(FeeApi.CryptoTransfer, new CryptoTransfer("Crypto", FeeApi.CryptoTransfer));
        registry.put(FeeApi.CryptoUpdate, new EntityUpdate("Crypto", FeeApi.CryptoUpdate, "Updates an existing account", 1));
        registry.put(FeeApi.CryptoDelete, new NoParametersAPI("Crypto", FeeApi.CryptoDelete, "Deletes an existing account"));
        registry.put(FeeApi.CryptoGetAccountRecords, new NoParametersAPI("Crypto", FeeApi.CryptoGetAccountRecords, "Retrieves records for an account"));
        registry.put(FeeApi.CryptoGetAccountBalance, new NoParametersAPI("Crypto", FeeApi.CryptoGetAccountBalance, "Retrieves an account’s balance"));
        registry.put(FeeApi.CryptoGetInfo, new NoParametersAPI("Crypto", FeeApi.CryptoGetInfo, "Retrieves an account’s information"));
        registry.put(FeeApi.CryptoApproveAllowance, new CryptoAllowance( FeeApi.CryptoApproveAllowance, "Allows a third-party to transfer on behalf of a delegating account (HIP-336)"));
        registry.put(FeeApi.CryptoDeleteAllowance, new CryptoAllowance(FeeApi.CryptoDeleteAllowance, "Deletes non-fungible approved allowances from an owner's account"));

        // HCS
        registry.put(FeeApi.ConsensusCreateTopic, new EntityCreate("Consensus", FeeApi.ConsensusCreateTopic, "Create a new topic", FREE_KEYS_DEFAULT, true));
        registry.put(FeeApi.ConsensusUpdateTopic, new EntityUpdate("Consensus", FeeApi.ConsensusUpdateTopic, "Update an existing topic", 1));
        registry.put(FeeApi.ConsensusDeleteTopic, new NoParametersAPI("Consensus", FeeApi.ConsensusDeleteTopic, "Delete an existing topic"));
        registry.put(FeeApi.ConsensusSubmitMessage, new HCSSubmit());
        registry.put(FeeApi.ConsensusGetTopicInfo, new NoParametersAPI("Consensus", FeeApi.ConsensusGetTopicInfo, "Retrieve a topic’s metadata"));

        // Token
        registry.put(FeeApi.TokenCreate, new EntityCreate("Token", FeeApi.TokenCreate, "Create a new token-type", FREE_KEYS_TOKEN, true));
        registry.put(FeeApi.TokenUpdate, new EntityUpdate("Token", FeeApi.TokenUpdate, "Update an existing token-type", 7));
        registry.put(FeeApi.TokenTransfer, new CryptoTransfer("Token", FeeApi.TokenTransfer));
        registry.put(FeeApi.TokenDelete, new NoParametersAPI("Token", FeeApi.TokenDelete, "Delete an existing token"));
        registry.put(FeeApi.TokenMint, new TokenMint());
        registry.put(FeeApi.TokenBurn, new TokenBurn());
        registry.put(FeeApi.TokenPause, new NoParametersAPI("Token", FeeApi.TokenPause, "Pauses a token"));
        registry.put(FeeApi.TokenUnpause, new NoParametersAPI("Token", FeeApi.TokenUnpause, "Unpauses a token"));

        registry.put(FeeApi.TokenAirdrop, new CryptoTransfer("Token", FeeApi.TokenAirdrop));
        registry.put(FeeApi.TokenClaimAirdrop, new TokenAirdropOperations(FeeApi.TokenClaimAirdrop,  "Claim a pending airdrop"));
        registry.put(FeeApi.TokenCancelAirdrop, new TokenAirdropOperations(FeeApi.TokenCancelAirdrop,  "Cancel a pending airdrop"));
        registry.put(FeeApi.TokenReject, new TokenAirdropOperations(FeeApi.TokenReject,  "Reject a token and send back to treasury"));

        registry.put(FeeApi.TokenFeeScheduleUpdate, new NoParametersAPI("Token", FeeApi.TokenFeeScheduleUpdate, "Updates the custom fee schedule for a token"));
        registry.put(FeeApi.TokenAssociateToAccount, new TokenAssociateDissociate(AssociateOrDissociate.Associate));
        registry.put(FeeApi.TokenDissociateFromAccount, new TokenAssociateDissociate(AssociateOrDissociate.Dissociate));
        registry.put(FeeApi.TokenGrantKycToAccount, new NoParametersAPI("Token", FeeApi.TokenGrantKycToAccount, "Grant KYC to an account from a particular token"));
        registry.put(FeeApi.TokenRevokeKycFromAccount, new NoParametersAPI("Token", FeeApi.TokenRevokeKycFromAccount, "Revoke KYC from an account for a particular token"));
        registry.put(FeeApi.TokenFreezeAccount, new NoParametersAPI("Token", FeeApi.TokenFreezeAccount, "Freeze an account for a particular token"));
        registry.put(FeeApi.TokenUnfreezeAccount, new NoParametersAPI("Token", FeeApi.TokenUnfreezeAccount, "Unfreeze an account for a particular token"));
        registry.put(FeeApi.TokenAccountWipe, new TokenWipe());
        registry.put(FeeApi.TokenGetInfo, new NoParametersAPI("Token", FeeApi.TokenGetInfo, "Retrieve a token’s metadata"));
        registry.put(FeeApi.TokenGetNftInfos, new TokenGetNftInfos(FeeApi.TokenGetNftInfos, "Retrieve multiple NFTs' information"));

        // Smart Contracts
        registry.put(FeeApi.ContractCreate, new ContractCreate(FeeApi.ContractCreate, "Create a new Smart Contract", true));
        registry.put(FeeApi.ContractUpdate, new EntityUpdate("Smart Contract", FeeApi.ContractUpdate, "Update an existing Smart Contract", 1));
        registry.put(FeeApi.ContractDelete, new NoParametersAPI("Smart Contract", FeeApi.ContractDelete, "Delete an existing smart contract"));
        registry.put(FeeApi.ContractCall, new ContractBasedOnGas(FeeApi.ContractCall, "Execute a smart contract call", false));
        registry.put(FeeApi.EthereumTransaction, new ContractBasedOnGas(FeeApi.EthereumTransaction, "Submits a wrapped Ethereum Transaction per HIP-410", false));
        registry.put(FeeApi.ContractGetInfo, new NoParametersAPI("Smart Contract", FeeApi.ContractGetInfo, "Retrieve a smart contract’s metadata"));
        registry.put(FeeApi.ContractCallLocal, new NoParametersAPI("Smart Contract", FeeApi.ContractCallLocal, "Execute a smart contract call on a single node"));
        registry.put(FeeApi.ContractGetBytecode, new NoParametersAPI("Smart Contract", FeeApi.ContractGetBytecode, "Retrieve a smart contract’s bytecode"));


        // File
        registry.put(FeeApi.FileCreate, new FileOperations(FeeApi.FileCreate, "Create a new file"));
        registry.put(FeeApi.FileUpdate, new FileOperations(FeeApi.FileUpdate, "Update an existing file"));
        registry.put(FeeApi.FileDelete, new NoParametersAPI("File", FeeApi.FileDelete, "Delete an existing file"));
        registry.put(FeeApi.FileAppend, new FileOperations(FeeApi.FileAppend, "Append to an existing file"));
        registry.put(FeeApi.FileGetContents, new NoParametersAPI("File", FeeApi.FileGetContents, "Retrieve the contents of a file"));
        registry.put(FeeApi.FileGetInfo, new NoParametersAPI("File", FeeApi.FileGetInfo, "Retrieve a file’s metadata"));


        // Miscellaneous
        registry.put(FeeApi.ScheduleCreate, new EntityCreate("Miscellaneous", FeeApi.ScheduleCreate, "Create a new scheduled transaction", FREE_KEYS_DEFAULT, false));
        registry.put(FeeApi.ScheduleSign, new NoParametersAPI("Miscellaneous", FeeApi.ScheduleSign, "Add a signature to a scheduled transaction"));
        registry.put(FeeApi.ScheduleDelete, new NoParametersAPI("Miscellaneous", FeeApi.ScheduleDelete, "Delete a scheduled transaction"));
        registry.put(FeeApi.ScheduleGetInfo, new NoParametersAPI("Miscellaneous", FeeApi.ScheduleGetInfo, "Retrieve information about a scheduled transaction"));

        registry.put(FeeApi.GetVersionInfo, new NoParametersAPI("Miscellaneous", FeeApi.GetVersionInfo, "Retrieve the current version of the network"));
        registry.put(FeeApi.TransactionGetReceipt, new NoParametersAPI("Miscellaneous", FeeApi.TransactionGetReceipt, "Retrieve a transaction’s receipt"));
        registry.put(FeeApi.TransactionGetRecord, new NoParametersAPI("Miscellaneous", FeeApi.TransactionGetRecord, "Retrieve a transaction’s record"));
        registry.put(FeeApi.SystemDelete, new NoParametersAPI("Miscellaneous", FeeApi.SystemDelete, "System delete an existing file"));
        registry.put(FeeApi.SystemUndelete, new NoParametersAPI("Miscellaneous", FeeApi.SystemUndelete, "System undelete an existing file"));
        registry.put(FeeApi.PrngTransaction, new NoParametersAPI("Miscellaneous", FeeApi.PrngTransaction, "Generate a pseudorandom number"));
        registry.put(FeeApi.CreateNode, new NoParametersAPI("Miscellaneous", FeeApi.CreateNode, "Add a new node to the address book"));
        registry.put(FeeApi.DeleteNode, new NoParametersAPI("Miscellaneous", FeeApi.DeleteNode, "Delete a node from the address book"));
        registry.put(FeeApi.UpdateNode, new NoParametersAPI("Miscellaneous", FeeApi.UpdateNode, "Modify node attributes"));
        registry.put(FeeApi.BatchTransaction, new NoParametersAPI("Miscellaneous", FeeApi.BatchTransaction, "Submit outer transaction containing a batch"));
    }
}
