package android.johanmagnusson.se.projector.views.siteDetails;

import android.johanmagnusson.se.projector.R;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.johanmagnusson.se.projector.constant.Firebase;
import android.johanmagnusson.se.projector.model.Site;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SiteActivity extends AppCompatActivity {

    private static final String TAG = SiteActivity.class.getSimpleName();

    private DatabaseReference mDatabaseSites;

    private ActionBar mActionBar;

    private String mSiteKey;
    private Site mSite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_site);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        if(mActionBar != null) { mActionBar.setDisplayHomeAsUpEnabled(true); }

        //todo: change fab action(s)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new EditSiteNameDialogFragment();
                dialog.show(getSupportFragmentManager(), "Dialog_tag");
            }
        });

        // Add site fragment and pass on any extra data
        if(savedInstanceState == null) {
            mSiteKey = getIntent().getStringExtra(DataKey.SITE_KEY);

            if(mSiteKey == null) {
                // No need to continue without a site key
                finish();
                return;
            }

            SiteFragment siteFragment = new SiteFragment();

            Bundle args = new Bundle();
            args.putString(DataKey.SITE_KEY, mSiteKey);
            siteFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.site_container, siteFragment).commit();

            mDatabaseSites = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Firebase.NODE_SITES)
                    .child(mSiteKey);

            mDatabaseSites.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Site site = dataSnapshot.getValue(Site.class);

                    // Site has been removed or unshared by owner
                    if(site == null) {
                        finish();
                        return;
                    }

                    mSite = site;
                    if(mActionBar != null) {mActionBar.setTitle(site.getName());}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, getString(R.string.firebase_read_error) + databaseError.getMessage());
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_site, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title;

        switch (id) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_edit:
                DialogFragment editDialog = new EditSiteNameDialogFragment().newInstance(mSiteKey, mSite.getName());
                editDialog.show(getSupportFragmentManager(), EditSiteNameDialogFragment.TAG);
                return true;

            case R.id.action_remove:
                DialogFragment removeDialog = new RemoveSiteDialogFragment().newInstance(mSiteKey);
                removeDialog.show(getSupportFragmentManager(), RemoveSiteDialogFragment.TAG);
                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
