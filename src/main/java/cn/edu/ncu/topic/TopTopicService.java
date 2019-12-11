package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.TopTopic;
import cn.edu.ncu.topic.rep.TopTopicRepository;
import cn.edu.ncu.topic.rep.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        TopTopic temp=findByTopicId(topTopic.getTopicId());
        if(temp==null){
            throw new NoSuchElementException();
        }
         return topTopicRepository.save(topTopic);
    }

    /**
     *删除帖子
     * @param topicId
     */
    void deleteTopTopicByTopicId(Long topicId){
       topTopicRepository.deleteTopTopicByTopicId(topicId);
    }

    /**
     *
     * @return返回一个List
     */
    List<TopTopic> findAll(){
        return  topTopicRepository.findAll();
    }

    /**
     * 通过Id查询加精帖子
     * @param id
     * @return
     */
    TopTopic findByTopicId(Long id){
        return topTopicRepository.findByTopicId(id)
                .orElseThrow(NoSuchElementException::new);
    }


}
