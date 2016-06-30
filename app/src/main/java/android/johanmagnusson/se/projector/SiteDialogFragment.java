package android.johanmagnusson.se.projector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SiteDialogFragment extends DialogFragment {

    public interface SiteDialogListener {
        public void onDialogPositiveClick(String siteId, String siteName);
    }

    private static final String TAG = SiteListFragment.class.getSimpleName();
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_NAME = "arg_name";

    private SiteDialogListener mListener;

    private EditText mSiteNameView;
    private String mTitle;
    private String mName;

    public static SiteDialogFragment newInstance(String title, String name) {
        SiteDialogFragment fragment = new SiteDialogFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_site, null);
        mSiteNameView = (EditText) rootView.findViewById(R.id.edittext_site_name);

        String positiveButtonText = getString(R.string.positive_button_text);

        if(TextUtils.isEmpty(mName)) {
            mSiteNameView.setHint(R.string.hint_site_name);
        }
        else {
            mSiteNameView.setText(mName);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setView(rootView)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String siteName = mSiteNameView.getText().toString();
                        //todo: Fix, add id
                        mListener.onDialogPositiveClick("", siteName);
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
            throw new ClassCastException(context.toString() + " must implement SiteDialogListener");
        }
    }
}



















