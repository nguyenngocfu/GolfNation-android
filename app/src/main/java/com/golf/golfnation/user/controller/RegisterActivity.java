package com.golf.golfnation.user.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.golf.golfnation.common.RequestManager;
import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.user.model.RegisterResponse;
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
public class RegisterActivity extends Activity implements LoaderCallbacks<Cursor>, Constants {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mTermAndConditionView;
    private CheckBox mCbConditionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.tv_username);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.tv_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mCbConditionView = (CheckBox) findViewById(R.id.cb_condition);
        mTermAndConditionView = (TextView) findViewById(R.id.tv_condition);
        String termAndCondition = "<u>Accept all conditions</u>";
        mTermAndConditionView.setText(Html.fromHtml(termAndCondition));
        mTermAndConditionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(TERM_OF_USE));
                startActivity(i);
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_register);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

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
        if(!mCbConditionView.isChecked()) {
            Toast.makeText(this, "Please accept the conditions first!", Toast.LENGTH_LONG).show();
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
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
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            String registerURL = String.format(REGISTER_URL, mEmailView.getText(), mPasswordView.getText());
            try {
                URL url = new URL(registerURL);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                StringRequest registerRequest = new StringRequest(Request.Method.GET, url.toString(), onCompleteRegisterListener, onErrorListener);
                RequestManager.getInstance(this).getRequestQueue().add(registerRequest);
                showProgress(true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
    }

    private Response.Listener<String> onCompleteRegisterListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            if(Status.STATUS_OK.equals(baseResponse.getStatus())) {
                Golfnation golfApp = (Golfnation) getApplication();
                RegisterResponse regResponse = gson.fromJson(response, RegisterResponse.class);
                golfApp.setUserDetail(regResponse.getDetail());
                // Move to next screen
                Intent iMain = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(iMain);
            } else {
                Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private Response.ErrorListener onErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            showProgress(false);
        }
    };

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
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
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


}

