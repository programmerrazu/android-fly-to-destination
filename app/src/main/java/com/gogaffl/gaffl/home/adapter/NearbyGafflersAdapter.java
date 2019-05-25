package com.gogaffl.gaffl.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.model.NearbyGaffler;

import java.util.ArrayList;

public class NearbyGafflersAdapter extends RecyclerView.Adapter<NearbyGafflersAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NearbyGaffler> mNearbyGafflers;


    public NearbyGafflersAdapter(Context context, ArrayList<NearbyGaffler> nearbyGafflers) {
        mContext = context;
        mNearbyGafflers = nearbyGafflers;
    }

    @NonNull
    @Override
    public NearbyGafflersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.nearby_user_sample, viewGroup, false);

        return new NearbyGafflersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyGafflersAdapter.MyViewHolder myViewHolder, int i) {
        final NearbyGaffler nearbyGaffler = mNearbyGafflers.get(i);
        myViewHolder.title.setText(nearbyGaffler.getName());
        myViewHolder.traveling.setText(nearbyGaffler.getTravelStatus());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_img)
                .error(R.drawable.ic_signal_wifi_off)
                .priority(Priority.HIGH);


        Glide.with(mContext).load(nearbyGaffler.getPictureUrl()).apply(options).
                thumbnail(0.5f).into(myViewHolder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return mNearbyGafflers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, traveling;
        public ImageView thumbnail;
        public Button mActionBtn;
        public ImageButton mCrossBtn;


        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.user_name);
            mActionBtn = view.findViewById(R.id.connect_and_chat_btn);
            thumbnail = view.findViewById(R.id.user_profile_pic);
            mCrossBtn = view.findViewById(R.id.cross_btn);
            traveling = view.findViewById(R.id.travelingTo);

        }


    }

}
