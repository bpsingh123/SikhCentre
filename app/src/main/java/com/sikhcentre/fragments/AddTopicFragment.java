package com.sikhcentre.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.sikhcentre.R;
import com.sikhcentre.adapters.AddTopicFragmentIndexAdapter;
import com.sikhcentre.models.IndexModel;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.viewmodel.AddTopicViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by brinder.singh on 15/04/17.
 */

public class AddTopicFragment extends BaseFragment {
    private CompositeDisposable subscription;
    private AddTopicViewModel addTopicViewModel = AddTopicViewModel.INSTANCE;
    @NonNull
    ISchedulerProvider schedulerProvider = MainSchedulerProvider.INSTANCE;
    private AddTopicFragmentIndexAdapter addTopicFragmentIndexAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_topic_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViews();
        bind();
        addTopicViewModel.handleLoadMetaResponse();
    }

    private void setUpViews() {
        ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.addTopicFragmentRVIndex);
        addTopicFragmentIndexAdapter = new AddTopicFragmentIndexAdapter(getActivity(), new IndexModel());
        expandableListView.setAdapter(addTopicFragmentIndexAdapter);
    }

    private void bind() {
        unBind();
        subscription = new CompositeDisposable();
        subscription.add(addTopicViewModel.getIndexModelPublishSubject()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<IndexModel>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull IndexModel indexModel) throws Exception {
                        addTopicFragmentIndexAdapter.setIndexModel(indexModel);
                    }
                }));

    }

    private void unBind() {
        if (subscription != null) {
            subscription.dispose();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unBind();
    }
}
