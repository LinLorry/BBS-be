package cn.edu.ncu.comment;

import cn.edu.ncu.comment.model.Comment;
import cn.edu.ncu.comment.model.CommentKey;
import cn.edu.ncu.comment.rep.CommentRepository;
import cn.edu.ncu.topic.model.Topic;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
public class CommentService {

    private EntityManager entityManager;

    private final CommentRepository commentRepository;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Add Comment Service
     * @param comment the comment will be add.
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "commentArrayCache", allEntries = true),
                    @CacheEvict(value = "topicMaxLocationCache", key = "#comment.topic.id")
            }
    )
    public void add(Comment comment) {
        entityManager.persist(comment);
    }

    /**
     * Load All Comments Of Topic
     * @param topic the topic.
     * @param pageNumber the page number.
     * @return the result of comments.
     */
    @Cacheable(value = "commentArrayCache", key = "#topic.id + #pageNumber")
    public Page<Comment> loadAllByTopic(Topic topic, Integer pageNumber) {
        return commentRepository.findAllByTopic(topic, PageRequest.of(pageNumber, 20));
    }

    /**
     * Get The Next Location Of Topic Comment
     * @param topicId the topic id.
     * @return the max location.
     */
    @Cacheable(value = "topicMaxLocationCache", key = "#topicId")
    public int getNextLocationByTopicId(Long topicId) {
        return Optional.ofNullable(commentRepository
                .getMaxLocationByTopicId(topicId))
                .orElse(0) + 1;
    }

    /**
     * Check Comment Exist by Comment Key
     * @param commentKey the comment key.
     * @return if exist return true, else return false.
     */
    @Cacheable(value = "commentExist", key = "#commentKey")
    public boolean checkByKey(CommentKey commentKey) {
        return commentRepository.existsById(commentKey);
    }
}
