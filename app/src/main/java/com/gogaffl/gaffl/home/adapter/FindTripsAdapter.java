package com.gogaffl.gaffl.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.model.JoinedUser;
import com.gogaffl.gaffl.home.model.JoinedUsers;
import com.gogaffl.gaffl.home.model.Trips;
import com.gogaffl.gaffl.home.view.FindTripsFragment;
import com.gogaffl.gaffl.home.view.HomeActivity;
import com.gogaffl.gaffl.home.view.TripsDetailsActivity;
import com.gogaffl.gaffl.profile.model.UserSendModel;

import java.util.ArrayList;

public class FindTripsAdapter extends RecyclerView.Adapter<FindTripsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Trips> mTripsList;
    private static String TRIP_ID;
    private HomeActivity mHomeActivity;


    public FindTripsAdapter(Context context, ArrayList<Trips> tripsList,
                            HomeActivity homeActivity) {
        mContext = context;
        mTripsList = tripsList;
        this.mHomeActivity = homeActivity;
    }

    @NonNull
    @Override
    public FindTripsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.find_trips_sample, viewGroup, false);

        return new FindTripsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindTripsAdapter.MyViewHolder myViewHolder, int i) {
        final Trips trip = mTripsList.get(i);
        myViewHolder.title.setText(mTripsList.get(i).getTitle());
        myViewHolder.desc.setText(trip.getDescription());
        myViewHolder.location.setText(trip.getPlace_name());
        String validity;
        if (trip.isDate_flexible()) {
            validity = "(felxible)";
        } else {
            validity = "";
        }
        System.out.println(trip.getId());
        myViewHolder.time.setText(" " + trip.getStart_date() + " - " + trip.getEnd_date() + " " + validity);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_img)
                .error(R.drawable.ic_signal_wifi_off)
                .priority(Priority.HIGH);


        Glide.with(mContext).load(trip.getCreator_picture_url()).apply(options).
                thumbnail(0.5f).into(myViewHolder.thumbnail);
        myViewHolder.name.setText(trip.getCreator_name());

        myViewHolder.mCardView.setOnClickListener(v -> {
            //get to specific fragment from a fragment of same activity
//            TRIP_ID = Integer.toString(trip.getId());
//            Fragment fragment = new TripDetailsFragment();
//            Bundle args = new Bundle();
//            args.putString("data", "This data has sent to FragmentTwo");
//            args.putInt("trip_id", trip.getId());
//            fragment.setArguments(args);
//            FragmentTransaction transaction = mFindTripsFragment.
//                    getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_container, fragment);
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            transaction.addToBackStack(null);
//            transaction.commit();
//            mHomeActivity.hideSearchBar();

            Intent intent = new Intent(mHomeActivity, TripsDetailsActivity.class);
            UserSendModel.setScreenValue(trip.getId());
            mHomeActivity.startActivity(intent);
            mHomeActivity.overridePendingTransition(R.anim.slide_in_up, 0);
        });
        if (Double.parseDouble(mTripsList.get(i).getCreator_rating()) == 0) {
            myViewHolder.myRatingbar.setVisibility(View.GONE);
        } else {
            myViewHolder.myRatingbar.setRating((float) Double.parseDouble(mTripsList.get(i).getCreator_rating()));
        }


        if (mTripsList.get(i).getJoined_users() != null) {

            if (mTripsList.get(i).getJoined_users().size() > 0) {
                myViewHolder.chatRecyclerView.setVisibility(View.VISIBLE);
                myViewHolder.chatRecyclerView.setNestedScrollingEnabled(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                ArrayList<JoinedUsers> joinedUsersList = mTripsList.get(i).getJoined_users();
                myViewHolder.chatRecyclerView.setLayoutManager(layoutManager);
                FindTripsChatMembersAdapter mChatMembersAdapter = new FindTripsChatMembersAdapter(mContext, joinedUsersList);
                myViewHolder.chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mChatMembersAdapter.notifyDataSetChanged();
                myViewHolder.chatRecyclerView.setAdapter(mChatMembersAdapter);
            } else {
                Toast.makeText(mContext, "joined user is null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public int getItemCount() {
        return mTripsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, time, location, name;
        public ImageView thumbnail;
        public CardView mCardView;
        public RecyclerView chatRecyclerView;
        public RatingBar myRatingbar;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.ft_user_title_text);
            title = view.findViewById(R.id.ft_title_text);
            desc = view.findViewById(R.id.ft_desc_text);
            time = view.findViewById(R.id.ft_time_text);
            location = view.findViewById(R.id.ft_location_text);
            thumbnail = view.findViewById(R.id.ft_owner_profile_pic);
            mCardView = view.findViewById(R.id.cardView_ft);
            chatRecyclerView = view.findViewById(R.id.joined_users_list);
            myRatingbar = view.findViewById(R.id.ratingBar);

        }
    }
}