package cn.edu.ncu.comment.model;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * The Comment Model
 * @author lorry
 * @author lin864464995@163.com
 */
@Entity
@IdClass(CommentKey.class)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false)
    private Integer location;

    @Id
    @ManyToOne
    @JsonIgnore
    private Topic topic;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    @Column(nullable = false, updatable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private Timestamp createTime;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return location.equals(comment.location) &&
                topic.getId().equals(comment.topic.getId()) &&
                user.getId().equals(comment.user.getId()) &&
                content.equals(comment.content) &&
                createTime.equals(comment.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, topic, user, content, createTime);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "location=" + location +
                ", topicId=" + topic.getId() +
                ", userId=" + user.getId() +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
