package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.TopTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopTopicRepository extends CrudRepository<TopTopic, Long> {
}
