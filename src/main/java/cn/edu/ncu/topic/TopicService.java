package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.util.SpecificationUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Caching(
            put = @CachePut(value = "topicCache", key = "#result.id"),
            evict = {
                    @CacheEvict(value = "topicExistCache", key = "#result.id"),
                    @CacheEvict(value = "allTopicArrayCache", allEntries = true),
                    @CacheEvict(value = "boutiqueTopicArrayCache", allEntries = true),
                    @CacheEvict(value = "commentOrderTopicArrayCache", allEntries = true),
                    @CacheEvict(value = "haveDemandTopicArrayCache", allEntries = true)
            }
    )
    public Topic addOrUpdate(Topic topic){
        return topicRepository.save(topic);
    }

    /**
     * Delete Topic by id
     * @param id the topic id
     * @throws EmptyResultDataAccessException if topic doesn't exist, throw this exception
     */
    @Caching(evict = {
            @CacheEvict(value = "topicCache", key = "#id"),
            @CacheEvict(value = "topicExistCache", key = "#id"),
            @CacheEvict(value = "allTopicArrayCache", allEntries = true),
            @CacheEvict(value = "boutiqueTopicArrayCache", allEntries = true),
            @CacheEvict(value = "commentOrderTopicArrayCache", allEntries = true),
            @CacheEvict(value = "haveDemandTopicArrayCache", allEntries = true)
    })
    public void deleteById(Long id) throws EmptyResultDataAccessException {
        topicRepository.deleteById(id);
    }

    @Cacheable(value = "topicExistCache", key = "#id")
    public boolean checkById(Long id) {
        return topicRepository.existsById(id);
    }

    /**
     * get the Topic by id
     * @param id the topic id.
     * @return topic which id is param.
     * @throws NoSuchElementException if topic doesn't exist, throw this exception.
     */
    @Cacheable(value = "topicCache", key = "#id")
    public Topic loadById(Long id) throws NoSuchElementException {
        return loadByIdNoCache(id);
    }

    @CachePut(value = "topicCache", key = "#id")
    public Topic loadByIdNoCache(Long id) {
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

    /**
     * Load All Topic
     * @param pageNumber the page number.
     * @return the page of topics.
     */
    @Cacheable(value = "allTopicArrayCache", key = "#pageNumber")
    public Page<Topic> loadAll(Integer pageNumber) {
        return topicRepository.findAll(PageRequest.of(pageNumber,20));
    }

    @Cacheable(value = "boutiqueTopicArrayCache", key = "#pageNumber")
    public Page<Topic> loadAllBoutique(Integer pageNumber) {
        return topicRepository.findAllByBoutiqueIsTrue(PageRequest.of(pageNumber, 20));
    }

    @Cacheable(value = "commentOrderTopicArrayCache", key = "#pageNumber")
    public Page<Topic> loadAllOrderByCountComment(Integer pageNumber) {
        return topicRepository.findAllOrderByCountComment(PageRequest.of(pageNumber, 20));
    }

    @Cacheable(value = "haveDemandTopicArrayCache", key = "#pageNumber")
    public Page<Topic> loadAllByDemandExists(Integer pageNumber) {
        return topicRepository.findAllByDemandExists(PageRequest.of(pageNumber, 20));
    }
}
