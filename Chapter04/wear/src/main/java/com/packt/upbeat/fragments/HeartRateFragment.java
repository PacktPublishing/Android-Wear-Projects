package com.packt.upbeat.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.packt.upbeat.utils.HeartBeatView;
import com.packt.upbeat.R;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;


public class HeartRateFragment extends Fragment implements SensorEventListener {

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private HeartBeatView heartbeat;
    private Sensor mHeartRateSensor;
    private SensorManager mSensorManager;
    private Integer currentValue = 0;
    private static final String TAG = "HeartRateFragment";
    private static final int SENSOR_PERMISSION_CODE = 123;

    private GoogleApiClient mGoogleApiClient;

    public HeartRateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.heart_rate_fragment, container, false);


        heartbeat = (HeartBeatView)rootView.findViewById(R.id.heartbeat);

        mContainerView = (BoxInsetLayout)rootView.findViewById(R.id.container);
        mTextView = (TextView)rootView.findViewById(R.id.heart_rate);
        mSensorManager = ((SensorManager)getActivity().getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Wearable.API).build();
        mGoogleApiClient.connect();


        requestSensorPermission();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mHeartRateSensor != null) {
            Log.d(TAG, "HEART RATE SENSOR NAME: " + mHeartRateSensor.getName() + " TYPE: " + mHeartRateSensor.getType());
            mSensorManager.unregisterListener(this, this.mHeartRateSensor);
            boolean isRegistered = mSensorManager.registerListener(this, this.mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
            Log.d(TAG, "HEART RATE LISTENER REGISTERED: " + isRegistered);
        } else {
            Log.d(TAG, "HEART RATE SENSOR NOT READY");
        }
        sendMessageToHandheld("0");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Log.d(TAG, "SENSOR UNREGISTERED");
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() == Sensor.TYPE_HEART_RATE && sensorEvent.values.length > 0) {

            for(Float value : sensorEvent.values) {

                int newValue = Math.round(value);

                if(currentValue != newValue) {
                    currentValue = newValue;

                    mTextView.setText(currentValue.toString());
                    heartbeat.setDurationBasedOnBPM(currentValue);
                    heartbeat.toggle();
                    sendMessageToHandheld(currentValue.toString());
                }

            }

        }
    }

    private void sendMessageToHandheld(final String message) {

        if (mGoogleApiClient == null)
            return;

        // use the api client to send the heartbeat value to our handheld
        final PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                final List<Node> nodes = result.getNodes();
                final String path = "/heartRate";

                for (Node node : nodes) {
                    Log.d(TAG, "SEND MESSAGE TO HANDHELD: " + message);

                    byte[] data = message.getBytes(StandardCharsets.UTF_8);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, data);
                }
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "ACCURACY CHANGED: " + i);
    }


    //Requesting permission
    private void requestSensorPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.BODY_SENSORS)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BODY_SENSORS}, SENSOR_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == SENSOR_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
