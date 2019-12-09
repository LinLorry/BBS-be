package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {
}
