package com.zafodb.smartexchange;

import android.content.Context;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.List;

public class web3jwrapper {

    private final static Web3j web3j;

    static DieselPrice dieselDeploy;

    static {
        web3j = Web3jFactory.build(new HttpService("https://kovan.infura.io/ROrdzkoD6Ua0TH7cyaSh"));
    }

    static String getClientVerison() {

        try {
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            return web3ClientVersion.getWeb3ClientVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static BigInteger getBlockNumber() {
        try {
            EthBlockNumber blockNo = web3j.ethBlockNumber().sendAsync().get();
            return blockNo.getBlockNumber();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static String createWallet(Context context) {
        try {
            return WalletUtils.generateNewWalletFile("aaa", context.getFilesDir(), false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static String deployReadyContract(Context context) {

        try {
            Credentials credentials = WalletUtils.loadCredentials("aaa", context.getFilesDir().getPath() + "/" + "UTC--2018-04-14T17-19-50.144--00f42f5423f199998c48a50b9ec39df44e36836b.json");

            DieselPrice dieselDeploy = DieselPrice.deploy(
                    web3j,
                    credentials,
                    new BigInteger("30000000000"),
                    new BigInteger("3000000"),
                    BigInteger.ZERO, BigInteger.valueOf(402)).sendAsync().get();

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }

//        byte[] bytes;
//        try {
//            bytes = Files.readAllBytes(Paths.get(context.getFilesDir().getPath() + "/" + "diesel.bin"));
//
//            Credentials credentials = WalletUtils.loadCredentials("aaa", context.getFilesDir().getPath() + "/" + "UTC--2018-04-14T17-19-50.144--00f42f5423f199998c48a50b9ec39df44e36836b.json");
//
//            Log.e("FILIP", "Check 0");
//
//            Transaction transaction = Transaction.createContractTransaction(
//                    credentials.getAddress(),
//                    getNonce(credentials.getAddress()),
//                    new BigInteger("30000000000"),
//                    new BigInteger("40000"),
//                    BigInteger.ZERO,
//                    new String(bytes));
//
//            Log.e("FILIP", "Check 1");
//            EthSendTransaction transactionResponse = web3j.send .ethSendTransaction(transaction).sendAsync().get();
//
//
//            return transactionResponse.getTransactionHash();
//        } catch (ClientConnectionException ce) {
//            Log.e("FILIP", "Check 2");
//            ce.getCause();
//            ce.getLocalizedMessage();
//        } catch (ExecutionException ee) {
//            ee.getLocalizedMessage();
//            ee.printStackTrace();
//
//        } catch (Exception e) {
//            Log.e("FILIP", "Check 3");
//            e.printStackTrace();
//        }
//
//
//        return "failed";
    }

    static String invokeDieselPriceUpdate(Context context) {

        try {
            Credentials credentials = WalletUtils.loadCredentials("aaa", context.getFilesDir().getPath() + "/" + "UTC--2018-04-14T17-19-50.144--00f42f5423f199998c48a50b9ec39df44e36836b.json");

            DieselPrice contract = DieselPrice.load(
                    "0xdDb813cC954994180edcF50aa9b3532e428Ac35E",
                    web3j,
                    credentials,
                    new BigInteger("30000000000"),
                    new BigInteger("7000000"));

            TransactionReceipt transactionReceipt = contract.update(BigInteger.ZERO).sendAsync().get();

            List eventValues = contract.getNewOraclizeQueryEvents(transactionReceipt);
            String out = "";


            for (Object l:eventValues){

                DieselPrice.NewOraclizeQueryEventResponse response = (DieselPrice.NewOraclizeQueryEventResponse) l;

                Log.d("FILIP", response.description);
                out = out + " " + response.description + " " + response.log;
            }

            return out;
//            return transactionReceipt.getTransactionHash();

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to invoke price update";
        }
    }

    static BigInteger getNonce(String address) {

        EthGetTransactionCount nonce;
        try {
            nonce = web3j
                    .ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            return nonce.getTransactionCount();

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
