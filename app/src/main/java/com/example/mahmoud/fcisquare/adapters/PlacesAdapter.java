package com.example.mahmoud.fcisquare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoud.fcisquare.R;
import com.example.mahmoud.fcisquare.model.Place;
import com.example.mahmoud.fcisquare.model.User;
import com.example.mahmoud.fcisquare.view_controller.activities.SplashActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahmoud on 4/20/2016.
 */
public class PlacesAdapter extends BaseAdapter {
    Context activity;
    List<Place> placeList;

    // url for the function follow
    String followURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/followUser";

    // url for the function un-follow
    String unfollowURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/unfollowUser";

    public PlacesAdapter(Context activity, List<Place> placeList) {
        this.activity = activity;
        this.placeList = placeList;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.place_fragment, null);
            viewHolder.placeName = (TextView) convertView.findViewById(R.id.placeName);
            viewHolder.placeCategory = (TextView) convertView.findViewById(R.id.placeCategory);
            viewHolder.placeTaste = (TextView) convertView.findViewById(R.id.placeTaste);
            viewHolder.placeLikesCounter = (TextView) convertView.findViewById(R.id.placeLikesCounter);
            viewHolder.placeLikeButton = (ImageView) convertView.findViewById(R.id.placeLikeButton);
            viewHolder.placeSaveButton = (ImageView) convertView.findViewById(R.id.placeSaveButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.placeName.setText(placeList.get(position).getName());
        viewHolder.placeCategory.setText(placeList.get(position).getCategory());
        viewHolder.placeTaste.setText(placeList.get(position).getTaste());
        viewHolder.placeLikeButton.setImageResource(placeList.get(position).getLikePlaceImage());
        viewHolder.placeSaveButton.setImageResource(placeList.get(position).getSavePlaceImage());
        viewHolder.placeLikesCounter.setText(placeList.get(position).getNumberOfLikes());

        return convertView;
    }

    public class ViewHolder {
        TextView placeName;
        TextView placeCategory;
        TextView placeTaste;
        ImageView placeLikeButton;
        ImageView placeSaveButton;
        TextView placeLikesCounter;
    }

    public void followService(final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, followURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> followData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the user
                followData.put("id", User.id);
                followData.put("email", email);
                return followData;
            }
        };
        SplashActivity.requestQueue.add(stringRequest);
    }

    public void unfollowService(final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, unfollowURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(activity, "Unfollow " + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> unfollowData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the user
                unfollowData.put("id", User.id);
                unfollowData.put("email", email);
                return unfollowData;
            }
        };
        SplashActivity.requestQueue.add(stringRequest);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

