package com.zafodb.smartexchange.UI;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;
import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.TradeDeal;
import com.zafodb.smartexchange.Wrappers.FirebaseWrapper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalletPickFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ValidateDeployFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidateDeployFragment extends CustomFragment implements MainActivity.FragmentUpdateListener {

    private TradeDeal tradeDeal;
    private BtcOffer mOffer;

    int offerStatus;

    private ValueEventListener mValueVentListener;
    private WalletPickFragment.OnFragmentInteractionListener mListener;

    Button validateTrue;

    public static ValidateDeployFragment newInstance(TradeDeal tradeDeal, BtcOffer btcOffer) {
        ValidateDeployFragment fragment = new ValidateDeployFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.VALIDATE_DEPLOY_PARAM1, tradeDeal);
        args.putSerializable(Constants.VALIDATE_DEPLOY_PARAM2, btcOffer);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tradeDeal = (TradeDeal) getArguments().getSerializable(Constants.VALIDATE_DEPLOY_PARAM1);
            mOffer = (BtcOffer) getArguments().getSerializable(Constants.VALIDATE_DEPLOY_PARAM2);

            mValueVentListener = FirebaseWrapper.getOfferStatus(getActivity(), this, mOffer);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_validate_deploy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView ethAmount = view.findViewById(R.id.textEthBalanceConf);
        TextView btcAddress = view.findViewById(R.id.textBtcAddressConf);
        TextView btcAmount = view.findViewById(R.id.textBtcAmountConf);
        TextView ethAddress = view.findViewById(R.id.textEthAddressConf);

        ethAmount.setText(String.valueOf(tradeDeal.getAmountWei()));
        btcAmount.setText(String.valueOf(tradeDeal.getAmountSatoshi()));
        btcAddress.setText(tradeDeal.getDestinationBtcAddress());
        ethAddress.setText(tradeDeal.getDestinationEthAddress());

        validateTrue = view.findViewById(R.id.buttonValidateTrue);
        validateTrue.setClickable(false);
        validateTrue.setBackgroundColor(Color.GRAY);
        validateTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.VALIDATION_SUCCESSFUL);
            }
        });

        Button validateFalse = view.findViewById(R.id.buttonValidateFalse);
        validateFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.VALIDATION_DENIED_BY_USER);
            }
        });
    }

    public void onButtonPressed(int interactionCase) {
        if (mListener != null) {
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
    public String getFragmentTag() {
        return Constants.VALIDATE_FRAGMENT_TAG;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        FirebaseWrapper.removeOfferDataValueListener(mOffer, mValueVentListener);
    }

    @Override
    public void pushUpdate(Bundle args) {
        offerStatus = args.getInt(Constants.OFFER_STATUS_TAG);

        if (offerStatus == Constants.DATA_STATUS_THEY_CONFIRMED) {
            validateTrue.setClickable(false);
            validateTrue.setBackgroundColor(Color.GRAY);

        } else if (offerStatus == Constants.DATA_STATUS_YOU_CONFIRMED) {
            validateTrue.setClickable(true);
            validateTrue.setBackgroundColor(Color.BLUE);

        }
    }
}
