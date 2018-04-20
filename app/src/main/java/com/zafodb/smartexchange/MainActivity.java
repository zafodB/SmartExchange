package com.zafodb.smartexchange;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.zafodb.smartexchange.UI.DeployContract;
import com.zafodb.smartexchange.UI.WalletPick;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
//        walletFileName = Web3jwrapper.createNewWallet(getApplicationContext());

//        createCacheWallet();

        walletFileName = "UTC--2018-04-19T14-54-48.712--967587b42d9425fa2c8d01de0dc8da00eb246804.json";
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
        File wallet = new File(getApplicationContext().getCacheDir().getPath()+"/UTC--2018-04-19T14-54-48.712--967587b42d9425fa2c8d01de0dc8da00eb246804.json");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(wallet, true));
            writer.append("{\"address\":\"00f42f5423f199998c48a50b9ec39df44e36836b\",\"id\":\"0189fdc8-b21a-4d9d-a168-bcbb74f1bde2\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"46dfeec16f9b35b4da9745f5581191e6\"},\"ciphertext\":\"ab0488e3b279428de1545f8ddac65d9902fce6fc779638bad522ddd09c915158\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"e11807215494f603dfb49c89456632363d7418f5dced9737b51dfb21e012f5a1\"},\"mac\":\"8c08582e75aa4eae1594f38ff30b4b036e7b9db685cce7351a01494b5efb16c5\"}}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
