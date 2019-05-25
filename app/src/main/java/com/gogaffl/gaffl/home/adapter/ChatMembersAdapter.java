package com.gogaffl.gaffl.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.model.JoinedUser;
import com.gogaffl.gaffl.home.model.JoinedUsers;

import java.util.ArrayList;

public class ChatMembersAdapter extends RecyclerView.Adapter<ChatMembersAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<JoinedUser> mJoinedUsers;
    private ArrayList<JoinedUsers> mJoinedUsersc;
    private String flexible = "";


    public ChatMembersAdapter(Context context, ArrayList<JoinedUser> joinedUsers) {
        mContext = context;
        mJoinedUsers = joinedUsers;
    }

    @NonNull
    @Override
    public ChatMembersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_list_layout, viewGroup, false);

        return new ChatMembersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMembersAdapter.MyViewHolder myViewHolder, int i) {
        final JoinedUser joinedUser = mJoinedUsers.get(i);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_img)
                .error(R.drawable.ic_signal_wifi_off)
                .priority(Priority.HIGH);


        Glide.with(mContext).load(joinedUser.getPictureUrl()).apply(options).
                thumbnail(0.5f).into(myViewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mJoinedUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.trip_joined_user_profile_pic);
        }
    }
}
