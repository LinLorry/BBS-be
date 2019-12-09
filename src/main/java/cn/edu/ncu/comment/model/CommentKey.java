package cn.edu.ncu.comment.model;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;

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

    @Id
    @ManyToOne
    private User user;

    public CommentKey() {
    }

    public CommentKey(Integer location, Long topicId, Long userId) {
        this.location = location;
        topic = new Topic();
        user = new User();

        topic.setId(topicId);
        user.setId(userId);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentKey)) return false;
        CommentKey that = (CommentKey) o;
        return location.equals(that.location) &&
                topic.equals(that.topic) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, topic, user);
    }
}
