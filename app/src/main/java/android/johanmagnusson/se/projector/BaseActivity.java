package android.johanmagnusson.se.projector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.johanmagnusson.se.projector.views.login.LoginActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = BaseActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    protected GoogleApiClient mGoogleApiClient;

    protected String mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // BUG: Gradle bug, resource string is not always created.
                //.requestIdToken(getString(R.string.default_web_client_id))
                // Workaround: Place Firebase OAuth client_id in global gradle properties instead.
                .requestIdToken(BuildConfig.FIREBASE_OAUTH_CLINET_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserId = sharedPreferences.getString(DataKey.USER_ID, null);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Listener is added in onStart()
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user == null) {
                    // Clear logged in user data
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(DataKey.USER_ID, null).apply();


                    startActivityForLogIn();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Removed in onStop()
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Added in onStart()
        if(mFirebaseAuthListener != null)
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
    }

    protected void logOut() {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                // Do nothing, Firebase AuthListener will handle the rest
            }
        });
    }

    private void startActivityForLogIn() {
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //OnConnectionFailedListener.onConnectionFailed
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
