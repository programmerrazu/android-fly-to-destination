package com.gogaffl.gaffl.profile.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.gogaffl.gaffl.R;


public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
//        Button skipButton = view.findViewById(R.id.phone_verify_skip_btn);
//        Button floatingActionButton = view.findViewById(R.id.continue_phone_verification_btn);
//
//
//        floatingActionButton.setOnClickListener(v -> {
//            changeFragment(R.anim.enter_from_right, new PhoneVerificationFragment());
//        });
//
//        skipButton.setOnClickListener(v -> {
//            Intent mIntent = new Intent(getActivity(), TestActivity.class);
//            startActivity(mIntent);
//        });

        return view;
    }

    public void changeFragment(int id, Fragment frag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(id, 0);
        transaction.remove(newInstance());
        transaction.replace(R.id.container_profile, frag);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}
