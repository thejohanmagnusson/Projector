package android.johanmagnusson.se.projector.views.sites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.johanmagnusson.se.projector.R;
import android.johanmagnusson.se.projector.constant.Firebase;
import android.johanmagnusson.se.projector.model.Site;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AddSiteDialogFragment extends DialogFragment{

    public static final String TAG = SiteListFragment.class.getSimpleName();

    public static AddSiteDialogFragment newInstance() {
        return new AddSiteDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //todo: handle rotation
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_site, null);

        String title = getString(R.string.create_site);

        final EditText siteNameView = (EditText) rootView.findViewById(R.id.edittext_site_name);
        siteNameView.setHint(R.string.hint_site_name);

        String positiveButtonText = getString(R.string.button_yes);
        String negativeButtonText = getString(R.string.button_no);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(rootView)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String siteName = siteNameView.getText().toString();

                        if(!TextUtils.isEmpty(siteName))
                            addSite(siteName);
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    private void addSite(String siteName) {
        // Create new site at /sites/$siteid
        DatabaseReference databaseSitesRef = FirebaseDatabase.getInstance()
                .getReference()
                .child(Firebase.NODE_SITES);

        String key = databaseSitesRef.push().getKey();
        Site site = new Site(siteName, "Anonymous");
        Map<String, Object> postValues = site.toMap();

        databaseSitesRef.child(key).setValue(postValues);
    }
}




















