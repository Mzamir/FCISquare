package com.example.mahmoud.fcisquare.view_controller.dialogs;

import android.app.Dialog;
import android.content.Context;
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
import com.example.mahmoud.fcisquare.GPSTracker;
import com.example.mahmoud.fcisquare.R;
import com.example.mahmoud.fcisquare.model.User;
import com.example.mahmoud.fcisquare.view_controller.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mahmoud on 4/27/2016.
 */
public class AddPlaceDialog extends Dialog {

    // url for the function login
    String addPlaceURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/addNewPlaces";

    EditText placeName, placeDescription, placeLat, placeLongt;

        Button addPlaceButton;

        String name, description, lat = "0", longt = "0";

        GPSTracker gps;

        public AddPlaceDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_place_dialog);
        addPlaceButton = (Button) findViewById(R.id.addPlaceButton);
        placeName = (EditText) findViewById(R.id.addPlaceName);
        placeDescription = (EditText) findViewById(R.id.addPlaceDescription);
//        placeLat = (EditText) findViewById(R.id.addPlaceLat);
//        placeLongt = (EditText) findViewById(R.id.addPlaceLongt);

        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (placeName.getText().toString().isEmpty() || placeDescription.getText().toString().isEmpty()) {
                    if (placeName.getText().toString().isEmpty())
                        placeName.setError("Empty Field");
                    if (placeDescription.getText().toString().isEmpty()) {
                        placeDescription.setError("Empty Field");
                    }
                } else {
                    name = placeName.getText().toString();
                    description = placeDescription.getText().toString();
                    gps = new GPSTracker(getContext());

                    // check if GPS enabled
//                    if (gps.canGetLocation()) {
//
//                        double latitude = gps.getLatitude();
//                        double longitude = gps.getLongitude();
//                        lat = String.valueOf(latitude);
//                        longt = String.valueOf(longitude);
//
//                        // \n is for new line
//                        Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                    } else {
//                        // can't get location
//                        // GPS or Network is not enabled
//                        // Ask user to enable GPS/network in settings
//                        gps.showSettingsAlert();
//                    }
                    addPlaceService(name, description, lat, longt);
                }
            }
        });
    }


    public void addPlaceService(final String name, final String description, final String lat, final String longt) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, addPlaceURL, new Response.Listener<String>() {
            @Override
            // response that we read from the server
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (Integer.parseInt(jsonObject.get("status").toString()) == 1) {
                        HomeActivity.placesAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "ID " + User.id.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "new place has been added", Toast.LENGTH_SHORT).show();
                        hide();
                    } else {
                        Toast.makeText(getContext(), "Error adding new place please try again", Toast.LENGTH_SHORT).show();
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
                loginData.put("name", name);
                loginData.put("description", description);
                loginData.put("lat", lat);
                loginData.put("longt", longt);
                return loginData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
