package cn.edu.ncu.topic;


import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.rep.DemandRepository;
import org.springframework.stereotype.Service;

@Service
public class DemandService  {

    private final DemandRepository demandRepository;

    public DemandService(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    /**
     * add Demand
     * @param demand add a new demand
     * @return Demand
     */

    public Demand add(Demand demand){
        return demandRepository.save(demand);
    }

    /**
     * update Demand
     * @param demand the demand will be update
     * @return demand
     */

    public Demand update(Demand demand){
        return demandRepository.save(demand);
    }

    /**
     * delete Demand
     * @param topicId the topicId of which you want to delete
     */

    void deleteDemandByTpoicId(Long topicId){
        demandRepository.deleteById(topicId);
    }
}
