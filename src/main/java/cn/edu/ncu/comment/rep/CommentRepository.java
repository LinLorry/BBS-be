package cn.edu.ncu.comment.rep;

import cn.edu.ncu.comment.model.Comment;
import cn.edu.ncu.comment.model.CommentKey;
import cn.edu.ncu.topic.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Comment Repository
 * @author lorry
 * @author lin864464995@163.com
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, CommentKey> {
    @Query("SELECT max(comment.location) FROM Comment comment WHERE comment.topic.id = ?1")
    Integer getMaxLocationByTopicId(Long topicId);

    Page<Comment> findAllByTopic(Topic topic, Pageable pageable);
}
