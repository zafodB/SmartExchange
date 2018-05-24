package com.zafodb.smartexchange;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ValueEventListener;
import com.zafodb.smartexchange.UI.ContractSentFragment;
import com.zafodb.smartexchange.UI.CreateOfferFragment;
import com.zafodb.smartexchange.UI.DeployContractFragment;
import com.zafodb.smartexchange.UI.OfferStatusFragment;
import com.zafodb.smartexchange.UI.OffersFragment;
import com.zafodb.smartexchange.UI.SendBitcoin;
import com.zafodb.smartexchange.UI.ValidateDeployFragment;
import com.zafodb.smartexchange.UI.WalletPickFragment;
import com.zafodb.smartexchange.Wrappers.EthereumWrapper;
import com.zafodb.smartexchange.Wrappers.FirebaseWrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @Author Filip Adamik
 */

public class MainActivity extends Activity implements WalletPickFragment.OnFragmentInteractionListener {

    private TradeDeal mTradeDeal;
    private BtcOffer mNewOffer;
    private BtcOffer mExistingOffer;
    private String mTransactionHash;
    private String mWalletFileName;
    private String destinationBtcAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

//        createCacheWallet();

        prepareWallet();
        openNewFragment(OffersFragment.newInstance(), Constants.OFFERS_FRAGMENT_TAG);
    }

    @Override
    public void onFragmentInteraction(int interactionCase) {
        switch (interactionCase) {
            case Constants.FROM_OFFERS_TO_WALLETPICK:
//                  TODO: Handle what offer was picked.
//                  TODO: see, if this is placed in a good spot.
                openNewFragment(WalletPickFragment.newInstance(getWalletAddress()), Constants.WALLET_PICK_FRAGMENT_TAG);
                break;
            case Constants.FROM_WALLET_PICK_TO_DEPLOY:
                openNewFragment(DeployContractFragment.newInstance(mExistingOffer), Constants.DEPLOY_FRAGMENT_TAG);
                break;
            case Constants.ETH_BALANCE_UPDATE:
                updateEthBalance();
                break;
            case Constants.VALIDATION_DENIED_BY_USER:
            case Constants.DELETE_OWN_OFFER:
                onBackPressed();
                break;
            case Constants.FROM_DEPLOY_TO_VALIDATE:
                openNewFragment(ValidateDeployFragment.newInstance(getmTradeDeal(), mExistingOffer), Constants.VALIDATE_FRAGMENT_TAG);
                break;
            case Constants.VALIDATION_SUCCESSFUL:
                openNewFragment(ContractSentFragment.newInstance(), Constants.SENT_FRAGMENT_TAG);
                deployContract();
                break;
            case Constants.OFFERS_UPDATE:
                updateOffers();
                break;
            case Constants.FROM_OFFERS_TO_NEWOFFER:
                openNewFragment(CreateOfferFragment.newInstance(), Constants.CREATE_OFFER_TAG);
                break;
            case Constants.CREATE_NEW_OFFER:
                FirebaseWrapper.createBtcOffer(mNewOffer);
                openNewFragment(OfferStatusFragment.newInstance(mNewOffer), Constants.OFFER_STATUS_FRAGMENT_TAG);
                break;
            case Constants.REMOVE_VALUE_EVENT_LISTENER:
                FirebaseWrapper.removeOffersListener();
                break;
            case Constants.CONTINUE_TO_SEND_BITCOIN:
                openNewFragment(SendBitcoin.newInstance(destinationBtcAddress, mNewOffer.getAmountSatoshiOffered()), Constants.SEND_BITCOIN_TAG);
                break;
        }
    }

    /**
     * If user presses back button after they just sent the contract, this method will prevent
     * {@link ValidateDeployFragment} from showing (to prevent accidental double-deploy.
     */
    @Override
    public void onBackPressed() {
        ValidateDeployFragment fragment = (ValidateDeployFragment) getFragmentManager().findFragmentByTag(Constants.VALIDATE_FRAGMENT_TAG);
        if (fragment != null) {
            getFragmentManager().popBackStack(WalletPickFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            CreateOfferFragment fragment1 = (CreateOfferFragment) getFragmentManager().findFragmentByTag(Constants.CREATE_OFFER_TAG);

            if (fragment1 != null) {
                getFragmentManager().popBackStack(CreateOfferFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * Should destroy the currently used wallet on destroy (as this should be empty).
     * However, this is not the ideal behaviour, should revisit this, whether it is needed.
     * <p>
     * TODO: revisit deleting the wallets on destroy.
     */
    @Override
    protected void onDestroy() {
//        File oldWallet = new File(getCacheDir().getPath() + "/" + mWalletFileName);
//
//        oldWallet.delete();

        super.onDestroy();
    }

    /**
     * Opens a fragment. Used to navigate forward in the app.
     *
     * @param fragment    Instance of fragment to be opened.
     * @param fragmentTag Fragment tag (defined in {@link Constants}).
     */
    private void openNewFragment(Fragment fragment, String fragmentTag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragmentTag);

        transaction.addToBackStack(fragment.getClass().getName());

        transaction.commit();
    }

    /**
     * Generates new wallet file. This wallet will be used to deploy the exchange contract.
     * User should transfer the funds to this wallet to continue.
     */
    private void prepareWallet() {

//        DISABLED TEMPORARILY
        mWalletFileName = EthereumWrapper.createNewWallet(getApplicationContext());

//        createCacheWallet();

//        mWalletFileName = "UTC--2018-04-21T19-27-42.888--12efbee9bbe117eef08190d5e144fd4d168421a5.json";
    }

    /**
     * Fetches the ETH address of the current wallet, using the {@link EthereumWrapper} class.
     *
     * @return kETH address formatted as string (and prefixed with '0x'.
     */
    private String getWalletAddress() {
        return EthereumWrapper.getWalletAddress(mWalletFileName, getApplicationContext());
    }

    /**
     * Updates eth balance of the current address, using the {@link EthereumWrapper} class.
     * <p>
     * Runs on a new thread, to avoid blocking UI.
     */
    private void updateEthBalance() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BigInteger bal = EthereumWrapper.getAddressBalance(getWalletAddress());

                FragmentUpdateListener pusher = (FragmentUpdateListener) getFragmentManager()
                        .findFragmentByTag(Constants.DEPLOY_FRAGMENT_TAG);

                Bundle args = new Bundle();
                args.putSerializable(Constants.BALANCE_AS_BIGINTEGER, bal);
                if (pusher != null) {
                    pusher.pushUpdate(args);
                } else {
                    Log.v("FILIP", "Couldn't push message, because fragment stopped existing.");
                }

            }
        }).start();
    }

    /**
     * Deploys new contract using {@link EthereumWrapper} class.
     * <p>
     * Since network communication is needed, the contract is deployed on a new separate thread, so
     * that UI is not blocked.
     * <p>
     * UI update needs to be carried back on the UI thread.
     */
    private void deployContract() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setmTransactionHash(EthereumWrapper.deployContract(getApplicationContext(), mWalletFileName, mTradeDeal));

                FirebaseWrapper.setOfferStatus(mExistingOffer, Constants.DATA_STATUS_ACCEPTED, getmTransactionHash(), mTradeDeal.getDestinationBtcAddress());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentUpdateListener pusher = (FragmentUpdateListener) getFragmentManager()
                                .findFragmentByTag(Constants.SENT_FRAGMENT_TAG);

                        Bundle args = new Bundle();
                        if (getmTransactionHash() == null){
                            args.putString(Constants.TRANSACTION_HASH, "There was an error");
                        } else {
                            args.putString(Constants.TRANSACTION_HASH, getmTransactionHash());
                        }

                        if (pusher != null) {
                            pusher.pushUpdate(args);
                        } else {
                            Log.v("FILIP", "Couldn't push message, because fragment stopped existing.");
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Attaches Value event listener from {@link FirebaseWrapper} class.
     *
     * @return ValueEventListener. This is needed to later dis-attach the listener.
     */
    public void updateOffers() {
        FirebaseWrapper.fetchExistingOffers(this);
    }

    public TradeDeal getmTradeDeal() {
        return mTradeDeal;
    }

    public void setmTradeDeal(TradeDeal mTradeDeal) {
        this.mTradeDeal = mTradeDeal;
    }

    public String getmTransactionHash() {
        return mTransactionHash;
    }

    public void setmNewOffer(BtcOffer mNewOffer) {
        this.mNewOffer = mNewOffer;
    }

    public void setmTransactionHash(String mTransactionHash) {
        this.mTransactionHash = mTransactionHash;
    }

    public void setmExistingOffer(BtcOffer mExistingOffer) {
        this.mExistingOffer = mExistingOffer;
    }

    public void setDestinationBtcAddress(String destinationBtcAddress) {
        this.destinationBtcAddress = destinationBtcAddress;
    }

    /**
     * Interface used to pushed new info to fragments.
     */
    public interface FragmentUpdateListener {
        void pushUpdate(Bundle args);
    }

    //    METHOD USED ONLY DURING DEVELOPEMENT
    void createCacheWallet() {
        File wallet = new File(getApplicationContext().getCacheDir().getPath() + "/UTC--2018-04-21T19-27-42.888--12efbee9bbe117eef08190d5e144fd4d168421a5.json");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(wallet, true));
            writer.append("{\"address\":\"12efbee9bbe117eef08190d5e144fd4d168421a5\",\"id\":\"98a855a2-bc9d-4917-9901-480401ce6a21\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"37172def66efa979eaae2cff6c8da7e5\"},\"ciphertext\":\"ca7f8dcac9888c5f97b88e6d50f65792afa735ebae305d92f4f6497a6e298565\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"7c7ade5a559e98acfe8167f5204ee96a34c63c073436366c563cc2927e3ea0ae\"},\"mac\":\"a6eae01fd041b787562b73aa83a7a272de4fefddba47e558c087473ff0025d91\"}}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
