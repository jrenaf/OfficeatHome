package com.example.officeathome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.HashMap;

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listView;
    TextView note_id;//向其他界面传值
    ArrayList<HashMap<String, String>> list;
    //fireBase
    String email;
    private TextView userName;
    private TextView availability;
    private TextView department;
    private TextView level;
    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        //get the user's email
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("targetID");
        userName = (TextView) findViewById(R.id.personalPageName);
        availability = (TextView) findViewById(R.id.personalPageAvb);
        department = (TextView) findViewById(R.id.personalPageDepartment);
        level = (TextView) findViewById(R.id.personalPageLevel);
        Query query = myRef.orderByChild("email").equalTo(email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Data parsing is being done within the extending classes.
                People people = dataSnapshot.getValue(People.class);
                userName.setText(people.username);
                if(people.availability){
                    availability.setText("Available!");
                }
                else{availability.setText("Not available!"); }
                department.setText(people.myDep);
                level.setText(people.level);
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
        listView = (ListView) findViewById(R.id.listView);

        //通过list获取数据库表中的所有id和title，通过ListAdapter给listView赋值
        final NoteOperator noteOperator = new NoteOperator(com.example.officeathome.OtherProfileActivity.this);
        list = noteOperator.getNoteList();
        final ListAdapter listAdapter = new SimpleAdapter(com.example.officeathome.OtherProfileActivity.this, list, R.layout.item_todo,
                new String[]{"id", "title"}, new int[]{R.id.note_id, R.id.note_title});
        listView.setAdapter(listAdapter);

        //通过添加界面传来的值判断是否要刷新listView
        Intent intent = getIntent();
        int flag = intent.getIntExtra("Insert", 0);
        if (flag == 1) {
            list = noteOperator.getNoteList();
            listView.setAdapter(listAdapter);}
        if (list.size() != 0) {
            //点击listView的任何一项跳到详情页面
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long i) {

                    String id = list.get(position).get("id");
                    Intent intent = new Intent();
                    intent.setClass(com.example.officeathome.OtherProfileActivity.this, DetailActivity.class);
                    intent.putExtra("note_id", Integer.parseInt(id));
                    startActivity(intent);
                }
            });
        }
        else {
            //Toast.makeText(this, "No todo now, please add one.", Toast.LENGTH_SHORT).show();
        }
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
    }
}
