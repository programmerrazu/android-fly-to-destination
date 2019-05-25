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
import com.gogaffl.gaffl.profile.model.JoinedTrip;

import java.util.ArrayList;

public class TripsJoinedAdapter extends RecyclerView.Adapter<TripsJoinedAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<JoinedTrip> mTripsJoinedList;


    public TripsJoinedAdapter(Context context, ArrayList<JoinedTrip> tripsJoinedList) {
        mContext = context;
        mTripsJoinedList = tripsJoinedList;
    }

    @NonNull
    @Override
    public TripsJoinedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trips_joined_sample_layout, viewGroup, false);

        return new TripsJoinedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsJoinedAdapter.MyViewHolder myViewHolder, int i) {
        final JoinedTrip joinedTrip = mTripsJoinedList.get(i);
        myViewHolder.title.setText(joinedTrip.getTitle());
        myViewHolder.desc.setText(joinedTrip.getDescription());
        myViewHolder.location.setText(joinedTrip.getPlaceName());
        String validity;
        if (joinedTrip.getDateFlexible()) {
            validity = "(felxible)";
        } else {
            validity = "";
        }
        myViewHolder.time.setText(" " + joinedTrip.getStartDate() + " - " + joinedTrip.getEndDate() + " " + validity);

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_img)
                .error(R.drawable.ic_signal_wifi_off)
                .override(350, 350)
                .priority(Priority.HIGH);


        Glide.with(mContext).load(joinedTrip.getCreatorPictureUrl()).apply(options).into(myViewHolder.thumbnail);

        myViewHolder.name.setText(joinedTrip.getCreatorName());

    }

    @Override
    public int getItemCount() {
        return mTripsJoinedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, time, location, name;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tj_user_title_text);
            title = view.findViewById(R.id.tj_title_text);
            desc = view.findViewById(R.id.tj_desc_text);
            time = view.findViewById(R.id.tj_time_text);
            location = view.findViewById(R.id.tj_location_text);
            thumbnail = view.findViewById(R.id.tj_owner_profile_pic);
        }
    }
}