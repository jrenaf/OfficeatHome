package com.example.officeathome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivHead;//头像显示
    private Bitmap head = null;//头像Bitmap
    private String headPath;
    private static String path="/sdcard/myHead/";//sd路径

    private FloatingActionButton backButton;
    //fireBase
    private String userEmail;
    private String targetEmail;
    private TextView userName;
    private TextView department;
    private TextView level;
    private TextView availability;
    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference headRef = storage.getReference("heads");
    private People people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        //get the user's email
        Bundle bundle = getIntent().getExtras();
        userEmail = bundle.getString("myID");
        targetEmail = bundle.getString("targetID");
        //head = bundle.getParcelable("myHead");
        headPath = bundle.getString("myHead");
        ivHead = findViewById(R.id.personalPagePhoto);
        userName = (TextView) findViewById(R.id.personalPageName);
        department = (TextView) findViewById(R.id.personalPageDepartment);
        level = (TextView) findViewById(R.id.personalPageLevel);
        availability = (TextView) findViewById(R.id.personalPageAvb);
        Query query = myRef.orderByChild("email").equalTo(targetEmail);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Data parsing is being done within the extending classes.
                people = dataSnapshot.getValue(People.class);
                userName.setText(people.username);
                if (people.availability) {
                    availability.setText("Available!");
                } else {
                    availability.setText("Not available!");
                }
                department.setText(people.myDep);
                level.setText(people.level);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        backButton = findViewById(R.id.goBackButton);
        backButton.setOnClickListener(this);
        setAvatar(ivHead);
    }

    public void showMessageBoard(View view) {
        // Do something in response to button click
        Intent messageIntent = new Intent(this, SecondActivity.class);
        messageIntent.putExtra("userEmail", userEmail);
        messageIntent.putExtra("targetEmail", targetEmail);
        startActivity(messageIntent);
    }
    @Override
    public void onClick(View view) {
        /*Intent intent = new Intent();
        intent.setClass(ProfileActivity.this, AddActivity.class);
        ProfileActivity.this.startActivity(intent);*/
        switch (view.getId()) {
            case R.id.goBackButton:
                Intent intent2 = new Intent(this, MainSearch.class);
                //Log.d("TAG", "*****Email address:" + email);
                Bundle bd = new Bundle();
                bd.putString("myID",userEmail);
                intent2.putExtras(bd);
                Bundle bd3 = new Bundle();
                bd3.putString("myHead",headPath);
                intent2.putExtras(bd3);
                //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                break;
            default:
                break;
        }
    }

    private void setAvatar(ImageView imv) {
        final long ONE_MEGABYTE = 2048 * 2048;
//        ImageView ivHead1 = (ImageView) findViewById(i1);
//        ImageView ivHead2 = (ImageView) findViewById(i2);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);

        headRef.child(targetEmail).getBytes(ONE_MEGABYTE).
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