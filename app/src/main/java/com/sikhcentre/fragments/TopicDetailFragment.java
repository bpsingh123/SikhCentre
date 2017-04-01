package com.sikhcentre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.viewmodel.TopicDetailViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by brinder.singh on 28/03/17.
 */

public class TopicDetailFragment extends BaseFragment {
    public static final String TOPIC = "topic";
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicDetailFragment.class);

    private TableLayout tableLayoutAuthor;
    private TableLayout tableLayoutRelatedTopics;
    private TableLayout tableLayoutSimilarTopics;
    private TableLayout tableLayoutReferences;
    private TopicDetailViewModel topicDetailViewModel;
    private LinearLayout linearLayoutFileView;
    private android.widget.Button openFileButton;
    private Button downloadFileButton;
    private Button deleteFileButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic_detail_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Topic topic = getArguments().getParcelable(TOPIC);
        topicDetailViewModel = new TopicDetailViewModel(getActivity(), topic, R.id.toolbar_audio);
        setUpViews(topic);
        setHasOptionsMenu(true);
    }

    private void setUpViews(Topic topic) {
        ((TextView) getView().findViewById(R.id.topicDetailTextViewTitle)).setText(topic.getTitle());
        tableLayoutAuthor = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutAuthor);
        tableLayoutRelatedTopics = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutRelatedTopics);
        tableLayoutSimilarTopics = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutSimilarTopics);
        tableLayoutReferences = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutReferences);

        topicDetailViewModel.handleAuthorPopulation(getActivity(), tableLayoutAuthor,
                getView().findViewById(R.id.topicDetailViewCardViewAuthor));
        topicDetailViewModel.handleRelatedTopicsPopulation(getActivity(), tableLayoutRelatedTopics,
                getView().findViewById(R.id.topicDetailViewCardViewRelatedTopics));
        topicDetailViewModel.handleSimilarTopicsPopulation(getActivity(), tableLayoutSimilarTopics,
                getView().findViewById(R.id.topicDetailViewCardViewSimilarTopics));
        topicDetailViewModel.handleReferencesPopulation(getActivity(), tableLayoutReferences,
                getView().findViewById(R.id.topicDetailViewCardViewReferences));

        linearLayoutFileView = (LinearLayout) getView().findViewById(R.id.topicDetailLLFileView);
        openFileButton = (Button) getView().findViewById(R.id.topicDetailOpenFile);
        downloadFileButton = (Button) getView().findViewById(R.id.topicDetailDownloadFile);
        deleteFileButton = (Button) getView().findViewById(R.id.topicDetailDeleteFile);

        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicDetailViewModel.openTopic();
            }
        });

        downloadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicDetailViewModel.downloadTopic(openFileButton, downloadFileButton, deleteFileButton);
            }
        });

        deleteFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicDetailViewModel.deleteTopic(openFileButton, downloadFileButton, deleteFileButton);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        topicDetailViewModel.handleTopic(getActivity(), linearLayoutFileView, openFileButton,
                downloadFileButton, deleteFileButton);
        bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void bind() {
        LOGGER.debug("bind");
        topicDetailViewModel.bind();

    }

    private void unBind() {
        LOGGER.debug("unBind");
        topicDetailViewModel.unBind();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_item_action_search).setVisible(false);
    }
}
