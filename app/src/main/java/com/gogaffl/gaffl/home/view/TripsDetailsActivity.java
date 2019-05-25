package com.gogaffl.gaffl.home.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.adapter.ChatMembersAdapter;
import com.gogaffl.gaffl.home.adapter.JoinedMembersAdapter;
import com.gogaffl.gaffl.home.adapter.NearbyGafflersAdapter;
import com.gogaffl.gaffl.home.adapter.PendingGafflersAdapter;
import com.gogaffl.gaffl.home.model.JoinedUser;
import com.gogaffl.gaffl.home.model.NearbyGaffler;
import com.gogaffl.gaffl.home.model.PendingRequest;
import com.gogaffl.gaffl.home.model.Plan;
import com.gogaffl.gaffl.home.model.Trips;
import com.gogaffl.gaffl.home.model.TripsDetailsModel;
import com.gogaffl.gaffl.home.repository.HomeServices;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripsDetailsActivity extends AppCompatActivity {

    int tripID;
    TextView tripTitle, tripType, tripLocation,
            tripDate, tripJoinedNo, tripOwnerName, tripDescription,
            tripMeetupPoint, tripOwnerAgeLocation, tripOwnerAbout,
            getTripOwnerContact, tripRequestNo, invitegafflerlabel,
            invitegafflerlabelSecond, pendinguserLabel, pendinguserNoLabel, tripOwnertitle,
            tripCreatorTitle, tripJoinedUsersLabel, tripJoinedUsersNo, tripJoinedusersLabel,
            tripCreatorBottom;


    ImageView tripOwnerPicFirst, getTripOwnerPicSecond;
    MaterialProgressBar mProgressBar;

    RecyclerView mRecyclerViewPendingUser, mRecyclerViewNearbyGaffler,
            mRecyclerViewChatMembers, mRecyclerViewJoinedMembers,
            mRecyclerViewNearbyGafflerSecond;
    ActionBar actionBar;
    Toolbar toolbar;

    public static int joinRequestID = 0;
    public static int flagID = 0;

    String age = "";
    String flexible = "";
    private NearbyGafflersAdapter mNearbyGafflersAdapter;
    private PendingGafflersAdapter mPendingGafflersAdapter;
    private ChatMembersAdapter mChatMembersAdapter;
    private JoinedMembersAdapter mJoinedMembersAdapter;
    View bottomView;
    NestedScrollView nestedScrollView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tripTitle = findViewById(R.id.trip_title);
        tripType = findViewById(R.id.trip_type);
        tripDate = findViewById(R.id.trip_time);
        tripCreatorTitle = findViewById(R.id.trip_creator_title);
        tripLocation = findViewById(R.id.trip_location);
        tripJoinedNo = findViewById(R.id.trip_joined_people_number);
        tripOwnerName = findViewById(R.id.trip_owner_name);
        tripDescription = findViewById(R.id.trip_description);
        tripMeetupPoint = findViewById(R.id.trip_meetup_point);
        tripOwnerAgeLocation = findViewById(R.id.trip_owner_location);
        tripOwnerAbout = findViewById(R.id.trip_owner_about);
        tripOwnertitle = findViewById(R.id.trip_owner_name_second);
        getTripOwnerContact = findViewById(R.id.trip_owner_contact_number);
        tripJoinedUsersLabel = findViewById(R.id.trip_joined_label);
        tripJoinedUsersNo = findViewById(R.id.trip_joined_users_number);
        tripRequestNo = findViewById(R.id.pending_request_number_label);
        tripJoinedusersLabel = findViewById(R.id.trip_joined_users_label);
        tripOwnerPicFirst = findViewById(R.id.trip_owner_profile_pic);
        getTripOwnerPicSecond = findViewById(R.id.trip_owner_profile_pic_second);
        mRecyclerViewNearbyGaffler = findViewById(R.id.recyclerView_nearby_users);
        mRecyclerViewPendingUser = findViewById(R.id.recyclerView_pending_requests);
        mRecyclerViewNearbyGafflerSecond = findViewById(R.id.recyclerView_nearby_users_second);
        mRecyclerViewChatMembers = findViewById(R.id.chat_list);
        mRecyclerViewJoinedMembers = findViewById(R.id.recyclerView_joined_users);
        invitegafflerlabel = findViewById(R.id.trip_invite_label);
        invitegafflerlabelSecond = findViewById(R.id.trip_invite_label_second);
        pendinguserLabel = findViewById(R.id.pending_request_label);
        mProgressBar = findViewById(R.id.progressBarIDDet);
        Button backButton = findViewById(R.id.back_button_trip_details);
        nestedScrollView = findViewById(R.id.nested_scroll_view_trip_details);
        bottomView = findViewById(R.id.bottom_holder);
        tripCreatorBottom = findViewById(R.id.trip_creator_title_bottom);

        backButton.setOnClickListener(v -> {
            onBackPressed();
            this.overridePendingTransition(0, R.anim.slide_in_bottom);
        });


        tripID = UserSendModel.getScreenValue();

        mProgressBar.setVisibility(View.VISIBLE);

        if (tripID > -1) {
            getTrips();
        }

        nestedScrollView.setSmoothScrollingEnabled(true);
//        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            boolean firstZeroPassed;
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                //first time you get 0, don't share it to others scrolls
//                if (scrollY == 0 && !firstZeroPassed) {
//                    firstZeroPassed = true;
//                    return;
//                }
//
//                //only if yOffset changed
//                if(scrollY>0){
//                    bottomView.setVisibility(View.GONE);
//                } else {
//                    bottomView.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        Handler delayhandler = new Handler();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                getTrips();
//
//            }
//        };
//        delayhandler.postDelayed(run, 5000);
    }


    public void getTrips() {

        HomeServices service = RetrofitInstance.getRetrofitInstance().
                create(HomeServices.class);
        boolean forceRefresh = false;
        Call<TripsDetailsModel> getPlacesCall = service.getTripsDetails(tripID, forceRefresh ? "no-cache" : null,
                "onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG");

        getPlacesCall.enqueue(new Callback<TripsDetailsModel>() {
            @Override
            public void onResponse(Call<TripsDetailsModel> call, Response<TripsDetailsModel> response) {
                if (response.isSuccessful()) {
                    Plan plan = response.body().getPlan();
                    tripTitle.setText(plan.getTitle());
                    tripType.setText(plan.getTripType());
                    tripLocation.setText(plan.getDestination() + ", ");
                    if (plan.getDateFlexible()) {
                        flexible = "(flexible)";
                    }
                    //+" "+flexible
                    tripDate.setText("From " + plan.getStartDate() + " to " + plan.getEndDate());

                    tripJoinedNo.setText(Integer.toString(plan.getJoinedUsers().size()));
                    tripOwnerName.setText(plan.getCreator().getName());
                    tripCreatorBottom.setText(plan.getCreator().getName());
                    tripOwnertitle.setText(plan.getCreator().getName());
                    tripDescription.setText(plan.getDescription());
                    tripMeetupPoint.setText(plan.getMeetupPoint());
                    tripJoinedUsersNo.setText(plan.getJoinedUsers().size() + " Members Joined This Trip");

                    if (plan.getCreator().getAge().length() > 0) {
                        age = plan.getCreator().getAge() + " Years Old";
                    }
                    tripOwnerAgeLocation.setText("From " + plan.getCreator().getFrom()
                            + ", " + age);
                    tripCreatorTitle.setText(plan.getCreator().getName());
                    tripOwnerAbout.setText(plan.getCreator().getAbout());
                    if (plan.getCreator().getPhoneCountryCode() == null) {
                        flexible = "Contact: contact not added.";
                    } else {
                        flexible = "Contact: +" + plan.getCreator().getPhoneCountryCode()
                                + "-" + plan.getCreator().getPhoneNumber();
                    }
                    getTripOwnerContact.setText(flexible);
                    tripRequestNo.setText(plan.getPendingRequests().size() + " People Requested to Join This Trip");

                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.user_img)
                            .error(R.drawable.ic_signal_wifi_off)
                            .override(350, 350)
                            .priority(Priority.HIGH);

                    Glide.with(TripsDetailsActivity.this).load(plan.getCreator().getPictureUrl()).apply(options).into(tripOwnerPicFirst);
                    Glide.with(TripsDetailsActivity.this).load(plan.getCreator().getPictureUrl()).
                            apply(options).into(getTripOwnerPicSecond);

                    boolean isCreator, isJoined = false;

                    isCreator = plan.getEligibility().getCreated();

                    if (plan.getJoinedUsers().size() > 0) isJoined = true;


                    if (plan.getNearbyGafflers().size() > 0) {
                        generateNearbyGafflerList(plan.getNearbyGafflers(), isCreator, isJoined);

                    }
                    if (plan.getNearbyGafflers().size() > 0) {
                        generateNearbyGafflerSecondList(plan.getNearbyGafflers(), isCreator, isJoined);

                    }
                    if (plan.getPendingRequests().size() > 0) {
                        generatePendingRequestList(plan.getPendingRequests(), isCreator);
                    }
                    if (plan.getJoinedUsers().size() > 0) {
                        generateJoinedMembersList(plan.getJoinedUsers(), isJoined);
                        generateJoinedMembersChatList(plan.getJoinedUsers(), isJoined);
                    }

                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TripsDetailsModel> call, Throwable t) {
                Toast.makeText(TripsDetailsActivity.this, "Data loading failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateNearbyGafflerList(ArrayList<NearbyGaffler> nearbyGafflers, boolean creator, boolean joined) {
        if (nearbyGafflers != null && creator && !joined) {
            invitegafflerlabel.setVisibility(View.VISIBLE);
            mRecyclerViewNearbyGaffler.setVisibility(View.VISIBLE);
            if (nearbyGafflers.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mRecyclerViewNearbyGaffler.setLayoutManager(layoutManager);
                mNearbyGafflersAdapter = new NearbyGafflersAdapter(this, nearbyGafflers);
                mRecyclerViewNearbyGaffler.setItemAnimator(new DefaultItemAnimator());
                mNearbyGafflersAdapter.notifyDataSetChanged();
                mRecyclerViewNearbyGaffler.setAdapter(mNearbyGafflersAdapter);

            } else {
                Toast.makeText(this, "nearby gaffler is null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void generateNearbyGafflerSecondList(ArrayList<NearbyGaffler> nearbyGafflers, boolean creator, boolean joined) {
        if (nearbyGafflers != null && creator && joined) {

            tripJoinedUsersLabel.setVisibility(View.VISIBLE);
            invitegafflerlabelSecond.setVisibility(View.VISIBLE);
            mRecyclerViewNearbyGafflerSecond.setVisibility(View.VISIBLE);

            if (nearbyGafflers.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                        LinearLayout.HORIZONTAL, false);
                mRecyclerViewNearbyGafflerSecond.setLayoutManager(layoutManager);
                mNearbyGafflersAdapter = new NearbyGafflersAdapter(this, nearbyGafflers);
                mRecyclerViewNearbyGafflerSecond.setItemAnimator(new DefaultItemAnimator());
                mNearbyGafflersAdapter.notifyDataSetChanged();
                mRecyclerViewNearbyGafflerSecond.setAdapter(mNearbyGafflersAdapter);

            } else {
                Toast.makeText(this, "nearby gaffler second is null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void generatePendingRequestList(ArrayList<PendingRequest> pendingRequests, boolean creator) {
        if (pendingRequests != null && creator) {
            tripRequestNo.setVisibility(View.VISIBLE);
            pendinguserLabel.setVisibility(View.VISIBLE);
            mRecyclerViewPendingUser.setVisibility(View.VISIBLE);
            if (pendingRequests.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                        LinearLayout.HORIZONTAL, false);
                mRecyclerViewPendingUser.setLayoutManager(layoutManager);
                mPendingGafflersAdapter = new PendingGafflersAdapter(this, pendingRequests);
                mRecyclerViewPendingUser.setItemAnimator(new DefaultItemAnimator());
                mPendingGafflersAdapter.notifyDataSetChanged();
                mRecyclerViewPendingUser.setAdapter(mPendingGafflersAdapter);

            } else {
                Toast.makeText(this, "pending user is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateJoinedMembersList(ArrayList<JoinedUser> joinedUsers, boolean joined) {
        if (joinedUsers != null && joined) {
            tripJoinedusersLabel.setVisibility(View.VISIBLE);
            tripJoinedUsersNo.setVisibility(View.VISIBLE);
            mRecyclerViewJoinedMembers.setVisibility(View.VISIBLE);
            mRecyclerViewJoinedMembers.setNestedScrollingEnabled(false);
            if (joinedUsers.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                mRecyclerViewJoinedMembers.setLayoutManager(layoutManager);
                mJoinedMembersAdapter = new JoinedMembersAdapter(this, joinedUsers);
                mRecyclerViewJoinedMembers.setItemAnimator(new DefaultItemAnimator());
                mJoinedMembersAdapter.notifyDataSetChanged();
                mRecyclerViewJoinedMembers.setAdapter(mJoinedMembersAdapter);

            } else {
                Toast.makeText(this, "joined user is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateJoinedMembersChatList(ArrayList<JoinedUser> joinedUsers, boolean joined) {
        if (joinedUsers != null && joined) {
            Button sChatButton;
            sChatButton = findViewById(R.id.chat_btn);
            sChatButton.setVisibility(View.VISIBLE);
            mRecyclerViewChatMembers.setVisibility(View.VISIBLE);
            mRecyclerViewChatMembers.setNestedScrollingEnabled(true);
            if (joinedUsers.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mRecyclerViewChatMembers.setLayoutManager(layoutManager);
                mChatMembersAdapter = new ChatMembersAdapter(this, joinedUsers);
                mRecyclerViewChatMembers.setItemAnimator(new DefaultItemAnimator());
                mChatMembersAdapter.notifyDataSetChanged();
                mRecyclerViewChatMembers.setAdapter(mChatMembersAdapter);

            } else {
                Toast.makeText(this, "joined user is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
