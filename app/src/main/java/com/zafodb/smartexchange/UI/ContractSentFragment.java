package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;

import org.spongycastle.crypto.agreement.srp.SRP6Client;

public class ContractSentFragment extends CustomFragment implements MainActivity.FragmentUpdateListener {

    private WalletPickFragment.OnFragmentInteractionListener mListener;

    TextView txHashView;
    TextView txHyperlinkView;

    public static ContractSentFragment newInstance() {
        return new ContractSentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contract_sent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txHashView = view.findViewById(R.id.textContractTxHash);
        txHyperlinkView = view.findViewById(R.id.textTxHyperlink);
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

    @Override
    public void pushUpdate(Bundle args) {
        String txHash = args.getString(Constants.TRANSACTION_HASH);

        txHashView.setText(txHash);

        txHyperlinkView.setText(Constants.ETHERSCAN_KOVAN_REFERENCE + txHash);
    }

    @Override
    public String getFragmentTag() {
        return Constants.SENT_FRAGMENT_TAG;
    }
}
