package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WalletPick#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletPick extends Fragment {

    private String walletAddress;

    private OnFragmentInteractionListener mListener;

    public static WalletPick newInstance(String param1) {
        WalletPick fragment = new WalletPick();
        Bundle args = new Bundle();
        args.putString(Constants.WALLET_PICK_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            walletAddress = getArguments().getString(Constants.WALLET_PICK_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet_pick, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView newPublicAddress = view.findViewById(R.id.textNewPublicAddress);

        newPublicAddress.setText(walletAddress);

        Button continueButton = view.findViewById(R.id.buttonContinue);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.FROM_WALLET_PICK_TO_DEPLOY);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int interactionCase);
    }
}
