package com.sikhcentre.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sikhcentre.R;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.models.FilterModel;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

/**
 * Created by brinder.singh on 29/01/17.
 */

public class FilterValueAdapter extends RecyclerView.Adapter<FilterValueAdapter.FilterValueViewHolder> {

    private List<Author> authorList;
    private List<Tag> tagList;
    private List<Topic.TopicType> topicTypeList;
    private boolean[] authorSelectStateList;
    private boolean[] tagSelectStateList;
    private boolean[] topicSelectStateTypeList;
    private FilterModel.FilterType currentFilterType;

    @Override
    public FilterValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_value_list_item, parent, false);
        return new FilterValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterValueViewHolder holder, final int position) {
        switch (currentFilterType) {
            case AUTHOR:
                holder.bindTopic(authorList.get(position).getName(), authorSelectStateList[position], currentFilterType);
                break;
            case TAG:
                holder.bindTopic(tagList.get(position).getName(), tagSelectStateList[position], currentFilterType);
                break;
            case TYPE:
                holder.bindTopic(topicTypeList.get(position).toString(), topicSelectStateTypeList[position], currentFilterType);
                break;
        }
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
        authorSelectStateList = getBooleanArrayWithDefault(authorList.size(), false);
        notifyDataSetChanged();
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
        tagSelectStateList = getBooleanArrayWithDefault(tagList.size(), false);
        notifyDataSetChanged();
    }

    public void setTopicTypeList(List<Topic.TopicType> topicTypeList) {
        this.topicTypeList = topicTypeList;
        topicSelectStateTypeList = getBooleanArrayWithDefault(topicTypeList.size(), false);
        notifyDataSetChanged();
    }

    public void setCurrentFilterType(FilterModel.FilterType currentFilterType) {
        this.currentFilterType = currentFilterType;
        notifyDataSetChanged();
    }

    public FilterModel.FilterType getCurrentFilterType() {
        return currentFilterType;
    }

    private boolean[] getBooleanArrayWithDefault(int size, boolean defaultValue) {
        boolean[] booleanArray = new boolean[size];
        Arrays.fill(booleanArray, defaultValue);
        return booleanArray;
    }

    @Override
    public int getItemCount() {
        switch (currentFilterType) {
            case AUTHOR:
                return getListSize(authorList);
            case TAG:
                return getListSize(tagList);
            case TYPE:
                return getListSize(topicTypeList);
        }
        return 0;
    }

    private int getListSize(List list) {
        return list != null ? list.size() : 0;
    }

    @Getter
    class FilterValueViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private CheckBox filterValueCheckbox;
        private FilterModel.FilterType filterType;

        FilterValueViewHolder(View itemView) {
            super(itemView);
            filterValueCheckbox = (CheckBox) itemView.findViewById(R.id.filterValueFragmentCheckBox);
            filterValueCheckbox.setOnCheckedChangeListener(this);
        }

        void bindTopic(String filterText, boolean isChecked, FilterModel.FilterType filterType) {
            this.filterType = filterType;
            filterValueCheckbox.setText(filterText);
            filterValueCheckbox.setChecked(isChecked);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (filterType) {
                case AUTHOR:
                    authorSelectStateList[getAdapterPosition()] = b;
                    break;
                case TAG:
                    tagSelectStateList[getAdapterPosition()] = b;
                    break;
                case TYPE:
                    topicSelectStateTypeList[getAdapterPosition()] = b;
                    break;
            }
        }
    }
}
