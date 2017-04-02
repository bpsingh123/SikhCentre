package com.sikhcentre.models;

import com.sikhcentre.entities.Topic;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by brinder.singh on 21/03/17.
 */
@Getter
public class MetaDataResponse {
    private List<TopicResponse> topics = new ArrayList<>();
    private List<AuthorResponse> authors = new ArrayList<>();
    private List<TagResponse> tags = new ArrayList<>();

    @Getter
    public static class TopicResponse {
        private Long id;
        private String title;
        private String url;
        private String info;
        private String content;
        private Topic.TopicType type;
        private List<Long> authorIds = new ArrayList<>();
        private List<TopicTagResponse> tags = new ArrayList<>();
        private List<RelatedTopicResponse> relatedTopics = new ArrayList<>();

    }

    @Getter
    public static class RelatedTopicResponse {
        private Long topicId;
        private Double weight;
    }

    @Getter
    public static class TopicTagResponse {
        private Long tagId;
        private Double weight;
    }

    @Getter
    public static class AuthorResponse {
        private Long id;
        private String name;
    }

    @Getter
    public static class TagResponse {
        private Long id;
        private String name;
    }

}
