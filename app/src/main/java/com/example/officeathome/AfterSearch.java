package com.example.officeathome;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AfterSearch extends AppCompatActivity {

    private String email;
    private String query;
    public ArrayList<People> mData = new ArrayList<People>();;
    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aftersearch);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("ID");
        query =  bundle.getString("q");


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = unwrap(v.getContext());
                Intent intent = new Intent(currentActivity, MainSearch.class);
                intent.putExtra("id", email);
                // Tell the new activity how return when finished.
                currentActivity.startActivity(intent);
                finish();
                overridePendingTransition(R.anim.up_in, R.anim.up_out);
            }
        });

        searchBy(query);
    }

    private void explodeView(ArrayList<People> mData){
        TableLayout tblayout = findViewById(R.id.table_layout);

        //first of all, clear all views
        int count = tblayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tblayout.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        //based on the size , format the tablelayout
        int size = mData.size();
        Log.d("TAG", "****************" + size);
        int blockSize;
        if(size%2==0) blockSize=size/2;
        else blockSize =  (int)Math.ceil(size/2)+1;
        Log.d("TAG", "****************" + blockSize);
        for (int i =0; i < blockSize; i++){
            if(size%2!=0 && i==blockSize-1) {
                People people = mData.get(2 * i);
                addOneBlock(people);
            }
            else{
                People people1 = mData.get(2 * i);
                People people2 = mData.get(2 * i + 1);
                addOneBlock(people1, people2);
            }
        }
    }
    private void addOneBlock(People people1, People people2){
        //first row of the block: two bitmap;
        TableLayout tblayout = findViewById(R.id.table_layout);
        TableRow tr1 = new TableRow(this);
        tr1.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ImageView imv1 = new ImageView(this);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
        imv1.setImageBitmap(bm);
        ImageView imv2 = new ImageView(this);
        imv2.setImageBitmap(bm);
        tr1.addView(imv1);
        tr1.addView(imv2);
        tblayout.addView(tr1);

        //second row of the block: names;
        TableRow tr2 = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText(people1.username);
        t1v.setTextColor(Color.BLUE);
        t1v.setTextSize(20);
        t1v.setGravity(Gravity.CENTER);

        TextView t2v = new TextView(this);
        t2v.setText(people2.username);
        t2v.setTextColor(Color.BLUE);
        t2v.setTextSize(20);
        t2v.setGravity(Gravity.CENTER);

        tr2.addView(t1v);
        tr2.addView(t2v);
        tblayout.addView(tr2);

        //third row of the block: department and level;
        TableRow tr3 = new TableRow(this);
        TextView t3v = new TextView(this);
        t3v.setText(people1.myDep+", "+people1.level);
        t3v.setTextColor(Color.BLACK);
        t3v.setGravity(Gravity.CENTER);

        TextView t4v = new TextView(this);
        t4v.setText(people2.myDep+", "+people2.level);
        t4v.setTextColor(Color.BLACK);
        t4v.setGravity(Gravity.CENTER);

        tr3.addView(t3v);
        tr3.addView(t4v);
        tblayout.addView(tr3);
    }


    private void addOneBlock(People people){
        //first row of the block: two bitmap;
        TableLayout tblayout = findViewById(R.id.table_layout);

        TableRow tr1 = new TableRow(this);
        tr1.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ImageView imv1 = new ImageView(this);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
        imv1.setImageBitmap(bm);
        tr1.addView(imv1);
        tr1.addView(new ImageView(this));
        tblayout.addView(tr1);

        //second row of the block: names;
        TableRow tr2 = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText(people.username);
        t1v.setTextColor(Color.BLUE);
        t1v.setTextSize(20);
        t1v.setGravity(Gravity.CENTER);

        tr2.addView(t1v);
        tr2.addView(new TextView(this));
        tblayout.addView(tr2);

        //third row of the block: department and level;
        TableRow tr3 = new TableRow(this);
        TextView t3v = new TextView(this);
        t3v.setText(people.myDep+", "+people.level);
        t3v.setTextColor(Color.BLACK);
        t3v.setGravity(Gravity.CENTER);

        tr3.addView(t3v);
        tr3.addView(new TextView(this));
        tblayout.addView(tr3);

    }
    private void searchBy(String keyword) {
        ArrayList<People> mData = new ArrayList<People>();
        Query query = myRef.orderByChild("username").equalTo(keyword);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // Data parsing is being done within the extending classes.
                mData.add(dataSnapshot.getValue(People.class));
                Log.d("TAG", "here****************"+mData.size());
                explodeView(mData);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                long totalChilds = dataSnapshot.getChildrenCount();
//                int count = 0;
//                query.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                        // Data parsing is being done within the extending classes.
//                        mData.add(dataSnapshot.getValue(People.class));
//                        Log.d("TAG", "here****************"+mData.size());
//                        count += 1;
//                        if(count >= totalChilds){
//                            explodeView();
//                        }
//                    }
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//                explodeView();
//            }
//            public void onCancelled(DatabaseError firebaseError) { }
//        });
    }

    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (Activity) context;
    }
}