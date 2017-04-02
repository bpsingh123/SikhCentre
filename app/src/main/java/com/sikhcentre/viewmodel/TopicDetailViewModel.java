package com.sikhcentre.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Reference;
import com.sikhcentre.entities.RelatedTopic;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicTag;
import com.sikhcentre.media.SikhCentreMediaPlayer;
import com.sikhcentre.media.TopicMediaHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by brinder.singh on 29/03/17.
 */

public class TopicDetailViewModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicDetailViewModel.class);
    private TopicMediaHandler topicMediaHandler;
    private Topic topic;
    private SikhCentreMediaPlayer sikhCentreMediaPlayer;

    public TopicDetailViewModel(Activity activity, Topic topic, int toolbarId) {
        this.topic = topic;
        topicMediaHandler = new TopicMediaHandler(topic, activity);
        sikhCentreMediaPlayer = new SikhCentreMediaPlayer(activity, toolbarId);
    }

    public void handleAuthorPopulation(Context context, TableLayout tableLayout, View view) {
        if (!shouldHeaderBeVisible(view, topic.getAuthors())) return;
        for (Author author : topic.getAuthors()) {
            tableLayout.addView(addRow(context, author.getName()));
        }
    }

    public void handleRelatedTopicsPopulation(Context context, TableLayout tableLayout, View view) {
        if (!shouldHeaderBeVisible(view, topic.getRelatedTopics())) return;
        for (RelatedTopic relatedTopic : topic.getRelatedTopics()) {
            tableLayout.addView(addRow(context, relatedTopic.getRelatedTopic().getTitle()));
        }
    }

    public void handleSimilarTopicsPopulation(Context context, TableLayout tableLayout, View view) {
        if (!shouldHeaderBeVisible(view, topic.getTopicTags())) return;
        for (TopicTag topicTag : topic.getTopicTags()) {
            tableLayout.addView(addRow(context, topicTag.getTopic().getTitle()));
        }
    }

    public void handleReferencesPopulation(Context context, TableLayout tableLayout, View view) {
        if (!shouldHeaderBeVisible(view, topic.getReferences())) return;
        for (Reference reference : topic.getReferences()) {
            tableLayout.addView(addRow(context, reference.getName()));
        }
    }


    private boolean shouldHeaderBeVisible(View view, List list) {
        if (list.isEmpty()) {
            view.setVisibility(View.GONE);
            return false;
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private TableRow addRow(Context context, String value) {
        TableRow row = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TextView textView = new TextView(context);
        textView.setText(value);
        row.addView(textView);
        return row;
    }

    public void handleTopic(LinearLayout linearLayoutFileView,
                            Button openFileButton, Button downloadFileButton, Button deleteFileButton) {
        LOGGER.debug("handleTopic: {}", topic.getTopicType());
        switch (topic.getTopicType()) {
            case AUDIO:
                sikhCentreMediaPlayer.start(topic);
                linearLayoutFileView.setVisibility(View.GONE);
                break;
            case PDF:
                setUpFileView(topicMediaHandler, openFileButton, downloadFileButton, deleteFileButton);
                linearLayoutFileView.setVisibility(View.VISIBLE);
            default:
                sikhCentreMediaPlayer.stop();
        }
    }

    private void setUpFileView(TopicMediaHandler topicMediaHandler, Button openFileButton,
                               Button downloadFileButton, Button deleteFileButton) {
        if (topicMediaHandler.isTopicDownloaded()) {
            openFileButton.setVisibility(View.VISIBLE);
            downloadFileButton.setVisibility(View.GONE);
            deleteFileButton.setVisibility(View.VISIBLE);
        } else {
            openFileButton.setVisibility(View.GONE);
            downloadFileButton.setVisibility(View.VISIBLE);
            deleteFileButton.setVisibility(View.GONE);
        }
    }

    public void openTopic() {
        topicMediaHandler.openTopic();
    }

    public void downloadTopic(Button openFileButton,
                              Button downloadFileButton, Button deleteFileButton) {
        topicMediaHandler.downloadTopic();
        setUpFileView(topicMediaHandler, openFileButton, downloadFileButton, deleteFileButton);
    }

    public void deleteTopic(Button openFileButton,
                            Button downloadFileButton, Button deleteFileButton) {
        topicMediaHandler.deleteTopic();
        setUpFileView(topicMediaHandler, openFileButton, downloadFileButton, deleteFileButton);
    }

    public void bind() {
        LOGGER.debug("bind");
        unBind();
        sikhCentreMediaPlayer.bind();

    }

    public void unBind() {
        LOGGER.debug("unBind");
        topicMediaHandler.unBind();
        sikhCentreMediaPlayer.unbind();
    }

}
