package com.sikhcentre.utils;

import com.sikhcentre.models.FilterSelectionModel;

/**
 * Created by brinder.singh on 14/04/17.
 */
public enum AppInfoProvider {
    INSTANCE;
    private FilterSelectionModel filterSelectionModel;

    public FilterSelectionModel getFilterSelectionModel() {
        if (filterSelectionModel == null) {
            filterSelectionModel = SCSharedPreferenceManager.INSTANCE.getFilterSelectionModel();
        }
        return filterSelectionModel;
    }

    public void setFilterSelectionModel(FilterSelectionModel filterSelectionModel) {
        this.filterSelectionModel = filterSelectionModel == null ? new FilterSelectionModel() : filterSelectionModel;
        SCSharedPreferenceManager.INSTANCE.setFilterSelectionModel(this.filterSelectionModel);
    }
}
