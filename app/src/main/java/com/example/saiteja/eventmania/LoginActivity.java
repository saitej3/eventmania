package com.example.saiteja.eventmania;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.saiteja.eventmania.helper.network.ConnectionDetector;
import com.example.saiteja.eventmania.helper.network.ServerUtilities;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private static final String TAG = "LoginActivity";
    //google login
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    String name;
    String email;

    //notifications
    ConnectionDetector cd;
    public static final String PROPERTY_REG_ID = "156719298888";
    String SENDER_ID = "156719298888";
    static final String TAGGCM = "GCMDemo";
    GoogleCloudMessaging gcm;
    Context context;
    String regid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(this,
                    "Internet Connection Error",
                    Toast.LENGTH_SHORT);
            return;
        }

        //google sign in
        findViewById(R.id.sign_in_button_login).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button_login);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

    }




    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {

            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            name=acct.getDisplayName();
            email=acct.getEmail();
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading !");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            SharedPreferences.Editor editor;
            SharedPreferences sharedPref = null;
            sharedPref = getSharedPreferences("user",Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            Log.d("name",name);
            Log.d("email",email);
            editor.putString("name", name);
            editor.putString("email", email);
            editor.commit();
            gcmId();

            final SharedPreferences prefs = getGCMPreferences(context);
            String registrationId = prefs.getString(PROPERTY_REG_ID, "");
            startActivity(new Intent(this,MainActivity.class));
        }

        else {

        }
    }


    public void gcmId()
    {
        if (true) {

            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAGGCM, "No valid Google Play Services APK found.");
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAGGCM, "Registration not found.");
            return "";
        }

        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {

        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private void registerInBackground() {
        new Reg().execute(null, null, null);
    }



    private void sendRegistrationIdToBackend() {

        ServerUtilities.register(this, name, email, regid);
    }


    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_login:
                signIn();
                break;

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    private class Reg extends AsyncTask<Void,Void,String>
    {

        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;

                sendRegistrationIdToBackend();
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

        protected void onPostExecute(String msg) {
        }
    }


}
