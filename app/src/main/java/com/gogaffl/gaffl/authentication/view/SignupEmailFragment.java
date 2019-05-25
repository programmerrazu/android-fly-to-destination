package com.gogaffl.gaffl.authentication.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.model.User;
import com.gogaffl.gaffl.authentication.viewmodel.SignupEmailFragmentViewModel;

public class SignupEmailFragment extends Fragment {

    private static final String TAG = "";
    private SignupEmailFragmentViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private EditText emailET;
    private User mUser;


    public static SignupEmailFragment newInstance() {
        return new SignupEmailFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_email_fragment, container, false);
        Button backButton = view.findViewById(R.id.back_button_signup_email);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_signup_email);

        emailET = view.findViewById(R.id.email_address_ET);

        floatingActionButton.setOnClickListener(v -> {
            String email = emailET.getText().toString();

            if (validation(email)) {
                User.setEmail(email);
                changeFragment(R.anim.enter_from_right, new SignupPasswordFragment());
            }
        });

        backButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_left, new SignupNameFragment());
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignupEmailFragmentViewModel.class);
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

    public boolean validation(String email) {
        boolean valid;
        if (email.isEmpty()) {
            emailET.setError("Email can not be blank");
            valid = false;
        } else {

            valid = true;
        }
        return valid;
    }

}
