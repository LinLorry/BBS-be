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
     * @param demand
     * @return
     */

    public Demand add(Demand demand){
        return demandRepository.save(demand);
    }

    /**
     * update Demand
     * @param demand
     * @return
     */

    public Demand update(Demand demand){
        return demandRepository.save(demand);
    }
}
