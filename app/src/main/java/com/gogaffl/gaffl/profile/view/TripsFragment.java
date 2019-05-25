package com.gogaffl.gaffl.profile.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.adapter.TripsCreatedAdapter;
import com.gogaffl.gaffl.profile.adapter.TripsJoinedAdapter;
import com.gogaffl.gaffl.profile.model.CreatedTrip;
import com.gogaffl.gaffl.profile.model.JoinedTrip;
import com.gogaffl.gaffl.profile.model.User;
import com.gogaffl.gaffl.profile.repository.ProfileRepository;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModelFactory;

import java.util.ArrayList;

public class TripsFragment extends Fragment {

    private static String userName, userPic;
    private TripsJoinedAdapter mTripsJoinedAdapter;
    private TripsCreatedAdapter mTripsCreatedAdapter;
    private ProfileViewModel viewModel;
    private RecyclerView recyclerViewTC, recyclerViewTJ;

    public static TripsFragment newInstance() {
        return new TripsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trips_fragment, container, false);
        recyclerViewTC = view.findViewById(R.id.trips_created_recycler_view);
        recyclerViewTJ = view.findViewById(R.id.trips_joined_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new ProfileViewModelFactory(getActivity().getApplication(),
                new ProfileRepository(getActivity().getApplication()))).get(ProfileViewModel.class);
        viewModel.getUserDataFromCloud().observe(getViewLifecycleOwner(), this::initFrag);

    }

    private void initFrag(User user) {
        if (user != null) {
            userName = user.getName();
            userPic = user.getPicture();
            if (user.getJoinedTrips().size() > 0) {
                generateTripsJoinedList(user.getJoinedTrips());
            }
            if (user.getCreatedTrips().size() > 0) {
                generateTripsCreatedList(user.getCreatedTrips());
            }
        }
    }

    private void generateTripsCreatedList(ArrayList<CreatedTrip> createdTrips) {
        if (createdTrips != null) {
            if (createdTrips.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerViewTC.setLayoutManager(layoutManager);
                mTripsCreatedAdapter = new TripsCreatedAdapter(getActivity(), createdTrips, userName, userPic);
                recyclerViewTC.setItemAnimator(new DefaultItemAnimator());
                mTripsCreatedAdapter.notifyDataSetChanged();
                recyclerViewTC.setAdapter(mTripsCreatedAdapter);

            }
        } else {
            Toast.makeText(getActivity(), "trips created is null", Toast.LENGTH_SHORT).show();
        }

    }

    private void generateTripsJoinedList(ArrayList<JoinedTrip> joinedTrips) {
        if (joinedTrips != null) {
            if (joinedTrips.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerViewTJ.setLayoutManager(layoutManager);
                mTripsJoinedAdapter = new TripsJoinedAdapter(getActivity(), joinedTrips);
                recyclerViewTJ.setItemAnimator(new DefaultItemAnimator());
                mTripsJoinedAdapter.notifyDataSetChanged();
                recyclerViewTJ.setAdapter(mTripsJoinedAdapter);

            }
        } else {
            Toast.makeText(getActivity(), "trips joined is null", Toast.LENGTH_SHORT).show();
        }
    }

}
