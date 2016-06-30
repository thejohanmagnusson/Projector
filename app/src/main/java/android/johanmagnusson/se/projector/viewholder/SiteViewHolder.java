package android.johanmagnusson.se.projector.viewholder;

import android.johanmagnusson.se.projector.R;
import android.johanmagnusson.se.projector.model.Site;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SiteViewHolder extends RecyclerView.ViewHolder {

    public TextView nameView;
    public TextView ownerView;

    public SiteViewHolder(View itemView) {
        super(itemView);

        nameView = (TextView) itemView.findViewById(R.id.site_title);
        ownerView = (TextView) itemView.findViewById(R.id.site_owner);
    }

    public void bindToPost(Site site) {
        nameView.setText(site.getName());
        ownerView.setText(site.getOwner());
    }
}
