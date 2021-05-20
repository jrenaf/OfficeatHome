package com.example.officeathome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExecutivesFragment extends Fragment {
    private TableLayout t1;
    private ArrayList<People> arr_people = new ArrayList<People>();;

    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myref = database.getReference("user");
    private ChildEventListener mylistener;

    private static final String TAG = "ExecutivesFragment";

    @Override
    public void onStart() {
        super.onStart();
        mylistener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                People newUser = dataSnapshot.getValue(People.class);
                arr_people.add(newUser);
                Log.d(TAG, "Username: " + newUser.username);
                Log.d(TAG, "Length: " + arr_people.size());
                //init();
                //addOneRow(newUser);
                //explodeView(arr_people);
                addOneData(arr_people);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        myref.orderByChild("myDep").equalTo("Executives").addChildEventListener(mylistener);

    }

    @Override
    public void onStop() {
        super.onStop();

        myref.orderByChild("myDep").equalTo("Executives").removeEventListener(mylistener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        t1 = view.findViewById(R.id.executive_table);

    }

    public ExecutivesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_tab_fragment1, container, false);

    }

    private void addOneData(ArrayList<People> mData){
        int datasize = mData.size();
        if (datasize %2 == 0){
            for (int i = t1.getChildCount()-3; i < t1.getChildCount(); i++) {
                View child = t1.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
            }

            addOneBlock(mData.get(datasize-2), mData.get(datasize-1));
        }
        else{
            addOneBlock(mData.get(datasize-1));
        }
    }

    private void addOneBlock(People people1, People people2){
        //first row of the block: two bitmap;
        TableRow tr1 = new TableRow(getActivity());
        tr1.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ImageView imv1 = new ImageView(getActivity());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
        imv1.setImageBitmap(bm);
        ImageView imv2 = new ImageView(getActivity());
        imv2.setImageBitmap(bm);
        tr1.addView(imv1);
        tr1.addView(imv2);
        t1.addView(tr1);

        //second row of the block: names;
        TableRow tr2 = new TableRow(getActivity());
        TextView t1v = new TextView(getActivity());
        t1v.setText(people1.username);
        t1v.setTextColor(Color.BLUE);
        t1v.setTextSize(20);
        t1v.setGravity(Gravity.CENTER);

        TextView t2v = new TextView(getActivity());
        t2v.setText(people2.username);
        t2v.setTextColor(Color.BLUE);
        t2v.setTextSize(20);
        t2v.setGravity(Gravity.CENTER);

        tr2.addView(t1v);
        tr2.addView(t2v);
        t1.addView(tr2);

        //third row of the block: department and level;
        TableRow tr3 = new TableRow(getActivity());
        TextView t3v = new TextView(getActivity());
        t3v.setText(people1.myDep+", "+people1.level);
        t3v.setTextColor(Color.BLACK);
        t3v.setGravity(Gravity.CENTER);

        TextView t4v = new TextView(getActivity());
        t4v.setText(people2.myDep+", "+people2.level);
        t4v.setTextColor(Color.BLACK);
        t4v.setGravity(Gravity.CENTER);

        tr3.addView(t3v);
        tr3.addView(t4v);
        t1.addView(tr3);
    }


    private void addOneBlock(People people){
        //first row of the block: two bitmap;

        TableRow tr1 = new TableRow(getActivity());
        tr1.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ImageView imv1 = new ImageView(getActivity());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
        imv1.setImageBitmap(bm);
        tr1.addView(imv1);
        tr1.addView(new ImageView(getActivity()));
        t1.addView(tr1);

        //second row of the block: names;
        TableRow tr2 = new TableRow(getActivity());
        TextView t1v = new TextView(getActivity());
        t1v.setText(people.username);
        t1v.setTextColor(Color.BLUE);
        t1v.setTextSize(20);
        t1v.setGravity(Gravity.CENTER);

        tr2.addView(t1v);
        tr2.addView(new TextView(getActivity()));
        t1.addView(tr2);

        //third row of the block: department and level;
        TableRow tr3 = new TableRow(getActivity());
        TextView t3v = new TextView(getActivity());
        t3v.setText(people.myDep+", "+people.level);
        t3v.setTextColor(Color.BLACK);
        t3v.setGravity(Gravity.CENTER);

        tr3.addView(t3v);
        tr3.addView(new TextView(getActivity()));
        t1.addView(tr3);

    }


}

