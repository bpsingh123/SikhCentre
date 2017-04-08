package com.sikhcentre.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.sikhcentre.R;
import com.sikhcentre.adapters.FilterKeyAdapter;
import com.sikhcentre.adapters.FilterValueAdapter;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.models.FilterModel;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.FragmentUtils;
import com.sikhcentre.utils.UIUtils;
import com.sikhcentre.viewmodel.FilterViewModel;
import com.sikhcentre.viewmodel.SearchViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by brinder.singh on 02/04/17.
 */

public class FilterFragment extends BaseFragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterFragment.class);

    private FilterKeyAdapter filterKeyAdapter;
    private FilterValueAdapter filterValueAdapter;
    private RecyclerView recyclerViewKey;
    private RecyclerView recyclerViewVals;
    private ImageButton cancelFilter;
    private ImageButton clearFilter;
    private ImageButton applyFilter;
    private SearchView searchView;
    private FilterViewModel filterViewModel = FilterViewModel.INSTANCE;
    private SearchViewModel searchViewModel = SearchViewModel.INSTANCE;

    private CompositeDisposable subscription;

    @NonNull
    ISchedulerProvider schedulerProvider = MainSchedulerProvider.INSTANCE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViews();
    }

    private void setUpViews() {
        recyclerViewKey = (RecyclerView) getView().findViewById(R.id.filterFragmentRVFilterKeys);
        recyclerViewVals = (RecyclerView) getView().findViewById(R.id.filterFragmentRVFilterValues);
        cancelFilter = (ImageButton) getView().findViewById(R.id.filterFragmentBtnCancel);
        clearFilter = (ImageButton) getView().findViewById(R.id.filterFragmentBtnClear);
        applyFilter = (ImageButton) getView().findViewById(R.id.filterFragmentBtnApply);
        searchView = (SearchView) getView().findViewById(R.id.filterFragmentSearch);

        cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showToast(getActivity(), "cancel filter");
            }
        });

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showToast(getActivity(), "Clear filter");
            }
        });

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showToast(getActivity(), "APPLY filter");
            }
        });

        recyclerViewKey.setLayoutManager(new LinearLayoutManager(getActivity()));
        filterKeyAdapter = new FilterKeyAdapter(FilterViewModel.getFilterKeys(),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        FilterModel filterModel = filterKeyAdapter.getFilterAt(pos);
                        if (filterModel.isSearchable()) {
                            searchView.setVisibility(View.VISIBLE);
                        } else {
                            searchView.setVisibility(View.GONE);
                        }
                        filterValueAdapter.setCurrentFilterType(filterModel.getFilterType());
                    }
                });
        recyclerViewKey.setAdapter(filterKeyAdapter);

        recyclerViewVals.setLayoutManager(new LinearLayoutManager(getActivity()));
        filterValueAdapter = new FilterValueAdapter();
        recyclerViewVals.setAdapter(filterValueAdapter);
        filterValueAdapter.setTopicTypeList(FilterViewModel.getTopicTypeList());
        filterValueAdapter.setCurrentFilterType(FilterModel.FilterType.AUTHOR);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                handleFilterQuery(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                handleFilterQuery(s);
                return false;
            }
        });

        cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.removeCurrentFragment(FilterFragment.this, getActivity().getSupportFragmentManager());
            }
        });
    }

    private void handleFilterQuery(String text) {
        switch (filterValueAdapter.getCurrentFilterType()) {
            case AUTHOR:
                searchViewModel.handleSearchAuthor(text);
                break;
            case TAG:
                searchViewModel.handleSearchTag(text);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
        searchViewModel.handleSearchAuthor("");
        searchViewModel.handleSearchTag("");
    }

    private void bind() {
        LOGGER.debug("bind");
        unBind();
        subscription = new CompositeDisposable();

        subscription.add(searchViewModel.getAuthorListSubjectAsObservable()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<List<Author>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Author> authorList) throws Exception {
                        filterValueAdapter.setAuthorList(authorList);
                    }

                }));

        subscription.add(searchViewModel.getTagListSubjectAsObservable()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<List<Tag>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Tag> tagList) throws Exception {
                        filterValueAdapter.setTagList(tagList);
                    }

                }));
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void unBind() {
        LOGGER.debug("unBind");
        if (subscription != null) {
            subscription.dispose();
        }
    }
}
