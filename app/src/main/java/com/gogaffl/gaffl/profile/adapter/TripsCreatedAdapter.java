package com.gogaffl.gaffl.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.model.CreatedTrip;

import java.util.ArrayList;

public class TripsCreatedAdapter extends RecyclerView.Adapter<TripsCreatedAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CreatedTrip> mTripsCreatedList;
    String userName, userPic;


    public TripsCreatedAdapter(Context context, ArrayList<CreatedTrip> tripsCreatedList, String userName, String userPic) {
        mContext = context;
        mTripsCreatedList = tripsCreatedList;
        this.userName = userName;
        this.userPic = userPic;
    }

    @NonNull
    @Override
    public TripsCreatedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trips_created_sample_layout, viewGroup, false);

        return new TripsCreatedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsCreatedAdapter.MyViewHolder myViewHolder, int i) {
        final CreatedTrip createdTrip = mTripsCreatedList.get(i);
        myViewHolder.title.setText(createdTrip.getTitle());
        myViewHolder.desc.setText(createdTrip.getDescription());
        myViewHolder.location.setText(createdTrip.getPlaceName());
        String validity;
        if (createdTrip.getDateFlexible()) {
            validity = "(felxible)";
        } else {
            validity = "";
        }
        myViewHolder.time.setText(" " + createdTrip.getStartDate() + " - " + createdTrip.getEndDate() + " " + validity);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_img)
                .error(R.drawable.ic_signal_wifi_off)
                .priority(Priority.HIGH);


        Glide.with(mContext).load(userPic).apply(options).into(myViewHolder.thumbnail);

        myViewHolder.name.setText(userName);

    }

    @Override
    public int getItemCount() {
        return mTripsCreatedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, time, location, name;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tc_title_text);
            desc = view.findViewById(R.id.tc_desc_text);
            time = view.findViewById(R.id.tc_time_text);
            location = view.findViewById(R.id.tc_location_text);
            name = view.findViewById(R.id.tc_user_title_text);
            thumbnail = view.findViewById(R.id.tc_owner_profile_pic);
        }
    }
}
