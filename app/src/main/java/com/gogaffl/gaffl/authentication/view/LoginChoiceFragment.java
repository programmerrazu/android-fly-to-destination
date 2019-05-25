package com.gogaffl.gaffl.authentication.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.helper.AuthService;
import com.gogaffl.gaffl.authentication.model.AuthResponse;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginChoiceFragment extends Fragment {

    private FragmentManager fm;
    private FragmentTransaction transaction;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private MySharedPreferences prefs;
    //google
    private static final int RC_SIGN_IN = 9001;
    //insta
    private String token = null;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;


    public LoginChoiceFragment() {
        // Required empty public constructor
    }

    public static LoginChoiceFragment newInstance() {
        return new LoginChoiceFragment();
    }

    public static void dumpIntent(Intent i) {

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e(TAG, "Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e(TAG, "[" + key + "=" + bundle.get(key) + "]");
            }
            Log.e(TAG, "Dumping Intent end");
        }
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


    //////GOOOGLE

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_choice, container, false);
        Button loginButton = view.findViewById(R.id.login_email_button);
        Button closeButton = view.findViewById(R.id.close_button_login_choice);

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        closeButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_left, new AuthFragment());
        });
        loginButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_right, new LoginFragment());
        });


        Button googleLogin = view.findViewById(R.id.login_google_button);
        googleLogin.setOnClickListener(v -> {
            signIn();
        });

        return view;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result, Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            final GoogleSignInAccount acct = result.getSignInAccount();

            // Signed in successfully, show authenticated UI.

            account = GoogleSignIn.getLastSignedInAccount(getContext());
            if (account != null) {
                String personName = "display name" + account.getDisplayName() + "given name " + account.getGivenName() + "family name " + account.getFamilyName();
                String token = account.getIdToken();
                String personEmail = account.getEmail();
                String personId = account.getId();
                String serverCode = account.getServerAuthCode();
                Uri personPhoto = account.getPhotoUrl();

                // Signed in successfully, show authenticated UI.
                System.out.println("GOOOOOGLE HERE : " + personName + " \n" + token + "\n "
                        + personPhoto + "\n " + personEmail + " \n" + personId + "\n " + serverCode + "\n ");

            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    // Retrieve and save the url to the users Cover photo if they have one
    // Retrieve and save the url to the users Cover photo if they have one
    public void setUp() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Go to the Google API Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        String clientId = "YOUR_CLIENT_ID";
        String clientSecret = "YOUR_CLIENT_SECRET";

        // Or your redirect URL for web based applications.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
        String scope = "https://www.googleapis.com/auth/contacts.readonly";

        // Step 1: Authorize -->
        String authorizationUrl =
                new GoogleBrowserClientRequestUrl(clientId, redirectUrl, Arrays.asList(scope)).build();

        // Point or redirect your user to the authorizationUrl.
        System.out.println("Go to the following link in your browser:");
        System.out.println(authorizationUrl);

        // Read the authorization code from the standard input stream.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the authorization code?");
        String code = in.readLine();
        // End of Step 1 <--

        // Step 2: Exchange -->
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                        .execute();
        // End of Step 2 <--

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    ///////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(result, task);
            dumpIntent(data);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(getContext());
        // [END on_start_sign_in]
    }

    public void socialLogin() {
        //showProgress();
        AuthService service = RetrofitInstance.getRetrofitInstance().create(AuthService.class);
        Logger.addLogAdapter(new AndroidLogAdapter());

        String personName = "";
        String personEmail = "";
        String personId = "";
        String personPhoto = "";
        String date = "";
        String token = "";
        String gender = "";


        Map<String, RequestBody> registerMap = new HashMap<>();
        registerMap.put("name", createRequestBody(personName));
        registerMap.put("email", createRequestBody(personEmail));
        registerMap.put("id", createRequestBody(personId));
        registerMap.put("picture", createRequestBody(personPhoto));
        registerMap.put("TOKEN", createRequestBody(date));
        registerMap.put("date_of_birth", createRequestBody(token));
        registerMap.put("gender", createRequestBody(gender));


        Call<AuthResponse> call = service.authUser(registerMap);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                String token = "";
                if (response.isSuccessful()) {
                    Logger.i(response.body().getInfo().getToken());
                    // Do awesome stuff
                    if (response.body() != null) {
                        try {
                            token = response.body().getInfo().getToken();
                            System.out.println("token is : " + token);
                            prefs.putString("_token", token);
                            prefs.putBoolean("isEmailTokenExist", true);

                        } catch (Exception t) {
                            Logger.e(t, "token error");
                        }
                    }

                    prefs.commit();
                    Toasty.success(getActivity(),
                            "logged in!!", Toasty.LENGTH_SHORT).show();

//                    mIntent = new Intent(getActivity(),
//                            TestActivity.class);//test
//                    startActivity(mIntent);
//                    getActivity().finish();

                } else {
                    Gson gson = new Gson();
                    Toasty.error(getActivity(),
                            "failed", Toasty.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: login unsuccessful");
            }
        });
    }


    //////////////////////////////////////


}
