package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.zafodb.smartexchange.Barcode.BarcodeReaderActivity;
import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.ValidationException;
import com.zafodb.smartexchange.Wrappers.BitcoinWrapper;
import com.zafodb.smartexchange.Wrappers.EthereumWrapper;

import java.math.BigInteger;

public class CreateOfferFragment extends CustomFragment {

    private WalletPickFragment.OnFragmentInteractionListener mListener;

    BtcOffer newOffer;

    TextView nickname;
    TextView ethAddress;
    TextView btcAmount;
    TextView ethAmount;


    public static CreateOfferFragment newInstance() {
        return new CreateOfferFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            newOffer = (BtcOffer) savedInstanceState.getSerializable(Constants.BTC_OFFER_TAG);
        } else {
            newOffer = new BtcOffer();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_offer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ethAddress = view.findViewById(R.id.inputOfferEthAddress);
        btcAmount = view.findViewById(R.id.inputOfferBtc);
        ethAmount = view.findViewById(R.id.inputOfferEth);
        nickname = view.findViewById(R.id.inputOfferNickname);

        Button qrEthAddress = view.findViewById(R.id.buttonOfferQr);
        qrEthAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQrReader(BarcodeReaderActivity.READ_ETH_ADDRESS);
            }
        });

        Button createNewOffer = view.findViewById(R.id.buttonCreateOffer);
        createNewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.CREATE_NEW_OFFER);
            }
        });

        updateUi();

        super.onViewCreated(view, savedInstanceState);
    }

    public void onButtonPressed(int interactionCase) {
        if (mListener != null) {
            switch (interactionCase) {
                case Constants.CREATE_NEW_OFFER:
                    if (validateInput()) {
                        MainActivity mActivity = (MainActivity) getActivity();
                        mActivity.setmNewOffer(newOffer);
                        mListener.onFragmentInteraction(interactionCase);
                    }
                    break;
            }
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

    private boolean validateInput(){
        try{
            newOffer.setAmountSatoshiOffered(btcAmount.getText().toString());
            newOffer.setAmountWeiWanted(EthereumWrapper.stringToWei(ethAmount.getText().toString()));

            String destinationEthAddress = ethAddress.getText().toString();
            if (!EthereumWrapper.validateAddress(destinationEthAddress)){
                throw new ValidationException("Eth address is not valid.");
            } else {
                newOffer.setDestinationEthAddress(destinationEthAddress);
            }

//            TODO handle nickanames
            newOffer.setNickname("Random name");

            return true;
        } catch (ValidationException ve){
//            TODO: handle errors
            Toast.makeText(getContext(), "Couldn't create offer due to invalid input.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void updateUi(){
        BigInteger satoshi = newOffer.getAmountSatoshiOffered();
        if (satoshi != null){
            btcAmount.setText(BitcoinWrapper.satoshiToBtcString(satoshi, 4));
        }

        String ethAddress = newOffer.getDestinationEthAddress();
        if (ethAddress != null){
            this.ethAddress.setText(ethAddress);
        }

        String nickname = newOffer.getNickname();
        if (nickname != null){
            this.nickname.setText(nickname);
        }

        BigInteger wei = newOffer.getAmountWeiWanted();
        if (wei != null){
            ethAmount.setText(EthereumWrapper.weiToString(wei, 4));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.BTC_OFFER_TAG, newOffer);
        super.onSaveInstanceState(outState);
    }

    void openQrReader(int readerRequest) {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext()) != ConnectionResult.SUCCESS) {
            Toast.makeText(getContext(), "Cannot use this feature right now.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getContext(), BarcodeReaderActivity.class);
            startActivityForResult(intent, readerRequest);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.BarcodeObject);

            switch (requestCode) {
                case BarcodeReaderActivity.READ_ETH_ADDRESS:
                    ethAddress.setText(barcode.displayValue);
                    break;
                default:
                    Log.e("FILIP", "Could not capture QR code." + CommonStatusCodes.getStatusCodeString(resultCode));
            }

        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getFragmentTag() {
        return Constants.CREATE_OFFER_TAG;
    }
}
