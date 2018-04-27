package com.zafodb.smartexchange.Wrappers;

import android.content.Context;
import android.util.Log;

import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.SmartExchange1;
import com.zafodb.smartexchange.TradeDeal;
import com.zafodb.smartexchange.ValidationException;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Web3jwrapper {

    private final static Web3j web3j;

//    static DieselPrice dieselDeploy;

    static {
//        web3j = Web3jFactory.build(new HttpService("https://kovan.infura.io/ROrdzkoD6Ua0TH7cyaSh"));
//        web3j = Web3jFactory.build(new HttpService("https://kovan.infura.io/IlXkpW67R8mNHL0HDIdO"));
        web3j = Web3jFactory.build(new HttpService(Constants.INFURA_NODE_URL));
    }


//    static String getClientVerison() {
//
//        try {
//            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
//            return web3ClientVersion.getWeb3ClientVersion();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

//    static BigInteger getBlockNumber() {
//        try {
//            EthBlockNumber blockNo = web3j.ethBlockNumber().sendAsync().get();
//            return blockNo.getBlockNumber();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    static String deployReadyContractTest(Context context) {
//
//        try {
//            Credentials credentials = WalletUtils.loadCredentials("aaa", context.getFilesDir().getPath() + "/" + "UTC--2018-04-14T17-19-50.144--00f42f5423f199998c48a50b9ec39df44e36836b.json");
//
//            DieselPrice dieselDeploy = DieselPrice.deploy(
//                    web3j,
//                    credentials,
//                    new BigInteger("30000000000"),
//                    new BigInteger("3000000"),
//                    BigInteger.ZERO, BigInteger.valueOf(402)).sendAsync().get();
//
//            return "success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "failed";
//        }

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
//            return transactionResponse.getmTransactionHash();
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
//    }

    /**
     * Creates and sends contract to the network. Unlocks the wallet file from the cache and uses
     * it to deploy the contract. If successful, returns the transaction hash.
     * <p>
     * TODO: allow user to pick Gas price (and maybe Gas limit).
     *
     * @param context        Needed in order to find cache dir.
     * @param walletFilename Needed to locate wallet file.
     * @param initialValue   Ether that will be transferred to the other party.
     * @param btcAddress     Bitcoin address of the user (this will be queried in the contract).
     * @param ethAddress     Ether address of the other party (destination of Ether).
     * @param satoshiAmount  Queried Expected Bitcoin amount to arrive to user's Bitcoin wallet.
     * @return Transaction hash, if successful. Lame error message otherwise.
     */
    static private String sendContract(Context context, String walletFilename,
                                       BigInteger initialValue, String btcAddress,
                                       String ethAddress, String satoshiAmount) {
        try {
            Credentials credentials = WalletUtils.loadCredentials("aaa", context.getCacheDir().getPath() + "/" + walletFilename);

            SmartExchange1 smartExchange1 = SmartExchange1.deploy(
                    web3j,
                    credentials,
                    new BigInteger("30000000000"),
                    new BigInteger("3000000"),
                    initialValue,
                    btcAddress,
                    ethAddress,
                    satoshiAmount)
                    .sendAsync()
                    .get();

            TransactionReceipt receipt = smartExchange1.getTransactionReceipt();

            return receipt.getTransactionHash();

//            TODO: Proper Error handling.
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "Failed due to: " + ioe.getMessage();
        } catch (CipherException ce) {
            ce.printStackTrace();
            return "Failed due to: " + ce.getMessage();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            return "Failed due to: " + ie.getMessage();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
            return "Failed due to: " + ee.getMessage();
        }
    }

//    static String invokeDieselPriceUpdate(Context context) {
//
//        try {
//            Credentials credentials = WalletUtils.loadCredentials("aaa", context.getFilesDir().getPath() + "/" + "UTC--2018-04-14T17-19-50.144--00f42f5423f199998c48a50b9ec39df44e36836b.json");
//
//            DieselPrice contract = DieselPrice.load(
//                    "0xdDb813cC954994180edcF50aa9b3532e428Ac35E",
//                    web3j,
//                    credentials,
//                    new BigInteger("30000000000"),
//                    new BigInteger("7000000"));
//
//            TransactionReceipt transactionReceipt = contract.update(BigInteger.ZERO).sendAsync().get();
//
//            List eventValues = contract.getNewOraclizeQueryEvents(transactionReceipt);
//            String out = "";
//
//
//            for (Object l : eventValues) {
//
//                DieselPrice.NewOraclizeQueryEventResponse response = (DieselPrice.NewOraclizeQueryEventResponse) l;
//
//                Log.d("FILIP", response.description);
//                out = out + " " + response.description + " " + response.log;
//            }
//
//            return out;
////            return transactionReceipt.getmTransactionHash();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to invoke price update";
//        }
//    }

    public static String createNewFilter() {
        EthFilter myFilter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, "0xdDb813cC954994180edcF50aa9b3532e428Ac35E");

//        web3j.ethNewFilter(myFilter)

        String output = "";

        try {
            EthLog myLogs = web3j.ethGetLogs(myFilter).sendAsync().get();

            List results = myLogs.getLogs();

            List<String> transactions = new ArrayList<String>();


            for (Object result : results) {
                EthLog.LogResult res = (EthLog.LogResult) result;

                EthLog.LogObject myObj = (EthLog.LogObject) res.get();

                transactions.add(myObj.getTransactionHash());

            }


            for (String txHash : transactions) {
                EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(txHash).sendAsync().get();

                TransactionReceipt txRecpt = transactionReceipt.getTransactionReceipt();

                List txLogs = txRecpt.getLogs();

                for (Object myLog : txLogs) {
                    org.web3j.protocol.core.methods.response.Log oneLog = (org.web3j.protocol.core.methods.response.Log) myLog;

                    output = output + " " + oneLog.getData();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
            return "failed to get logs";

        }


        return output;
    }

    static String createNewWallet(Context context) {
//        TODO Remove temporary password.
        try {
            return WalletUtils.generateNewWalletFile("aaa", context.getCacheDir(), false);
        } catch (Exception e) {
            Log.e("FILIP", "Couldn't create new wallet because of exception.");

            e.printStackTrace();

            return null;
        }
    }

    public static String getWalletAddress(String walletFilename, Context context) {
        try {
            Credentials creds = WalletUtils.loadCredentials("aaa", context.getCacheDir().getPath() + "/" + walletFilename);

            return creds.getAddress();
        } catch (Exception e) {
            Log.e("FILIP", "Couldn't fetch wallet address because of exception.");
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Checks balance of queried address.
     *
     * @param address Ethereu address to check.
     * @return Balance in Wei.
     * <p>
     * TODO: Do proper error handling.
     */
    public static BigInteger getAddressBalance(String address) {
//        TODO remove temporary
//        TEMPORARY arrangement
//       address = "0x967587b42d9425fa2c8d01de0dc8da00eb246804";
//       address = "0x00f42f5423f199998c48a50b9ec39df44e36836b";
        address = "0x12eFbeE9BBE117EEf08190d5e144FD4D168421A5";

        try {
            EthGetBalance ethGetBalance = web3j
                    .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            return ethGetBalance.getBalance();

        } catch (InterruptedException ie) {
            Log.e("FILIP", "Couldn't fetch wallet address because of exception.");
            ie.printStackTrace();

            return null;
        } catch (ExecutionException ee) {
            Log.e("FILIP", "Couldn't fetch wallet address because of exception.");
            ee.printStackTrace();

            return null;
        }
    }

    /**
     * Converts amount of Wei (supplied by getBalance method) to user-readable format (Ether).
     *
     * @param wei      Address balance in Wei.
     * @param decimals Decimals displayed in result.
     * @return User-readable string, ending in " kETH".
     */
    public static String weiToString(BigInteger wei, int decimals) {
        BigDecimal divisor = new BigDecimal(Constants.WEIS_IN_ETHER);
        BigDecimal temp = new BigDecimal(wei);

        temp = temp.divide(divisor);
        temp = temp.setScale(decimals, RoundingMode.HALF_DOWN);

        String out = temp.toPlainString();

        return out + " kETH";
    }

    public static BigInteger stringToWei(String ethAsString) throws ValidationException {
        try {
            BigDecimal eth = new BigDecimal(ethAsString);

            eth = eth.multiply(new BigDecimal(Constants.WEIS_IN_ETHER));

            if (eth.compareTo(BigDecimal.ONE) <= 0) {
                throw new ValidationException("Amount is too small.");
            }

            return eth.toBigInteger();

//        TODO Let user know whats wrong (if exception is thrown)
        } catch (NumberFormatException ne) {
            throw new ValidationException(ne.getMessage(), ne.getCause());
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

    /**
     * Verifies whether the supplied String is a valid kETH address.
     *
     * @param address Supplied string (user input).
     * @return True if address is valid.
     */
    public static boolean validateAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }

    /**
     * Prepares contract for deployment. Subtracts the estimated contract deployment cost
     * (i.e. gas * gas price) from the amount that will be send with the contract.
     *
     * @param context        This is needed in order to read WalletFile from the cache.
     * @param walletFileName This is needed to locate the WalletFile
     * @param tradeDeal      This includes the details of the contract.
     * @return Transaction hash, if successful. 'Failed due to :' otherwise.
     * TODO: Revisit proper error handling.
     */
    public static String deployContract(Context context, String walletFileName, TradeDeal tradeDeal) {
        BigInteger amountToSend = tradeDeal.getAmountWei()
                .subtract(new BigInteger(Constants.ESTIMATED_CONTRACT_PRICE));

        return sendContract(context, walletFileName, amountToSend,
                tradeDeal.getDestinationBtcAddress(), tradeDeal.getDestinationEthAddress(),
                tradeDeal.getAmountSatoshi().toString());
    }
}
