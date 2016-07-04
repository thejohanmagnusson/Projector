package android.johanmagnusson.se.projector.views.siteDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.johanmagnusson.se.projector.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddProjectDialogFragment extends DialogFragment {

    public static final String TAG = AddProjectDialogFragment.class.getSimpleName();

    public static AddProjectDialogFragment newInstance() { return new AddProjectDialogFragment(); }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_project, null);

        String title = getString(R.string.create_project);

        final EditText projectNameView = (EditText) rootView.findViewById(R.id.edittext_project_name);
        projectNameView.setHint(R.string.hint_project_name);

        final EditText projectNumberView = (EditText) rootView.findViewById(R.id.edittext_project_number);
        projectNumberView.setHint(R.string.hint_project_number);

        String positiveButtonText = getString(R.string.button_yes);
        String negativeButtonText = getString(R.string.button_no);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(rootView)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String projectName = projectNameView.getText().toString();
                        String projectNumber = projectNumberView.getText().toString();

                        if(!TextUtils.isEmpty(projectName) && !TextUtils.isEmpty(projectNumber))
                            addProject(projectName, projectNumber);
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

    private void addProject(String projectName, String projectNumber) {
        // Create new site at /sites/$siteid
//        DatabaseReference databaseSitesRef = FirebaseDatabase.getInstance()
//                .getReference()
//                .child(Firebase.NODE_SITES);
//
//        String key = databaseSitesRef.push().getKey();
//        Site site = new Site(siteName, "Anonymous");
//        Map<String, Object> postValues = site.toMap();
//
//        databaseSitesRef.child(key).setValue(postValues);

        Toast.makeText(getActivity(), projectName + " : " + projectNumber, Toast.LENGTH_LONG).show();
    }
}
