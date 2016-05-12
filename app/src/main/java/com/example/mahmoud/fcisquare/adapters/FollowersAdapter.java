package com.example.mahmoud.fcisquare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mahmoud.fcisquare.R;
import com.example.mahmoud.fcisquare.model.Followers;
import com.example.mahmoud.fcisquare.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahmoud on 3/27/2016.
 */
public class FollowersAdapter extends BaseAdapter {

    Context activity;
    List<Followers> followersList;

    // url for the function follow
    String followURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/followUser";

    // url for the function un-follow
    String unfollowURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/unfollowUser";

    public FollowersAdapter(Context activity, List<Followers> followerses) {
        this.activity = activity;
        this.followersList = followerses;
    }

    @Override
    public int getCount() {
        return followersList.size();
    }

    @Override
    public Object getItem(int position) {
        return followersList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.followers_fragment, null);
            viewHolder.button = (Button) convertView.findViewById(R.id.followersButton);
            viewHolder.username = (TextView) convertView.findViewById(R.id.followersName);
            viewHolder.email = (TextView) convertView.findViewById(R.id.followerEmail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.button.setText(followersList.get(position).getButton());
        viewHolder.username.setText(followersList.get(position).getName());
        viewHolder.email.setText(followersList.get(position).getEmail());
        final ViewHolder finalViewHolder = viewHolder;

        finalViewHolder.button.setText(followersList.get(position).getButton());
        finalViewHolder.username.setText(followersList.get(position).getName());
        finalViewHolder.email.setText(followersList.get(position).getEmail());

        finalViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "before", Toast.LENGTH_SHORT).show();
                if (followersList.get(position).getButton().toString() == "Unfollow") {
                    Toast.makeText(activity, "after", Toast.LENGTH_SHORT).show();
                    followersList.get(position).setButton("follow");
                    unfollowService(followersList.get(position).getEmail());
                    Collections.swap(followersList, position, followersList.size() - 1);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "after", Toast.LENGTH_SHORT).show();
                    followersList.get(position).setButton("unfollow");
                    followService(followersList.get(position).getEmail());
                    Collections.swap(followersList, position, followersList.size() - 1);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView username;
        Button button;
        TextView email;
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
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
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
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
}
