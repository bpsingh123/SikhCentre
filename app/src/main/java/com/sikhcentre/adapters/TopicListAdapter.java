package com.sikhcentre.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;

import java.util.Collections;
import java.util.List;

/**
 * Created by brinder.singh on 29/01/17.
 */

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicListViewHolder> {

    private List<Topic> topicList;

    public TopicListAdapter() {
        topicList = Collections.emptyList();
    }

    @Override
    public TopicListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_item, parent, false);
        return new TopicListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicListViewHolder holder, int position) {
        holder.bindTopic(topicList.get(position));
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return topicList != null ? topicList.size() : 0;
    }

    static class TopicListViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        TopicListViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textViewTopicItemTitle);
        }

        void bindTopic(Topic topic) {
            title.setText(topic.getTitle());
        }
    }
}
