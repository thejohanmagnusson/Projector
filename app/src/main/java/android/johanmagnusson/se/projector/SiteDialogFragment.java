package android.johanmagnusson.se.projector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SiteDialogFragment extends DialogFragment {

    public interface SiteDialogListener {
        public void onDialogPositiveClick(String siteName);
    }

    SiteDialogListener mListener;

    EditText mSiteNameView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_site, null);
        mSiteNameView = (EditText) rootView.findViewById(R.id.edit_text_site_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Create site")
                .setView(rootView)
                .setPositiveButton("Looks good", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String siteName = mSiteNameView.getText().toString();
                        mListener.onDialogPositiveClick(siteName);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the Listener so we can send events to the host
            mListener = (SiteDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }
}



















