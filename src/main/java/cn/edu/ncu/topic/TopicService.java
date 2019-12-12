package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.SpecificationUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TopicService {
    private final TopicRepository topicRepository;
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    /*
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
*/
    Topic add(Topic topic){
        return topicRepository.save(topic);
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
     *
     * @param pageNumber
     * @return
     */
    Page<Topic>findAll(Integer pageNumber) {
        return topicRepository.findAll(PageRequest.of(pageNumber,20));
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


    /**
     * 模糊查询分页查询?
     * @param equalMap
     * @param likeMap
     * @return
*/
    Page<Topic> load(Map<String, Object> equalMap,
                     Map<String, Object> likeMap, Integer pageNumber) {
        SpecificationUtil specificationUtil = new SpecificationUtil();
        specificationUtil.addEqualMap(equalMap);
        specificationUtil.addLikeMap(likeMap);
        Specification<Topic> specification = specificationUtil.getSpecification();
        return topicRepository.findAll(specification, PageRequest.of(pageNumber, 20));
    }


}
