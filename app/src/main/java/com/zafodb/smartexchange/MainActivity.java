package com.zafodb.smartexchange;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

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
        }
    }

    @Override
    public void onFragmentInteraction(int interactionCase, TradeDeal dealInstance) {
        switch (interactionCase) {
            case FROM_DEPLOY_TO_VALIDATE:
                openValidationFragment(dealInstance);
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        File oldWallet = new File(getCacheDir().getPath() + "/" + walletFileName);
//
//        oldWallet.delete();

        super.onDestroy();
    }

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

    void openValidationFragment(TradeDeal tradeDeal){
        ValidateDeploy newFragment = new ValidateDeploy();
        Bundle args = new Bundle();
        args.putSerializable("trade_deal_instance", tradeDeal);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment, "validateFragTag");
        transaction.addToBackStack(null);

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

                if (bal != null) {
                    pusher.pushBalance(Web3jwrapper.ethBalanceToString(bal));
                } else {
                    pusher.pushBalance("Error");
                }
            }
        }).start();
    }

    public interface PushDataToFragment {
        void pushBalance(String bal);
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
