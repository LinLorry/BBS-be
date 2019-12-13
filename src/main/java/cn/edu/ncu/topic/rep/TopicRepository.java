package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Topic Repository
 * @author LYW
 * @author 2354640364@qq.com
 * @see org.springframework.data.repository.CrudRepository
 */
@Repository
public interface TopicRepository extends CrudRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {
    Optional<Topic> findById(long id);
    Page<Topic>findAll(Pageable pageable);
    boolean existsById(long id);
    Topic save(Topic topic);

}
