package com.hedera.node.app.hapi.fees.apis.common;

public enum FeeApi {
    // Crypto
    CryptoCreate,
    CryptoTransfer,
    CryptoUpdate,
    CryptoDelete,
    CryptoGetAccountRecords,
    CryptoGetAccountBalance,
    CryptoGetInfo,
    CryptoApproveAllowance,
    CryptoDeleteAllowance,

    // HCS
    ConsensusCreateTopic,
    ConsensusUpdateTopic,
    ConsensusDeleteTopic,
    ConsensusSubmitMessage,
    ConsensusGetTopicInfo,

    // Token
    TokenCreate,
    TokenUpdate,
    TokenUpdateNfts,
    TokenMintFungible,
    TokenMintNonFungible,
    TokenTransfer,
    TokenDelete,
    TokenMint,
    TokenBurn,
    TokenPause,
    TokenUnpause,

    TokenAirdrop,
    TokenClaimAirdrop,
    TokenCancelAirdrop,
    TokenReject,

    TokenFeeScheduleUpdate,
    TokenAssociateToAccount,
    TokenDissociateFromAccount,
    TokenGrantKycToAccount,
    TokenRevokeKycFromAccount,
    TokenFreezeAccount,
    TokenUnfreezeAccount,
    TokenAccountWipe,
    TokenGetInfo,
    GetAccountNftInfo,
    TokenGetNftInfo,
    TokenGetNftInfos,


    // Smart Contracts
    ContractCreate,
    ContractUpdate,
    ContractDelete,
    ContractCall,
    EthereumTransaction,
    ContractGetInfo,
    ContractCallLocal,
    ContractGetBytecode,
    GetBySolidityID,
    ContractGetRecords,
    EthereumTransactionSuccess,
    EthereumTransactionFail,

    // File
    FileCreate,
    FileUpdate,
    FileDelete,
    FileAppend,
    FileGetContents,
    FileGetInfo,

    // Miscellaneous
    ScheduleCreate,
    ScheduleSign,
    ScheduleDelete,
    ScheduleGetInfo,

    GetVersionInfo,
    TransactionGetReceipt,
    TransactionGetRecord,
    SystemDelete,
    SystemUndelete,
    PrngTransaction,
    CreateNode,
    DeleteNode,
    UpdateNode,
    BatchTransaction,

    // These are not Hedera APIs, but these are the add-ons
    PerSignature,
    PerKey,
    PerHCSByte,
    PerFileByte,
    PerCryptoTransferAccount,
    PerGas,
    ConsensusCreateTopicCustomFeeSurcharge,
    ConsensusSubmitMessageCustomFeeSurcharge,
    TokenCreateCustomFeeSurcharge,
    TokenTransferCustomFeeSurcharge,
    TokenAirdropCustomFeeSurcharge,

    MaxApi;


    public static FeeApi fromString(String type) {
        return FeeApi.valueOf(type);
    }
}
