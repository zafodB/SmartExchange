package com.zafodb.smartexchange.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.TradeDeal;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalletPick.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ValidateDeploy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidateDeploy extends Fragment {

    private TradeDeal tradeDeal;

    private WalletPick.OnFragmentInteractionListener mListener;

    public static ValidateDeploy newInstance(TradeDeal tradeDeal) {
        ValidateDeploy fragment = new ValidateDeploy();
        Bundle args = new Bundle();
        args.putSerializable(Constants.VALIDATE_DEPLOY_PARAM1, tradeDeal);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tradeDeal = (TradeDeal) getArguments().getSerializable(Constants.VALIDATE_DEPLOY_PARAM1);
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

        Button validateTrue = view.findViewById(R.id.buttonValidateTrue);
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
                onButtonPressed(Constants.VALIDATION_UNSUCCESSFUL);
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
    }
}
