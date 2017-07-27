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

public class RecyclerViewCaloryAdapter
        extends WearableRecyclerView.Adapter<RecyclerViewCaloryAdapter.ViewHolder> {

    private List<CaloryItem> mCalory = new ArrayList<>();
    private Context mContext;

    public RecyclerViewCaloryAdapter(List<CaloryItem> mCalory, Context mContext) {
        this.mCalory = mCalory;
        this.mContext = mContext;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Title;

        ViewHolder(View view) {
            super(view);
            Title = (TextView) view.findViewById(R.id.health_tip);
        }
    }

    @Override
    public RecyclerViewCaloryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calory_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Title.setText(mCalory.get(position).getCalories());
    }

    @Override
    public int getItemCount() {
        return mCalory.size();
    }

}
