package com.zafodb.smartexchange;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.zafodb.smartexchange.UI.DeployContract;
import com.zafodb.smartexchange.UI.WalletPick;

import java.math.BigInteger;

public class MainActivity extends Activity implements WalletPick.OnFragmentInteractionListener {

    public static final int FROM_WALLET_PICK_TO_DEPLOY = 564;
    public static final int ETH_BALANCE_UPDATE = 565;

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
    public void onFragmentInteraction(int interaction_case) {

        switch (interaction_case) {
            case FROM_WALLET_PICK_TO_DEPLOY:
                openDeployingFragment();
                break;
            case ETH_BALANCE_UPDATE:
                updateEthBalance();
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

    private void prepareWallet() {

//        DISABLED TEMPORARILY
//        walletFileName = web3jwrapper.createNewWallet(getApplicationContext());

        walletFileName = "UTC--2018-04-19T14-54-48.712--967587b42d9425fa2c8d01de0dc8da00eb246804.json";
    }

    String getWalletAddress() {
        return web3jwrapper.getWalletAddress(walletFileName, getApplicationContext());
    }

    void updateEthBalance() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BigInteger bal = web3jwrapper.getAddressBalance(getWalletAddress());

                PushDataToFragment pusher = (PushDataToFragment) getFragmentManager().findFragmentByTag("deployFragTag");

                if (bal != null) {
                    pusher.pushBalance(web3jwrapper.ethBalanceToString(bal));
                } else {
                    pusher.pushBalance("Error");
                }
            }
        }).start();
    }

    public interface PushDataToFragment {
        void pushBalance(String bal);
    }
}
