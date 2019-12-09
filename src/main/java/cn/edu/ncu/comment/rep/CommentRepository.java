package cn.edu.ncu.comment.rep;

import cn.edu.ncu.comment.model.Comment;
import cn.edu.ncu.comment.model.CommentKey;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, CommentKey> {
    Integer countAllByTopicAndUser(Topic topic, User user);
}
