package com.rene.pomodorotrello.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.fragments.TasksFragment;
import com.rene.pomodorotrello.util.Constants;

public class TasksActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TasksFragment.OnFragmentInteractionListener {

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), TasksActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tasks_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pomodoro) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[3];
        Context context;

        PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;

            tabTitles[Constants.TO_DO_ID] = context.getResources().getString(R.string.tab_tasks_title_todo);
            tabTitles[Constants.DOING_ID] = context.getResources().getString(R.string.tab_tasks_title_doing);
            tabTitles[Constants.DONE_ID] = context.getResources().getString(R.string.tab_tasks_title_done);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TasksFragment();
            Bundle arguments = new Bundle();

            switch (position) {
                case Constants.TO_DO_ID:
                    arguments.putInt(TasksFragment.LIST_ID, Constants.TO_DO_ID);
                    fragment.setArguments(arguments);
                    return fragment;
                case Constants.DOING_ID:
                    arguments.putInt(TasksFragment.LIST_ID, Constants.DOING_ID);
                    fragment.setArguments(arguments);
                    return fragment;
                case Constants.DONE_ID:
                    arguments.putInt(TasksFragment.LIST_ID, Constants.DONE_ID);
                    fragment.setArguments(arguments);
                    return fragment;
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        View getTabView(int position) {
            View tab = LayoutInflater.from(TasksActivity.this).inflate(R.layout.tab_tasks_layout, null);
            TextView tv = (TextView) tab.findViewById(R.id.title_text);
            tv.setText(tabTitles[position]);
            return tab;
        }

    }
}
