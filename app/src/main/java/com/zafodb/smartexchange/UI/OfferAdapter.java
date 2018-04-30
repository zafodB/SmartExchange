package com.zafodb.smartexchange.UI;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.Wrappers.BitcoinjWrapper;
import com.zafodb.smartexchange.Wrappers.Web3jwrapper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private List<BtcOffer> mBtcOffers;
    private static OffersFragment parentFragment;

    public OfferAdapter(List<BtcOffer> btcOffers, OffersFragment fragment) {
        if (btcOffers == null){
            mBtcOffers = new ArrayList<>();
            mBtcOffers.add(new BtcOffer(BigInteger.ZERO, BigInteger.ZERO, "to be updated", "to be updated"));
        } else {
            mBtcOffers = btcOffers;
        }

        parentFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setupContent(mBtcOffers.get(position));
    }

    @Override
    public int getItemCount() {
        return mBtcOffers.size();
    }

    static void offerClicked(int position) {
//        TODO: Do stuff. Convert this to interface maybe? Dunno

        RecyclerViewClickListener listener = parentFragment;
        listener.recyclerViewItemClicked(position);

        Log.d("FILIP", "Offer clicked");
//
    }

    public void updateList(List<BtcOffer> offers) {
        this.mBtcOffers = offers;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nickname;
        private final TextView btcAmount;
        private final TextView ethAmount;
        private final TextView idView;

        ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    offerClicked(getAdapterPosition());
                    Log.v("FILIP", "Something was clicked lol");

                    view.setBackgroundColor(Color.rgb(147, 188, 255));
                }
            });

            nickname = v.findViewById(R.id.labelOfferNickname);
            btcAmount = v.findViewById(R.id.labelOfferAmountBtc);
            ethAmount = v.findViewById(R.id.labelOfferAmountEth);
            idView = v.findViewById(R.id.labelOfferId);
        }

        void setupContent(BtcOffer offer){
            nickname.setText(offer.getNickname());
            btcAmount.setText(BitcoinjWrapper.satoshiToBtcString(offer.getAmountSatoshiOffered(), 4));
            ethAmount.setText(Web3jwrapper.weiToString(offer.getAmountWeiWanted(), 4));
//            TODO consider how IDs should look like
            idView.setText("#ID");
        }
    }

    BtcOffer getOfferAtPosition(int position){
        if (mBtcOffers != null && mBtcOffers.size() != 0){
            return mBtcOffers.get(position);
        }
        else return null;
    }

    public interface RecyclerViewClickListener{
        void recyclerViewItemClicked(int position);
    }
}
