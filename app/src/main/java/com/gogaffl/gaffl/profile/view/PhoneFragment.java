package com.gogaffl.gaffl.profile.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.view.HomeActivity;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.profile.repository.ProfileServices;
import com.gogaffl.gaffl.profile.viewmodel.PhoneViewModel;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneFragment extends Fragment implements
        View.OnClickListener {

    private PhoneViewModel mViewModel;
    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneVerifyLayout;
    private ViewGroup mCodeVerifyLayout;

    private TextView mStatusText;
    private TextView mDetailText;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private EditText mPhoneCodeField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;
    private MySharedPreferences prefs;


    public static PhoneFragment newInstance() {
        return new PhoneFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_fragment, container, false);
        Button skipButton = view.findViewById(R.id.phone_verify_skip_btn);
        mPhoneCodeField = view.findViewById(R.id.country_code);
        mPhoneNumberField = view.findViewById(R.id.phone_no_field);
        mVerificationField = view.findViewById(R.id.verify_code_field);
        mDetailText = view.findViewById(R.id.detailText);

        mPhoneVerifyLayout = view.findViewById(R.id.phone_verify_layout);
        mCodeVerifyLayout = view.findViewById(R.id.verify_code_screen);


        mStartButton = view.findViewById(R.id.buttonStartVerification);
        mVerifyButton = view.findViewById(R.id.verifyPhone);
        mResendButton = view.findViewById(R.id.buttonResend);

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");

//        floatingActionButton.setOnClickListener(v -> {
//
//               changeFragment(R.anim.enter_from_right, new PhoneVerificationFragment());
//
//        });

        Button backButton = view.findViewById(R.id.code_back_button);
        backButton.setOnClickListener(v -> {
            mPhoneVerifyLayout.setVisibility(View.VISIBLE);
            mCodeVerifyLayout.setVisibility(View.GONE);
        });

        if (UserSendModel.isPhoneEdit()) {
            skipButton.setText("back");
        }


        skipButton.setOnClickListener(v -> {
            if (UserSendModel.isPhoneEdit()) {
                Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(mIntent);
                getActivity().finish();
            } else {
                Intent mIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(mIntent);
                getActivity().finish();
            }

        });


        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(view.findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]


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
        mViewModel = ViewModelProviders.of(this).get(PhoneViewModel.class);
        // TODO: Use the ViewModel
        //mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_VERIFY_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {

        String code = prefs.getString("country_code", "880");
        String phone = prefs.getString("phone_number", "151515661651");

        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(mStartButton, mPhoneNumberField);
                //mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                mCodeVerifyLayout.setVisibility(View.VISIBLE);
                mPhoneVerifyLayout.setVisibility(View.INVISIBLE);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText(R.string.status_verification_failed);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                postPhoneNumber(code, phone);
                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                //postPhoneNumber(code,phone);
                break;
        }

//        if (user == null) {
//            // Signed out
//            mPhoneVerifyLayout.setVisibility(View.VISIBLE);
//            mCodeVerifyLayout.setVisibility(View.GONE);
//        } else {
//            // Signed in
//            mPhoneVerifyLayout.setVisibility(View.GONE);
//            mCodeVerifyLayout.setVisibility(View.VISIBLE);
//
//            mPhoneNumberField.setText(null);
//            mVerificationField.setText(null);
//            //mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStartVerification:
                if (!validatePhoneNumber()) {
                    return;
                }
                checkPhoneNumber();
                break;
            case R.id.verifyPhone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.buttonResend:
                resendVerificationCode(mPhoneCodeField.getText().toString() + mPhoneNumberField.getText().toString(), mResendToken);
                break;
//            case R.id.signOutButton:
//                signOut();
//                break;
        }
    }

    public void checkPhoneNumber() {
        //showProgress();

        ProfileServices service = RetrofitInstance.getRetrofitInstance().create(ProfileServices.class);
        Logger.addLogAdapter(new AndroidLogAdapter());


        // creating JSONObject
        org.json.JSONObject jo = new JSONObject();
        String code = mPhoneCodeField.getText().toString();
        String newCode = code.substring(1, code.length());
        String phone = mPhoneNumberField.getText().toString();
        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        prefs.putString("country_code", code);
        prefs.putString("phone_number", phone);
        prefs.commit();

        HashMap<String, String> phoneDetails = new HashMap<>();
        phoneDetails.put("country_code", newCode);
        phoneDetails.put("phone_number", phone);


        Call<PhoneResponse> call = service.checkPhone("shakil@gmail.com", "r7EzdKdjmVKh1sstByYt", phoneDetails);

        call.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {

                PhoneResponse phoneResponse = response.body();

                if (response.body().getSuccess()) {
                    if (response.body().getSuccess()) {
                        mDetailText.setText("Number available");
                        mCodeVerifyLayout.setVisibility(View.VISIBLE);
                        mPhoneVerifyLayout.setVisibility(View.INVISIBLE);
                        startPhoneNumberVerification(mPhoneCodeField.getText().toString() + mPhoneNumberField.getText().toString());
                    }

                } else {
                    //String error = phoneResponse.getInfo().getError().toString();
                    Toasty.error(getActivity(),
                            "Number already Exist", Toasty.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: phone no input unsuccessful");

            }
        });
    }

    public void postPhoneNumber(String c_code, String p_number) {
        //showProgress();

        ProfileServices service = RetrofitInstance.getRetrofitInstance().create(ProfileServices.class);
        Logger.addLogAdapter(new AndroidLogAdapter());


        String code = c_code;
        String newCode = code.substring(1, code.length());

        HashMap<String, String> phoneDetails = new HashMap<>();
        phoneDetails.put("country_code", newCode);
        phoneDetails.put("phone_number", p_number);


        Call<PhoneResponse> call = service.postPhone("shakil@gmail.com", "r7EzdKdjmVKh1sstByYt", phoneDetails);

        call.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {

                if (response.isSuccessful()) {
                    mDetailText.setText(R.string.status_verification_succeeded);
                    UserSendModel.setUpdateCache(true);

                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("buttonChange", 126);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toasty.error(getActivity(),
                            "verification post failed", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: phon no input unsuccessful");

            }
        });
    }

}
