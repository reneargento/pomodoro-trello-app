package com.rene.pomodorotrello.activities;

import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.fragments.ConfigFragment;

public class ConfigActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ConfigFragment.OnFragmentInteractionListener{

    private ConfigFragment configFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        configFragment = new ConfigFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, configFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tasks) {
           // configFragment = new ConfigFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, configFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_pomodoro) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (configFragment != null) {
            configFragment.userIsInteracting = true;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
