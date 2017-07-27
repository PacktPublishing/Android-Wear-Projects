package com.packt.smartchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UsersListActivity extends AppCompatActivity {

    private ListView mUsersList;
    private TextView mNoUsersText;
    private ArrayList<String> mArraylist = new ArrayList<>();
    private int totalUsers = 0;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        mUsersList = (ListView)findViewById(R.id.usersList);
        mNoUsersText = (TextView)findViewById(R.id.noUsersText);

        mProgressDialog = new ProgressDialog(UsersListActivity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        String url = "https://packt-wear.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(UsersListActivity.this);
        rQueue.add(request);

        mUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User.chatWith = mArraylist.get(position);
                startActivity(new Intent(UsersListActivity.this, ChatActivity.class));
            }
        });

    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(User.username)) {
                    mArraylist.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            mNoUsersText.setVisibility(View.VISIBLE);
            mUsersList.setVisibility(View.GONE);
        }
        else{
            mNoUsersText.setVisibility(View.GONE);
            mUsersList.setVisibility(View.VISIBLE);
            mUsersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArraylist));
        }

        mProgressDialog.dismiss();
    }
}
