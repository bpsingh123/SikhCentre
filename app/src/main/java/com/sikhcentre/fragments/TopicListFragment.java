package com.sikhcentre.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sikhcentre.R;
import com.sikhcentre.adapters.TopicListAdapter;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.viewmodel.SearchViewModel;

import java.util.List;

import rx.Observer;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by brinder.singh on 28/01/17.
 */

public class TopicListFragment extends BaseFragment {
    private RecyclerView topicRecyclerView;
    private TopicListAdapter topicListAdapter;

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

        topicRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_topic);
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        topicListAdapter = new TopicListAdapter();
        topicRecyclerView.setAdapter(topicListAdapter);

        bind();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unBind();
        super.onDestroy();
    }

    private void bind() {
        subscription = new CompositeSubscription();

        subscription.add(searchViewModel.getTopicListObservable()
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

    }

    private void unBind() {
        subscription.unsubscribe();
    }
}
