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

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.viewmodel.ForgotPassViewModel;

public class ForgotPassFragment extends Fragment {

    private ForgotPassViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;

    public static ForgotPassFragment newInstance() {
        return new ForgotPassFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_pass_fragment, container, false);

        Button backButton = view.findViewById(R.id.view_button_tj);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_forgot);

        backButton.setOnClickListener(v1 -> {
            ExitFragment(new LoginFragment());
        });

        floatingActionButton.setOnClickListener(v -> {

        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ForgotPassViewModel.class);
        // TODO: Use the ViewModel
    }

    public void changeFragment(Fragment frag) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, 0);
        transaction.remove(newInstance());
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void ExitFragment(Fragment frag) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, 0);
        transaction.replace(R.id.container, frag, getTag());
        transaction.addToBackStack(getTag());
        transaction.commit();
    }

}
