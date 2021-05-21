// COMP4521     WAN Yuxuan  20493150    ywanaf@connect.ust.hk
// COMP4521     REN Jiming  20493019    jrenaf@connect.ust.hk
// COMP4521     YIN Yue     20493368    yyinai@connect.ust.hk
package com.example.officeathome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final LinkedList<String> mMesList;
    private final LinkedList<String> mAuthorList;
    private final LinkedList<String> mDateList;
    private LayoutInflater mInflater;

    public MessageAdapter(Context context, LinkedList<String> messageList,
                          LinkedList<String> authorList, LinkedList<String> datelist){
        mInflater = LayoutInflater.from(context);
        this.mMesList = messageList;
        this.mAuthorList = authorList;
        this.mDateList = datelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.message_item,
                parent, false);
        return new ViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String mCurrent = mMesList.get(position);
        holder.messageItemView.setText(mCurrent);

        String mAuthor = mAuthorList.get(position);
        holder.messageAuthorView.setText(mAuthor);

        String mDate = mDateList.get(position);
        holder.messageDateView.setText(mDate);
    }

    @Override
    public int getItemCount() {
        return mMesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView messageItemView;
        private final TextView messageAuthorView;
        private final TextView messageDateView;
        final MessageAdapter mAdapter;

        public ViewHolder(@NonNull View itemView, MessageAdapter adapter) {
            super(itemView);

            messageItemView = itemView.findViewById(R.id.message_one);
            messageAuthorView = itemView.findViewById(R.id.message_author);
            messageDateView = itemView.findViewById(R.id.message_date);
            this.mAdapter = adapter;
        }
    }
}

