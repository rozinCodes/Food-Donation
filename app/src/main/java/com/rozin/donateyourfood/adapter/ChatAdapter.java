package com.rozin.donateyourfood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rozin.donateyourfood.R;
import com.rozin.donateyourfood.models.Message;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

// Adapter for display chats on a Recycler View
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> mMessages; // The list of Message objects
    private Context mContext; // The context
    private String mUserId; // The user id

    // Initialize the class and its vars
    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);

        // If it is a message from myself it has to be displayed in one way
        if (isMe) {

            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.INVISIBLE);
            holder.body.setGravity(Gravity.END);

            holder.body.setBackground(mContext.getResources().getDrawable(R.drawable.sender_bg));
        } else { // And if it is from the another person chatting with me, it has to be displayed in other way
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.body.setGravity(Gravity.RIGHT);            holder.imageMe.setVisibility(View.INVISIBLE);

            holder.body.setBackground(mContext.getResources().getDrawable(R.drawable.reciever_bg));
        }

        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
        // Glide allows us to load image async and from URLs, also it saves in cache the images so we only
        // have to download them once (if they are the same)
//        Glide.with(mContext).load(getProfileUrl(message.getUserId())).into(profileView);

        // Finally set the message
        holder.body.setText(message.getBody());
    }

    // Function: It creates a custom avatar image, generating a special URL
    // using the Gravatar service
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;
        LinearLayout layoutBg;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
            layoutBg = (LinearLayout) itemView.findViewById(R.id.msg_bg);
        }
    }
}