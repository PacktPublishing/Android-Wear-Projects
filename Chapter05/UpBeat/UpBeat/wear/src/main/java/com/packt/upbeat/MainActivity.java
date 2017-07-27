package com.packt.upbeat;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.drawer.WearableActionDrawer;
import android.support.wearable.view.drawer.WearableDrawerLayout;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.packt.upbeat.fragments.DrinkWaterFragment;
import com.packt.upbeat.fragments.HeartRateFragment;
import com.packt.upbeat.fragments.StepCounterFragment;
import com.packt.upbeat.utils.DrawerItem;

import java.util.ArrayList;

public class MainActivity extends WearableActivity  implements
        WearableActionDrawer.OnMenuItemClickListener{


    private static final String TAG = "MainActivity";
    private WearableDrawerLayout mWearableDrawerLayout;
    private WearableNavigationDrawer mWearableNavigationDrawer;
    private WearableActionDrawer mWearableActionDrawer;
    private ArrayList<DrawerItem> drawer_itemArrayList;
    private int mSelectedScreen;

    private HeartRateFragment mHeartFragment;
    private DrinkWaterFragment mDrinkFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        drawer_itemArrayList = initializeScreenSystem();
        mSelectedScreen = 0;


        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

        // Initialize content to first screen.
        mDrinkFragment = new DrinkWaterFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, mDrinkFragment).commit();

        // Main Wearable Drawer Layout that holds all the content
        mWearableDrawerLayout = (WearableDrawerLayout) findViewById(R.id.drawer_layout);

        // Top Navigation Drawer
        mWearableNavigationDrawer =
                (WearableNavigationDrawer) findViewById(R.id.top_navigation_drawer);

        mWearableNavigationDrawer.setAdapter(new NavigationAdapter(this));

        // Bottom Action Drawer
        mWearableActionDrawer =
                (WearableActionDrawer) findViewById(R.id.bottom_action_drawer);

        mWearableActionDrawer.setOnMenuItemClickListener(this);

        // Temporarily peeks the navigation and action drawers to ensure the user is aware of them.
        ViewTreeObserver observer = mWearableDrawerLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWearableDrawerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mWearableDrawerLayout.peekDrawer(Gravity.TOP);
                mWearableDrawerLayout.peekDrawer(Gravity.BOTTOM);
            }
        });
    }

    private ArrayList<DrawerItem> initializeScreenSystem() {
        ArrayList<DrawerItem> screens = new ArrayList<DrawerItem>();
        String[] FragmentArrayNames = getResources().getStringArray(R.array.screens);

        for (int i = 0; i < FragmentArrayNames.length; i++) {
            String planet = FragmentArrayNames[i];
            int FragmentResourceId =
                    getResources().getIdentifier(planet, "array", getPackageName());
            String[] fragmentInformation = getResources().getStringArray(FragmentResourceId);

            screens.add(new DrawerItem(
                    fragmentInformation[0],   // Name
                    fragmentInformation[1]));
        }

        return screens;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuItemClick(): " + menuItem);

        final int itemId = menuItem.getItemId();

        String toastMessage = "";

        switch (itemId) {
            case R.id.menu_about:
                toastMessage = "Brilliant wrist and mobile app for health";
                break;
            case R.id.menu_helathtips:
                startActivity(new Intent(MainActivity.this, HealthTipsActivity.class));
                toastMessage = drawer_itemArrayList.get(mSelectedScreen).getName();
                break;
            case R.id.menu_calory:
                startActivity(new Intent(MainActivity.this, CaloryChartActivity.class));
                toastMessage = drawer_itemArrayList.get(mSelectedScreen).getName();
                break;
        }


        mWearableDrawerLayout.closeDrawer(mWearableActionDrawer);

        if (toastMessage.length() > 0) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    toastMessage,
                    Toast.LENGTH_SHORT);
            toast.show();
            return true;
        } else {
            return false;
        }
    }


    private final class NavigationAdapter
            extends WearableNavigationDrawer.WearableNavigationDrawerAdapter {

        private final Context mContext;

        public NavigationAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return drawer_itemArrayList.size();
        }

        @Override
        public void onItemSelected(int position) {
            Log.d(TAG, "WearableNavigationDrawerAdapter.onItemSelected(): " + position);
            mSelectedScreen = position;

            if(position==0) {
                final DrinkWaterFragment drinkWaterFragment = new DrinkWaterFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, drinkWaterFragment)
                        .commit();

            }else if(position == 1){
                final HeartRateFragment sectionFragment = new HeartRateFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, sectionFragment)
                        .commit();
            }else if(position == 2){
                final StepCounterFragment stepCounterFragment = new StepCounterFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, stepCounterFragment)
                        .commit();
            }

        }

        @Override
        public String getItemText(int pos) {
            return drawer_itemArrayList.get(pos).getName();
        }

        @Override
        public Drawable getItemDrawable(int pos) {
            String navigationIcon = drawer_itemArrayList.get(pos).getNavigationIcon();

            int drawableNavigationIconId =
                    getResources().getIdentifier(navigationIcon, "drawable", getPackageName());

            return mContext.getDrawable(drawableNavigationIconId);
        }
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.v("myTag", "Main activity received message: " + message);
            // Display message in UI
        }
    }





    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
    }


}
