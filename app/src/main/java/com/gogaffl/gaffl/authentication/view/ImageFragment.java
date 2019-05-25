package com.gogaffl.gaffl.authentication.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.helper.AuthService;
import com.gogaffl.gaffl.authentication.model.AuthResponse;
import com.gogaffl.gaffl.authentication.model.User;
import com.gogaffl.gaffl.authentication.viewmodel.ImageViewModel;
import com.gogaffl.gaffl.profile.InitialActivity;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.profile.repository.ProfileServices;
import com.gogaffl.gaffl.profile.view.ProfileActivity;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;

public class ImageFragment extends Fragment {

    private ImageViewModel mViewModel;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_PHOTO = 2;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final String TAG = "image-frag";
    Call<AuthResponse> call;
    Map<String, RequestBody> params;
    ProgressDialog pDialog;
    private MySharedPreferences prefs;
    private Intent mIntent;
    private Uri mMediaUri;
    private Uri fileUri;
    int uID;
    Call<PhoneResponse> callEditPic;
    private String mImageFileLocation = "";
    private String postPath;
    private ImageView mImageView;
    private Button mImageButton;
    private String mediaPath, email, token;

    public static ImageFragment newInstance() {
        return new ImageFragment();
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void changeFragment(int id, Fragment frag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(id, 0);
        transaction.remove(newInstance());
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        // TODO: Use the ViewModel
    }

    public static RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), file);
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), s);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);

        Button backButton = view.findViewById(R.id.avatar_back_button);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_avatar);
        mImageView = view.findViewById(R.id.avatar);
        mImageButton = view.findViewById(R.id.camera_button);

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");

        initDialog();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //mImageButton.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            //mImageButton.setEnabled(true);
        }


        floatingActionButton.setOnClickListener(v -> {
            if (UserSendModel.getUid() != -1) {
                uploadEditedPic();
            } else {
                uploadFile();
            }
        });

        backButton.setOnClickListener(v -> {
            if (UserSendModel.getUid() != -1) {
                mIntent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(mIntent);
                getActivity().finish();
            } else {
                changeFragment(R.anim.enter_from_left, new SignupGenderFragment());
            }

        });

        mImageButton.setOnClickListener(v -> showFileChooser());

        return view;
    }

    private void showFileChooser() {

        new MaterialDialog.Builder(getActivity())
                .title("Set your profile picture")
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback((dialog, view, which, text) -> {
                    switch (which) {
                        case 0:
                            if (isExternalStorageAvailable()) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                            } else {
                                Toasty.error(getContext(),
                                        "Storage not Available!", Toasty.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            if (isDeviceSupportCamera()) captureImage();
                            else Toasty.error(getContext(),
                                    "Storage not Available!", Toasty.LENGTH_SHORT).show();
                            break;
                        case 2:
                            mImageView.setImageResource(R.drawable.user_img);
                            break;
                    }
                })
                .show();
    }


//    public boolean validation() {
//        Boolean valid = false;
//
//
//        return valid;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mImageButton.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    // Set the Image in ImageView for Previewing the Media
                    mImageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                    cursor.close();

                    postPath = mediaPath;
                }


            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {

                    Glide.with(this).load(mImageFileLocation).into(mImageView);
                    postPath = mImageFileLocation;

                } else {
                    Glide.with(this).load(fileUri).into(mImageView);
                    postPath = fileUri.getPath();

                }

            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    protected void initDialog() {

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {

        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above

            Intent callCameraApplicationIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (callCameraApplicationIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(callCameraApplicationIntent, REQUEST_TAKE_PHOTO);
            }

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();

            } catch (IOException e) {
                // Here we call the function that will try to catch the exception made by the throw function
                Logger.e(e, "Exception error in generating the file");
            }
            /*Here we add an extra file to the intent to put the address on to.
            For this purpose we use the FileProvider, declared in the AndroidManifest.*/

            try {
                Uri outputUri = FileProvider.getUriForFile(
                        getContext(),
                        "com.imaginers.huthat.fileprovider",
                        photoFile);
                callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            } catch (Exception e) {
                Logger.e(e, "file provider error");
            }

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.i("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }


//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }

    File createImageFile() throws IOException {
        Logger.i("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.i("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.i("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
        User.setFileUri(fileUri);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // Uploading Image/Video
    private void uploadFile() {
        fileUri = User.getFileUri();

        Logger.addLogAdapter(new AndroidLogAdapter());

        String name = "", password = "", email, gender = "", birthdate = "", firstName = "", lastName = "";

        firstName = User.getFirstName();
        lastName = User.getLastName();
        name = firstName + " " + lastName;
        email = User.getEmail();
        password = User.getPassword();
        gender = User.getGender();
        birthdate = User.getAge();


//        name = "onirban";
//        email = "onirbangaffl@gmail.com";
//        password = "12345678";
//        gender = "male";
//        birthdate = "1994-1-27";

//        params = new HashMap<>();
//        if (!name.isEmpty()&& name!=null) params.put("name", createRequestBody(name));
//        if (!gender.isEmpty()&& gender!=null) params.put("gender", createRequestBody(gender.toLowerCase()));
//        if (!password.isEmpty()&& password!=null) params.put("password", createRequestBody(password));
//        if (!email.isEmpty()&& email!=null) params.put("email", createRequestBody(email));
//        if (birthdate != null) params.put("date_of_birth", createRequestBody(birthdate));
        //params.put("_method", createRequestBody("PUT"));

        params = new HashMap<>();
        if (firstName != null && lastName != null) params.put("name", createRequestBody(name));
        if (gender != null) params.put("gender", createRequestBody(gender.toLowerCase()));
        if (password != null) params.put("password", createRequestBody(password));
        if (email != null) params.put("email", createRequestBody(email));
        if (birthdate != null) params.put("date_of_birth", createRequestBody(birthdate));

        AuthService getResponse = RetrofitInstance.getRetrofitInstance().create(AuthService.class);

        if (postPath == null || postPath.equals("")) {
            Logger.i("uri not found");
        } else {
            File file = new File(postPath);
            //Parsing any Media type file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            showpDialog();
            call = getResponse.registerUser(params, fileToUpload);
            Logger.i("both file ran");

        }

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful()) {
                    hidepDialog();
                    Logger.i(response.body().getInfo().getToken());
                    prefs.putString("token", response.body().getInfo().getToken().toString());
                    prefs.putString("email", email);
                    prefs.putBoolean("token_exist", true);
                    prefs.commit();
                    //next activity code here
                    Toasty.success(getActivity(),
                            "Successful!", Toasty.LENGTH_SHORT).show();
                    mIntent = new Intent(getActivity(), InitialActivity.class);
                    startActivity(mIntent);
                } else {
                    hidepDialog();
                    Gson gson = new Gson();
                    try {
                        String body = response.errorBody().string();
                        // Logger.json(body);
                        AuthResponse signupError = new AuthResponse();
                        signupError = gson.fromJson(
                                body,
                                AuthResponse.class);

                        Toasty.error(getActivity(),
                                signupError.getInfo().getEmail().get(0), Toasty.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                hidepDialog();
                Logger.e(t, "Response gotten is");
            }
        });
    }

    private void uploadEditedPic() {
        fileUri = User.getFileUri();

        Logger.addLogAdapter(new AndroidLogAdapter());
        uID = UserSendModel.getUid();
        email = UserSendModel.getEmail();
        token = UserSendModel.getToken();

        ProfileServices services = RetrofitInstance.getRetrofitInstance().create(ProfileServices.class);

        if (postPath == null || postPath.equals("")) {
            Logger.i("uri not found");
        } else {
            File file = new File(postPath);
            //Parsing any Media type file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            RequestBody avatar = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("user[picture]", "avatar.jpg", avatar);
            MultipartBody requestBody = builder.build();


            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            showpDialog();
            callEditPic = services.uploadPic(email, token, requestBody, uID);

        }

        callEditPic.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {

                if (response.isSuccessful()) {
                    hidepDialog();
                    Toasty.success(getActivity(),
                            "Upload Successful!", Toasty.LENGTH_SHORT).show();
                    mIntent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(mIntent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                hidepDialog();
                Logger.e(t, "Response gotten is");
            }
        });
    }

}
