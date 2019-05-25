package com.gogaffl.gaffl.authentication.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.model.User;
import com.gogaffl.gaffl.authentication.viewmodel.SignupAgeViewModel;
import com.gogaffl.gaffl.tools.SelectDateFragment;

public class SignupAgeFragment extends Fragment {

    private SignupAgeViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;


    public static SignupAgeFragment newInstance() {
        return new SignupAgeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_age_fragment, container, false);
        Button backButton = view.findViewById(R.id.back_button_signup_date);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_signup_age);
        TextView dateView = view.findViewById(R.id.date_TV);


        floatingActionButton.setOnClickListener(v -> {
            boolean tracker = SelectDateFragment.isTracker();

            if (tracker) {
                String date = dateView.getText().toString();
                User.setAge(date);
                changeFragment(R.anim.enter_from_right, new SignupGenderFragment());
            } else {
                dateView.setError("set your birth date");
            }
        });

        backButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_left, new SignupPasswordFragment());
        });

        view.findViewById(R.id.date_TV).setOnClickListener(v -> {
            DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getFragmentManager(), "DatePicker");
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignupAgeViewModel.class);
        // TODO: Use the ViewModel
    }

    public void changeFragment(int id, Fragment frag) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.setCustomAnimations(id, 0);
        transaction.remove(newInstance());
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
