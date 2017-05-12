package com.sikhcentre.models;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by brinder.singh on 15/04/17.
 */
@Getter
@Setter
public class IndexModel {
    public enum IndexKey {
        TOPIC("Topic"),
        AUTHOR("Author"),
        TAG("Tag");

        private final String name;

        IndexKey(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    private List<IndexKey> indexKeys = Arrays.asList(IndexKey.values());
    private MetaDataResponse metaDataResponse;

    public IndexModel(MetaDataResponse metaDataResponse) {
        this.metaDataResponse = metaDataResponse;
    }

    public IndexModel() {
        this.metaDataResponse = new MetaDataResponse();
    }

    public int getIndexValSize(int indexKeyPos) {
        int size = 0;
        switch (indexKeys.get(indexKeyPos)) {
            case TOPIC:
                size = metaDataResponse.getTopics().size();
                break;
            case TAG:
                size = metaDataResponse.getTags().size();
                break;
            case AUTHOR:
                size = metaDataResponse.getAuthors().size();
                break;
        }
        return size;
    }

    public Object getIndexValueObject(int indexKeyPos, int indexValPos) {
        Object valObject = null;
        switch (indexKeys.get(indexKeyPos)) {
            case TOPIC:
                valObject = metaDataResponse.getTopics().get(indexValPos);
                break;
            case TAG:
                valObject = metaDataResponse.getTags().get(indexValPos);
                break;
            case AUTHOR:
                valObject = metaDataResponse.getAuthors().get(indexValPos);
                break;
        }
        return valObject;
    }

    public String getIndexValueString(int indexKeyPos, int indexValPos) {
        String valString = "";
        switch (indexKeys.get(indexKeyPos)) {
            case TOPIC:
                valString = metaDataResponse.getTopics().get(indexValPos).getTitle();
                break;
            case TAG:
                valString = metaDataResponse.getTags().get(indexValPos).getName();
                break;
            case AUTHOR:
                valString = metaDataResponse.getAuthors().get(indexValPos).getName();
                break;
        }
        return valString;
    }

    public String getIndexKeyString(int indexKeyPos) {
        return indexKeys.get(indexKeyPos).toString();
    }
}
