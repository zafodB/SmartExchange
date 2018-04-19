package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deploy_contract, container, false);

        ethBalance = view.findViewById(R.id.ethBallanceCurrent);
        refreshBalance();

        refreshBalanceButton = view.findViewById(R.id.refreshBalanceButton);

        refreshBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshBalance();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    void refreshBalance(){
        mListener.onFragmentInteraction(MainActivity.ETH_BALANCE_UPDATE);
    }

    @Override
    public void pushBalance(String bal) {
        if (bal == null){
            ethBalance.setText("Error");
        } else {
            ethBalance.setText(bal);
        }
    }
}
