package com.gogaffl.gaffl.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.model.JoinedUser;

import java.util.ArrayList;

public class JoinedMembersAdapter extends RecyclerView.Adapter<JoinedMembersAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<JoinedUser> mJoinedUsers;
    private String flexible = "";


    public JoinedMembersAdapter(Context context, ArrayList<JoinedUser> joinedUsers) {
        mContext = context;
        mJoinedUsers = joinedUsers;
    }

    @NonNull
    @Override
    public JoinedMembersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.joined_members_sample, viewGroup, false);

        return new JoinedMembersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedMembersAdapter.MyViewHolder myViewHolder, int i) {
        final JoinedUser joinedUser = mJoinedUsers.get(i);
        myViewHolder.title.setText(joinedUser.getName());
        if (joinedUser.getAbout().length() < 1) {
            flexible = "No bio added...";
        } else {
            flexible = joinedUser.getAbout();
        }
        myViewHolder.about.setText(flexible);
        if (joinedUser.getPhoneCountryCode() == null) {
            flexible = "Contact: contact not added.";
        } else {
            flexible = "Contact: +" + joinedUser.getPhoneCountryCode()
                    + "-" + joinedUser.getPhoneNumber();
        }
        if (!joinedUser.getIdVerified()) {
            myViewHolder.verified.setVisibility(View.GONE);
        }
        myViewHolder.contact.setText(flexible);
        if (Double.parseDouble(joinedUser.getRating()) == 0) {
            myViewHolder.mRatingBar.setVisibility(View.GONE);
        }
        myViewHolder.mRatingBar.setRating((long) Double.parseDouble(joinedUser.getRating()));
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
        public TextView title, about, contact;
        public ImageView thumbnail;
        public AppCompatImageView verified;
        public Button mProfileBtn, mMessageBtn;
        public RatingBar mRatingBar;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title_user);
            about = view.findViewById(R.id.trip_owner_about);
            contact = view.findViewById(R.id.trip_owner_contact_number);
            mProfileBtn = view.findViewById(R.id.profile_btn);
            mMessageBtn = view.findViewById(R.id.message_btn);
            thumbnail = view.findViewById(R.id.trip_owner_profile_pic_second);
            mRatingBar = view.findViewById(R.id.myRatingBar);
            verified = view.findViewById(R.id.imageView2);
        }
    }
}
