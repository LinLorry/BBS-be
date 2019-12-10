package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Topic Repository
 * @author LYW
 * @author 2354640364@qq.com
 * @see org.springframework.data.repository.CrudRepository
 */
@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {
    Optional<Topic> findById(long id);

    boolean existsById(long id);
}
