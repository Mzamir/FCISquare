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
 * Created by Mahmoud on 3/27/2016.
 */
public class SignupDialog extends Dialog {

    // url for the function sign up
    String signupURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/signup";

    EditText signupName, signupPassword, signupEmail;

    Button signupButton;

    public SignupDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup);
        // Signup UI
        signupButton = (Button) findViewById(R.id.signupButton);
        signupEmail = (EditText) findViewById(R.id.signupemail);
        signupName = (EditText) findViewById(R.id.signupname);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signupPassword.getText().toString().isEmpty())
                    signupPassword.setError("Fill Password");
                else if (signupName.getText().toString().isEmpty())
                    signupName.setError("Fill name");
                else if (signupEmail.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText().toString()).matches())
                    signupEmail.setError("Invalid Email address");
                else {
                    //BODY
                    signupService(signupName.getText().toString(), signupEmail.getText().toString(), signupPassword.getText().toString());
                    getContext().startActivity(new Intent(getContext(), HomeActivity.class));
                }
            }
        });
    }

    public void signupService(final String name, final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, signupURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("Error"))
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    else {
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("username", name);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> signupData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                signupData.put("name", name);
                signupData.put("email", email);
                signupData.put("pass", password);
                return signupData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
