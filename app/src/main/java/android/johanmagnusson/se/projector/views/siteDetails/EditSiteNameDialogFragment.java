package android.johanmagnusson.se.projector.views.siteDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.johanmagnusson.se.projector.R;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.johanmagnusson.se.projector.constant.Firebase;
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

import java.util.HashMap;

public class EditSiteNameDialogFragment extends DialogFragment {

    public static final String TAG = EditSiteNameDialogFragment.class.getSimpleName();

    private EditText mSiteNameView;
    private String mSiteKey;
    private String mSiteName;

    public static EditSiteNameDialogFragment newInstance(String siteKey, String siteName) {
        EditSiteNameDialogFragment fragment = new EditSiteNameDialogFragment();

        Bundle args = new Bundle();
        args.putString(DataKey.SITE_KEY, siteKey);
        args.putString(DataKey.SITE_NAME, siteName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSiteKey = getArguments().getString(DataKey.SITE_KEY);
            mSiteName = getArguments().getString(DataKey.SITE_NAME);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_site, null);
        mSiteNameView = (EditText) rootView.findViewById(R.id.edittext_site_name);

        String positiveButtonText = getString(R.string.button_yes);
        String negativeButtonText = getString(R.string.button_no);

        mSiteNameView.setText(mSiteName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.edit_site_title))
                .setView(rootView)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String siteName = mSiteNameView.getText().toString();

                        if(!TextUtils.isEmpty(siteName))
                            updateSiteName(siteName);
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

    private void updateSiteName(String siteName) {
        // Update site at /sites/$siteid
        DatabaseReference databaseSiteRef = FirebaseDatabase.getInstance()
                .getReference()
                .child(Firebase.NODE_SITES)
                .child(mSiteKey);

        HashMap<String, Object> updatedProperties = new HashMap<>();
        updatedProperties.put(Firebase.PROPERTY_SITE_NAME, siteName);

        databaseSiteRef.updateChildren(updatedProperties);
    }
}



















