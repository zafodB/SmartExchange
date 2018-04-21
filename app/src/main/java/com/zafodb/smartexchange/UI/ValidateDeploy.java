package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "trade_deal_instance";

    TextView ethAmount;
    TextView btcAddress;
    TextView btcAmount;
    TextView ethAddress;

    private TradeDeal tradeDeal;

    private WalletPick.OnFragmentInteractionListener mListener;

    public ValidateDeploy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ValidateDeploy.
     */
    public static ValidateDeploy newInstance(TradeDeal param1) {
        ValidateDeploy fragment = new ValidateDeploy();
        Bundle args = new Bundle();

        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tradeDeal = (TradeDeal) getArguments().getSerializable(ARG_PARAM1);
        }
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

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
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
