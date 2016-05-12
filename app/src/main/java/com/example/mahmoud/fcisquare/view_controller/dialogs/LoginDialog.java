package com.example.mahmoud.fcisquare.view_controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mahmoud.fcisquare.R;
import com.example.mahmoud.fcisquare.model.User;
import com.example.mahmoud.fcisquare.view_controller.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mahmoud on 3/25/2016.
 */
public class LoginDialog extends Dialog {

    // url for the function login
    String loginURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/login";

    EditText loginEmail, loginPassword;

    Button loginButton;

    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        loginButton = (Button) findViewById(R.id.loginBuuton);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginEmail.getText().toString().isEmpty())
                    loginEmail.setError("Empty Field");
//                else if (placeDescription.getText().toString().isEmpty())
//                    placeDescription.setError("Empty Field");
//                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(placeName.getText().toString()).matches())
//                    placeName.setError("invalid Email address");
                else {
                    //BODY
                    loginService(loginEmail.getText().toString(), loginPassword.getText().toString());
                }
            }
        });
    }

    public void loginService(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>() {
            @Override
            // response that we read from the server
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("Error"))
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    else {
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("username", jsonObject.getString("name"));
                        User.id = jsonObject.getString("userID");
                        getContext().startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            // to send the paramerters to the request
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> loginData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                loginData.put("email", email);
                loginData.put("pass", password);
                return loginData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
