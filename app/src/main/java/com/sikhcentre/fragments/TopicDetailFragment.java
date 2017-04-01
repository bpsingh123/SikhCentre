package com.sikhcentre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.media.SikhCentreMediaPlayer;
import com.sikhcentre.viewmodel.TopicDetailViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by brinder.singh on 28/03/17.
 */

public class TopicDetailFragment extends BaseFragment {
    public static final String TOPIC = "topic";
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicDetailFragment.class);

    private Topic topic;
    private TableLayout tableLayoutAuthor;
    private TableLayout tableLayoutRelatedTopics;
    private TableLayout tableLayoutSimilarTopics;
    private TableLayout tableLayoutReferences;
    private TopicDetailViewModel topicDetailViewModel;
    private SikhCentreMediaPlayer sikhCentreMediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic_detail_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        topic = getArguments().getParcelable(TOPIC);
        topicDetailViewModel = TopicDetailViewModel.INSTANCE;
        setUpViews();
        topicDetailViewModel.handleTopic(getActivity(), topic, sikhCentreMediaPlayer);
    }

    private void setUpViews() {
        ((TextView) getView().findViewById(R.id.topicDetailTextViewTitle)).setText(topic.getTitle());
        tableLayoutAuthor = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutAuthor);
        tableLayoutRelatedTopics = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutRelatedTopics);
        tableLayoutSimilarTopics = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutSimilarTopics);
        tableLayoutReferences = (TableLayout) getView().findViewById(R.id.topicDetailTableLayoutReferences);
        topicDetailViewModel.handleAuthorPopulation(getActivity(), tableLayoutAuthor,
                getView().findViewById(R.id.topicDetailViewCardViewAuthor), topic);
        topicDetailViewModel.handleRelatedTopicsPopulation(getActivity(), tableLayoutRelatedTopics,
                getView().findViewById(R.id.topicDetailViewCardViewRelatedTopics), topic);
        topicDetailViewModel.handleSimilarTopicsPopulation(getActivity(), tableLayoutSimilarTopics,
                getView().findViewById(R.id.topicDetailViewCardViewSimilarTopics), topic);
        topicDetailViewModel.handleReferencesPopulation(getActivity(), tableLayoutReferences,
                getView().findViewById(R.id.topicDetailViewCardViewReferences), topic);

        sikhCentreMediaPlayer = new SikhCentreMediaPlayer(getActivity(), R.id.toolbar_audio);
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void bind() {
        LOGGER.debug("bind");
        unBind();
        sikhCentreMediaPlayer.bind();

    }

    private void unBind() {
        LOGGER.debug("unBind");
        sikhCentreMediaPlayer.unbind();
    }
}
