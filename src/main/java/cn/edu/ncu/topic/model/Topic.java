package cn.edu.ncu.topic.model;

import cn.edu.ncu.user.model.User;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The Topic Model
 * @author lorry
 * @author lin864464995@163.com
 */
@Entity
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Type(type = "text")
    @JsonIgnore
    private String content;

    @ManyToOne(optional = false)
    @JsonIgnore
    private User createUser;

    @Column(nullable = false)
    private Timestamp createTime;

    @Column(nullable = false, columnDefinition = "Boolean default false")
    private Boolean boutique = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", referencedColumnName = "topic_id")
    @JsonIgnore
    private Demand demand;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", referencedColumnName = "topic_id")
    @JsonIgnore
    private TopTopic topTopic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Boolean getBoutique() {
        return boutique;
    }

    public void setBoutique(Boolean boutique) {
        this.boutique = boutique;
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    @JsonAnyGetter
    public Map<String, Object> getInfo() {
        Map<String, Object> map = new HashMap<>();

        map.put("createUserName", createUser.getUsername());
        map.put("createUserId", createUser.getId());
        map.put("top", topTopic != null);

        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic)) return false;
        Topic topic = (Topic) o;
        return id.equals(topic.id) &&
                title.equals(topic.title) &&
                content.equals(topic.content) &&
                createUser.getId().equals(topic.createUser.getId()) &&
                createTime.equals(topic.createTime) &&
                boutique.equals(topic.boutique) &&
                Objects.equals(demand, topic.demand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, createUser, createTime, boutique, demand);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Topic{id=");
        stringBuilder.append(id);
        stringBuilder.append(", title='");
        stringBuilder.append(title);
        stringBuilder.append("\", content='");
        stringBuilder.append(content);
        stringBuilder.append(", createUserId=");
        stringBuilder.append(createUser.getId());
        stringBuilder.append(", createTime=");
        stringBuilder.append(createTime);
        stringBuilder.append(", boutique=");
        stringBuilder.append(boutique);

        stringBuilder.append(", demand=");
        if (demand != null) {
            stringBuilder.append(demand);
        } else {
            stringBuilder.append("null");
        }

        stringBuilder.append('}');

        return stringBuilder.toString();
    }
}
