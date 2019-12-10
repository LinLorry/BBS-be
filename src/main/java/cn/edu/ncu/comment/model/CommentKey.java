package cn.edu.ncu.comment.model;

import cn.edu.ncu.topic.model.Topic;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Comment Key
 * @author lorry
 * @author lin864464995@163.com
 */
@Embeddable
public class CommentKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false)
    private Integer location;

    @Id
    @ManyToOne
    private Topic topic;

    public CommentKey() {
    }

    public CommentKey(Integer location, Long topicId) {
        this.location = location;
        this.topic = new Topic();
        this.topic.setId(topicId);
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentKey)) return false;
        CommentKey that = (CommentKey) o;
        return location.equals(that.location) &&
                topic.getId().equals(that.topic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, topic.getId());
    }

    @Override
    public String toString() {
        return "CommentKey{" +
                "location=" + location +
                ", topicId=" + topic.getId() +
                '}';
    }
}
