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
import com.example.mahmoud.fcisquare.adapters.PlacesAdapter;
import com.example.mahmoud.fcisquare.model.Place;
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
public class CheckinPlacesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    List<Place> placeList;
    PlacesAdapter placesAdapter;
    ListView placeListView;
    StringRequest stringRequest;
    String checkindPlaceURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/userCheckins";

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
//        mPage = getArguments().getInt(ARG_PAGE);
    }
    // get data from the backend

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.savedplaces_listview, container, false);
        placeListView = (ListView) view.findViewById(R.id.savedplacesListview);
        placeList = new ArrayList<>();
        placesAdapter = new PlacesAdapter(getActivity(), placeList);
        fillListView();
        placeListView.setAdapter(placesAdapter);
        return view;
    }

    private void fillListView() {
        stringRequest = new StringRequest(Request.Method.POST, checkindPlaceURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "checkin Response " + response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response == null) {
                        Toast.makeText(getActivity(), "Empty checkin Response", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject object;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            placeList.add(new Place(object.getString("Place"), object.getString("NumberOfCheckins"), object.getString("Rate"), R.drawable.like, R.drawable.save));
                            placesAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "checkin error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> savedPlaces = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                savedPlaces.put("id", User.id.toString());
                return savedPlaces;
            }
        };
        placesAdapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}