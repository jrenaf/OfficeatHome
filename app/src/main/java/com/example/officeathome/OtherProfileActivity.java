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
    private String email;
    private TextView userName;
    private TextView department;
    private TextView level;
    private Switch availSwitch;
    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference headRef = storage.getReference("heads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        //get the user's email
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("myID");
        //head = bundle.getParcelable("myHead");
        headPath = bundle.getString("myHead");
        userName = (TextView) findViewById(R.id.personalPageName);
        department = (TextView) findViewById(R.id.personalPageDepartment);
        level = (TextView) findViewById(R.id.personalPageLevel);
        initView();
        Query query = myRef.orderByChild("email").equalTo(email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Data parsing is being done within the extending classes.
                People people = dataSnapshot.getValue(People.class);
                userName.setText(people.username);
                if (people.availability) {
                    availSwitch.setChecked(true);
                } else {
                    availSwitch.setChecked(false);
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
    }

    public void showMessageBoard(View view) {
        // Do something in response to button click
        Intent messageIntent = new Intent(this, MessageBoard.class);
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
                bd.putString("myID",email);
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

    private void initView() {
        ivHead = (ImageView) findViewById(R.id.personalPagePhoto);
        if(headPath != null){
            try {
                FileInputStream is = this.openFileInput(headPath);
                head = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(head != null){
            ivHead.setImageBitmap(head);
        }
        ivHead.setOnClickListener(this);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName =path + "head.jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if(b == null){}
                else{b.flush();
                    b.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}