package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.rep.DemandRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CachePut(value = "demandCache", key = "#demand.topicId")
    public Demand addOrUpdate(Demand demand) {
        return demandRepository.save(demand);
    }

    /**
     * delete Demand
     * @param topicId the topicId of which you want to delete
     */
    @CacheEvict(value = "demandCache", key = "#topicId")
    public void deleteById(Long topicId) {
        demandRepository.deleteById(topicId);
    }

    /**
     * find
     * @param topicId demand's topicId
     * @return demand
     */
    @Cacheable(value = "demandCache", key = "#topicId")
    public Demand loadById(Long topicId) {
        return loadByIdNoCache(topicId);
    }

    @CachePut(value = "demandCache", key = "#topicId")
    public Demand loadByIdNoCache(Long topicId) {
        return demandRepository.findById(topicId)
                .orElseThrow(NoSuchElementException::new);
    }
}
