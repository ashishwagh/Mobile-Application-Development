package com.assignment4.ashish.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OfficialViewHolder  extends RecyclerView.ViewHolder  {

    public TextView name;
    public TextView party;
    public TextView office;

    public OfficialViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.official_name);
        party = (TextView) view.findViewById(R.id.official_party);
        office = (TextView) view.findViewById(R.id.official_office);
    }
}
