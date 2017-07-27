package com.packt.upbeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableRecyclerView;

import com.packt.upbeat.utils.Calory;
import com.packt.upbeat.utils.CaloryItem;
import com.packt.upbeat.utils.HealthTips;
import com.packt.upbeat.utils.HealthTipsItem;
import com.packt.upbeat.utils.RecyclerViewAdapter;
import com.packt.upbeat.utils.RecyclerViewCaloryAdapter;

import java.util.ArrayList;
import java.util.List;

public class CaloryChartActivity  extends WearableActivity {

    private RecyclerViewCaloryAdapter mAdapter;
    private List<CaloryItem> myDataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calory_chart);

        WearableRecyclerView recyclerView = (WearableRecyclerView) findViewById(R.id.wearable_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        myDataSet = new ArrayList<CaloryItem>();
        for (int i = 0; i < Calory.nameArray.length; i++) {
            myDataSet.add(new CaloryItem(
                    Calory.nameArray[i]
            ));
        }

        mAdapter = new RecyclerViewCaloryAdapter(myDataSet,CaloryChartActivity.this);
        recyclerView.setAdapter(mAdapter);

    }
}
