package android.johanmagnusson.se.projector;

import android.content.Intent;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.johanmagnusson.se.projector.views.siteDetails.SiteActivity;
import android.johanmagnusson.se.projector.views.sites.AddSiteDialogFragment;
import android.johanmagnusson.se.projector.views.sites.SiteListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
                          implements SiteListFragment.SiteListListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create site action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getString(R.string.create_site);

                DialogFragment dialog = new AddSiteDialogFragment().newInstance();
                dialog.show(getSupportFragmentManager(), AddSiteDialogFragment.TAG);
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

    // SiteListFragment.SiteListListener
    @Override
    public void onSiteSelected(String siteKey) {
        Intent intent = new Intent(this, SiteActivity.class);
        intent.putExtra(DataKey.SITE_KEY, siteKey);
        startActivity(intent);
    }
}
