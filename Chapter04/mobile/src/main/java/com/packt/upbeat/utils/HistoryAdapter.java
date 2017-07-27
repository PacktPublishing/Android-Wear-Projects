package com.packt.upbeat.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.packt.upbeat.HistoryActivity;
import com.packt.upbeat.R;
import com.packt.upbeat.models.StepCounts;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by ashok.kumar on 27/05/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    public List<StepCounts> steps;
    public Context mContext;

    public HistoryAdapter(List<StepCounts> steps, Context mContext) {
        this.steps = steps;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.steps.setText(steps.get(i).getData()+" Steps");
        viewHolder.date.setText(steps.get(i).getReceivedDateTime());

        int value = Integer.valueOf(steps.get(i).getData());
        DecimalFormat df = new DecimalFormat("#.00") ;
        String kms = String.valueOf(df.format(value * 0.000762)) + " kms" ;
        viewHolder.calory.setText(String.valueOf((int)(value * 0.045)) + " calories " + "burnt");
        viewHolder.distance.setText("Distance: "+kms);

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView steps,calory,distance,date;

        public ViewHolder(View itemView) {
            super(itemView);
            steps = (TextView) itemView.findViewById(R.id.steps);
            calory = (TextView) itemView.findViewById(R.id.calories);
            distance = (TextView) itemView.findViewById(R.id.distance);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }

}
