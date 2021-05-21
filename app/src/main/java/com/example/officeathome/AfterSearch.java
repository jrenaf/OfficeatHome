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

import android.provider.MediaStore;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class AfterSearch extends AppCompatActivity implements View.OnClickListener {

    private String email;
    private String query;
    private ArrayList<People> mData = new ArrayList<People>();
    private ArrayList<People> mData2 = new ArrayList<People>();

    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");


    // Use a service account
    // Access a Cloud Firestore instance from your Activity
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference headRef = storage.getReference("heads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aftersearch);

        Intent intent= this.getIntent();
        email = intent.getStringExtra("myID");
        query = intent.getStringExtra("q");


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = unwrap(v.getContext());
                Intent intent = new Intent(currentActivity, MainSearch.class);
                intent.putExtra("myID", email);
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
        mData2 = mData;
        Log.d("TAG", "****************" + size);
        int blockSize;
        if(size%2==0) blockSize=size/2;
        else blockSize =  (int)Math.ceil(size/2)+1;
        Log.d("TAG", "****************" + blockSize);
        for (int i =0; i < blockSize; i++){
            if(size%2!=0 && i==blockSize-1) {
                People people = mData.get(2 * i);
                addOneBlock(people,i);

            }
            else{
                People people1 = mData.get(2 * i);
                People people2 = mData.get(2 * i + 1);
                addOneBlock(people1, people2,i);
            }
        }
    }
    private void addOneBlock(People people1, People people2, int i){
        //first row of the block: two bitmap;
        TableLayout tblayout = findViewById(R.id.table_layout);
        TableRow tr1 = new TableRow(this);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 24, 0, 0);
        tr1.setLayoutParams(params);
        ImageView imv1 = new ImageView(this);
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
        //imv1.setImageBitmap(bm);
        imv1.setId(2*i);
        imv1.setOnClickListener(this);
        ImageView imv2 = new ImageView(this);
        //imv2.setImageBitmap(bm);
        imv2.setId(2*i+1);
        imv2.setOnClickListener(this);
        setAvatar(imv1, imv2);
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



    private void addOneBlock(People people, int i){
        //first row of the block: two bitmap;
        TableLayout tblayout = findViewById(R.id.table_layout);

        TableRow tr1 = new TableRow(this);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 24, 0, 0);
        tr1.setLayoutParams(params);
        ImageView imv1 = new ImageView(this);
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
        //imv1.setImageBitmap(bm);
        imv1.setId(2*i);
        imv1.setOnClickListener(this);
        setAvatar(imv1);

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
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
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
    @Override
    public void onClick(View view) {
        if (email != mData2.get(view.getId()).email) {
            Intent intent = new Intent(AfterSearch.this, OtherProfileActivity.class);
            intent.putExtra("myID", email);
            intent.putExtra("targetID",mData2.get(view.getId()).email) ;
            startActivity(intent);
        }
        else{}
    }

    private void setAvatar(ImageView imv1, ImageView imv2) {
        final long ONE_MEGABYTE = 1024 * 1024;
//        ImageView ivHead1 = (ImageView) findViewById(i1);
//        ImageView ivHead2 = (ImageView) findViewById(i2);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);

        headRef.child(mData2.get(imv1.getId()).email).getBytes(ONE_MEGABYTE).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        Bitmap head1;
                        head1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imv1.setImageBitmap(head1);
                        //Toast.makeText(AfterSearch.this, "Download Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                imv1.setImageBitmap(bm);
            }
        });

        headRef.child(mData2.get(imv2.getId()).email).getBytes(ONE_MEGABYTE).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        Bitmap head2;
                        head2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imv2.setImageBitmap(head2);
                        //Toast.makeText(AfterSearch.this, "Download Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                imv2.setImageBitmap(bm);
            }
        });

    }

    private void setAvatar(ImageView imv) {
        final long ONE_MEGABYTE = 2048 * 2048;
//        ImageView ivHead1 = (ImageView) findViewById(i1);
//        ImageView ivHead2 = (ImageView) findViewById(i2);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);

        headRef.child(mData2.get(imv.getId()).email).getBytes(ONE_MEGABYTE).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        Bitmap head;
                        head = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imv.setImageBitmap(head);
                        //Toast.makeText(AfterSearch.this, "Download Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                imv.setImageBitmap(bm);
            }
        });

    }
}
