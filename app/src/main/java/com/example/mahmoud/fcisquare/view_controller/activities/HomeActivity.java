package com.example.mahmoud.fcisquare.view_controller.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.mahmoud.fcisquare.adapters.PlacesAdapter;
import com.example.mahmoud.fcisquare.model.Place;
import com.example.mahmoud.fcisquare.model.User;
import com.example.mahmoud.fcisquare.view_controller.dialogs.AddPlaceDialog;
import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.ActionsFragment;
import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.CheckinPlacesFragment;
import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.FollowersFragment;
import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.NotificationsFragment;
import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.SavedPlacesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String name, email;
    TextView usernameText, emailText;
    Toolbar toolbar;

    List<Place> placeList;
    List<Place> sortedPlacesList;
    public static PlacesAdapter placesAdapter;
    ListView placeListView;

    StringRequest stringRequest;
    String allPlacesURL = "http://checkin-swe2.rhcloud.com/FCISquare/rest/showHomePage";
    String sortRatedPlaces = "http://checkin-swe2.rhcloud.com/FCISquare/rest/sortRatedPlaces";
    String sortCheckIns = "http://checkin-swe2.rhcloud.com/FCISquare/rest/sortNumOfCheckins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.sort);
        toolbar.setOverflowIcon(drawable);
        toolbar.setBackgroundColor(getResources().getColor(R.color.view_bg));
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.HomeFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlaceDialog addPlaceDialog = new AddPlaceDialog(HomeActivity.this);
                addPlaceDialog.show();
            }
        });
        initiate();
        placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, PlaceActivity.class);
                intent.putExtra("placeName", placeList.get(position).getName());
                intent.putExtra("placeID", placeList.get(position).getPlaceID());
                startActivity(intent);
            }
        });
    }

    public void initiate() {
        placeListView = (ListView) findViewById(R.id.homeListView);
        placeList = new ArrayList<>();
        fillListView(allPlacesURL);
        placesAdapter = new PlacesAdapter(HomeActivity.this, placeList);
        placeListView.setAdapter(placesAdapter);
        placesAdapter.notifyDataSetChanged();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        usernameText = (TextView) headerView.findViewById(R.id.navUsername);
        emailText = (TextView) headerView.findViewById(R.id.navEmail);
        usernameText.setText(getIntent().getStringExtra("username"));
        emailText.setText(getIntent().getStringExtra("email"));
    }

    private void fillListView(final String url) {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(HomeActivity.this, "Home Response " + response.toString(), Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
//                        Toast.makeText(HomeActivity.this, "number " + i + " " + object.toString(), Toast.LENGTH_SHORT).show();
                        placeList.add(new Place(object.getString("place"), object.getString("text"), R.drawable.like, R.drawable.save, object.getString("id")));
                        placesAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Home Error " + error.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void fillSortedList(String url) {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(HomeActivity.this, "Home Response " + response.toString(), Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(HomeActivity.this, "Response " + jsonArray.toString(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
//                        Toast.makeText(HomeActivity.this, "number " + i + " " + object.toString(), Toast.LENGTH_SHORT).show();
                        sortedPlacesList.add(new Place(object.getString("Place"), object.getString("NumberOfCheckins"), object.getString("Rate"), R.drawable.like, R.drawable.save));
                        placesAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Home Error " + error.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) android.support.v4.view.MenuItemCompat.getActionView(searchItem);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sortByCheckin) {
            sortedPlacesList = new ArrayList<>();
            fillSortedList(sortCheckIns);
            placesAdapter = new PlacesAdapter(HomeActivity.this, sortedPlacesList);
            placeListView.setAdapter(placesAdapter);
            placesAdapter.notifyDataSetChanged();
        } else if (id == R.id.sortByRate) {
            sortedPlacesList = new ArrayList<>();
            fillSortedList(sortRatedPlaces);
            placesAdapter = new PlacesAdapter(HomeActivity.this, sortedPlacesList);
            placeListView.setAdapter(placesAdapter);
            placesAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();
        int commit = 0;
        int id = item.getItemId();
        if (id == R.id.nav_followers) {
            toolbar.setTitle("Followers");
            commit = fragmentManager
                    .beginTransaction()
                    .replace(R.id.view_content, new FollowersFragment(),
                            "FRAGMENT").addToBackStack(null).commit();
        } else if (id == R.id.nav_home) {
            toolbar.setTitle("Home");
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        } else if (id == R.id.nav_checkins) {
            toolbar.setTitle("Checkins");
            commit = fragmentManager
                    .beginTransaction()
                    .replace(R.id.view_content, new CheckinPlacesFragment(),
                            "FRAGMENT").addToBackStack(null).commit();
        } else if (id == R.id.nav_myPlaces) {
            toolbar.setTitle("MyPlaces");
            commit = fragmentManager
                    .beginTransaction()
                    .replace(R.id.view_content, new SavedPlacesFragment(),
                            "FRAGMENT").addToBackStack(null).commit();
        } else if (id == R.id.nav_notification) {
            toolbar.setTitle("Notification");
            commit = fragmentManager
                    .beginTransaction()
                    .replace(R.id.view_content, new NotificationsFragment(),
                            "FRAGMENT").addToBackStack(null).commit();
        } else if (id == R.id.nav_actions) {
            toolbar.setTitle("Actions");
            commit = fragmentManager
                    .beginTransaction()
                    .replace(R.id.view_content, new ActionsFragment(),
                            "FRAGMENT").addToBackStack(null).commit();
        } else if (id == R.id.nav_Logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
