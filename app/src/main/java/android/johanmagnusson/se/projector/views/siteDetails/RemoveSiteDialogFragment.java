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
import android.view.LayoutInflater;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RemoveSiteDialogFragment extends DialogFragment{

    public static final String TAG = RemoveSiteDialogFragment.class.getSimpleName();

    private String mSiteKey;

    public static RemoveSiteDialogFragment newInstance(String siteKey) {
        Bundle args = new Bundle();
        args.putString(DataKey.SITE_KEY, siteKey);

        RemoveSiteDialogFragment removeSiteDialogFragment = new RemoveSiteDialogFragment();
        removeSiteDialogFragment.setArguments(args);
        return removeSiteDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Bundle args = getArguments();

            // No need to continue if no args
            if(args == null) {
                dismiss();
                return;
            }

            mSiteKey = args.getString(DataKey.SITE_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String positiveButtonText = getString(R.string.button_yes);
        String negativeButtonText = getString(R.string.button_no);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.remove_site_title))
                .setMessage(getString(R.string.remove_site_message))
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        removeSite();
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

    private void removeSite() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Firebase.NODE_PROJECTS + "/" + mSiteKey, null);
        childUpdates.put("/" + Firebase.NODE_SITES + "/" + mSiteKey, null);

        databaseRef.updateChildren(childUpdates);
    }
}
