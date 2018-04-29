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

public class OfferStatus extends Fragment implements MainActivity.FragmentUpdateListener, ConfirmDialogFragment.NoticeDialogListener {

    private BtcOffer mOffer;

    private TextView offerStatus;
    private Button continueButton;
    private ValueEventListener mValueEventListener;

    private WalletPick.OnFragmentInteractionListener mListener;

    public static OfferStatus newInstance(BtcOffer offer) {
        OfferStatus fragment = new OfferStatus();
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

        mValueEventListener = FirebaseWrapper.getOfferStatus(getActivity(), mOffer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offer_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button deleteOffer = view.findViewById(R.id.buttonDeleteOffer);
        deleteOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.DELETE_OWN_OFFER);
            }
        });

        offerStatus = view.findViewById(R.id.textOfferStatus);

        continueButton = view.findViewById(R.id.buttonContinueOffer);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This is clickable.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onButtonPressed(int interactionCase) {
        if (mListener != null) {
            switch (interactionCase) {
                case Constants.DELETE_OWN_OFFER:
                    FirebaseWrapper.deleteOwnOffer(mOffer);
            }
            mListener.onFragmentInteraction(interactionCase);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletPick.OnFragmentInteractionListener) {
            mListener = (WalletPick.OnFragmentInteractionListener) context;
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
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FirebaseWrapper.confirmBtcOfferFirst(mOffer);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
