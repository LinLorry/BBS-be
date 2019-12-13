package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
public interface TopicRepository extends CrudRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    Optional<Topic> findById(long id);

    Page<Topic>findAll(Pageable pageable);

    Page<Topic> findAllByBoutiqueIsTrue(Pageable pageable);

    @Query("SELECT topic " +
            "FROM Topic topic " +
            "LEFT JOIN Comment comment ON comment.topic = topic " +
            "GROUP BY (topic.id) " +
            "ORDER BY COUNT(comment) DESC ")
    Page<Topic> findAllOrderByCountComment(Pageable pageable);

    @Query("SELECT topic " +
            "FROM Topic topic " +
            "INNER JOIN Demand demand ON demand.topic = topic " +
            "WHERE demand.winner = null " +
            "ORDER BY topic.id")
    Page<Topic> findAllByDemandExists(Pageable pageable);
}
