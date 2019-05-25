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
import com.gogaffl.gaffl.authentication.viewmodel.SignupNameViewModel;

public class SignupNameFragment extends Fragment {

    private SignupNameViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    EditText firstNameET, lastNameET;
    private boolean valid = true;


    public static SignupNameFragment newInstance() {
        return new SignupNameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_name_fragment, container, false);


        Button backButton = view.findViewById(R.id.back_button_signup_name);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_signup_name);
        firstNameET = view.findViewById(R.id.first_name_ET);
        lastNameET = view.findViewById(R.id.last_name_ET);


        floatingActionButton.setOnClickListener(v -> {

            String fName = firstNameET.getText().toString();
            String lName = lastNameET.getText().toString();

            if (validation(fName, lName)) {

                SignupEmailFragment fragment = new SignupEmailFragment();
                User.setFirstName(fName);
                User.setLastName(lName);
                changeFragment(R.anim.enter_from_right, fragment);
            }

        });

        backButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_left, new RegistrationFragment());
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignupNameViewModel.class);
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

    public boolean validation(String fName, String lName) {
        if (fName.isEmpty()) {
            firstNameET.setError("First Name can not be blank");
            valid = false;
        } else {

            valid = true;
        }
        if (lName.isEmpty()) {
            lastNameET.setError("Last Name can not be blank");
            valid = false;
        } else {

            valid = true;
        }

        return valid;
    }

}
