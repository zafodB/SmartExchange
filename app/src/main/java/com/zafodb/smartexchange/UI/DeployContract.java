package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.zafodb.smartexchange.Barcode.BarcodeReaderActivity;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.TradeDeal;
import com.zafodb.smartexchange.ValidationException;
import com.zafodb.smartexchange.Wrappers.Web3jwrapper;

import java.math.BigInteger;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link DeployContract.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link DeployContract#newInstance} factory method to
// * create an instance of this fragment.
// */
public class DeployContract extends Fragment implements MainActivity.FragmentUpdateListener {

    TextView ethBalance;
    ImageButton refreshBalanceButton;
    TextView btcAddress;
    TextView btcAmount;
    TextView ethAddress;

    private TradeDeal tradeDeal;

    private WalletPick.OnFragmentInteractionListener mListener;

    public static DeployContract newInstance() {
        return new DeployContract();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            walletFilename = getArguments().getString(Constants.DEPLOY_CONTRACT_PARAM1);
//            walletAddress = getArguments().getString(Constants.DEPLOY_CONTRACT_PARAM2);
        }

        tradeDeal = TradeDeal.makeEmptyDeal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deploy_contract, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ethBalance = view.findViewById(R.id.ethBallanceCurrent);
        refreshBalance();

        btcAddress = view.findViewById(R.id.inputBtcAddress);
        btcAmount = view.findViewById(R.id.inputBtcAmount);
        ethAddress = view.findViewById(R.id.inputEthAddress);

        refreshBalanceButton = view.findViewById(R.id.buttonRefreshBalance);
        refreshBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshBalance();
            }
        });

        Button validateButton = view.findViewById(R.id.button_validate_proceed);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput(btcAddress.getText().toString(), btcAmount.getText().toString(), ethAddress.getText().toString())) {

                    MainActivity activity = (MainActivity) getActivity();
                    activity.setmTradeDeal(tradeDeal);

                    onButtonPressed(Constants.FROM_DEPLOY_TO_VALIDATE);
                } else Log.i("FILIP", "Something wrong");
            }
        });

        Button qrEthAddress = view.findViewById(R.id.buttonQrEth);
        qrEthAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQrReader(BarcodeReaderActivity.READ_ETH_ADDRESS);
            }
        });

        Button qrBtcAddress = view.findViewById(R.id.buttonQrBtc);
        qrBtcAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQrReader(BarcodeReaderActivity.READ_BTC_ADDRESS);
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

    void refreshBalance() {
        mListener.onFragmentInteraction(Constants.ETH_BALANCE_UPDATE);
    }

    @Override
    public void pushUpdate(Bundle args) {
        BigInteger balance = (BigInteger) args.getSerializable(Constants.BALANCE_AS_BIGINTEGER);

        if (balance == null) {
            ethBalance.setText("Error");
        } else {
            tradeDeal.setAmountWei(balance);
            ethBalance.setText(Web3jwrapper.ethBalanceToString(balance, Constants.BALANCE_DISPLAY_DECIMALS));
        }
    }

    boolean validateInput(String address, String btcToMonitor, String destEthAddress) {
        try {
            tradeDeal.setDestinationBtcAddress(address);
            tradeDeal.setAmountSatoshi(btcToMonitor);
            tradeDeal.setDestinationEthAddress(destEthAddress);
        } catch (ValidationException e) {
            Log.e("FILIP", e.getMessage());
//            TODO present more user friendly form of error message
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

//        TODO Actually check and verify ETH balance
        if(tradeDeal.getAmountWei() == null){
            return false;
        }

        Log.i("FILIP", "Validation successful.");
        return true;
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
                case BarcodeReaderActivity.READ_BTC_ADDRESS:
                    btcAddress.setText(barcode.displayValue);
                    break;

                case BarcodeReaderActivity.READ_ETH_ADDRESS:
                    ethAddress.setText(barcode.displayValue);
                    break;

                default:
                    Log.e("FILIP", "Could not capture QR code." + CommonStatusCodes.getStatusCodeString(resultCode));
            }

        } else super.onActivityResult(requestCode, resultCode, data);
    }


}
