package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.user.model.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TopicService {
    private final TopicRepository topicRepository;


    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    /**
     * Add Topic
     * @param json have the topic data json.
     * @param user the publish user.
     * @return the topic have been created.
     */
    Topic addTopic(JSONObject json, User user, Demand demand) {
        Topic topic = new Topic();

        topic.setTitle(json.getString("title"));
        topic.setContent(json.getString("content"));
        topic.setCreateTime(json.getTimestamp("time"));
        topic.setBoutique(json.getBoolean("boutique"));//加精
        topic.setDemand(demand);//奖赏
        topic.setCreateUser(user);

        topicRepository.save(topic);
        return topic;
    }

    /**
     * Delete Topic by id
     * @param id the topic id
     * @throws EmptyResultDataAccessException if topic doesn't exist, throw this exception
     */
    void deleteById(Long id) throws EmptyResultDataAccessException {
        topicRepository.deleteById(id);
    }



    /**
     * get the Topic by id
     * @param id the topic id.
     * @return topic which id is param.
     * @throws NoSuchElementException if topic doesn't exist, throw this exception.
     */
    public Topic loadTopicById(long id) throws NoSuchElementException {
        return topicRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
