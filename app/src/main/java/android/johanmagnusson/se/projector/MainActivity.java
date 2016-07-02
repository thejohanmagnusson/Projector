package android.johanmagnusson.se.projector;

import android.content.Intent;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.johanmagnusson.se.projector.constant.Firebase;
import android.johanmagnusson.se.projector.model.Site;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MainActivity extends AppCompatActivity
                          implements SiteDialogFragment.SiteDialogListener,
                                     SiteListFragment.SiteListListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //todo: handle two pane mode on tablets

        // Create site action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getString(R.string.create_site);

                DialogFragment dialog = new SiteDialogFragment().newInstance(title, "");
                dialog.show(getSupportFragmentManager(), "Dialog_tag");
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // SiteDialogFragment.SiteDialogListener
    @Override
    public void onDialogPositiveClick(String siteId, String siteName) {
        if(!TextUtils.isEmpty(siteName))
            createSite(siteName);
    }

    private void createSite(String siteName) {
        // Create new site at /sites/$siteid
        String key = mDatabase.child(Firebase.NODE_SITES).push().getKey();
        Site site = new Site(siteName, "Anonymous");
        Map<String, Object> postValues = site.toMap();
        mDatabase.child(Firebase.NODE_SITES).child(key).setValue(postValues);
    }

    // SiteListFragment.SiteListListener
    @Override
    public void onSiteSelected(String sitekey) {
        Intent intent = new Intent(this, SiteActivity.class);
        intent.putExtra(DataKey.SITE_KEY, sitekey);
        startActivity(intent);
    }
}
