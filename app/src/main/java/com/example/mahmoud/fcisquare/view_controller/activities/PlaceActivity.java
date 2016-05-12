package com.example.mahmoud.fcisquare.view_controller.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.mahmoud.fcisquare.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahmoud on 4/28/2016.
 */
public class PlaceActivity extends ActionBarActivity implements AbsListView.OnScrollListener {

    private static final int MAX_ROWS = 50;
    private int lastTopValue = 0;

    private List<String> commentList = new ArrayList<>();
    private ListView listView;
    private ImageView backgroundImage;
    private ArrayAdapter adapter;

    Button savePlace, checkinPlace, commentButton, likeButton;
    TextView placeName;
    EditText commentField;

    String placeID;
    StringRequest stringRequest;
    String savePlaceURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/savePlace";
    String checkinPlaceURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/checkIn";
    String commentURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/comment";
    String likeURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/likeCheckIn";
    ListView commentListView;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        listView = (ListView) findViewById(R.id.commentListView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // inflate custom header and attach it to the list
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.place_activity_header, listView, false);
        listView.addHeaderView(header, listView, false);
        placeName = (TextView) header.findViewById(R.id.placName_PlaceActivity);
        placeName.setText(getIntent().getStringExtra("placeName"));
        placeID = getIntent().getStringExtra("placeID");
        toolbar.setTitle(placeName.getText().toString());

        savePlace = (Button) findViewById(R.id.saveButton_PlaceActivity);
        checkinPlace = (Button) findViewById(R.id.checkinButton_PlaceActivity);
        commentButton = (Button) findViewById(R.id.commentButton_PlaceActivity);
        likeButton = (Button) findViewById(R.id.LikeButton_PlaceActivity);
        commentField = (EditText) findViewById(R.id.comment_PlaceActivity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new ArrayAdapter(this, R.layout.activity_place_listview, commentList);
        listView.setAdapter(adapter);

        // we take the background image and button reference from the header
        backgroundImage = (ImageView) header.findViewById(R.id.listHeaderImage);
        listView.setOnScrollListener(this);

        savePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlceService(savePlaceURL, placeID);
            }
        });
        checkinPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInPlceService(checkinPlaceURL, placeID, commentField.getText().toString());
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeService(likeURL, placeID, User.id);
            }
        });
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentField.getText().toString().isEmpty() == false) {
                    commentList.add(commentField.getText().toString());
                    commentService(commentURL, commentField.getText().toString(), placeID.toString(), User.id.toString());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Rect rect = new Rect();
        backgroundImage.getLocalVisibleRect(rect);
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            backgroundImage.setY((float) (rect.top / 2.0));
        }
    }

    private void savePlceService(String url, final String placeID) {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (Integer.parseInt(jsonObject.getString("status")) == 1) {
                        Toast.makeText(PlaceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PlaceActivity.this, "Already Saved", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlaceActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> allFollowersData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                // Save place
                allFollowersData.put("placeID", placeID.toString());
                allFollowersData.put("userID", User.id.toString());

                return allFollowersData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void checkInPlceService(String url, final String placeID, final String comment) {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(PlaceActivity.this, "Home Response " + response.toString(), Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    if (Integer.parseInt(jsonObject.getString("status")) == 1) {
                        Toast.makeText(PlaceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PlaceActivity.this, "already checked in", Toast.LENGTH_SHORT).show();
                    }
//                    if (jsonObject.getString("status") == "1" && saveOrCheckin == 1) {
//                        Toast.makeText(PlaceActivity.this, "Place has been saved", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(PlaceActivity.this, "Error saving place", Toast.LENGTH_SHORT).show();
//                    }
//                    if (jsonObject.getString("status") == "1" && saveOrCheckin == 2) {
//                        Toast.makeText(PlaceActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(PlaceActivity.this, "Error checkin  place", Toast.LENGTH_SHORT).show();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlaceActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> allFollowersData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                // Save place
                allFollowersData.put("placeID", placeID.toString());
                allFollowersData.put("Text", comment.toString());
                allFollowersData.put("userID", User.id.toString());
                return allFollowersData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void commentService(final String url, final String comment, final String placeID, final String userID) {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(PlaceActivity.this, "Home Response " + response.toString(), Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status") == "1") {
                        Toast.makeText(PlaceActivity.this, "comment has been saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PlaceActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlaceActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> allFollowersData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                allFollowersData.put("CheckinID", placeID.toString());
                allFollowersData.put("userID", userID.toString());
                allFollowersData.put("Text", comment.toString());
                return allFollowersData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void likeService(final String url, final String placeID, final String userID) {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(PlaceActivity.this, "Home Response " + response.toString(), Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status") == "1") {
                        Toast.makeText(PlaceActivity.this, "Like has been saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PlaceActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlaceActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> allFollowersData = new HashMap<String, String>();
                // take the input from the edit text
                // Key in the database , value from the edit text
                allFollowersData.put("checkInID", placeID);
                allFollowersData.put("userID", userID);
                return allFollowersData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
//public class PlaceActivity extends Activity {
//    Button savePlace, checkinPlace;
//    TextView placeName;
//    List<String> comments;
//    String placeID;
//    StringRequest stringRequest;
//    String savePlaceURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/savePlace";
//    String checkinPlaceURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/checkIn";
//    ListView commentListView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_place);
//        savePlace = (Button) findViewById(R.id.saveButton_PlaceActivity);
//        checkinPlace = (Button) findViewById(R.id.checkinButton_PlaceActivity);
//        placeName = (TextView) findViewById(R.id.placName_PlaceActivity);
//        commentListView = (ListView) findViewById(R.id.commentsListView);
//        // get the name from the Home Activity
//        placeName.setText(getIntent().getStringExtra("placeName"));
//        placeID = getIntent().getStringExtra("placeID");
//        savePlace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveAndcheckinPlace(savePlaceURL, 1);
//            }
//        });
//        checkinPlace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveAndcheckinPlace(checkinPlaceURL, 2);
//            }
//        });
//
//
//    }
//

