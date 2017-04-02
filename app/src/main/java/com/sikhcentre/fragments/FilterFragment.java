package com.sikhcentre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sikhcentre.R;

/**
 * Created by brinder.singh on 02/04/17.
 */

public class FilterFragment extends BaseFragment {
    private RecyclerView recyclerViewKey;
    private RecyclerView recyclerViewVals;
    private Button cancelFilter;
    private Button clearFilter;
    private Button applyFilter;
    private SearchView searchView;

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
        cancelFilter = (Button) getView().findViewById(R.id.filterFragmentBtnCancel);
        clearFilter = (Button) getView().findViewById(R.id.filterFragmentBtnClear);
        applyFilter = (Button) getView().findViewById(R.id.filterFragmentBtnApply);
        searchView = (SearchView) getView().findViewById(R.id.filterFragmentSearch);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
