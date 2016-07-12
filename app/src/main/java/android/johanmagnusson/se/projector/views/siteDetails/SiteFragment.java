package android.johanmagnusson.se.projector.views.siteDetails;

import android.johanmagnusson.se.projector.R;
import android.johanmagnusson.se.projector.constant.DataKey;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SiteFragment extends Fragment {

    private static final String TAG = SiteFragment.class.getSimpleName();

    private String mUserId;
    private String mSiteKey;

    public static SiteFragment newInstance(String userId, String siteKey) {
        Bundle args = new Bundle();
        args.putString(DataKey.USER_ID, userId);
        args.putString(DataKey.SITE_KEY, siteKey);

        SiteFragment siteFragment = new SiteFragment();
        siteFragment.setArguments(args);

        return siteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Bundle args = getArguments();

            if(args != null) {
                mUserId = args.getString(DataKey.USER_ID);
                mSiteKey = args.getString(DataKey.SITE_KEY);
            }
            else {
                mUserId = null;
            }

            Toast.makeText(getActivity(), "Key: " + mSiteKey, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_site, container, false);

        return root;
    }
}
