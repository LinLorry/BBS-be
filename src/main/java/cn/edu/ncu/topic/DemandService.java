package cn.edu.ncu.topic;


import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.rep.DemandRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public Demand addOrUpdate(Demand demand){
        return demandRepository.save(demand);
    }

    /**
     * delete Demand
     * @param topicId the topicId of which you want to delete
     */

    void deleteDemandByTopicId(Long topicId){
        demandRepository.deleteById(topicId);
    }

    /**
     * find all Demand
     * @return demand list
     */

    List<Demand> findAll(){
        Iterable<Demand> demands = demandRepository.findAll();
        List<Demand> list = new ArrayList<>();
        demands.forEach(list::add);

        return list;
    }

    /**
     * find
     * @param topicId demand's topicId
     * @return demand
     */
    Demand loadById(Long topicId){
        return demandRepository.findById(topicId)
                .orElseThrow(NoSuchElementException::new);
    }


}