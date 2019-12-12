package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.TopTopic;
import cn.edu.ncu.topic.rep.TopTopicRepository;
import cn.edu.ncu.topic.rep.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TopTopicService {
    private final TopTopicRepository topTopicRepository;

    private final TopicRepository topicRepository;
    public TopTopicService(TopTopicRepository topTopicRepository, TopicRepository topicRepository) {
        this.topTopicRepository = topTopicRepository;
        this.topicRepository = topicRepository;
    }

    /**
     * 添加置顶
     * @param topTopic
     * @return 返回加精帖子本身
     */
    TopTopic add(TopTopic topTopic){
        topicRepository.findById(topTopic.getTopicId())
                .orElseThrow(NoSuchElementException::new);
        return topTopicRepository.save(topTopic);
    }

    /**
     *删除帖子
     * @param topicId
     */
    void deleteByTopicId(Long topicId){
        topTopicRepository.deleteTopTopicByTopicId(topicId);
    }

    /**
     *
     * @return返回一个List
     */
    List<TopTopic> findAll(){
        return  topTopicRepository.findAll();
    }
}
