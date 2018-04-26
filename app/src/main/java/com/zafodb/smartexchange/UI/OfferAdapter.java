package com.zafodb.smartexchange.UI;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zafodb.smartexchange.BtcOffer;
import com.zafodb.smartexchange.R;
import com.zafodb.smartexchange.Wrappers.Web3jwrapper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private List<BtcOffer> mBtcOffers;

    public OfferAdapter(List<BtcOffer> btcOffers) {

        Log.v("FILIP", "Was here");
        if (btcOffers == null){
            mBtcOffers = new ArrayList<>();
            mBtcOffers.add(new BtcOffer(BigInteger.ZERO, BigInteger.ZERO, "to be updated", "to be updated"));
        } else {
            mBtcOffers = btcOffers;
        }
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
                }
            });

            nickname = v.findViewById(R.id.labelOfferNickname);
            btcAmount = v.findViewById(R.id.labelOfferAmountBtc);
            ethAmount = v.findViewById(R.id.labelOfferAmountEth);
            idView = v.findViewById(R.id.labelOfferId);
        }

        void setupContent(BtcOffer offer){
            nickname.setText(offer.getNickname());
            btcAmount.setText(offer.getAmountSatoshiOffered().toString());
//            TODO: convert satoshi into human-readable format
            ethAmount.setText(Web3jwrapper.ethBalanceToString(offer.getAmountWeiWanted(), 4));
//            TODO consider how IDs should look like
            idView.setText("#LOL");
        }
    }
}
