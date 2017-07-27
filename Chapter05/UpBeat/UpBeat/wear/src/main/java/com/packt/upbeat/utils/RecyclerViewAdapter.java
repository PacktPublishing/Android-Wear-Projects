package com.packt.upbeat.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.packt.upbeat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok.kumar on 28/05/17.
 */

public class RecyclerViewAdapter
        extends WearableRecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<HealthTipsItem> mListTips = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(List<HealthTipsItem> mListTips, Context mContext) {
        this.mListTips = mListTips;
        this.mContext = mContext;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Title, info;

        ViewHolder(View view) {
            super(view);
            Title = (TextView) view.findViewById(R.id.health_tip);
            info = (TextView) view.findViewById(R.id.tip_details);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_tips_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Title.setText(mListTips.get(position).getTitle());
        holder.info.setText(mListTips.get(position).getMoreInfo());
    }

    @Override
    public int getItemCount() {
        return mListTips.size();
    }


}
