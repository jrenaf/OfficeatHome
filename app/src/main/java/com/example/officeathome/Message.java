// COMP4521     WAN Yuxuan  20493150    ywanaf@connect.ust.hk
// COMP4521     REN Jiming  20493019    jrenaf@connect.ust.hk
// COMP4521     YIN Yue     20493368    yyinai@connect.ust.hk
package com.example.officeathome;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message {
    public String sender_uid;
    public String receiver_uid;
    public String text;
    public String date;
    public String sender_name;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String sender_uid, String receiver_uid, String text, String date, String  sender_name) {
        this.sender_uid = sender_uid;
        this.receiver_uid = receiver_uid;
        this.text = text;
        this.date = date;
        this.sender_name = sender_name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sender_uid", sender_uid);
        result.put("receiver_uid", receiver_uid);
        result.put("text", text);
        result.put("date", date);
        result.put("sender_name", sender_name);

        return result;
    }

}
