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

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String sender_uid, String receiver_uid, String text) {
        this.sender_uid = sender_uid;
        this.receiver_uid = receiver_uid;
        this.text = text;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sender_uid", sender_uid);
        result.put("receiver_uid", receiver_uid);
        result.put("text", text);

        return result;
    }

}
