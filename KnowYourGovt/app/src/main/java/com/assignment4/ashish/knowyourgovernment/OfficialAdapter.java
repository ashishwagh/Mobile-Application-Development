package com.assignment4.ashish.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private List<Official> officialsList;
    private MainActivity mainAct;

    public OfficialAdapter(List<Official> empList, MainActivity ma) {
        this.officialsList = empList;
        mainAct = ma;
    }

    @Override
    public OfficialViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_list, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int position) {
        Official off = officialsList.get(position);
        holder.name.setText(off.getName());
        holder.office.setText(off.getOffice());

        if(!off.getParty().isEmpty() && !off.getParty().equalsIgnoreCase("unknown"))
            holder.party.setText("(" + off.getParty()  +")");
        else
            holder.party.setText("(Unknown)");
    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }
}
