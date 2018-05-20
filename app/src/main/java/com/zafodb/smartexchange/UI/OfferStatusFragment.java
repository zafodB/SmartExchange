package com.zafodb.smartexchange.UI;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ValueEventListener;
import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.Wrappers.FirebaseWrapper;

public class OfferStatusFragment extends CustomFragment implements MainActivity.FragmentUpdateListener, ConfirmDialogFragment.NoticeDialogListener {

    private BtcOffer mOffer;
    private String destinationBtcAddress;

    private TextView offerStatus;
    private TextView txLink;
    private Button continueButton;
    Button deleteOffer;

    private ValueEventListener mValueEventListener;

    private WalletPickFragment.OnFragmentInteractionListener mListener;

    public static OfferStatusFragment newInstance(BtcOffer offer) {
        OfferStatusFragment fragment = new OfferStatusFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.BTC_OFFER_PARAM1, offer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOffer = (BtcOffer) getArguments().getSerializable(Constants.BTC_OFFER_PARAM1);
        }

        mValueEventListener = FirebaseWrapper.getOfferStatus(getActivity(), this, mOffer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offer_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deleteOffer = view.findViewById(R.id.buttonDeleteOffer);
        deleteOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.DELETE_OWN_OFFER);
            }
        });

        offerStatus = view.findViewById(R.id.textOfferStatus);
        txLink = view.findViewById(R.id.textOfferHyperlink);

        continueButton = view.findViewById(R.id.buttonContinueOffer);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.CONTINUE_TO_SEND_BITCOIN);
            }
        });
    }

    public void onButtonPressed(int interactionCase) {
        if (mListener != null) {
            switch (interactionCase) {
                case Constants.DELETE_OWN_OFFER:
                    FirebaseWrapper.deleteOwnOffer(mOffer, mValueEventListener);
                    break;
                case Constants.CONTINUE_TO_SEND_BITCOIN:
                    MainActivity activity = (MainActivity) getActivity();
                    activity.setDestinationBtcAddress(destinationBtcAddress);
            }
            mListener.onFragmentInteraction(interactionCase);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletPickFragment.OnFragmentInteractionListener) {
            mListener = (WalletPickFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        FirebaseWrapper.removeOfferDataValueListener(mOffer, mValueEventListener);
    }

    private void openConfDialog() {
        DialogFragment newFragment = new ConfirmDialogFragment();
        newFragment.show(getFragmentManager(), Constants.CONFIRM_DIALOG_TAG);
    }


    @Override
    public void pushUpdate(Bundle args) {
        int status = args.getInt(Constants.OFFER_STATUS_TAG);
        switch (status) {
            case 11:
                offerStatus.setText(getString(R.string.text_status_sent));
                break;
            case 12:
                offerStatus.setText(getString(R.string.text_status_conf1));
                openConfDialog();
                break;
            case 13:
                offerStatus.setText(getString(R.string.text_status_conf2));
                break;
            case 14:
                offerStatus.setText(getString(R.string.text_status_accepted));
                continueButton.setEnabled(true);
                deleteOffer.setEnabled(false);

                String txHash = args.getString(Constants.OFFER_STATUS_TX_HASH);
                if (txHash != null){
                    txLink.setVisibility(View.VISIBLE);
                    txLink.setText(Constants.ETHERSCAN_KOVAN_REFERENCE + txHash);
                } else{
                    txLink.setText(getString(R.string.text_status_link_error));
                }

                destinationBtcAddress = args.getString(Constants.OFFER_STATUS_BTC_ADDRESS);
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FirebaseWrapper.setOfferStatus(mOffer, Constants.DATA_STATUS_YOU_CONFIRMED);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public String getFragmentTag() {
        return Constants.OFFER_STATUS_FRAGMENT_TAG;
    }
}
