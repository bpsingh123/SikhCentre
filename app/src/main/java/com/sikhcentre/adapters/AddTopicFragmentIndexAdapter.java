package com.sikhcentre.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.models.IndexModel;

/**
 * Created by brinder.singh on 15/04/17.
 */

public class AddTopicFragmentIndexAdapter extends BaseExpandableListAdapter {
    private IndexModel indexModel;
    private Context context;

    public AddTopicFragmentIndexAdapter(Context context, IndexModel indexModel) {
        this.indexModel = indexModel;
        this.context = context;
    }

    public void setIndexModel(IndexModel indexModel) {
        this.indexModel = indexModel;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return indexModel.getIndexKeys().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return indexModel.getIndexValSize(i);
    }

    @Override
    public Object getGroup(int i) {
        return indexModel.getIndexKeyString(i);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return indexModel.getIndexValueObject(listPosition, expandedListPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.add_topic_index_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.addTopicIndexGroupTitleTV);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.add_topic_index_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.addTopicIndexItemTitleTV);
        expandedListTextView.setText(indexModel.getIndexValueString(listPosition, expandedListPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
