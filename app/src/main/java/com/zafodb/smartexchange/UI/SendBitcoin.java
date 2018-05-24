package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.Wrappers.BitcoinWrapper;

import java.math.BigInteger;

public class SendBitcoin extends Fragment {
    private String destinationBtcAddress;
    private BigInteger bitcoinAmount;

    private TextView timeLeft;

    private WalletPickFragment.OnFragmentInteractionListener mListener;


    public static SendBitcoin newInstance(String btcDestinaiton, BigInteger btcAmount) {
        SendBitcoin fragment = new SendBitcoin();
        Bundle args = new Bundle();
        args.putString(Constants.DESTINATION_BTC_ADDRESS_PARAM, btcDestinaiton);
        args.putSerializable(Constants.DESTINATION_BTC_AMOUNT_PARAM, btcAmount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            destinationBtcAddress = getArguments().getString(Constants.DESTINATION_BTC_ADDRESS_PARAM);
            bitcoinAmount = (BigInteger) getArguments().getSerializable(Constants.DESTINATION_BTC_AMOUNT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_bitcoin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textBtcAmount = view.findViewById(R.id.textSendBitcoinAmount);
        textBtcAmount.setText(BitcoinWrapper.satoshiToBtcString(bitcoinAmount, 4));

        TextView textBtcAddress = view.findViewById(R.id.textSendBitcoinAddress);
        textBtcAddress.setText(destinationBtcAddress);

        timeLeft = view.findViewById(R.id.textRemainingTime);

        myTimer counter = new myTimer(600000,1000);
        counter.start();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    class myTimer extends CountDownTimer {
        myTimer (long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft.setText(String.valueOf(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            timeLeft.setText("0");
        }
    }
}
