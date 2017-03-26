package com.sikhcentre.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brinder.singh on 29/01/17.
 */

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicListViewHolder> {

    private List<Topic> topicList;
    private AdapterView.OnItemClickListener onItemClickListener;

    public TopicListAdapter(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        topicList = Collections.emptyList();
    }

    @Override
    public TopicListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_item, parent, false);
        return new TopicListViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(TopicListViewHolder holder, int position) {
        holder.bindTopic(topicList.get(position));
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    public Topic getTopicAt(int pos) {
        return topicList != null && pos < topicList.size() ? topicList.get(pos) : null;
    }


    @Override
    public int getItemCount() {
        return topicList != null ? topicList.size() : 0;
    }

    static class TopicListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageViewType;
        private TextView title;
        private TextView author;
        private TextView tags;
        private LinearLayout linearLayout;
        private static Map<Topic.TopicType, Integer> topicTypeIconMap;
        private AdapterView.OnItemClickListener onItemClickListener;

        static {
            topicTypeIconMap = new HashMap<>();
            topicTypeIconMap.put(Topic.TopicType.IMAGE, R.drawable.ic_photo_blue_900_24dp);
            topicTypeIconMap.put(Topic.TopicType.TEXT, R.drawable.ic_text_format_blue_900_24dp);
            topicTypeIconMap.put(Topic.TopicType.AUDIO, R.drawable.ic_audiotrack_blue_900_24dp);
            topicTypeIconMap.put(Topic.TopicType.VIDEO, R.drawable.ic_music_video_blue_900_24dp);
            topicTypeIconMap.put(Topic.TopicType.PDF, R.drawable.ic_picture_as_pdf_blue_900_24dp);
        }

        TopicListViewHolder(View itemView, AdapterView.OnItemClickListener onItemClickListener) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutItem);
            title = (TextView) itemView.findViewById(R.id.textViewTopicItemTitle);
            author = (TextView) itemView.findViewById(R.id.textViewTopicItemAuthor);
            tags = (TextView) itemView.findViewById(R.id.textViewTopicItemTags);
            imageViewType = (ImageView) itemView.findViewById(R.id.imageViewItemType);
            linearLayout.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        void bindTopic(Topic topic) {
            title.setText(topic.getTitle());
            author.setText(topic.getAuthorString());
//            tags.setText(topic.getTagString());
            imageViewType.setImageResource(topicTypeIconMap.get(topic.getTopicType()));
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }
}
