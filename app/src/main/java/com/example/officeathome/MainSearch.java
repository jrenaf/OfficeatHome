package com.example.officeathome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainSearch extends AppCompatActivity{

    /**
     * Creates the content view and toolbar, sets up the tab layout, and sets up
     * a page adapter to manage views in fragments. The user clicks a tab and
     * navigates to the view fragment.
     *
     * @param savedInstanceState Saved instance state bundle.
     */
    public String email;
    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");

    private Bitmap selfHead;
    private String selfHeadPath;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference headRef = storage.getReference("heads");
    private FloatingActionButton meButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Intent intent=this.getIntent();
        email = intent.getStringExtra("myID");
        selfHeadPath = intent.getStringExtra("myHead");

        meButton = findViewById(R.id.fab);
        if(selfHeadPath.equals(""))meButton.setVisibility(View.GONE);

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
                goToAfterSearchPage(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<People> mData = searchBy(newText);
                return false;
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//
        // Read the settings from the shared preferences, put them into the
        // SettingsActivity, and display a toast.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingActivity.KEY_PREF_EXAMPLE_SWITCH, false);
        Toast.makeText(this, switchPref.toString(),
                Toast.LENGTH_SHORT).show();

        initSelfHead();

    }

    private void initSelfHead() {
        final long ONE_MEGABYTE = 1024 * 1024;
        headRef.child(email).getBytes(ONE_MEGABYTE).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        selfHead = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        //Write file
                        selfHeadPath = "myHead.jpeg";
                        FileOutputStream stream = null;
                        try {
                            stream = openFileOutput(selfHeadPath, Context.MODE_PRIVATE);
                            selfHead.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                            stream.close();
                            selfHead.recycle();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        meButton.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                selfHead = null;
                selfHeadPath = null;
                Toast.makeText(MainSearch.this,"Download Failed",Toast.LENGTH_SHORT).show();
            }
        });
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

    public void launchMeProfile(View view) {
        Intent intent = new Intent(MainSearch.this, ProfileActivity.class);
        intent.putExtra("myID", email);
        //intent.putExtra("myHead", selfHead);
        intent.putExtra("myHead", selfHeadPath);
        startActivity(intent);
        finish();
    }

    public ArrayList<People> searchBy(String keyword) {
        ArrayList<People> mData = new ArrayList<People>();
        Query query = myRef.orderByChild("username").equalTo(keyword);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mData.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    // Data parsing is being done within the extending classes.
                    mData.add(dataSnapshot.getValue(People.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mData;
    }

    public void goToAfterSearchPage(String query){
        Intent intent = new Intent(this, AfterSearch.class);
        intent.putExtra("q", query);
        intent.putExtra("myID", email);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }
}



