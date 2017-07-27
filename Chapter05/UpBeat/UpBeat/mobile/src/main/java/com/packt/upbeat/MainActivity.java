package com.packt.upbeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.packt.upbeat.models.StepCounts;
import com.packt.upbeat.utils.HeartBeatView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient googleClient;
    private Realm realm;

    private AppCompatButton mReset, mHistory, mHeartPulse;
    private TextView mSteps, mHeart, mCalory;
    private HeartBeatView heartbeat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heartbeat = (HeartBeatView)findViewById(R.id.heartbeat);
        mSteps = (TextView) findViewById(R.id.steps);
        mHeart = (TextView) findViewById(R.id.heart);
        mCalory = (TextView) findViewById(R.id.calory);
        mReset = (AppCompatButton) findViewById(R.id.reset);
        mHistory = (AppCompatButton) findViewById(R.id.history);
        mHeartPulse = (AppCompatButton) findViewById(R.id.pulseRequest);


        // Build a new GoogleApiClient that includes the Wearable API
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        RealmResults<StepCounts> results = realm.where(StepCounts.class).findAll();

        if(results.size() == 0){
            mSteps.setText("Steps: ");
        }else{
            mSteps.setText("Steps: "+results.get(results.size()-1).getData());
            int value = Integer.valueOf(results.get(results.size()-1).getData());
            mCalory.setText(String.valueOf((int)(value * 0.045)) + "\ncalories" + "\nburnt");

        }

        for(StepCounts Steps : results){
            Steps.getReceivedDateTime();
            Log.d("time", Steps.getReceivedDateTime());
        }

        // Register the local broadcast receiver
        IntentFilter DataFilter = new IntentFilter(Intent.ACTION_SEND);
        HeartRateReciver DataReceiver = new HeartRateReciver();
        LocalBroadcastManager.getInstance(this).registerReceiver(DataReceiver, DataFilter);

        // Register the local broadcast receiver
        IntentFilter StepFilter = new IntentFilter(Intent.ACTION_SEND);
        StepReceiver StepReceiver = new StepReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(StepReceiver, StepFilter);


        mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        mHeartPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendToDataLayerThread("/heart", "Start upbeat for heart rate").start();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset();
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class StepReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.v("steps", "Mainstepsactivity received message: " + message);

            String data = intent.getStringExtra("heart");

            if(data != null){
                mHeart.setText(data);
                heartbeat.setDurationBasedOnBPM(Integer.valueOf(data));
                heartbeat.toggle();
            }


            realm.beginTransaction();
            StepCounts Steps = realm.createObject(StepCounts.class);
            Steps.setData(message);
            String TimeStamp = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                TimeStamp = DateFormat.getDateTimeInstance().format(System.currentTimeMillis());
            }
            Steps.setReceivedDateTime(TimeStamp);
            realm.commitTransaction();

            mSteps.setText("Steps:"+ message);
            int value = Integer.valueOf(message);
            mCalory.setText(String.valueOf((int)(value * 0.045)) + "\ncalories" + "\nburnt");

            // Displaysage in UI

//            new SendToDataLayerThread("/message_path","You:-\n" + message).start();
        }
    }

    public class HeartRateReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("heart");
            Log.v("heart", "Mainhaertactivity received message: " + data);

            mHeart.setText(data);
            heartbeat.setDurationBasedOnBPM(Integer.valueOf(data));
            heartbeat.toggle();

            // Displaysage in UI

//            new SendToDataLayerThread("/message_path","You:-\n" + message).start();
        }
    }

    class SendToDataLayerThread extends Thread {
        String path;
        String message;

        // Constructor to send a message to the data layer
        SendToDataLayerThread(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleClient, node.getId(), path, message.getBytes()).await();
                if (result.getStatus().isSuccess()) {
                    Log.v("myTag", "Message: {" + message + "} sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.v("myTag", "ERROR: failed to send Message");
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();

    }

    @Override
    protected void onStop() {
        if (null != googleClient && googleClient.isConnected()) {
            googleClient.disconnect();
        }
        super.onStop();
    }

    public void Reset(){
        RealmResults<StepCounts> results = realm.where(StepCounts.class).findAll();

        realm.beginTransaction();

        results.deleteAllFromRealm();

        realm.commitTransaction();
    }
}
