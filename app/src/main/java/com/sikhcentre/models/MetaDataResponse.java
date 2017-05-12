package com.sikhcentre.models;

import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.RelatedTopic;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicTag;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by brinder.singh on 21/03/17.
 */
@Getter
public class MetaDataResponse {
    private List<TopicResponse> topics = new ArrayList<>();
    private List<AuthorResponse> authors = new ArrayList<>();
    private List<TagResponse> tags = new ArrayList<>();

    @Getter
    @Setter
    public static class TopicResponse {
        private Long id;
        private String title;
        private String url;
        private String info;
        private String content;
        private Topic.TopicType type;

        public TopicResponse() {
        }

        public TopicResponse(Long id, String title, String url, String info, String content, Topic.TopicType type) {
            this.id = id;
            this.title = title;
            this.url = url;
            this.info = info;
            this.content = content;
            this.type = type;
        }

        private List<Long> authorIds = new ArrayList<>();
        private List<TopicTagResponse> tags = new ArrayList<>();
        private List<RelatedTopicResponse> relatedTopics = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class RelatedTopicResponse {
        private Long topicId;
        private Double weight;

        public RelatedTopicResponse() {
        }

        public RelatedTopicResponse(Long topicId, Double weight) {
            this.topicId = topicId;
            this.weight = weight;
        }
    }

    @Getter
    @Setter
    public static class TopicTagResponse {
        private Long tagId;
        private Double weight;

        public TopicTagResponse() {
        }

        public TopicTagResponse(Long tagId, Double weight) {
            this.tagId = tagId;
            this.weight = weight;
        }
    }

    @Getter
    @Setter
    public static class AuthorResponse {
        private Long id;
        private String name;

        public AuthorResponse() {
        }

        public AuthorResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class TagResponse {
        private Long id;
        private String name;

        public TagResponse() {
        }

        public TagResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public MetaDataResponse() {
    }

    public MetaDataResponse(List<Topic> topicList, List<Tag> tagList, List<Author> authorList) {

        for (Topic topic : topicList) {
            TopicResponse topicResponse = new TopicResponse(topic.getId(), topic.getTitle(),
                    topic.getUrl(), topic.getInfo(), topic.getContent(), topic.getTopicType());
            for (Author author : topic.getAuthors()) {
                topicResponse.getAuthorIds().add(author.getId());
            }
            for (TopicTag topicTag : topic.getTopicTags()) {
                topicResponse.getTags().add(new TopicTagResponse(topicTag.getTagId(), topicTag.getWeight()));
            }
            for (RelatedTopic relatedTopic : topic.getRelatedTopics()) {
                topicResponse.getRelatedTopics().add(new RelatedTopicResponse(relatedTopic.getId(), relatedTopic.getWeight()));
            }
            topics.add(topicResponse);
        }

        for (Author author : authorList) {
            authors.add(new AuthorResponse(author.getId(), author.getName()));
        }

        for (Tag tag : tagList) {
            tags.add(new TagResponse(tag.getId(), tag.getName()));
        }
    }

}
