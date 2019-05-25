package com.gogaffl.gaffl.authentication.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.helper.AuthService;
import com.gogaffl.gaffl.authentication.model.FbUserModel;
import com.gogaffl.gaffl.authentication.model.LinkedinEmail;
import com.gogaffl.gaffl.authentication.model.LinkedinPic;
import com.gogaffl.gaffl.authentication.model.LinkedinUser;
import com.gogaffl.gaffl.authentication.model.User;
import com.gogaffl.gaffl.authentication.viewmodel.RegistrationViewModel;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class RegistrationFragment extends Fragment {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String TAG = "register";
    private RegistrationViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    /****YOUR LINKEDIN APP INFO HERE*********/
    private static final String API_KEY = "81hual36aq601t";
    Intent mIntent;

    private static final int RC_SIGN_IN = 9001;
    private static final int REQ_SIGN_IN_REQUIRED = 55664;
    private MySharedPreferences prefs;
    AccessTokenTracker accessTokenTracker;
    private GoogleSignInClient mGoogleSignInClient;
    ProfileTracker profileTracker;
    String profileImageUrl;
    String userid;
    //google
    private String token = null;
    //facebook
    private AccessToken mAccessToken;
    private CallbackManager callbackManager;
    //linkedin
    /*CONSTANT FOR THE AUTHORIZATION PROCESS*/
    private static final String SECRET_KEY = "mG45M9AsTUuNsKOW";
    private static final String CALLBACK_URL = "oauth-testing:///";
    //This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String STATE = "E3ZYKC1T6H2yP4z";
    private static final String REDIRECT_URI = "https://www.google.com";
    //private static final String SCOPES = "r_fullprofile%20r_emailaddress%20r_network";
    private static final String SCOPES = "r_liteprofile";
    private static final String SCOPE_EMAIL = "r_emailaddress ";
    /*********************************************/

    //These are constants used for build the urls
    private static final String AUTHORIZATION_URL =
            "https://www.linkedin.com/oauth/v2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/oauth/v2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String SCOPE_PARAM = "scope";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    Activity mActivity = getActivity();
    private WebView webView;
    private ProgressDialog pd;
    String linkName, linkId, linkPic, linkEmail;
    private String mAccountName;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        // TODO: Use the ViewModel
    }

    /**
     * Method that generates the url for get the authorization token from the Service
     *
     * @return Url
     */
    private static String getAuthorizationUrl() {
        String URL = AUTHORIZATION_URL
                + QUESTION_MARK
                + RESPONSE_TYPE_PARAM
                + EQUALS
                + RESPONSE_TYPE_VALUE
                + AMPERSAND
                + CLIENT_ID_PARAM
                + EQUALS
                + API_KEY
                + AMPERSAND
                + SCOPE_PARAM
                + EQUALS
                + SCOPE_EMAIL
                + SCOPES
                + AMPERSAND
                + STATE_PARAM
                + EQUALS
                + STATE
                + AMPERSAND
                + REDIRECT_URI_PARAM
                + EQUALS
                + REDIRECT_URI;
        Log.i("authorization URL", "" + URL);
        return URL;
    }


    //GOOGLE

    private static String getAccessTokenUrl(String authorizationToken) {
        String URL = ACCESS_TOKEN_URL
                + QUESTION_MARK
                + GRANT_TYPE_PARAM
                + EQUALS
                + GRANT_TYPE
                + AMPERSAND
                + RESPONSE_TYPE_VALUE
                + EQUALS
                + authorizationToken
                + AMPERSAND
                + CLIENT_ID_PARAM
                + EQUALS
                + API_KEY
                + AMPERSAND
                + REDIRECT_URI_PARAM
                + EQUALS
                + REDIRECT_URI
                + AMPERSAND
                + SECRET_KEY_PARAM
                + EQUALS
                + SECRET_KEY;
        Log.i("accessToken URL", "" + URL);
        return URL;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.startTracking();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook option to init
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                String fbCurToken = currentAccessToken.getToken();
                String fbOldToken = oldAccessToken.getToken();

                System.out.println("this is current fb token: " + fbCurToken);
                System.out.println("this is old fb token: " + fbOldToken);

                prefs = MySharedPreferences.getInstance(getActivity(),
                        "Gaffl_Prefs");
                prefs.putString("fb_c_token", fbCurToken);
                prefs.putString("fb_o_token", fbOldToken);
                prefs.commit();

            }
        };

        // If the access token is available already assign it.
        mAccessToken = AccessToken.getCurrentAccessToken();
        Logger.i("fb access token is available from before");

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };

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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
            if (acct != null) {
                String personName = acct.getDisplayName();
                System.out.println("this is token auth code: " + acct.getIdToken());
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                if (personName == null) personName = "No Name";
                if (personPhoto == null)
                    personPhoto = Uri.parse("https://static.thenounproject.com/png/17241-200.png");

                System.out.println(personName + " " + personPhoto + " " + personEmail + " " + personId);
                User.setFirstName(personName);
                User.setUid(personId);
                User.setFileUri(personPhoto);
                User.setEmail(personEmail);
                User.setProvider("Google");
                User.setSocialStatus(true);
                changeFragment(R.anim.enter_from_right, new SignupAgeFragment());

                //socialSignup(personName, personEmail, personId, personPhoto);

            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            mAccountName = acct.getEmail();
        }

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


        }

        if (requestCode == REQ_SIGN_IN_REQUIRED && resultCode == RESULT_OK) {
            // We had to sign in - now we can finish off the token request.
            new RetrieveTokenTask().execute(mAccountName);
        }
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String scopes = "oauth2:profile email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // ((TextView) findViewById(R.id.token_value)).setText("Token Value: " + s);

        }
    }

    ///////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        // [END on_start_sign_in]
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        Button backButton = view.findViewById(R.id.close_button);
        Button emailSignupButton = view.findViewById(R.id.create_account_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        revokeAccess();


        emailSignupButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_right, new SignupNameFragment());
        });

        backButton.setOnClickListener(v -> {
            changeFragment(R.anim.enter_from_left, new AuthFragment());
        });


        Button googleLogin = view.findViewById(R.id.google_button);
        googleLogin.setOnClickListener(v -> {
            signIn();
        });

        Button fbButton = view.findViewById(R.id.facebook_button);

        fbButton.setOnClickListener(v -> {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            //LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            System.out.println("onSuccess");
                            mAccessToken = loginResult.getAccessToken();
                            System.out.println("onSuccess: token is: " + mAccessToken);
                            boolean isLoggedIn = mAccessToken != null && !mAccessToken.isExpired();
                            //prefs.putString("fbtoken",mAccessToken.getToken());
                            getUserProfile(mAccessToken);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            System.out.println("onCancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Log.v("LoginActivity", exception.getCause().toString());
                        }
                    });
        });

        //Initialize session
        Button liLoginButton = (Button) view.findViewById(R.id.linkedin_button);
        liLoginButton.setOnClickListener(v -> {
            signupLinkedin(view);
        });


        return view;
    }

    //facebook profile
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                (object, response) -> {
                    try {
                        //You can fetch user Info like this…
                        //object.getJSONObject(“picture”).
                        userid = AccessToken.getCurrentAccessToken().getUserId();
                        System.out.println("this is user id: " + userid);
                        System.out.println(response.toString());
                        ;
                        FbUserModel fbUserModel = new Gson().fromJson(object.toString(), FbUserModel.class);
                        String urlfb = fbUserModel.getPicture().getData().getUrl();
                        String name = object.getString("name");
                        String email = object.getString("email");
                        String uid = object.getString("id");
                        Uri myUri = Uri.parse(urlfb);

                        //object.getString(“name”);
                        //object.getString(“email”));
                        //object.getString(“id”));
                        System.out.println(name + "\n" + email + "\n" + uid + "\n" + myUri + "\n");
                        User.setFirstName(name);
                        User.setUid(uid);
                        User.setFileUri(myUri);
                        User.setEmail(email);
                        User.setProvider("Facebook");
                        User.setSocialStatus(true);
                        changeFragment(R.anim.enter_from_right, new SignupAgeFragment());
                        //socialSignup(name, email, uid, myUri);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    ////////////////////////////////////////LINKEDIN////////////////////////////////////////////////

    public void signupLinkedin(View view) {

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = prefs.getString("li_access_token", "default");

        if (!tokenOfLi.isEmpty()) {

            prepareLinkedinData();
            getEmailOfLi();

        } else {

            webView = (WebView) view.findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.requestFocus(View.FOCUS_DOWN);
            pd = ProgressDialog.show(getActivity(), "", "Loading..", true);

            //Set a custom web view client
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    //This method will be executed each time a page finished loading.
                    //The only we do is dismiss the progressDialog, in case we are showing any.
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                    //This method will be called when the Auth proccess redirect to our RedirectUri.
                    //We will check the url looking for our RedirectUri.
                    if (authorizationUrl.startsWith(REDIRECT_URI)) {
                        Log.i("Authorize", "");
                        Uri uri = Uri.parse(authorizationUrl);
                        //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                        //If not, that means the request may be a result of CSRF and must be rejected.
                        String stateToken = uri.getQueryParameter(STATE_PARAM);
                        if (stateToken == null || !stateToken.equals(STATE)) {
                            Log.e("Authorize", "State token doesn't match");
                            return true;
                        }

                        //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                        String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                        if (authorizationToken == null) {
                            Log.i("Authorize", "The user doesn't allow authorization.");
                            return true;
                        }
                        Log.i("Authorize", "Auth token received: " + authorizationToken);
                        prefs = MySharedPreferences.getInstance(getActivity(),
                                "Gaffl_Prefs");


                        prefs.putString("li_auth_token", authorizationToken);
                        prefs.commit();

                        //Generate URL for requesting Access Token
                        String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                        //We make the request in a AsyncTask
                        new PostRequestAsyncTask().execute(accessTokenUrl);
                    } else {
                        //Default behaviour
                        Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                        webView.loadUrl(authorizationUrl);
                    }
                    return true;
                }
            });

            //Get the authorization Url
            String authUrl = getAuthorizationUrl();
            Log.i("Authorize", "Loading Auth Url: " + authUrl);
            //Load the authorization URL into the webView
            webView.loadUrl(authUrl);

        }

    }


    public void onNewIntent(Intent intent) {
        dealWithLinkedinResponse(intent);
    }

    private void dealWithLinkedinResponse(Intent intent) {
        Uri uri = intent.getData();
        System.out.println("URI=" + uri);
        if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            //authoriseNewUser(oauthVerifier);
        }
    }

    public void prepareLinkedinData() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        int timeOut = 5 * 60;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.linkedin.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        /*Create handle for the RetrofitInstance interface*/
        AuthService service = retrofit.create(AuthService.class);
        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = prefs.getString("li_access_token", "default");
        Logger.i("Main token is here: " + tokenOfLi);

        /*Call the method with parameter in the interface to get the employee data*/
        Call<LinkedinUser> call = service.getLinkedinUserProfile(tokenOfLi);

        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<LinkedinUser>() {
            @Override
            public void onResponse(Call<LinkedinUser> call, Response<LinkedinUser> response) {
                if (response.body() != null) {
                    LinkedinUser linkedinUser = response.body();
                    linkName =
                            linkedinUser.getLastName().getLocalized().getEn_US()
                                    + " " +
                                    linkedinUser.getLastName().getLocalized().getEn_US();

                    linkId = linkedinUser.getId();

                    //String photoUrl = linkedinUser.getProfilePicture().getDisplayImage().

                    System.out.println("user ran linkedin: " + linkName);
//                    String jsonObject = new Gson().toJson(response);
//                    Logger.i("Linkedin Response:", jsonObject);

                    getPicOfLi();


                } else {
                    Toast.makeText(getActivity(), "No Response found! " + response.body(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LinkedinUser> call, Throwable t) {
                Log.d("Error linkedin user", t.toString());
                Toast.makeText(getContext(), "error linkedin User!",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getPicOfLi() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.linkedin.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        /*Create handle for the RetrofitInstance interface*/
        AuthService service = retrofit.create(AuthService.class);

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = "Bearer " + prefs.getString("li_access_token", "default");
        if (tokenOfLi.length() > 5) {
            /*Call the method with parameter in the interface to get the employee data*/
            String projection = "(id,profilePicture(displayImage~:playableStreams))";
            Call<LinkedinPic> call = service.getLinkedinUserPic(tokenOfLi, projection);

            /*Log the URL called*/
            Log.wtf("URL Called", call.request().url() + "");


            call.enqueue(new Callback<LinkedinPic>() {
                @Override
                public void onResponse(Call<LinkedinPic> call, Response<LinkedinPic> response) {
                    if (response.body() != null) {

                        linkPic = response.body().getProfilePicture().
                                getDisplayImageSee().
                                getElements().get(3).
                                getIdentifiers().get(0).
                                getIdentifier().toString();
//
                        System.out.println("ran perfectly\n " + new Gson().toJson(response.body()) + "\n" + linkPic);
//                        //Logger.i("Linkedin Response:", new Gson().toJson(response));
//
//                        if (response.isSuccessful())
//                            Log.e("Success", new Gson().toJson(response.body()));
//                        else
//                            Log.e("unSuccess", new Gson().toJson(response.errorBody()));

                        getEmailOfLi();

                    } else {
                        Toast.makeText(getActivity(), "No Response found! " + response.body(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LinkedinPic> call, Throwable t) {
                    Log.d("Error Linkedin Pic", t.toString());
                    Toast.makeText(getContext(), "Error linkedin Pic!",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void getEmailOfLi() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.linkedin.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        /*Create handle for the RetrofitInstance interface*/
        AuthService service = retrofit.create(AuthService.class);

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = "Bearer " + prefs.getString("li_access_token", "default");
        if (tokenOfLi.length() > 5) {
            /*Call the method with parameter in the interface to get the employee data*/
            String q = "members";
            String projection = "(elements*(handle~))";
            Call<LinkedinEmail> call = service.getLinkedinUserEmail(tokenOfLi, q, projection);

            /*Log the URL called*/
            Log.wtf("URL Called", call.request().url() + "");


            call.enqueue(new Callback<LinkedinEmail>() {
                @Override
                public void onResponse(Call<LinkedinEmail> call, Response<LinkedinEmail> response) {
                    if (response.body() != null) {

                        linkEmail = response.body().getElements().get(0).getHandleEmail().getEmailAddress();
                        System.out.println("email is" + linkEmail);
//
                        System.out.println("ran perfectly\n " + new Gson().toJson(response.body()) + "\n" + linkEmail);
//                        //Logger.i("Linkedin Response:", new Gson().toJson(response));
//
//                        if (response.isSuccessful())
//                            Log.e("Success", new Gson().toJson(response.body()));
//                        else
//                            Log.e("unSuccess", new Gson().toJson(response.errorBody()));
                        if (linkName != null && linkEmail != null && linkId != null && linkPic != null) {
                            User.setFirstName(linkName);
                            User.setUid(linkEmail);
                            User.setFileUri(Uri.parse(linkPic));
                            User.setEmail(linkEmail);
                            User.setProvider("Linkedin");
                            User.setSocialStatus(true);
                            changeFragment(R.anim.enter_from_right, new SignupAgeFragment());
                            //socialSignup(linkName, linkEmail, linkId, Uri.parse(linkPic));
                        }


                    } else {
                        Toast.makeText(getActivity(), "No Response found! " + response.body(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LinkedinEmail> call, Throwable t) {
                    Log.d("Error Linkedin Email", t.toString());
                    Toast.makeText(getContext(), "Error linkedin Email!",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getActivity(), "", "Loading..", true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if (urls.length > 0) {
                String url = urls[0];

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    okhttp3.Response response = okHttpClient.newCall(request).execute();
                    if (response != null && response.isSuccessful()) {
                        //If status is OK 200
                        JSONObject resultJson = new JSONObject(response.body().string());
                        //Extract data from JSON Response
                        int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;

                        String accessToken =
                                resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                        Log.e("Tokenm", "" + accessToken);
                        prefs.putString("li_access_token", accessToken);
                        prefs.commit();

                        if (expiresIn > 0 && accessToken != null) {
                            Log.i("Authorize", "This is the access Token: "
                                    + accessToken
                                    + ". It will expires in "
                                    + expiresIn
                                    + " secs");

                            //Calculate date of expiration
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.SECOND, expiresIn);
                            long expireDate = calendar.getTimeInMillis();

                            ////Store both expires in and access token in shared preferences
                            SharedPreferences preferences =
                                    getActivity().getSharedPreferences("user_info", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putLong("expires", expireDate);
                            editor.putString("accessToken", accessToken);
                            editor.commit();
                            return true;
                        }
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (status) {
                //If everything went Ok, change to another activity.
                prepareLinkedinData();
                //Logger.i("this is email"+getEmailOfLi());
                webView.setVisibility(View.INVISIBLE);

//                Intent startProfileActivity = new Intent(MainActivity.this, HomeActivity.class);
//                MainActivity.this.startActivity(startProfileActivity);
            }
        }
    }

}