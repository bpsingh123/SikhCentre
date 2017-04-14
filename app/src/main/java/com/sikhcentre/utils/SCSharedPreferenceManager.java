package com.sikhcentre.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sikhcentre.models.FilterSelectionModel;

/**
 * Created by brinder.singh on 14/04/17.
 */

public enum SCSharedPreferenceManager {
    INSTANCE;
    private static final String FILE_NAME = "app_config";
    private static final String FILTER_KEY = "filter";
    protected SharedPreferences sharedPreferences;

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setFilterSelectionModel(FilterSelectionModel filterSelectionModel) {
        putString(FILTER_KEY, filterSelectionModel.toJSONString());
    }

    public FilterSelectionModel getFilterSelectionModel() {
        return FilterSelectionModel.fromJSON(getString(FILTER_KEY, null));
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

}
