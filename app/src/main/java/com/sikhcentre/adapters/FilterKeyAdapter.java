package com.sikhcentre.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.models.FilterModel;

import java.util.List;

/**
 * Created by brinder.singh on 29/01/17.
 */

public class FilterKeyAdapter extends RecyclerView.Adapter<FilterKeyAdapter.FilterKeyViewHolder> {

    private List<FilterModel> filterModelList;
    private AdapterView.OnItemClickListener onItemClickListener;

    public FilterKeyAdapter(List<FilterModel> filterModelList, AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.filterModelList = filterModelList;
        notifyDataSetChanged();
    }

    @Override
    public FilterKeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_key_list_item, parent, false);
        return new FilterKeyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(FilterKeyViewHolder holder, int position) {
        holder.bindTopic(filterModelList.get(position));
    }

    public FilterModel getFilterAt(int pos) {
        return filterModelList != null && pos < filterModelList.size() ? filterModelList.get(pos) : null;
    }


    @Override
    public int getItemCount() {
        return filterModelList != null ? filterModelList.size() : 0;
    }

    static class FilterKeyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView filterKeyTV;
        private AdapterView.OnItemClickListener onItemClickListener;
        private LinearLayout linearLayoutFilterKeyItem;

        FilterKeyViewHolder(View itemView, AdapterView.OnItemClickListener onItemClickListener) {
            super(itemView);
            filterKeyTV = (TextView) itemView.findViewById(R.id.textViewFilterKey);
            linearLayoutFilterKeyItem = (LinearLayout) itemView.findViewById(R.id.linearLayoutFilterKeyItem);
            linearLayoutFilterKeyItem.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        void bindTopic(FilterModel filterModel) {
            filterKeyTV.setText(filterModel.getKey());
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }
}
