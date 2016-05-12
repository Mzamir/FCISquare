package com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mahmoud.fcisquare.R;
import com.example.mahmoud.fcisquare.adapters.NotificationAdapter;
import com.example.mahmoud.fcisquare.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahmoud on 4/18/2016.
 */
public class NotificationsFragment extends android.support.v4.app.Fragment {

    List<Notification> notificationList;
    NotificationAdapter notificationAdapter;
    ListView notificationListview;
    StringRequest stringRequest;
    Button unFollowButton;
    String notificationsURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/getAllNotifications";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listview, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        notificationListview = (ListView) v.findViewById(R.id.listview);
        notificationList = new ArrayList<Notification>();
        fillListView();
        notificationAdapter = new NotificationAdapter(getActivity(), notificationList);
        notificationListview.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }

    private void fillListView() {
        stringRequest = new StringRequest(Request.Method.POST, notificationsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        notificationList.add(new Notification(object.getString("text"),
                                object.getString("time"), object.getString("checkinID"), object.getString("date")));
                        notificationAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> allFollowersData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                String id = "2";
                //User.id.toString()
                allFollowersData.put("id", id);
                return allFollowersData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
