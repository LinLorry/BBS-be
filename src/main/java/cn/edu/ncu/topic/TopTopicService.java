package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.TopTopic;
import cn.edu.ncu.topic.rep.TopTopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class TopTopicService {
    private final TopTopicRepository topTopicRepository;
    public TopTopicService(TopTopicRepository topTopicRepository) {
        this.topTopicRepository = topTopicRepository;
    }

    /**
     * 添加置顶
     * @param topTopic
     * @return 返回加精帖子本身
     */
    TopTopic add(TopTopic topTopic){
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
