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

public class FindTripsChatMembersAdapter extends RecyclerView.Adapter<FindTripsChatMembersAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<JoinedUsers> mJoinedUsers;


    public FindTripsChatMembersAdapter(Context context, ArrayList<JoinedUsers> joinedUsers) {
        mContext = context;
        mJoinedUsers = joinedUsers;
    }

    @NonNull
    @Override
    public FindTripsChatMembersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.find_trip_chat_list_layout, viewGroup, false);

        return new FindTripsChatMembersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindTripsChatMembersAdapter.MyViewHolder myViewHolder, int i) {
        final JoinedUsers joinedUser = mJoinedUsers.get(i);

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
