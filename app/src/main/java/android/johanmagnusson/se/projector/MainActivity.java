package android.johanmagnusson.se.projector;

import android.content.Intent;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.johanmagnusson.se.projector.constant.Firebase;
import android.johanmagnusson.se.projector.model.User;
import android.johanmagnusson.se.projector.views.siteDetails.SiteActivity;
import android.johanmagnusson.se.projector.views.sites.AddSiteDialogFragment;
import android.johanmagnusson.se.projector.views.sites.SiteListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity
                          implements SiteListFragment.SiteListListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mDatabaseUser;
    private ValueEventListener mDatabaseUserListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add fragment
        SiteListFragment siteListFragment = new SiteListFragment().newInstance(mUserId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.site_list_container, siteListFragment, siteListFragment.TAG).commit();

        // Create site action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getString(R.string.create_site);

                DialogFragment dialog = new AddSiteDialogFragment().newInstance(mUserId);
                dialog.show(getSupportFragmentManager(), AddSiteDialogFragment.TAG);
            }
        });

        // Get user from Firebase, user id is fetched in the BaseActivity
        mDatabaseUser = FirebaseDatabase.getInstance()
                .getReference()
                .child(Firebase.NODE_USERS)
                .child(mUserId);

        // Listener is added in onStart()
        mDatabaseUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user != null) {
                    Toast.makeText(MainActivity.this, "User is " + user.getName(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "User is NULL", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, getString(R.string.firebase_read_error) + databaseError.getMessage());
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Removed in onStop()
        mDatabaseUser.addValueEventListener(mDatabaseUserListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Removed in onStart()
        mDatabaseUser.removeEventListener(mDatabaseUserListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_log_out){
            logOut();
        }
        else if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // SiteListFragment.SiteListListener
    @Override
    public void onSiteSelected(String siteKey) {
        Intent intent = new Intent(this, SiteActivity.class);
        intent.putExtra(DataKey.SITE_KEY, siteKey);
        startActivity(intent);
    }
}
