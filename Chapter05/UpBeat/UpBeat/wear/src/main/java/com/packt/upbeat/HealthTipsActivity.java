package com.packt.upbeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableRecyclerView;

import com.packt.upbeat.utils.HealthTips;
import com.packt.upbeat.utils.HealthTipsItem;
import com.packt.upbeat.utils.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class HealthTipsActivity extends WearableActivity {

    private RecyclerViewAdapter mAdapter;
    private List<HealthTipsItem> myDataSet = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);
        WearableRecyclerView recyclerView = (WearableRecyclerView) findViewById(R.id.wearable_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        myDataSet = new ArrayList<HealthTipsItem>();
        for (int i = 0; i < HealthTips.nameArray.length; i++) {
            myDataSet.add(new HealthTipsItem(
                    HealthTips.nameArray[i],
                    HealthTips.versionArray[i]
            ));
        }

        mAdapter = new RecyclerViewAdapter(myDataSet,HealthTipsActivity.this);
        recyclerView.setAdapter(mAdapter);
    }
}
