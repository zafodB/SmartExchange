package com.zafodb.smartexchange.Wrappers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.UI.ConfirmDialogFragment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirebaseWrapper {

    private static FirebaseDatabase fireData;
//    private static FirebaseAuth fieAuth;
//    private static FirebaseUser fireUser;

    static {
        fireData = FirebaseDatabase.getInstance();
//        fireAuth = FirebaseAuth.getInstance();
    }

//    public static void signIn(Activity activity) {
//        fireAuth.signInAnonymously()
//                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            fireUser = fireAuth.getCurrentUser();
//                            goOn = true;
//
//                            Log.d("FILIP", "signInAnonymously:success");
//                        } else {
//                            // If sign in fails, display a message to the user.
////                            TODO: inform user about the failure
//                            Log.w("FILIP", "signInAnonymously:failure", task.getException());
//                        }
//                    }
//                });
//    }

    public static void createBtcOffer(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);

        String offerUID = offer.getOfferId().toString();

        ref.child(offerUID).child(Constants.DATA_OFFER_BTC).setValue(offer.getAmountSatoshiOffered().toString());
        ref.child(offerUID).child(Constants.DATA_OFFER_ETH).setValue(offer.getAmountWeiWanted().toString());
        ref.child(offerUID).child(Constants.DATA_OFFER_NICKNAME).setValue(offer.getNickname());
        ref.child(offerUID).child(Constants.DATA_OFFER_ETHADDRESS).setValue(offer.getDestinationEthAddress());
        ref.child(offerUID).child(Constants.DATA_OFFER_STATUS).setValue(Constants.DATA_STATUS_NEW);
    }

    public static void deleteOwnOffer(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);

        ref.child(offer.getOfferId().toString()).removeValue();
    }

    public static ValueEventListener getOfferStatus(final Activity activity, BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.FragmentUpdateListener pusher =
                        (MainActivity.FragmentUpdateListener) activity
                                .getFragmentManager()
                                .findFragmentByTag(Constants.OFFER_STATUS_FRAGMENT_TAG);

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

    public static void removeOfferDataValueListener(BtcOffer offer, ValueEventListener listener){
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS);

        ref.child(offer.getOfferId().toString()).removeEventListener(listener);

    }

    public static void confirmBtcOfferFirst(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS).child(offer.getOfferId().toString());
        ref.child(Constants.DATA_OFFER_STATUS).setValue(Constants.DATA_STATUS_YOU_CONFIRMED);
    }

    public static void confirmBtcOfferSecond(BtcOffer offer) {
        DatabaseReference ref = fireData.getReference(Constants.DATA_BTC_OFFERS).child(offer.getOfferId().toString());
        ref.child(Constants.DATA_OFFER_STATUS).setValue(Constants.DATA_STATUS_THEY_CONFIRMED);
    }

    public static ValueEventListener fetchExistingOffers(final Activity activity) {
        DatabaseReference ref = fireData.getReference();
//
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<BtcOffer> offers = new ArrayList<>();

                for (DataSnapshot databaseOffer : dataSnapshot.getChildren()) {

                    Long status = (Long) databaseOffer.child(Constants.DATA_OFFER_STATUS).getValue();
                    if (status != null) {
                        int offerStatus = status.intValue();
                        if (offerStatus != Constants.DATA_STATUS_NEW) {
                            continue;
                        }

                        BigInteger satoshiOffered =  new BigInteger((String) databaseOffer
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

                        BtcOffer offer = new BtcOffer(satoshiOffered, weiWanted, ethAddress, nickname);

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
                Log.d("FILIP", "databse error");
            }
        };

        ref.child(Constants.DATA_BTC_OFFERS).addValueEventListener(listener);

        return listener;


//        ref.addValueEventListener(listener);


//        DEVELOP SECTION START
//        BigInteger satoshi = new BigInteger("54261");
//        BigInteger wei = new BigInteger("8989997");
//        String address = "0x98945480002160ffds465";
//        String name = "zafod";
//
//        BtcOffer offer1 = new BtcOffer(satoshi, wei, address, name);
//
//        satoshi = new BigInteger("40000");
//        wei = new BigInteger("7770000000");
//        address = "0x11111ffffffaaaaa";
//        name = "Filip";
//
//        BtcOffer offer2 = new BtcOffer(satoshi, wei, address, name);
//
//
//        offers.add(offer1);
//        offers.add(offer2);

//        DEVELOP SECTION END

//        return offers;
    }

    public static void removeOffersListener(ValueEventListener listener){
        DatabaseReference ref = fireData.getReference();
//
        ref.child(Constants.DATA_BTC_OFFERS).removeEventListener(listener);
    }
}
