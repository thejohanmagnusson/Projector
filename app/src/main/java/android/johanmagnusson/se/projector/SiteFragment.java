package android.johanmagnusson.se.projector;

import android.johanmagnusson.se.projector.constant.DataKey;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SiteFragment extends Fragment {

    private static final String TAG = SiteFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_site, container, false);

        if(savedInstanceState == null) {

            Bundle args = getArguments();
            String siteKey = args.getString(DataKey.SITE_KEY);

            Toast.makeText(getActivity(), siteKey, Toast.LENGTH_LONG).show();
        }

        return root;
    }
}
