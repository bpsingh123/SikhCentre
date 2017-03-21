package com.sikhcentre.models;

import com.sikhcentre.entities.Topic;

import java.util.List;

/**
 * Created by brinder.singh on 21/03/17.
 */

public class Response {
    private List<TopicResponse> topics;
    private List<AuthorResponse> authors;
    private List<TagResponse> tags;

    public static class TopicResponse {
        private Long id;
        private String title;
        private String url;
        private Topic.TopicType type;
        private List<Long> authorIds;
        private List<Long> tagIds;

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public Topic.TopicType getType() {
            return type;
        }

        public List<Long> getAuthorIds() {
            return authorIds;
        }

        public List<Long> getTagIds() {
            return tagIds;
        }
    }

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

    public static class TagResponse {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public List<TopicResponse> getTopics() {
        return topics;
    }

    public List<AuthorResponse> getAuthors() {
        return authors;
    }

    public List<TagResponse> getTags() {
        return tags;
    }
}
