package android.johanmagnusson.se.projector;

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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SiteListFragment extends Fragment {

    private static final String TAG = SiteListFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Site, SiteViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;

    public SiteListFragment() {
        // Required empty public constructor
    }

    public static SiteListFragment newInstance(String param1, String param2) {
        SiteListFragment fragment = new SiteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        Query siteQuery = mDatabase; // Using root node since we have no child nodes yet
        mAdapter = new FirebaseRecyclerAdapter<Site, SiteViewHolder>(Site.class, R.layout.site_item, SiteViewHolder.class, siteQuery) {
            @Override
            protected void populateViewHolder(SiteViewHolder viewHolder, final Site model, int position) {
                final DatabaseReference ref = getRef(position);

                viewHolder.bindToPost(model);

                // Click listener
                String siteKey = ref.getKey(); //todo: key not used yet
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), model.getName(), Toast.LENGTH_SHORT).show();
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
}
