package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
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
import com.zafodb.smartexchange.BitcoinjWrapper;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.TradeDeal;
import com.zafodb.smartexchange.Web3jwrapper;

import java.math.BigInteger;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link DeployContract.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link DeployContract#newInstance} factory method to
// * create an instance of this fragment.
// */
public class DeployContract extends Fragment implements MainActivity.PushDataToFragment {
    private static final String ARG_PARAM1 = "walletFileName";
    private static final String ARG_PARAM2 = "walletAddress";

    // TODO: Rename and change types of parameters
    private String walletFilename;
    private String walletAddress;

    TextView ethBalance;
    ImageButton refreshBalanceButton;
    TextView btcAddress;
    TextView btcAmount;
    TextView ethAddress;

    private TradeDeal tradeDeal;

    private WalletPick.OnFragmentInteractionListener mListener;

    public DeployContract() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DeployContract.
     */
    // TODO: Rename and change types and number of parameters
    public static DeployContract newInstance(String param1, String param2) {
        DeployContract fragment = new DeployContract();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            walletFilename = getArguments().getString(ARG_PARAM1);
            walletAddress = getArguments().getString(ARG_PARAM2);
        }

        tradeDeal = TradeDeal.makeEmptyDeal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deploy_contract, container, false);

        ethBalance = view.findViewById(R.id.ethBallanceCurrent);
        refreshBalance();

        btcAddress = view.findViewById(R.id.btcAddressInput);
        btcAmount = view.findViewById(R.id.btcAmountInput);
        ethAddress = view.findViewById(R.id.ethAddressInput);

        refreshBalanceButton = view.findViewById(R.id.refreshBalanceButton);
        refreshBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshBalance();
            }
        });

        Button validateButton = view.findViewById(R.id.validateDeployBtn);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput(btcAddress.getText().toString(), btcAmount.getText().toString(), ethAddress.getText().toString())) {
                    onButtonPressed(MainActivity.FROM_DEPLOY_TO_VALIDATE);
                } else Log.i("FILIP", "Something wrong");
            }
        });

        Button qrEthAddress = view.findViewById(R.id.qrEthAddress);
        qrEthAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQrReader(BarcodeReaderActivity.READ_ETH_ADDRESS);
            }
        });

        Button qrBtcAddress = view.findViewById(R.id.qrBtcAddress);
        qrBtcAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQrReader(BarcodeReaderActivity.READ_BTC_ADDRESS);
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int interactionCase) {
        if (mListener != null) {
            mListener.onFragmentInteraction(interactionCase, tradeDeal);
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
        mListener.onFragmentInteraction(MainActivity.ETH_BALANCE_UPDATE);
    }

    @Override
    public void pushBalance(String bal) {
        if (bal == null) {
            ethBalance.setText("Error");
        } else {
            ethBalance.setText(bal);
        }
    }

    boolean validateInput(String address, String btcToMonitor, String destEthAddress) {

        try {
            tradeDeal.setDestinationBtcAddress(address);
            tradeDeal.setAmountSatoshi(btcToMonitor);
            tradeDeal.setDestinationEthAddress(destEthAddress);
        } catch (Exception e) {
            Log.e("FILIP", e.getMessage());
//            TODO present more user friendly form of error message
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

//        TODO Actually check and verify ETH balance
        tradeDeal.setAmountWei(new BigInteger("123"));

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
