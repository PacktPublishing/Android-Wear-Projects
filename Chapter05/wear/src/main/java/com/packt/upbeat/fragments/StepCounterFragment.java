package com.packt.upbeat.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.packt.upbeat.R;
import com.packt.upbeat.services.EventReceiver;
import com.packt.upbeat.utils.Reporter;
import com.packt.upbeat.utils.StepsTaken;
import com.packt.upbeat.services.WearStepService;


public class StepCounterFragment extends Fragment implements Reporter {


    private TextView tv;
    private Handler handler = new Handler();


    public StepCounterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.step_counter_fragment, container, false);

        getActivity().startService(new Intent(getActivity(), WearStepService.class));


        tv = (TextView)rootView.findViewById(R.id.steps);
        tv.setText(String.valueOf(StepsTaken.getSteps()));

        return rootView;
    }

    @Override
    public void notifyEvent(final Object o) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (o instanceof StepsTaken)
                    tv.setText(String.valueOf(StepsTaken.getSteps()));
            }
        });

    }

    @Override
    public void onResume() {
        EventReceiver.register(StepsTaken.class, this);
        super.onResume();
    }

    @Override
    public void onPause() {
        EventReceiver.remove(StepsTaken.class, this);
        super.onPause();
    }

}
