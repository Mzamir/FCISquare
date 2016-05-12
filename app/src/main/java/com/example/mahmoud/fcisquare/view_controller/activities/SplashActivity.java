package com.example.mahmoud.fcisquare.view_controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mahmoud.fcisquare.R;
import com.example.mahmoud.fcisquare.view_controller.dialogs.LoginDialog;
import com.example.mahmoud.fcisquare.view_controller.dialogs.SignupDialog;

public class SplashActivity extends AppCompatActivity {

    public static RequestQueue requestQueue;
    Button createAccount, haveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        initiate();
    }

    public void initiate() {
//        Services services = new Services();

        //Splash Activity UI
        createAccount = (Button) findViewById(R.id.createnewaccount);
        haveAccount = (Button) findViewById(R.id.haveaccount);
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog loginDialog = new LoginDialog(SplashActivity.this);
                loginDialog.show();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupDialog signupDialog = new SignupDialog(SplashActivity.this);
                signupDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
