package com.zafodb.smartexchange.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.MainActivity;
import com.zafodb.smartexchange.R;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends CustomFragment implements MainActivity.FragmentUpdateListener, OfferAdapter.RecyclerViewClickListener {

    private WalletPickFragment.OnFragmentInteractionListener mListener;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    OfferAdapter mAdapter;
    TextView noOffers;
    int mPositionClicked;


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
        mAdapter = new OfferAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        onButtonPressed(Constants.OFFERS_UPDATE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button continueButton = view.findViewById(R.id.buttonContinueOffers);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setmExistingOffer(mAdapter.getOfferAtPosition(mPositionClicked));
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

        noOffers = view.findViewById(R.id.labelNoOffersAtm);
        noOffers.setVisibility(View.INVISIBLE);
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

    @Override
    public void pushUpdate(Bundle args) {
        List<BtcOffer> offers = (ArrayList<BtcOffer>) args.getSerializable(Constants.OFFERS_LIST);
        if (offers != null && offers.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            noOffers.setVisibility(View.VISIBLE);
        } else if (offers != null) {
            noOffers.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.updateList(offers);
        }
    }

    @Override
    public void recyclerViewItemClicked(int position) {
        for (int childCount = mRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {

            OfferAdapter.ViewHolder holder = (OfferAdapter.ViewHolder) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
            if (i != position) {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }

        mPositionClicked = position;
//
    }

    @Override
    public String getFragmentTag() {
        return Constants.OFFER_STATUS_FRAGMENT_TAG;
    }
}
