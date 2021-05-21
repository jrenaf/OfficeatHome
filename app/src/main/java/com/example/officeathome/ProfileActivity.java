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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listView;
    Button add;//添加按钮
    TextView note_id;//向其他界面传值
    ArrayList<HashMap<String, String>> list;

    private ImageView ivHead;//头像显示
    private Button btnTakephoto;//拍照
    //private Button btnPhotos;//相册
    private Bitmap head = null;//头像Bitmap
    private String headPath;
    private static String path="/sdcard/myHead/";//sd路径

    private FloatingActionButton backButton;
    //fireBase
    private String email;
    private TextView userName;
    private TextView availability;
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
        setContentView(R.layout.profile);
        //get the user's email
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("myID");
        //head = bundle.getParcelable("myHead");
        headPath = bundle.getString("myHead");
        userName = (TextView) findViewById(R.id.personalPageName);
        availability = (TextView) findViewById(R.id.personalPageAvb);
        department = (TextView) findViewById(R.id.personalPageDepartment);
        level = (TextView) findViewById(R.id.personalPageLevel);
        availSwitch = (Switch) findViewById(R.id.switchAvailable);
        availSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //This line has the error
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(availSwitch.isChecked()){
                    availability.setText("Available!");
                    myRef.child(user.getUid()).child("availability").setValue(true);
                    //Toast.makeText(this, "No todo now, please add one.", Toast.LENGTH_SHORT).show();
                }
                else {availability.setText("Not available!");
                    myRef.child(user.getUid()).child("availability").setValue(false);
                }
            }
        });
        initView();
        Query query = myRef.orderByChild("email").equalTo(email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Data parsing is being done within the extending classes.
                People people = dataSnapshot.getValue(People.class);
                userName.setText(people.username);
                if(people.availability){
                    availability.setText("Available!");
                    availSwitch.setChecked(true);
                }
                else{availability.setText("Not available!"); availSwitch.setChecked(false);}
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
        //initView();
        listView = (ListView) findViewById(R.id.listView);
        add = (Button) findViewById(R.id.add);

        add.setOnClickListener(this);
        backButton = findViewById(R.id.goBackButton);
        backButton.setOnClickListener(this);

        //通过list获取数据库表中的所有id和title，通过ListAdapter给listView赋值
        final NoteOperator noteOperator = new NoteOperator(ProfileActivity.this);
        list = noteOperator.getNoteList();
        final ListAdapter listAdapter = new SimpleAdapter(ProfileActivity.this, list, R.layout.item_todo,
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
                    intent.setClass(ProfileActivity.this, DetailActivity.class);
                    Bundle bd = new Bundle();
                    Bundle bd2 = new Bundle();
                    bd.putString("myID",email);
                    bd2.putString("myHead",headPath);
                    intent.putExtras(bd);
                    intent.putExtras(bd2);
                    intent.putExtra("note_id", Integer.parseInt(id));
                    startActivity(intent);
                }
            });

            //长按实现对列表的删除
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setMessage("Delete this todo？");
                    builder.setTitle("Reminder");

                    //添加AlterDialog.Builder对象的setPositiveButton()方法
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            String id = list.get(position).get("id");
                            noteOperator.delete(Integer.parseInt(id));
                            list.remove(position);
                            //listAdapter.notify();
                            listView.setAdapter(listAdapter);
                        }
                    });

                    //添加AlterDialog.Builder对象的setNegativeButton()方法
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        } else {
            //Toast.makeText(this, "No todo now, please add one.", Toast.LENGTH_SHORT).show();
        }
    }
    public void showMessageBoard(View view) {
        // Do something in response to button click
        Intent messageIntent = new Intent(this, ThirdActivity.class);
        messageIntent.putExtra("myEmail", email);
        startActivity(messageIntent);
    }
    @Override
    public void onClick(View view) {
        /*Intent intent = new Intent();
        intent.setClass(ProfileActivity.this, AddActivity.class);
        ProfileActivity.this.startActivity(intent);*/
        switch (view.getId()) {
            case R.id.personalPagePhoto://从相册里面取照片
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                break;
            case R.id.add:
                Intent intent = new Intent(ProfileActivity.this, AddActivity.class);
                //intent.setClass(ProfileActivity.this, AddActivity.class);
                Bundle bd1 = new Bundle();
                bd1.putString("myID",email);
                intent.putExtras(bd1);
                Bundle bd2 = new Bundle();
                bd2.putString("myHead",headPath);
                intent.putExtras(bd2);
                startActivity(intent);
                finish();
                break;
            case R.id.goBackButton:
                Intent intent2 = new Intent(ProfileActivity.this, MainSearch.class);
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
//        final long ONE_MEGABYTE = 1024 * 1024;
//        headRef.child(email).getBytes(ONE_MEGABYTE).
//                addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        // Data for "images/island.jpg" is returns, use this as needed
//                        head = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        ivHead.setImageBitmap(head);
//                        Toast.makeText(ProfileActivity.this,"Download Success",Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(ProfileActivity.this,"Download Failed",Toast.LENGTH_SHORT).show();
//            }
//        });
        ivHead.setOnClickListener(this);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if(head!=null){
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);//保存在SD卡中
                        setPicToCloud(head);
                        ivHead.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToCloud(Bitmap head) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        head.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = headRef.child(email).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(ProfileActivity.this, "Failed Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(ProfileActivity.this, "Successful Upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ;
    /**
     * 调用系统的裁剪
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
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