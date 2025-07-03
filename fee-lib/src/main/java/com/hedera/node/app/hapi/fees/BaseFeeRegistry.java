package com.hedera.node.app.hapi.fees;
import com.hedera.node.app.hapi.fees.apis.common.FeeApi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class BaseFeeRegistry {

    private static final Map<FeeApi, Double> BASE_FEES;

    static {
        Map<FeeApi, Double> fees = new HashMap<>();
        // Addons
        fees.put(FeeApi.PerSignature, 0.0001);
        fees.put(FeeApi.PerKey, 0.01);
        fees.put(FeeApi.PerHCSByte, 0.000011);
        fees.put(FeeApi.PerFileByte, 0.000011);
        fees.put(FeeApi.PerCryptoTransferAccount, 0.00001);
        fees.put(FeeApi.PerGas, 0.0000000852);
        fees.put(FeeApi.ConsensusCreateTopicCustomFeeSurcharge, 1.99);
        fees.put(FeeApi.ConsensusSubmitMessageCustomFeeSurcharge, 0.0499);
        fees.put(FeeApi.TokenCreateCustomFeeSurcharge, 1.00000);
        fees.put(FeeApi.TokenTransferCustomFeeSurcharge, 0.001);
        fees.put(FeeApi.TokenAirdropCustomFeeSurcharge, 0.001);

        // Crypto service
        fees.put(FeeApi.CryptoCreate, 0.05000);
        fees.put(FeeApi.CryptoUpdate, 0.00022);
        fees.put(FeeApi.CryptoTransfer, 0.00010);
        fees.put(FeeApi.CryptoDelete, 0.00500);
        fees.put(FeeApi.CryptoGetAccountRecords, 0.00010);
        fees.put(FeeApi.CryptoGetAccountBalance, 0.00000);
        fees.put(FeeApi.CryptoGetInfo, 0.00010);
        fees.put(FeeApi.CryptoApproveAllowance, 0.05000);
        fees.put(FeeApi.CryptoDeleteAllowance, 0.05000);

        // HCS
        fees.put(FeeApi.ConsensusCreateTopic, 0.01000);
        fees.put(FeeApi.ConsensusUpdateTopic, 0.00022);
        fees.put(FeeApi.ConsensusDeleteTopic, 0.00500);
        fees.put(FeeApi.ConsensusSubmitMessage, 0.00010);
        fees.put(FeeApi.ConsensusGetTopicInfo, 0.00010);

        // HTS
        fees.put(FeeApi.TokenCreate, 1.00000);
        fees.put(FeeApi.TokenDelete, 0.00100);
        fees.put(FeeApi.TokenUpdate, 0.00100);
        fees.put(FeeApi.TokenMintFungible, 0.00100);
        fees.put(FeeApi.TokenMintNonFungible, 0.02000);
        fees.put(FeeApi.TokenBurn, 0.00100);
        fees.put(FeeApi.TokenPause, 0.00100);
        fees.put(FeeApi.TokenUnpause, 0.00100);
        fees.put(FeeApi.TokenGetInfo, 0.00010);
        fees.put(FeeApi.TokenGrantKycToAccount, 0.00100);
        fees.put(FeeApi.TokenRevokeKycFromAccount, 0.00100);
        fees.put(FeeApi.TokenFreezeAccount, 0.00100);
        fees.put(FeeApi.TokenUnfreezeAccount, 0.00100);
        fees.put(FeeApi.TokenAccountWipe, 0.00100);
        fees.put(FeeApi.TokenAssociateToAccount, 0.05000);
        fees.put(FeeApi.TokenDissociateFromAccount, 0.05000);
        fees.put(FeeApi.TokenTransfer, 0.001);
        fees.put(FeeApi.TokenAirdrop, 0.10000);
        fees.put(FeeApi.TokenClaimAirdrop, 0.00100);
        fees.put(FeeApi.TokenCancelAirdrop, 0.00100);
        fees.put(FeeApi.TokenReject, 0.00100);
        fees.put(FeeApi.TokenFeeScheduleUpdate, 0.00100);
        fees.put(FeeApi.GetAccountNftInfo, 0.00010);
        fees.put(FeeApi.TokenGetNftInfo, 0.00010);
        fees.put(FeeApi.TokenGetNftInfos, 0.00010);

        // Scheduled Transactions
        fees.put(FeeApi.ScheduleCreate, 0.01000);
        fees.put(FeeApi.ScheduleSign, 0.00100);
        fees.put(FeeApi.ScheduleDelete, 0.00100);
        fees.put(FeeApi.ScheduleGetInfo, 0.00010);

        // Smart Contracts

        fees.put(FeeApi.ContractCreate, 1.00000);
        fees.put(FeeApi.ContractUpdate, 0.02600);
        fees.put(FeeApi.ContractDelete, 0.00700);
        fees.put(FeeApi.ContractCall, 0.00000);
        fees.put(FeeApi.ContractCallLocal, 0.00100);
        fees.put(FeeApi.ContractGetBytecode, 0.05000);
        fees.put(FeeApi.GetBySolidityID, 0.00010);
        fees.put(FeeApi.ContractGetInfo, 0.00010);
        fees.put(FeeApi.ContractGetRecords, 0.00010);
        fees.put(FeeApi.EthereumTransactionSuccess, 0.00000);
        fees.put(FeeApi.EthereumTransactionFail, 0.00010);
        // Files
        fees.put(FeeApi.FileCreate, 0.05000);
        fees.put(FeeApi.FileUpdate, 0.05000);
        fees.put(FeeApi.FileDelete, 0.00700);
        fees.put(FeeApi.FileAppend, 0.05000);
        fees.put(FeeApi.FileGetContents, 0.00010);
        fees.put(FeeApi.FileGetInfo, 0.00010);

        // Misc
        fees.put(FeeApi.GetVersionInfo, 0.00010);
        fees.put(FeeApi.TransactionGetReceipt, 0.00000);
        fees.put(FeeApi.TransactionGetRecord, 0.00010);
        fees.put(FeeApi.SystemDelete, 0.00000);
        fees.put(FeeApi.SystemUndelete, 0.00000);
        fees.put(FeeApi.CreateNode, 0.00100);
        fees.put(FeeApi.DeleteNode, 0.00100);
        fees.put(FeeApi.UpdateNode, 0.00100);
        fees.put(FeeApi.PrngTransaction, 0.00100);
        fees.put(FeeApi.BatchTransaction, 0.00100);

        BASE_FEES = Collections.unmodifiableMap(fees);
    }

    private BaseFeeRegistry() {
        // prevent instantiation
    }

    public static double getBaseFee(FeeApi api) {
        return BASE_FEES.getOrDefault(api, 0.0);
    }

}
