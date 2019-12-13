package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.util.SpecificationUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic addOrUpdate(Topic topic){
        return topicRepository.save(topic);
    }

    /**
     * Delete Topic by id
     * @param id the topic id
     * @throws EmptyResultDataAccessException if topic doesn't exist, throw this exception
     */
    public void deleteById(Long id) throws EmptyResultDataAccessException {
        topicRepository.deleteById(id);
    }

    public boolean checkById(Long id) {
        return topicRepository.existsById(id);
    }

    /**
     * Load All Topic
     * @param pageNumber the page number.
     * @return the page of topics.
     */
    Page<Topic> loadAll(Integer pageNumber) {
        return topicRepository.findAll(PageRequest.of(pageNumber,20));
    }

    /**
     * get the Topic by id
     * @param id the topic id.
     * @return topic which id is param.
     * @throws NoSuchElementException if topic doesn't exist, throw this exception.
     */
    public Topic loadById(long id) throws NoSuchElementException {
        return topicRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * 模糊查询分页查询?
     * @param equalMap
     * @param likeMap
     * @return
*/
    public Page<Topic> load(Map<String, Object> equalMap,
                     Map<String, Object> likeMap, Integer pageNumber) {
        SpecificationUtil specificationUtil = new SpecificationUtil();
        specificationUtil.addEqualMap(equalMap);
        specificationUtil.addLikeMap(likeMap);
        Specification<Topic> specification = specificationUtil.getSpecification();
        return topicRepository.findAll(specification, PageRequest.of(pageNumber, 20));
    }

    Page<Topic> loadAllBoutique(Integer pageNumber) {
        return topicRepository.findAllByBoutiqueIsTrue(PageRequest.of(pageNumber, 20));
    }

    Page<Topic> loadAllOrderByCountComment(Integer pageNumber) {
        return topicRepository.findAllOrderByCountComment(PageRequest.of(pageNumber, 20));
    }

    Page<Topic> loadAllByDemandExists(Integer pageNumber) {
        return topicRepository.findAllByDemandExists(PageRequest.of(pageNumber, 20));
    }
}
