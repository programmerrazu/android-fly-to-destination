package com.gogaffl.gaffl.home.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.view.AuthFragment;
import com.gogaffl.gaffl.home.adapter.FindTripsAdapter;
import com.gogaffl.gaffl.home.model.Trips;
import com.gogaffl.gaffl.home.repository.HomeRepository;
import com.gogaffl.gaffl.home.viewmodel.HomeViewModel;
import com.gogaffl.gaffl.home.viewmodel.HomeViewModelFactory;
import com.gogaffl.gaffl.profile.viewmodel.InterestViewModel;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindTripsFragment extends Fragment {

    private static final String BACK_STACK_ROOT_TAG = "trip_listing_fragment";
    boolean isNavigationHide = false;
    boolean isSearchBarHide = false;
    RecyclerView recyclerViewFt;
    private HomeViewModel mViewModel;
    private RecyclerView.LayoutManager layoutManager;

    public FindTripsFragment() {
        // Required empty public constructor
    }

    public static FindTripsFragment newInstance() {
        return new FindTripsFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, new HomeViewModelFactory(getActivity().getApplication(),
                new HomeRepository(getActivity().getApplication()))).get(HomeViewModel.class);
        mViewModel.getTripsDataFromCloud().observe(getViewLifecycleOwner(), this::initTripListing);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_trips, container, false);

        recyclerViewFt = view.findViewById(R.id.find_trips_recycler_view);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Logger.i("Now I can access my EditText!");
    }

    private void initTripListing(ArrayList<Trips> tripsArrayList) {

        if (tripsArrayList.size() > 0) {
            generateTripsListing(tripsArrayList);
        }


    }

    private void generateTripsListing(ArrayList<Trips> trips) {
        if (trips != null) {

            layoutManager = new LinearLayoutManager(getContext());
            recyclerViewFt.setLayoutManager(layoutManager);
            FindTripsAdapter mFindTripsAdapter = new FindTripsAdapter(
                    getApplicationContext(), trips, (HomeActivity) getActivity());
            recyclerViewFt.setItemAnimator(new DefaultItemAnimator());
            mFindTripsAdapter.notifyDataSetChanged();
            recyclerViewFt.setHasFixedSize(true);
            recyclerViewFt.setAdapter(mFindTripsAdapter);
            recyclerViewFt.setItemViewCacheSize(20);
            recyclerViewFt.setDrawingCacheEnabled(true);
            recyclerViewFt.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//            recyclerViewFt.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    mSwitch =(Switch) findViewById(R.id.mySwitch);
//                    toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (dy > 0) {
//                                mSwitch.setVisibility(View.GONE);
//                                Toasty.success(getApplicationContext(),"scrolling up",Toasty.LENGTH_SHORT).show();
//                            }
//                        }
//                    },100);
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(dy<=0){
//                                // Scrolling down
//                                mSwitch.setVisibility(View.VISIBLE);
//                                Toasty.success(getApplicationContext(),"scrolling down",Toasty.LENGTH_SHORT).show();
//                            }
//                        }
//                    },500);
//
//
//                    y=dy;
//                }
//
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//
//                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                        // Do something
//                    } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                        if(y<=0){
//
//                        }
//
//                    } else {
//                        // Do something
//                    }
//                }
//
//            });

        } else {
            Toast.makeText(getApplicationContext(), "pending user is null", Toast.LENGTH_SHORT).show();
        }
    }


}
