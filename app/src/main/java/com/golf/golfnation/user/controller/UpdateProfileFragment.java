package com.golf.golfnation.user.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.golf.golfnation.Golfnation;
import com.golf.golfnation.R;
import com.golf.golfnation.common.Constants;
import com.golf.golfnation.common.ImageUtils;
import com.golf.golfnation.common.RequestManager;
import com.golf.golfnation.common.event.ProfileUpdateEvent;
import com.golf.golfnation.common.model.PhotoDetail;
import com.golf.golfnation.common.model.UploadPhotoResponse;
import com.golf.golfnation.common.view.CircleTransform;
import com.golf.golfnation.common.volley.MultipartRequest;
import com.golf.golfnation.user.model.LoginResponse;
import com.golf.golfnation.user.model.UserDetail;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link UpdateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends Fragment implements Constants {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Bitmap selectedImage;
    private Bitmap resizedImage;
    private static final int SELECT_PHOTO = 90;
    private Uri imageUri;
    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvZipcode;
    private TextView tvCourse;
    private TextView tvHandicap;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyProfileFragment.
     */
    public static UpdateProfileFragment newInstance() {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_profile, container, false);

        UserDetail userDetail = ((Golfnation) getActivity().getApplication()).getUserDetail();
        ivProfile = (ImageView) v.findViewById(R.id.profile_img);
        Picasso.with(getActivity()).load(userDetail.getPhotoName()).resize(400, 400).memoryPolicy(MemoryPolicy.NO_CACHE).transform(new CircleTransform()).into(ivProfile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        tvName = (TextView) v.findViewById(R.id.tv_name);
        String name = userDetail.getFirstname() + " " + userDetail.getLastname();
        if (!name.trim().isEmpty()) {
            tvName.setText(userDetail.getFirstname() + " " + userDetail.getLastname());
        }
        //tvAddress = (TextView) v.findViewById(R.id.tv_address);
        //tvAddress.setText(userDetail.getLocation());
        //tvZipcode = (TextView) v.findViewById(R.id.tv_zipcode);
        //tvZipcode.setText(userDetail.getZip_code());
        tvCourse = (TextView) v.findViewById(R.id.tv_course);
        tvCourse.setText(userDetail.getHome_course());
        tvHandicap = (TextView) v.findViewById(R.id.tv_handicap);
        tvHandicap.setText(userDetail.getHandicap());

        Button btnUpdate = (Button) v.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                if (resizedImage == null) {
                    updateProfile();
                } else {
                    completedUpload = false;
                    //new UploadImageAsyncTask().execute();
                    //PhotoMultipartRequest
                    String url = UPLOAD_USER_PROFILE__URL;

                    File filePart = ImageUtils.persistImage(getActivity().getApplicationContext(), resizedImage, getFileNameFromURI(imageUri));
                    MultipartRequest stringRequest = new MultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {

                        @Override
                        public void onResponse(NetworkResponse response) {
                            showProgress(false);
                            Golfnation app = (Golfnation) getActivity().getApplication();
                            try {
                                JSONObject result = new JSONObject(new String(response.data));
                                String status = result.getString("Status");
                                String message = result.getString("Message");
                                JSONObject details = result.getJSONObject("details");
                                String photoURL = details.getString("photoName");
                                app.getUserDetail().setPhotoName(photoURL);
                                EventBus.getDefault().post(new ProfileUpdateEvent(photoURL));
                                Log.i("Messsage", message);
                            } catch (Exception e) {
                                Log.d("JSON Exception", e.toString());

                            }
                            if (completedUpload) {

                                //Log.e("Observer number ", "nr : " + app.getUserDetail().countObservers());
                                //app.getUserDetail().notifyObservers(app.getUserDetail().getPhotoName());
                                Fragment fragment = MyProfileFragment.newInstance();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container, fragment, fragment.getClass().toString())
                                        .commit();

                            } else {
                                completedUpload = true;
                                updateProfile();
                            }


                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showProgress(false);
                            Log.d("ERROR", "Error [" + error + "]");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            Golfnation app = (Golfnation) getActivity().getApplication();
                            params.put("user_id", app.getUserDetail().getUser_id());
                            return params;
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            // file name could found file base or direct access from real path
                            // for now just get bitmap data from ImageView
                            Golfnation app = (Golfnation) getActivity().getApplication();
                            params.put("uploaded_file", new DataPart(app.getUserDetail().getUser_id() + System.currentTimeMillis() + "profile.png", ImageUtils.getBinaryData(resizedImage), "image/png"));
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestManager.getInstance(getActivity()).getRequestQueue().add(stringRequest);
                }

            }
        });
        return v;
    }

    public void updateProfile() {
        Golfnation app = (Golfnation) getActivity().getApplication();
        String registerURL = String.format(UPDATE_PROFILE_URL, app.getUserDetail().getUser_id(),
                tvName.getText(), app.getUserDetail().getEmail(), app.getUserDetail().getUsername(),
                "", "", tvCourse.getText().toString(), tvHandicap.getText().toString());
        try {
            URL url = new URL(registerURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            StringRequest updateProfileRequest = new StringRequest(Request.Method.GET, url.toString(), onCompleteRegisterListener, onErrorListener);
            RequestManager.getInstance(getActivity()).getRequestQueue().add(updateProfileRequest);
            showProgress(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Response.Listener<String> onCompleteRegisterListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (completedUpload) showProgress(false);
            Gson gson = new Gson();
            LoginResponse baseResponse = gson.fromJson(response, LoginResponse.class);

            Golfnation app = (Golfnation) getActivity().getApplication();
            app.getUserDetail().setHandicap(baseResponse.getDetails().getHandicap());
            app.getUserDetail().setHome_course(baseResponse.getDetails().getHome_course());
            if (Status.STATUS_OK.equals(baseResponse.getStatus())) {

                if (completedUpload) {
                    // Move to profile screen
                    Fragment fragment = MyProfileFragment.newInstance();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment, fragment.getClass().toString())
                            .commit();
                } else {
                    completedUpload = true;
                }
            } else {
                Toast.makeText(getActivity(), baseResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private Response.ErrorListener onErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (completedUpload) showProgress(false);
        }
    };

    ProgressDialog pDialog;

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        if (show) {
            if (pDialog == null)
                pDialog = ProgressDialog.show(getActivity(), "", "Loading..", false);
            else
                pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    String selectedFilePath;
    boolean completedUpload = true;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        imageUri = imageReturnedIntent.getData();
                        selectedFilePath = getRealPathFromURI(imageUri);
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        resizedImage = ImageUtils.resizeBitmap(selectedImage, 400, 400);
                        Picasso.with(getActivity()).load(imageUri).transform(new CircleTransform()).into(ivProfile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public class UploadImageAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Golfnation app = (Golfnation) getActivity().getApplication();
            //String response = ImageUtils.uploadUserPhoto(resizedImage, app.getUserDetail().getUser_id(), System.currentTimeMillis() + ".png");
            HashMap<String, String> data = new HashMap<>();
            data.put("user_id", app.getUserDetail().getUser_id());
            data.put("uploaded_file", ImageUtils.getStringImage(resizedImage));
            String response = ImageUtils.sendPostRequest(Constants.UPLOAD_USER_PROFILE__URL, data);
            Gson gson = new Gson();
            try {
                UploadPhotoResponse ptResponse = gson.fromJson(response, UploadPhotoResponse.class);
                PhotoDetail detail = ptResponse.getDetails();
                UserDetail ud = app.getUserDetail();
                ud.setPhotoName(detail.getPhotoName());
                app.setUserDetail(ud);
            } catch (Exception ex) {
                Log.e("Gson error", ex.getMessage());
                //Toast.makeText(getActivity(), "Can't change profile image", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showProgress(false);
            if (completedUpload) {

                Golfnation app = (Golfnation) getActivity().getApplication();
                //Log.e("Observer number ", "nr : " + app.getUserDetail().countObservers());
                //app.getUserDetail().notifyObservers(app.getUserDetail().getPhotoName());
                Fragment fragment = MyProfileFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().toString())
                        .commit();

            } else {
                completedUpload = true;
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getFileNameFromURI(Uri uri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        Cursor metaCursor = cr.query(uri, projection, null, null, null);
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    return metaCursor.getString(0);
                }
            } finally {
                metaCursor.close();
            }
        }
        return null;
    }

}
