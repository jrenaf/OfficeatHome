package com.example.officeathome;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back;
    private Button save;
    private EditText title;
    private EditText context;
    private int note_id=0;
    private String get_title;
    private String get_context;
    private String headPath;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("myID");
        headPath = bundle.getString("myHead");

        title=(EditText) findViewById(R.id.title_detail);
        context=(EditText) findViewById(R.id.context_detail);
        back=(Button)findViewById(R.id.back_detail);
        save=(Button)findViewById(R.id.save_detail);

        back.setOnClickListener(this);
        save.setOnClickListener(this);

        //接收listView中点击item传来的note_id,
        Intent intent=getIntent();
        note_id=intent.getIntExtra("note_id",0);
        NoteOperator noteOperator=new NoteOperator( this);
        Note note = noteOperator.getNoteById(note_id);

        title.setText(String.valueOf(note.title));
        context.setText(String.valueOf(note.context));
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.back_detail)) {
            Intent intent = new Intent(DetailActivity.this, ProfileActivity.class);
            Bundle bd = new Bundle();
            bd.putString("myID",email);
            intent.putExtras(bd);
            Bundle bd2 = new Bundle();
            bd2.putString("myHead",headPath);
            intent.putExtras(bd2);
            startActivity(intent);
        }else if(view == findViewById(R.id.save_detail)){
            get_title=title.getText().toString().trim();
            get_context=context.getText().toString().trim();
            if(TextUtils.isEmpty(get_title)||TextUtils.isEmpty(get_context)){
                Toast.makeText(this,"The modified content cannot be empty.",Toast.LENGTH_SHORT).show();
            }else{
                Note note=new Note();
                note.note_id=note_id;
                note.title=get_title;
                note.context=get_context;
                NoteOperator noteOperator=new NoteOperator(DetailActivity.this);
                noteOperator.update(note);

                Toast.makeText(this,"Modify successfully!",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(DetailActivity.this,ProfileActivity.class);
                Bundle bd = new Bundle();
                bd.putString("myID",email);
                intent.putExtras(bd);
                Bundle bd2 = new Bundle();
                bd2.putString("myHead",headPath);
                intent.putExtras(bd2);
                startActivity(intent);

            }
        }
    }
}