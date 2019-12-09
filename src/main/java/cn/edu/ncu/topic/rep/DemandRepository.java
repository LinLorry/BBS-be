package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.Demand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends CrudRepository<Demand, Long> {
}
