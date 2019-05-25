package com.gogaffl.gaffl.home.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.adapter.ChatMembersAdapter;
import com.gogaffl.gaffl.home.adapter.JoinedMembersAdapter;
import com.gogaffl.gaffl.home.adapter.NearbyGafflersAdapter;
import com.gogaffl.gaffl.home.adapter.PendingGafflersAdapter;
import com.gogaffl.gaffl.home.model.JoinedUser;
import com.gogaffl.gaffl.home.model.NearbyGaffler;
import com.gogaffl.gaffl.home.model.PendingRequest;
import com.gogaffl.gaffl.home.model.Plan;
import com.gogaffl.gaffl.home.model.TripsDetailsModel;
import com.gogaffl.gaffl.home.repository.HomeServices;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.Tools;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripDetailsFragment extends Fragment {

    int tripID;
    TextView tripTitle, tripType, tripLocation,
            tripDate, tripJoinedNo, tripOwnerName, tripDescription,
            tripMeetupPoint, tripOwnerAgeLocation, tripOwnerAbout,
            getTripOwnerContact, tripRequestNo, invitegafflerlabel,
            invitegafflerlabelSecond, pendinguserLabel, pendinguserNoLabel, tripOwnertitle,
            tripCreatorTitle, tripJoinedUsersLabel, tripJoinedUsersNo, tripJoinedusersLabel;

    ImageView tripOwnerPicFirst, getTripOwnerPicSecond;
    MaterialProgressBar mProgressBar;

    RecyclerView mRecyclerViewPendingUser, mRecyclerViewNearbyGaffler,
            mRecyclerViewChatMembers, mRecyclerViewJoinedMembers,
            mRecyclerViewNearbyGafflerSecond;
    ActionBar actionBar;
    Toolbar toolbar;

    String age = "";
    String flexible = "";
    private NearbyGafflersAdapter mNearbyGafflersAdapter;
    private PendingGafflersAdapter mPendingGafflersAdapter;
    private ChatMembersAdapter mChatMembersAdapter;
    private JoinedMembersAdapter mJoinedMembersAdapter;


    public TripDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tripTitle = view.findViewById(R.id.trip_title);
        tripType = view.findViewById(R.id.trip_type);
        tripDate = view.findViewById(R.id.trip_time);
        tripCreatorTitle = view.findViewById(R.id.trip_creator_title);
        tripLocation = view.findViewById(R.id.trip_location);
        tripJoinedNo = view.findViewById(R.id.trip_joined_people_number);
        tripOwnerName = view.findViewById(R.id.trip_owner_name);
        tripDescription = view.findViewById(R.id.trip_description);
        tripMeetupPoint = view.findViewById(R.id.trip_meetup_point);
        tripOwnerAgeLocation = view.findViewById(R.id.trip_owner_location);
        tripOwnerAbout = view.findViewById(R.id.trip_owner_about);
        tripOwnertitle = view.findViewById(R.id.trip_owner_name_second);
        getTripOwnerContact = view.findViewById(R.id.trip_owner_contact_number);
        tripJoinedUsersLabel = view.findViewById(R.id.trip_joined_label);
        tripJoinedUsersNo = view.findViewById(R.id.trip_joined_users_number);
        tripRequestNo = view.findViewById(R.id.pending_request_number_label);
        tripJoinedusersLabel = view.findViewById(R.id.trip_joined_users_label);
        tripOwnerPicFirst = view.findViewById(R.id.trip_owner_profile_pic);
        getTripOwnerPicSecond = view.findViewById(R.id.trip_owner_profile_pic_second);
        mRecyclerViewNearbyGaffler = view.findViewById(R.id.recyclerView_nearby_users);
        mRecyclerViewPendingUser = view.findViewById(R.id.recyclerView_pending_requests);
        mRecyclerViewNearbyGafflerSecond = view.findViewById(R.id.recyclerView_nearby_users_second);
        mRecyclerViewJoinedMembers = view.findViewById(R.id.recyclerView_joined_users);
        invitegafflerlabel = view.findViewById(R.id.trip_invite_label);
        invitegafflerlabelSecond = view.findViewById(R.id.trip_invite_label_second);
        pendinguserLabel = view.findViewById(R.id.pending_request_label);
        mProgressBar = view.findViewById(R.id.progressBarIDDet);
        Button backButton = view.findViewById(R.id.back_button_trip_details);

        backButton.setOnClickListener(v -> {
            if (getFragmentManager().getBackStackEntryCount() > 0)
                getFragmentManager().popBackStack();
            else
                getActivity().onBackPressed();
        });

        initToolbar();

        //here is your arguments
        Bundle bundle = getArguments();
        //here is your list array
        tripID = bundle.getInt("trip_id");

        mProgressBar.setVisibility(View.VISIBLE);

        if (tripID > -1) {
            getTrips();
        }

//        Handler delayhandler = new Handler();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                getTrips();
//
//            }
//        };
//        delayhandler.postDelayed(run, 5000);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getTrips();
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
                    Glide.with(getContext()).load(plan.getCreator().getPictureUrl()).
                            diskCacheStrategy(DiskCacheStrategy.ALL).into(tripOwnerPicFirst);
                    Glide.with(getContext()).load(plan.getCreator().getPictureUrl()).
                            diskCacheStrategy(DiskCacheStrategy.ALL).into(getTripOwnerPicSecond);

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
                    }

                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TripsDetailsModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Data loading failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initToolbar() {
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(getActivity());
        actionBar.hide();
    }



    public void changeFragment(Fragment frag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right);
        transaction.replace(R.id.frame_container, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void generateNearbyGafflerList(ArrayList<NearbyGaffler> nearbyGafflers, boolean creator, boolean joined) {
        if (nearbyGafflers != null && creator && !joined) {
            invitegafflerlabel.setVisibility(View.VISIBLE);
            mRecyclerViewNearbyGaffler.setVisibility(View.VISIBLE);
            if (nearbyGafflers.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                mRecyclerViewNearbyGaffler.setLayoutManager(layoutManager);
                mNearbyGafflersAdapter = new NearbyGafflersAdapter(getActivity(), nearbyGafflers);
                mRecyclerViewNearbyGaffler.setItemAnimator(new DefaultItemAnimator());
                mNearbyGafflersAdapter.notifyDataSetChanged();
                mRecyclerViewNearbyGaffler.setAdapter(mNearbyGafflersAdapter);

            } else {
                Toast.makeText(getActivity(), "nearby gaffler is null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void generateNearbyGafflerSecondList(ArrayList<NearbyGaffler> nearbyGafflers, boolean creator, boolean joined) {
        if (nearbyGafflers != null && creator && joined) {

            tripJoinedUsersLabel.setVisibility(View.VISIBLE);
            invitegafflerlabelSecond.setVisibility(View.VISIBLE);
            mRecyclerViewNearbyGafflerSecond.setVisibility(View.VISIBLE);

            if (nearbyGafflers.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                        LinearLayout.HORIZONTAL, false);
                mRecyclerViewNearbyGafflerSecond.setLayoutManager(layoutManager);
                mNearbyGafflersAdapter = new NearbyGafflersAdapter(getActivity(), nearbyGafflers);
                mRecyclerViewNearbyGafflerSecond.setItemAnimator(new DefaultItemAnimator());
                mNearbyGafflersAdapter.notifyDataSetChanged();
                mRecyclerViewNearbyGafflerSecond.setAdapter(mNearbyGafflersAdapter);

            } else {
                Toast.makeText(getActivity(), "nearby gaffler second is null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void generatePendingRequestList(ArrayList<PendingRequest> pendingRequests, boolean creator) {
        if (pendingRequests != null && creator) {
            tripRequestNo.setVisibility(View.VISIBLE);
            pendinguserLabel.setVisibility(View.VISIBLE);
            mRecyclerViewPendingUser.setVisibility(View.VISIBLE);
            if (pendingRequests.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                        LinearLayout.HORIZONTAL, false);
                mRecyclerViewPendingUser.setLayoutManager(layoutManager);
                mPendingGafflersAdapter = new PendingGafflersAdapter(getActivity(), pendingRequests);
                mRecyclerViewPendingUser.setItemAnimator(new DefaultItemAnimator());
                mPendingGafflersAdapter.notifyDataSetChanged();
                mRecyclerViewPendingUser.setAdapter(mPendingGafflersAdapter);

            } else {
                Toast.makeText(getActivity(), "pending user is null", Toast.LENGTH_SHORT).show();
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mRecyclerViewJoinedMembers.setLayoutManager(layoutManager);
                mJoinedMembersAdapter = new JoinedMembersAdapter(getActivity(), joinedUsers);
                mRecyclerViewJoinedMembers.setItemAnimator(new DefaultItemAnimator());
                mJoinedMembersAdapter.notifyDataSetChanged();
                mRecyclerViewJoinedMembers.setAdapter(mJoinedMembersAdapter);

            } else {
                Toast.makeText(getActivity(), "joined user is null", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
