package com.gogaffl.gaffl.authentication.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.viewmodel.AuthViewModel;
import com.gogaffl.gaffl.home.TestActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

import static android.support.constraint.Constraints.TAG;

public class AuthFragment extends Fragment {

    private static final int RC_SIGN_IN = 9001;
    private AuthViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private GoogleSignInClient mGoogleSignInClient;
    /**
     * success : true
     * Info : Logged out
     */

    private boolean success;
    private String info;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment, container, false);
        Button loginButton = view.findViewById(R.id.login_button);
        //Button continueButton = view.findViewById(R.id.continue_login_button);
        Button signupButton = view.findViewById(R.id.signup_button);
        Button joinButton = view.findViewById(R.id.join_button);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.

        loginButton.setOnClickListener(v -> {

            changeFragment(new LoginChoiceFragment());

        });

        signupButton.setOnClickListener(v -> {
            changeFragment(new RegistrationFragment());
        });

//        continueButton.setOnClickListener(v -> {
//            signIn();
//        });

        joinButton.setOnClickListener(v -> {
            changeFragment(new RegistrationFragment());
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        updateUI(account);
        // [END on_start_sign_in]
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // TODO: Use the ViewModel
    }

    public void changeFragment(Fragment frag) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right);
        transaction.remove(newInstance());
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
            String authCode = account.getServerAuthCode();
            String idToken = account.getIdToken();
            Intent mIntent = new Intent(getActivity(), TestActivity.class);
            startActivity(mIntent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Executor) this, task -> {
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener((Executor) this, task -> {
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            //do if account exist
        } else {
            //else do the other
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
