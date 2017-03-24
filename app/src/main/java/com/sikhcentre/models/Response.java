package com.sikhcentre.models;

import com.sikhcentre.entities.Topic;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by brinder.singh on 21/03/17.
 */
@Getter
public class Response {
    private List<TopicResponse> topics = new ArrayList<>();
    private List<AuthorResponse> authors = new ArrayList<>();
    private List<TagResponse> tags = new ArrayList<>();

    @Getter
    public static class TopicResponse {
        private Long id;
        private String title;
        private String url;
        private Topic.TopicType type;
        private List<Long> authorIds;
        private List<Long> tagIds;

    }

    @Getter
    public static class AuthorResponse {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    @Getter
    public static class TagResponse {
        private Long id;
        private String name;
    }

}
