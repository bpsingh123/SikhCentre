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
import android.widget.Button;

import com.sikhcentre.R;
import com.sikhcentre.adapters.TopicListAdapter;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.FragmentUtils;
import com.sikhcentre.viewmodel.SearchViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by brinder.singh on 28/01/17.
 */

public class TopicListFragment extends BaseFragment {
    private TopicListAdapter topicListAdapter;

    @NonNull
    private CompositeDisposable subscription;

    @NonNull
    private SearchViewModel searchViewModel;

    @NonNull
    ISchedulerProvider schedulerProvider = MainSchedulerProvider.INSTANCE;

    private Button filterButton;

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicListFragment.class);

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
                searchViewModel.handleSelectedTopic(topicListAdapter.getTopicAt(pos));
            }
        });
        topicRecyclerView.setAdapter(topicListAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(topicRecyclerView.getContext(),
                layoutManager.getOrientation());
        topicRecyclerView.addItemDecoration(dividerItemDecoration);

        filterButton = (Button) getView().findViewById(R.id.topic_list_filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.replaceFragment(R.id.container_framelayout, new FilterFragment(),
                        getActivity().getIntent().getExtras(), getActivity().getSupportFragmentManager(), FragmentUtils.FragmentTag.FILTER);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        LOGGER.debug("onResume");
        bind();
        searchViewModel.handleSearchTopic("");
    }

    @Override
    public void onPause() {
        super.onPause();
        LOGGER.debug("onPause");
        unBind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bind() {
        LOGGER.debug("bind");
        unBind();
        subscription = new CompositeDisposable();

        subscription.add(searchViewModel.getTopicListSubjectAsObservable()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<List<Topic>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Topic> topics) throws Exception {
                        topicListAdapter.setTopicList(topics);
                    }

                }));
    }

    private void unBind() {
        LOGGER.debug("unBind");
        if (subscription != null) {
            subscription.dispose();
        }
    }
}
