package com.sikhcentre.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by brinder.singh on 08/04/17.
 */
@Getter
@Setter
public class FilterModel {

    public enum FilterType {
        AUTHOR("Author"),
        TYPE("Type"),
        TAG("Tag");

        private final String name;

        FilterType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    public FilterModel(String key, boolean isSearchable, FilterType filterType) {
        this.key = key;
        this.isSearchable = isSearchable;
        this.filterType = filterType;
    }

    private String key;
    private boolean isSearchable;
    private FilterType filterType;


}
