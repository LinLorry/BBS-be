package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.rep.DemandRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DemandService  {

    private final DemandRepository demandRepository;

    public DemandService(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    /**
     * addOrUpdate Demand
     * @param demand addOrUpdate a new demand
     * @return Demand
     */
    @Caching(evict = {
            @CacheEvict(value = "haveDemandTopicArrayCache", allEntries = true),
            @CacheEvict(value = "topicCache", key = "#result.topicId")
    })
    public Demand addOrUpdate(Demand demand) {
        return demandRepository.save(demand);
    }

    /**
     * delete Demand
     * @param topicId the topicId of which you want to delete
     */
    @Caching(evict = {
            @CacheEvict(value = "haveDemandTopicArrayCache", allEntries = true),
            @CacheEvict(value = "topicCache", key = "#result.topicId")
    })
    public void deleteById(Long topicId) {
        demandRepository.deleteById(topicId);
    }

    /**
     * find
     * @param topicId demand's topicId
     * @return demand
     */
    public Demand loadById(Long topicId) {
        return demandRepository.findById(topicId)
                .orElseThrow(NoSuchElementException::new);
    }
}
