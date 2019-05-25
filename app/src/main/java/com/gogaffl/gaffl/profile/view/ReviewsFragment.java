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

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.adapter.ReviewAdapter;
import com.gogaffl.gaffl.profile.model.Review;
import com.gogaffl.gaffl.profile.model.User;
import com.gogaffl.gaffl.profile.repository.ProfileRepository;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModelFactory;

import java.util.ArrayList;
import java.util.Objects;

public class ReviewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileViewModel viewModel;
    private ArrayList<Review> reviews;
    private ReviewAdapter mReviewAdapter;

    public static ReviewsFragment newInstance() {
        return new ReviewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviews_fragment, container, false);
        recyclerView = view.findViewById(R.id.review_recycler_view);

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
        reviews = user.getReviews();
        if (reviews.size() > 0) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mReviewAdapter = new ReviewAdapter(getActivity(), reviews);
            recyclerView.setAdapter(mReviewAdapter);
        }
    }
}
