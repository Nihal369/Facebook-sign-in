package com.lmntrx.android.nihal.facebooksign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        LoginButton loginButton = (LoginButton) findViewById(R.id.loginButton);
        //loginButton.setReadPermissions("email");
        //loginButton.setReadPermissions("user_friends");
        //loginButton.setReadPermissions("public_profile");
       // loginButton.setReadPermissions("user_birthday");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Profile myProfile = Profile.getCurrentProfile();

                Log.i("Mava", myProfile.getName());
                Log.i("Mava", myProfile.getFirstName());
                Log.i("Mava", myProfile.getId());
                Log.i("Mava", myProfile.getLastName());

                ProfilePictureView picture=(ProfilePictureView)findViewById(R.id.profilePicView);
                picture.setProfileId(Profile.getCurrentProfile().getId());
                TextView text=(TextView)findViewById(R.id.textView);
                try {
                    text.setText(myProfile.getName());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    TextView textView2=(TextView)findViewById(R.id.textView2);
                                    textView2.setText(email);
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    TextView textView3=(TextView)findViewById(R.id.textView3);
                                    textView3.setText(birthday);
                                    String gender=object.getString("gender");
                                    TextView textView4=(TextView)findViewById(R.id.textView4);
                                    textView4.setText(gender);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}