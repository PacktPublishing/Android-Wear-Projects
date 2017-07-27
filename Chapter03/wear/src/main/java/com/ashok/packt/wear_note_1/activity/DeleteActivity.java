package com.ashok.packt.wear_note_1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;

import com.ashok.packt.wear_note_1.R;
import com.ashok.packt.wear_note_1.utils.ConfirmationUtils;
import com.ashok.packt.wear_note_1.utils.Constants;

public class DeleteActivity extends WearableActivity implements DelayedConfirmationView.DelayedConfirmationListener  {

    private DelayedConfirmationView mDelayedConfirmationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        mDelayedConfirmationView = (DelayedConfirmationView) findViewById(R.id.delayed_confirmation);
        mDelayedConfirmationView.setListener(this);
        mDelayedConfirmationView.setTotalTimeMs(3000);
        mDelayedConfirmationView.start();

    }

   @Override
    public void onTimerFinished(View view) {
        int itemPosition = getIntent().getIntExtra(Constants.ITEM_POSITION, -1);
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_POSITION, itemPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTimerSelected(View view) {
        ConfirmationUtils.showMessage(getString(R.string.cancel), this);
        mDelayedConfirmationView.reset();
        finish();
    }
}
