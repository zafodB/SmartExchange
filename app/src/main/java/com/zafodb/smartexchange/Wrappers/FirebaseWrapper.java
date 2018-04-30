package com.zafodb.smartexchange.Wrappers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.UI.CustomFragment;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * FirebaseWrapper class wraps all the functionality that deals with Firebase.
 */
public class FirebaseWrapper {

    private static FirebaseDatabase fireData;
    private static ValueEventListener offersListener;

    static {
        fireData = FirebaseDatabase.getInstance();
    }

    /**
     * Fetch the list of existing offers from the database. Saves the reference to the ValueEventListener,
     * so that is can be deleted afterwards.
     *
     * @param activity Activity is needed to find the fragment that called this method. Updater
     *                 interface of this fragment will be then supplied with results.
     */
    public static void fetchExistingOffers(final Activity activity) {
        DatabaseReference ref = fireData.getReference();
        offersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<BtcOffer> offers = new ArrayList<>();
                for (DataSnapshot databaseOffer : dataSnapshot.getChildren()) {

                    Long status = (Long) databaseOffer.child(Constants.DATA_OFFER_STATUS).getValue();
                    if (status != null) {
                        String uuid = databaseOffer.getKey();

                        int offerStatus = status.intValue();
                        if (offerStatus != Constants.DATA_STATUS_NEW) {
                            continue;
                        }
                        BigInteger satoshiOffered = new BigInteger((String) databaseOffer
                                .child(Constants.DATA_OFFER_BTC)
                                .getValue());
                        BigInteger weiWanted = new BigInteger((String) databaseOffer
                                .child(Constants.DATA_OFFER_ETH)
                                .getValue());
                        String ethAddress = (String) databaseOffer
                                .child(Constants.DATA_OFFER_ETHADDRESS)
                                .getValue();
                        String nickname = (String) databaseOffer
                                .child(Constants.DATA_OFFER_NICKNAME)
                                .getValue();

                        BtcOffer offer = new BtcOffer(uuid, satoshiOffered, weiWanted, ethAddress, nickname);
                        offers.add(offer);
                    }
                }
                MainActivity.FragmentUpdateListener pusher =
                        (MainActivity.FragmentUpdateListener) activity
                                .getFragmentManager()
                                .findFragmentByTag(Constants.OFFERS_FRAGMENT_TAG);

                Bundle args = new Bundle();
                args.putSerializable(Constants.OFFERS_LIST, offers);

                pusher.pushUpdate(args);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                TODO: handle the error.
                Log.d("FILIP", "databse error");
            }
        };
        ref.child(Constants.DATA_BTC_OFFERS).addValueEventListener(offersListener);
    }

    /**
     * Create new Bitcoin offer in the database.
     *
     * @param offer Offer to create.
     *
     * TODO: Check, if offer was created.
     */
    public static void createBtcOffer(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);

        String offerUID = offer.getOfferId().toString();

        ref.child(offerUID).child(Constants.DATA_OFFER_BTC).setValue(offer.getAmountSatoshiOffered().toString());
        ref.child(offerUID).child(Constants.DATA_OFFER_ETH).setValue(offer.getAmountWeiWanted().toString());
        ref.child(offerUID).child(Constants.DATA_OFFER_NICKNAME).setValue(offer.getNickname());
        ref.child(offerUID).child(Constants.DATA_OFFER_ETHADDRESS).setValue(offer.getDestinationEthAddress());
        ref.child(offerUID).child(Constants.DATA_OFFER_STATUS).setValue(Constants.DATA_STATUS_NEW);
    }

    /**
     * Delete own offer from database.
     *
     * @param offer Offer to be deleted.
     */
    public static void deleteOwnOffer(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);
        ref.child(offer.getOfferId().toString()).removeValue();
    }

    /**
     * Get status of a particular offer.
     *
     * @param activity Needed to find a fragment that called this method.
     * @param fragment Needed to find a fragment that called this method to deliver back the result.
     * @param offer to check for status.
     * @return reference of ValueEventListener. This is needed in order to delete the listener at
     * a later time.
     */
    public static ValueEventListener getOfferStatus(final Activity activity, final CustomFragment fragment, BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.FragmentUpdateListener pusher =
                        (MainActivity.FragmentUpdateListener) activity
                                .getFragmentManager()
                                .findFragmentByTag(fragment.getFragmentTag());

                Long temp = (Long) dataSnapshot.child(Constants.DATA_OFFER_STATUS).getValue();
                int status = temp.intValue();

                Bundle args = new Bundle();
                args.putInt(Constants.OFFER_STATUS_TAG, status);

                pusher.pushUpdate(args);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: handle error.
            }
        };
        ref.child(offer.getOfferId().toString()).addValueEventListener(listener);
        return listener;
    }

    /**
     * Update status of a bitcoin offer after user selected the offer.
     * @param offer Offer to confirm.
     */
    public static void confirmBtcOfferFirst(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS).child(offer.getOfferId().toString());
        ref.child(Constants.DATA_OFFER_STATUS).setValue(Constants.DATA_STATUS_THEY_CONFIRMED);
    }

    /**
     * Update status of a bitcoin offer after the owner of this offer has confirmed.
     * @param offer Offer to confirm.
     */
    public static void confirmBtcOfferSecond(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS).child(offer.getOfferId().toString());
        ref.child(Constants.DATA_OFFER_STATUS).setValue(Constants.DATA_STATUS_YOU_CONFIRMED);
    }

    /**
     * Remove ValueEventListener from offers overview.
     */
    public static void removeOffersListener() {
        DatabaseReference ref = fireData.getReference();
        ref.child(Constants.DATA_BTC_OFFERS).removeEventListener(offersListener);
    }

    /**
     * Remove ValueEventListener from a particular offer.
     *
     * @param offer Offer to remove listener from.
     * @param listener listener to remove.
     */
    public static void removeOfferDataValueListener(BtcOffer offer, ValueEventListener listener) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);
        ref.child(offer.getOfferId().toString()).removeEventListener(listener);
    }
}
