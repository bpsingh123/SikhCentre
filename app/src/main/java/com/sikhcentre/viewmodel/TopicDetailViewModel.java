package com.sikhcentre.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Reference;
import com.sikhcentre.entities.RelatedTopic;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicTag;

import java.util.List;

/**
 * Created by brinder.singh on 29/03/17.
 */

public enum TopicDetailViewModel {
    INSTANCE;

    public void handleAuthorPopulation(Context context, TableLayout tableLayout, View view, Topic topic) {
        if (!shouldHeaderBeVisible(view, topic.getAuthors())) return;
        for (Author author : topic.getAuthors()) {
            tableLayout.addView(addRow(context, author.getName()));
        }
    }

    public void handleRelatedTopicsPopulation(Context context, TableLayout tableLayout, View view, Topic topic) {
        if (!shouldHeaderBeVisible(view, topic.getRelatedTopics())) return;
        for (RelatedTopic relatedTopic : topic.getRelatedTopics()) {
            tableLayout.addView(addRow(context, relatedTopic.getRelatedTopic().getTitle()));
        }
    }

    public void handleSimilarTopicsPopulation(Context context, TableLayout tableLayout, View view, Topic topic) {
        if (!shouldHeaderBeVisible(view, topic.getTopicTags())) return;
        for (TopicTag topicTag : topic.getTopicTags()) {
            tableLayout.addView(addRow(context, topicTag.getTopic().getTitle()));
        }
    }

    public void handleReferencesPopulation(Context context, TableLayout tableLayout, View view, Topic topic) {
        if (!shouldHeaderBeVisible(view, topic.getReferences())) return;
        for (Reference reference : topic.getReferences()) {
            tableLayout.addView(addRow(context, reference.getName()));
        }
    }


    private boolean shouldHeaderBeVisible(View view, List list) {
        if(list.isEmpty()){
            view.setVisibility(View.GONE);
            return false;
        }else {
            view.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private TableRow addRow(Context context, String value){
        TableRow row = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TextView textView = new TextView(context);
        textView.setText(value);
        row.addView(textView);
        return row;
    }
}
