package com.packt.smartchat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mUsername, mPassword;
    private Button mSubmitButton;
    public String mUserStr, mPassStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mUsername = (EditText)findViewById(R.id.input_email);
        mPassword = (EditText)findViewById(R.id.input_password);
        mSubmitButton = (Button)findViewById(R.id.btn_submit);

        Firebase.setAndroidContext(this);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserStr = mUsername.getText().toString();
                mPassStr = mPassword.getText().toString();

                // Validation
                if(mUserStr.equals("")){
                    mUsername.setError("can't be blank");
                }
                else if(mPassStr.equals("")){
                    mPassword.setError("can't be blank");
                }
                else if(!mUserStr.matches("[A-Za-z0-9]+")){
                    mUsername.setError("only alphabet or number allowed");
                }
                else if(mUserStr.length()<5){
                    mUsername.setError("at least 5 characters long");
                }
                else if(mPassStr.length()<5){
                    mPassword.setError("at least 5 characters long");
                }else {
                    final ProgressDialog pd = new ProgressDialog(RegistrationActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://packt-wear.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://packt-wear.firebaseio.com/users");
                            if(s.equals("null")) {
                                reference.child(mUserStr).child("password").setValue(mPassStr);
                                Toast.makeText(RegistrationActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(mUserStr)) {
                                        reference.child(mUserStr).child("password").setValue(mPassStr);
                                        Toast.makeText(RegistrationActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(RegistrationActivity.this);
                    rQueue.add(request);
                }
            }
        });

    }
}

