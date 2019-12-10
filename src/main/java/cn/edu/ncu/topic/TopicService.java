package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.user.model.User;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;

public class TopicService {
    private final TopicRepository topicRepository;

    private final SessionFactory sessionFactory;

    public TopicService(TopicRepository topicRepository, EntityManagerFactory factory) {
        this.topicRepository = topicRepository;
        if(factory.unwrap(SessionFactory.class) == null){
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    /**
     * Add Topic
     * @param json have the topic data json.
     * @param user the publish user.
     * @return the topic have been created.
     */
    Topic addTopic(JSONObject json, User user, Demand demand) {
        Topic topic = new Topic();

        topic.setId(json.getLong("id"));
        topic.setTitle(json.getString("title"));
        topic.setContent(json.getString("content"));
        topic.setCreateTime(json.getTimestamp("time"));
        topic.setBoutique(json.getBoolean("boutique"));//加精
        topic.setDemand(demand);//奖赏
        topic.setCreateUser(user);

        topicRepository.save(topic);
        return topic;
    }

}
