package com.example.officeathome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.preference.PreferenceManager;

import com.google.android.material.tabs.TabLayout;

public class MainSearch extends AppCompatActivity{

    /**
     * Creates the content view and toolbar, sets up the tab layout, and sets up
     * a page adapter to manage views in fragments. The user clicks a tab and
     * navigates to the view fragment.
     *
     * @param savedInstanceState Saved instance state bundle.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);


        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label4));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label5));

        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Use PagerAdapter to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager2 viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        final FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Setting a listener for clicks.
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

        //Set a listener on search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainSearch.this, "No Match found", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//
//        // Read the settings from the shared preferences, put them into the
//        // SettingsActivity, and display a toast.
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        Boolean switchPref = sharedPref.getBoolean
//                (SettingActivity.KEY_PREF_EXAMPLE_SWITCH, false);
//        Toast.makeText(this, switchPref.toString(),
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        // if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    /**
     * Handles option menu selections and automatically handles clicks
     * on the Up button in the app bar.
     *
     * @param item Item in options menu
     * @return True if Settings is selected in the options menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // If option menu item is Settings, return true.
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,
                    SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



