// COMP4521     WAN Yuxuan  20493150    ywanaf@connect.ust.hk
// COMP4521     REN Jiming  20493019    jrenaf@connect.ust.hk
// COMP4521     YIN Yue     20493368    yyinai@connect.ust.hk
package com.example.officeathome;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {
    private EditText mMes;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private String senderEmail;
    private String targetEmail;
    private String receiver_Uid;
    private String sender_uid;

    private String TAG="SecondActivity";

//    private final LinkedList<String> mMesList = new LinkedList<>();
//    private final LinkedList<String> mAuthList = new LinkedList<>();
//    private final LinkedList<String> mDateList = new LinkedList<>();
    private final LinkedList<Message> mMessageList = new LinkedList<>();

    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");
    private DatabaseReference messageRef = database.getReference("message");
    private ChildEventListener listener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Bundle bundle = getIntent().getExtras();
        senderEmail = bundle.getString("userEmail");
        targetEmail = bundle.getString("targetEmail");

        mMes = (EditText)findViewById(R.id.edit_message);
//        for(int i = 0; i < 20; i++){
//            mMesList.add("Hello"+i);
//        }
        // Get a handle to the RecyclerView.
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_gchat);
        // Create an adapter and supply the data to be displayed.
//        mAdapter = new MessageAdapter(this, mMesList, mAuthList, mDateList);
        mAdapter = new MessageAdapter(this, mMessageList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        Query query = myRef.orderByChild("email").equalTo(targetEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    receiver_Uid = childSnapshot.getKey();
                    initBoard();
                }
                Toast.makeText(SecondActivity.this, receiver_Uid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sender_uid = user.getUid();
        //Toast.makeText(SecondActivity.this, sender_uid, Toast.LENGTH_SHORT).show();

//        messageRef.child(receiver_Uid).orderByChild("sender_name").equalTo(senderEmail).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                //for (DataSnapshot snapshot1 : snapshot.getChildren()){
//                int ListSize = mMesList.size();
//                Message ld = snapshot.getValue(Message.class);
//                mMesList.addLast(ld.text);
//                mAuthList.addLast(ld.sender_name);
//                mDateList.addLast(ld.date);
//                mRecyclerView.getAdapter().notifyItemInserted(ListSize);
//                mRecyclerView.smoothScrollToPosition(ListSize);
//                //}
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void initBoard(){
        messageRef.child(sender_uid).orderByChild("sender_name").equalTo(targetEmail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //for (DataSnapshot snapshot1 : snapshot.getChildren()){
                int ListSize = mMessageList.size();
                Message ld = snapshot.getValue(Message.class);
//                mMesList.addLast(ld.text);
//                mAuthList.addLast(ld.sender_name);
//                mDateList.addLast(ld.date);
                mMessageList.addLast(ld);
                Collections.sort(mMessageList, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o1.date.compareTo(o2.date);
                    }
                });
//                mRecyclerView.getAdapter().notifyItemInserted(ListSize);
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(ListSize);
                //}

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageRef.child(receiver_Uid).orderByChild("sender_name").equalTo(senderEmail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //for (DataSnapshot snapshot1 : snapshot.getChildren()){
                int ListSize = mMessageList.size();
                Message ld = snapshot.getValue(Message.class);
//                mMesList.addLast(ld.text);
//                mAuthList.addLast(ld.sender_name);
//                mDateList.addLast(ld.date);
                mMessageList.addLast(ld);
                Collections.sort(mMessageList, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o1.date.compareTo(o2.date);
                    }
                });
//                mRecyclerView.getAdapter().notifyItemInserted(ListSize);
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(ListSize);
                //}

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onUserInput(View view) {
        String message = mMes.getText().toString();

        writeMessageTo(receiver_Uid, message, senderEmail);
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        mMes.getText().clear();
    }


    private void writeMessageTo(String receiver_uid, String text, String sender_name) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = messageRef.child(receiver_uid).push().getKey();
        String date = getCurrentTime();

        Message mes = new Message(sender_uid, receiver_uid, text, date, sender_name);

        messageRef.child(receiver_uid).child(key).setValue(mes);
    }

    private String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();
        return strDate;
    }
}
