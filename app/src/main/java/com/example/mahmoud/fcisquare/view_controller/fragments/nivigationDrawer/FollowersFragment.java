package com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mahmoud.fcisquare.adapters.FollowersAdapter;
import com.example.mahmoud.fcisquare.model.Followers;
import com.example.mahmoud.fcisquare.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahmoud on 5/6/2016.
 */
public class FollowersFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    List<Followers> followersList;
    FollowersAdapter followersAdapter;
    ListView followersListview;
    StringRequest stringRequest;
    String followersURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/getAllFollowers";

    public static SavedPlacesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SavedPlacesFragment fragment = new SavedPlacesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview, container, false);
        followersListview = (ListView) view.findViewById(R.id.listview);
        followersList = new ArrayList<>();

        // get data from the backend
        stringRequest = new StringRequest(Request.Method.POST, followersURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response == null) {
                        Toast.makeText(getActivity(), "Empty Response", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = new JSONArray(response);
                        Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            followersList.add(new Followers(object.getString("name"), "Unfollow", object.getString("email")));
                            followersAdapter.notifyDataSetChanged();
                        }
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
                Map<String, String> allFollowersData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                allFollowersData.put("id", User.id.toString());
                return allFollowersData;
            }
        };

        // set listview adapter after full it with the data from the backend
        followersAdapter = new FollowersAdapter(getActivity(), followersList);
        followersListview.setAdapter(followersAdapter);
        followersAdapter.notifyDataSetChanged();

        // add the queue to the volley to be execute
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return view;
    }
}
