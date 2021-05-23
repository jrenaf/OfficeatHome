// COMP4521     WAN Yuxuan  20493150    ywanaf@connect.ust.hk
// COMP4521     REN Jiming  20493019    jrenaf@connect.ust.hk
// COMP4521     YIN Yue     20493368    yyinai@connect.ust.hk
package com.example.officeathome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MarketingFragment extends Fragment implements  View.OnClickListener{
    public String email;
    private TableLayout t1;
    private String myHead;
    private ArrayList<People> arr_people = new ArrayList<People>();;

    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myref = database.getReference("user");
    private ChildEventListener mylistener;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference headRef = storage.getReference("heads");

    private static final String TAG = "MarketingFragment";

    @Override
    public void onStart() {
        super.onStart();
        email = ((MainSearch)this.getActivity()).email;
        myHead = ((MainSearch)this.getActivity()).selfHeadPath;

        arr_people = new ArrayList<>();
        int count = t1.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = t1.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        mylistener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                People newUser = dataSnapshot.getValue(People.class);
                arr_people.add(newUser);
                Log.d(TAG, "Username: " + newUser.username);
                Log.d(TAG, "Length: " + arr_people.size());

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

        myref.orderByChild("myDep").equalTo("Marketing").addChildEventListener(mylistener);

    }

    @Override
    public void onStop() {
        super.onStop();

        myref.orderByChild("myDep").equalTo("Marketing").removeEventListener(mylistener);
    }

    public MarketingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_tab_fragment4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        t1 = view.findViewById(R.id.marketing_table);

    }

    private void addOneData(ArrayList<People> mData){
        int datasize = mData.size();
        if (datasize %2 == 0){
            for (int i = t1.getChildCount()-3; i < t1.getChildCount(); i++) {
                View child = t1.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
            }

            addOneBlock(mData.get(datasize-2), mData.get(datasize-1), datasize -2);
        }
        else{
            addOneBlock(mData.get(datasize-1), datasize -1);
        }
    }

    private void addOneBlock(People people1, People people2, int i){
        //first row of the block: two bitmap;
        TableRow tr1 = new TableRow(getActivity());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 24, 0, 0);
        tr1.setLayoutParams(params);
        ImageView imv1 = new ImageView(getActivity());
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
//        imv1.setImageBitmap(bm);
        imv1.setId(i);
        imv1.setOnClickListener(this);
        ImageView imv2 = new ImageView(getActivity());
//        imv2.setImageBitmap(bm);
        imv2.setId(i+1);
        imv2.setOnClickListener(this);
        setAvatar(imv1, imv2);
        if(!arr_people.get(imv1.getId()).availability) setBlackAndWhite(imv1);
        if(!arr_people.get(imv2.getId()).availability) setBlackAndWhite(imv2);
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


    private void addOneBlock(People people, int i){
        //first row of the block: two bitmap;

        TableRow tr1 = new TableRow(getActivity());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 24, 0, 0);
        tr1.setLayoutParams(params);
        ImageView imv1 = new ImageView(getActivity());
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);
//        imv1.setImageBitmap(bm);
        imv1.setId(i);
        imv1.setOnClickListener(this);
        setAvatar(imv1);
        if(arr_people.get(imv1.getId()).availability) setBlackAndWhite(imv1);
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

    private void setBlackAndWhite(ImageView imv){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imv.setColorFilter(filter);
    }
    @Override
    public void onClick(View view) {
        if (!email.equals(arr_people.get(view.getId()).email)) {
            Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
            intent.putExtra("myID", email);
            intent.putExtra("myHead", myHead);
            intent.putExtra("targetID",arr_people.get(view.getId()).email);
            startActivity(intent);
        }
        else{
        }
    }

    private void setAvatar(ImageView imv1, ImageView imv2) {
        final long ONE_MEGABYTE = 1024 * 1024;
//        ImageView ivHead1 = (ImageView) findViewById(i1);
//        ImageView ivHead2 = (ImageView) findViewById(i2);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person_avatar);

        headRef.child(arr_people.get(imv1.getId()).email).getBytes(ONE_MEGABYTE).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        Bitmap head1;
                        head1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        head1 = Bitmap.createScaledBitmap(head1, 250,250, true);
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

        headRef.child(arr_people.get(imv2.getId()).email).getBytes(ONE_MEGABYTE).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        Bitmap head2;
                        head2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        head2 = Bitmap.createScaledBitmap(head2, 250,250, true);
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

        headRef.child(arr_people.get(imv.getId()).email).getBytes(ONE_MEGABYTE).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        Bitmap head;
                        head = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        head = Bitmap.createScaledBitmap(head, 250,250, true);
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
