package android.johanmagnusson.se.projector;

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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class SiteActivity extends AppCompatActivity
                          implements SiteDialogFragment.SiteDialogListener {

    private static final String TAG = SiteActivity.class.getSimpleName();

    private DatabaseReference mDatabase;

    private ActionBar mActionBar;

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
                DialogFragment dialog = new SiteDialogFragment();
                dialog.show(getSupportFragmentManager(), "Dialog_tag");
            }
        });

        // Add site fragment and pass on any extra data
        if(savedInstanceState == null) {
            String siteKey = getIntent().getStringExtra(DataKey.SITE_KEY);

            SiteFragment siteFragment = new SiteFragment();

            if(siteKey != null) {
                Bundle args = new Bundle();
                args.putString(DataKey.SITE_KEY, siteKey);
                siteFragment.setArguments(args);
            }

            getSupportFragmentManager().beginTransaction().add(R.id.site_container, siteFragment).commit();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        else if (id == R.id.action_edit) {
            String title = getString(R.string.edit_site);

            DialogFragment dialog = new SiteDialogFragment().newInstance(title, "");
            dialog.show(getSupportFragmentManager(), "Dialog_tag");
            return true;
        }
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(String siteId, String siteName) {
        if(!TextUtils.isEmpty(siteName))
            updateSite(siteId, siteName);
    }

    //todo: Fix method
    private void updateSite(String siteId, String siteName) {
        // Update site at /sites/$siteid
        DatabaseReference ref = mDatabase.child(Firebase.NODE_SITES);

        Site site = new Site(siteName, "Anonymous");
        Map<String, Object> postValues = site.toMap();
        ref.updateChildren(postValues);
    }
}
