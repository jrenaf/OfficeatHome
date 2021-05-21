// COMP4521     WAN Yuxuan  20493150    ywanaf@connect.ust.hk
// COMP4521     REN Jiming  20493019    jrenaf@connect.ust.hk
// COMP4521     YIN Yue     20493368    yyinai@connect.ust.hk
package com.example.officeathome;

import com.google.firebase.database.IgnoreExtraProperties;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;

import androidx.core.content.res.ResourcesCompat;

import java.nio.ByteBuffer;
import java.util.Base64;

@IgnoreExtraProperties
public class People  {

    //public String uid;
    public String email;
    public String username;
    public String password;
    public String level;
    public boolean availability;
    public String myDep;
    //public String avatar;

    public People() {
        // Default constructor required for calls to DataSnapshot.getValue(People.class)
    }

    public People(String email, String username, String password, String level, boolean availability,
                  String dep) {
        //this.uid = uid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.level = level;
        this.availability = availability;
        this.myDep = dep;

//        ImageUtil iu = new ImageUtil();
//        Resources res = getApplicationContext().getResources();
//        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.person_avatar, null);
//        Bitmap  bitmap = ((BitmapDrawable) drawable).getBitmap();
//        this.avatar = iu.convert(bitmap);
    }
}
