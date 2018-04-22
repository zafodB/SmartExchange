package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.TradeDeal;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ValidateDeploy.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ValidateDeploy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidateDeploy extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    TextView ethAmount;
    TextView btcAddress;
    TextView btcAmount;
    TextView ethAddress;

    private TradeDeal tradeDeal;

    private WalletPick.OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        tradeDeal = activity.getTradeDeal();
    }

    public ValidateDeploy() {
        // Required empty public constructor

    }

    public static ValidateDeploy newInstance() {
        return new ValidateDeploy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validate_deploy, container, false);

        ethAmount = view.findViewById(R.id.confEthBal);
        btcAddress = view.findViewById(R.id.confBtcAddr);
        btcAmount = view.findViewById(R.id.confBtcAmount);
        ethAddress = view.findViewById(R.id.confEthAddr);

        ethAmount.setText(String.valueOf(tradeDeal.getAmountWei()));
        btcAmount.setText(String.valueOf(tradeDeal.getAmountSatoshi()));
        btcAddress.setText(tradeDeal.getDestinationBtcAddress());
        ethAddress.setText(tradeDeal.getDestinationEthAddress());

        Button validateTrue = view.findViewById(R.id.validateBtnTrue);
        validateTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(MainActivity.VALIDATION_SUCCESSFUL);
            }
        });

        Button validateFalse = view.findViewById(R.id.validateBtnFalse);
        validateFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(MainActivity.VALIDATION_UNSUCCESSFUL);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
