package com.sikhcentre.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.sikhcentre.R;
import com.sikhcentre.adapters.TopicListAdapter;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.SikhCentreMediaPlayer;
import com.sikhcentre.viewmodel.SearchViewModel;

import java.util.List;

import rx.Observer;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by brinder.singh on 28/01/17.
 */

public class TopicListFragment extends BaseFragment {
    private TopicListAdapter topicListAdapter;
    private SikhCentreMediaPlayer sikhCentreMediaPlayer;

    @NonNull
    private CompositeSubscription subscription;

    @NonNull
    private SearchViewModel searchViewModel;

    @NonNull
    ISchedulerProvider schedulerProvider = MainSchedulerProvider.INSTANCE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = SearchViewModel.INSTANCE;
        setUpViews();
    }

    private void setUpViews() {
        RecyclerView topicRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_topic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        topicRecyclerView.setLayoutManager(layoutManager);
        topicListAdapter = new TopicListAdapter(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                searchViewModel.handleSelectedTopic(getActivity(), topicListAdapter.getTopicAt(pos));
            }
        });
        topicRecyclerView.setAdapter(topicListAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(topicRecyclerView.getContext(),
                layoutManager.getOrientation());
        topicRecyclerView.addItemDecoration(dividerItemDecoration);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bind() {
        subscription = new CompositeSubscription();

        subscription.add(searchViewModel.getTopicListSubjectAsObservable()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Observer<List<Topic>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<Topic> topics) {
                        topicListAdapter.setTopicList(topics);
                    }
                }));

        subscription.add(searchViewModel.getSelectedTopicSubjectAsObservable()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Observer<Topic>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Topic topic) {

                        switch (topic.getTopicType()) {
                            case AUDIO:
                                sikhCentreMediaPlayer.start(topic);
                                break;
                            default:
                                sikhCentreMediaPlayer.stop();
                        }

                    }
                }));

        sikhCentreMediaPlayer.bind();

    }

    private void unBind() {
        subscription.unsubscribe();
        sikhCentreMediaPlayer.unbind();
    }
}
