package com.gogaffl.gaffl.home.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gogaffl.gaffl.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartTripsFragment extends Fragment {


    public StartTripsFragment() {
        // Required empty public constructor
    }
    //fontStyle


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/sf_pro_text.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_start_trips, container, false);



        return rootView;
    }

}
