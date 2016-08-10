package com.golf.golfnation.user.controller;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.golf.golfnation.Golfnation;
import com.golf.golfnation.MainActivity;
import com.golf.golfnation.R;
import com.golf.golfnation.common.Constants;
import com.golf.golfnation.common.PreferenceManager;
import com.golf.golfnation.common.RequestManager;
import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.user.model.LoginResponse;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>, Constants {

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(PreferenceManager.getBoolean(getApplication(), Constants.Key.LOGIN)) {
            // load user detail
            // Move to next screen
            Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(iMain);
            return;
        }*/

        if(!PreferenceManager.getBoolean(this, Key.LOGIN)) {
            setContentView(R.layout.activity_login);

            // Set up the login form.
            mUserNameView = (AutoCompleteTextView) findViewById(R.id.tv_username);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.tv_password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.login);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            TextView tvRegister = (TextView) findViewById(R.id.prompt_register);
            tvRegister.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });
        } else {
            try {
                String profileURL = String.format(USER_PROFILE_URL, PreferenceManager.getString(this, Key.USER_ID));
                URL url = new URL(profileURL);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                StringRequest signinRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                        if(Constants.Status.STATUS_OK.equals(baseResponse.getStatus())) {
                            Golfnation golfApp = (Golfnation) getApplication();
                            LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
                            golfApp.setUserDetail(loginResponse.getDetails());
                            // Move to next screen
                            Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
                            iMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(iMain);
                            PreferenceManager.saveBoolean(LoginActivity.this, Key.LOGIN, true);
                        } else {
                            PreferenceManager.saveBoolean(LoginActivity.this, Key.LOGIN, false);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestManager.getInstance(this).getRequestQueue().add(signinRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mUserNameView.setError(getString(R.string.error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            try {
                String signinURL = String.format(SIGN_IN_URL, mUserNameView.getText(), mPasswordView.getText());
                URL url = new URL(signinURL);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                StringRequest signinRequest = new StringRequest(Request.Method.GET, url.toString(), onCompleteLoginListener, onErrorListener);
                RequestManager.getInstance(this).getRequestQueue().add(signinRequest);
                showProgress(true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private Response.Listener<String> onCompleteLoginListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            if(Constants.Status.STATUS_OK.equals(baseResponse.getStatus())) {
                Golfnation golfApp = (Golfnation) getApplication();
                LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
                golfApp.setUserDetail(loginResponse.getDetails());
                // Move to next screen
                Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
                iMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iMain);
                PreferenceManager.saveBoolean(LoginActivity.this, Key.LOGIN, true);
                PreferenceManager.saveString(LoginActivity.this, Key.USER_ID, loginResponse.getDetails().getUser_id());
            } else {
                Toast.makeText(LoginActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private Response.ErrorListener onErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            showProgress(false);
        }
    };


    ProgressDialog pDialog;
    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        if(show) {
            if(pDialog == null)
                pDialog = ProgressDialog.show(this, "","Loading..", false);
            else
                pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserNameView.setAdapter(adapter);
    }

}

