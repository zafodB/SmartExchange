package com.zafodb.smartexchange;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.zafodb.smartexchange.UI.ContractSent;
import com.zafodb.smartexchange.UI.DeployContract;
import com.zafodb.smartexchange.UI.ValidateDeploy;
import com.zafodb.smartexchange.UI.WalletPick;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class MainActivity extends Activity implements WalletPick.OnFragmentInteractionListener {

    public static final int FROM_WALLET_PICK_TO_DEPLOY = 564;
    public static final int ETH_BALANCE_UPDATE = 565;
    public static final int FROM_DEPLOY_TO_VALIDATE = 567;
    public static final int VALIDATION_UNSUCCESSFUL = 568;
    public static final int VALIDATION_SUCCESSFUL = 589;

    public TradeDeal getTradeDeal() {
        return tradeDeal;
    }

    public void setTradeDeal(TradeDeal tradeDeal) {
        this.tradeDeal = tradeDeal;
    }

    private TradeDeal tradeDeal;

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    private String transactionHash;

    String walletFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareWallet();

        WalletPick walletPickFragment = WalletPick.newInstance(getWalletAddress());

        getFragmentManager().beginTransaction()
                .addToBackStack(walletPickFragment.toString())
                .replace(R.id.fragment_container, walletPickFragment).commit();
    }

    @Override
    public void onFragmentInteraction(int interactionCase) {

        switch (interactionCase) {
            case FROM_WALLET_PICK_TO_DEPLOY:
                openDeployingFragment();
                break;
            case ETH_BALANCE_UPDATE:
                updateEthBalance();
                break;
            case VALIDATION_UNSUCCESSFUL:
                onBackPressed();
                break;
            case FROM_DEPLOY_TO_VALIDATE:
                openValidationFragment(tradeDeal);
                break;
            case VALIDATION_SUCCESSFUL:
                openSentFragment();
                deployContract();
        }
    }

    @Override
    protected void onDestroy() {
//        File oldWallet = new File(getCacheDir().getPath() + "/" + walletFileName);
//
//        oldWallet.delete();

        super.onDestroy();
    }

    //    TODO repeated method 1/3
    void openDeployingFragment() {
        DeployContract newFragment = new DeployContract();
        Bundle args = new Bundle();
        args.putString("walletFileName", walletFileName);
        args.putString("walletAddress", getWalletAddress());
        newFragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment, "deployFragTag");

//        TODO look at what addtobackstack does
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    //    TODO repeated method 2/3
    void openValidationFragment(TradeDeal tradeDeal){
        ValidateDeploy newFragment = new ValidateDeploy();
        Bundle args = new Bundle();
        args.putSerializable("trade_deal_instance", tradeDeal);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment, "validateFragTag");
//        transaction.addToBackStack(null);

        transaction.commit();
    }

//    TODO repeated method 3/3
    void openSentFragment(){
        ContractSent newFragment = new ContractSent();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, "sentFragTag");
        transaction.commit();
    }

    private void prepareWallet() {

//        DISABLED TEMPORARILY
//        walletFileName = Web3jwrapper.createNewWallet(getApplicationContext());

//        createCacheWallet();

        walletFileName = "UTC--2018-04-21T19-27-42.888--12efbee9bbe117eef08190d5e144fd4d168421a5.json";
    }

    String getWalletAddress() {
        return Web3jwrapper.getWalletAddress(walletFileName, getApplicationContext());
    }

    void updateEthBalance() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BigInteger bal = Web3jwrapper.getAddressBalance(getWalletAddress());

                PushDataToFragment pusher = (PushDataToFragment) getFragmentManager().findFragmentByTag("deployFragTag");

                pusher.pushBalance(bal);

            }
        }).start();
    }

    public interface PushDataToFragment {
        void pushBalance(BigInteger bal);

        void pushTxHash(String txHash);
    }

    void deployContract(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                setTransactionHash(Web3jwrapper.deployContract(getApplicationContext(), walletFileName, tradeDeal));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PushDataToFragment pusher = (PushDataToFragment) getFragmentManager().findFragmentByTag("sentFragTag");

                        pusher.pushTxHash(getTransactionHash());
                    }
                });

            }
        }).start();

    }

//    METHOD USED ONLY DURING DEVELOPEMENT
    void createCacheWallet(){
        File wallet = new File(getApplicationContext().getCacheDir().getPath()+"/UTC--2018-04-21T19-27-42.888--12efbee9bbe117eef08190d5e144fd4d168421a5.json");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(wallet, true));
            writer.append("{\"address\":\"12efbee9bbe117eef08190d5e144fd4d168421a5\",\"id\":\"98a855a2-bc9d-4917-9901-480401ce6a21\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"37172def66efa979eaae2cff6c8da7e5\"},\"ciphertext\":\"ca7f8dcac9888c5f97b88e6d50f65792afa735ebae305d92f4f6497a6e298565\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"7c7ade5a559e98acfe8167f5204ee96a34c63c073436366c563cc2927e3ea0ae\"},\"mac\":\"a6eae01fd041b787562b73aa83a7a272de4fefddba47e558c087473ff0025d91\"}}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
