package cn.edu.ncu.topic.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Top Topic Model
 * @author lorry
 * @author lin864464995@163.com
 */
@Entity
public class TopTopic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false)
    private Long topicId;

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "topic_id", referencedColumnName = "id")
    private Topic topic;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Topic getTopic() {
        return topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopTopic)) return false;
        TopTopic topTopic = (TopTopic) o;
        return topicId.equals(topTopic.topicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicId);
    }

    @Override
    public String toString() {
        return "TopTopic{" +
                "topicId=" + topicId +
                '}';
    }
}
