package android.johanmagnusson.se.projector.views.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.johanmagnusson.se.projector.BuildConfig;
import android.johanmagnusson.se.projector.MainActivity;
import android.johanmagnusson.se.projector.R;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

// This class should NOT extend BaseActivity!
// It handles it's own Firebase and GoogleApiClient
public class LoginActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String KEY_RESOLVING_ERROR = "resolving_error";

    private static final int RC_SIGN_IN = 9001;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mResolvingError = savedInstanceState.getBoolean(KEY_RESOLVING_ERROR, false);
        }

        setContentView(R.layout.activity_google);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

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

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Listener is added in onStart()
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null) {
                    onAuthenticated(user);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_RESOLVING_ERROR, mResolvingError);
        super.onSaveInstanceState(outState);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                // Authenticate with Firebase
                firebaseAuthWithGoogle(account);
            }
            else {
                // Sign in failed
                // TODO: show error to user
            }
        }
        else if(requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if(!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        // TODO: show progress?

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "---- signInWithCredential:onComplete:" + task.isSuccessful());

                        // Successful login is notified to the mFirebaseAuthListener.

                        // If sign in fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "---- signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "---- Authentication failed.", Toast.LENGTH_LONG).show();
                        }

                        // TODO: hide progress?
                    }
                });
    }

    private void onAuthenticated(FirebaseUser user) {
        // Save current authenticated user id
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DataKey.USER_ID, user.getUid()).apply();

        // Launch main activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //OnConnectionFailedListener.onConnectionFailed
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(mResolvingError) {
            // Already trying to resolve the error.
            return;
        }
        else if(connectionResult.hasResolution()) {
            try {
                // Try to resolve the error.
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            }
            catch (IntentSender.SendIntentException e) {
                // Try again.
                mGoogleApiClient.connect();
            }
        }
        else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), ErrorDialogFragment.TAG);
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public static final String TAG = ErrorDialogFragment.class.getSimpleName();

        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((LoginActivity) getActivity()).onDialogDismissed();
        }
    }
}
