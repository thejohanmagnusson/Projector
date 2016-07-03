package android.johanmagnusson.se.projector.views.sites;

import android.content.Context;
import android.johanmagnusson.se.projector.R;
import android.johanmagnusson.se.projector.constant.Firebase;
import android.johanmagnusson.se.projector.model.Site;
import android.johanmagnusson.se.projector.viewholder.SiteViewHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SiteListFragment extends Fragment {

    public interface SiteListListener {
        public void onSiteSelected(String siteKey);
    }

    private static final String TAG = SiteListFragment.class.getSimpleName();

    private SiteListListener mListener;

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Site, SiteViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;

    public SiteListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_site_list, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) root.findViewById(R.id.site_list);
        mRecyclerView.setHasFixedSize(true);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mManager);

        Query siteQuery = mDatabase.child(Firebase.NODE_SITES);

        mAdapter = new FirebaseRecyclerAdapter<Site, SiteViewHolder>(Site.class, R.layout.site_item, SiteViewHolder.class, siteQuery) {
            @Override
            protected void populateViewHolder(SiteViewHolder viewHolder, final Site model, int position) {
                final DatabaseReference ref = getRef(position);

                viewHolder.bindToPost(model);

                // Item click listener
                final String siteKey = ref.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onSiteSelected(siteKey);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the Listener so we can send events to the host
            mListener = (SiteListListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement SiteListListener");
        }
    }
}
