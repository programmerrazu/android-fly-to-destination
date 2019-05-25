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
import com.gogaffl.gaffl.authentication.viewmodel.SignupPasswordViewModel;

public class SignupPasswordFragment extends Fragment {

    private SignupPasswordViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private EditText passET;
    private User mUser;

    public static SignupPasswordFragment newInstance() {
        return new SignupPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_password_fragment, container, false);
        Button backButton = view.findViewById(R.id.back_button_signup_pass);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_signup_pass);
        passET = view.findViewById(R.id.password_ET);

        floatingActionButton.setOnClickListener(v -> {
            String pass = passET.getText().toString();
            if (validation(pass)) {
                User.setPassword(pass);
                changeFragment(R.anim.enter_from_right, new SignupAgeFragment());
            }
        });

        backButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_left, new SignupEmailFragment());
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignupPasswordViewModel.class);
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

    public boolean validation(String pass) {
        boolean valid;
        if (pass.isEmpty()) {
            passET.setError("pass need more than 6 characters");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

}
