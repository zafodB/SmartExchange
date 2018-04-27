package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment implements MainActivity.FragmentUpdateListener{

    private WalletPick.OnFragmentInteractionListener mListener;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    OfferAdapter mAdapter;

    public static OffersFragment newInstance() {
        return new OffersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewOffers);
        mAdapter = new OfferAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button continueButton = view.findViewById(R.id.buttonContinueOffers);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.FROM_OFFERS_TO_WALLETPICK);
            }
        });

        Button refreshOffersButton = view.findViewById(R.id.buttonRefreshOffers);
        refreshOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.OFFERS_UPDATE);
            }
        });

        Button newOfferButton = view.findViewById(R.id.buttonCreateOffers);
        newOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.FROM_OFFERS_TO_NEWOFFER);
            }
        });

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

    @Override
    public void pushUpdate(Bundle args) {
        List<BtcOffer> offers = (ArrayList<BtcOffer>) args.getSerializable(Constants.OFFERS_LIST);
        mAdapter.updateList(offers);
    }

}
