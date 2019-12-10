package cn.edu.ncu.comment.rep;

import cn.edu.ncu.comment.model.Comment;
import cn.edu.ncu.comment.model.CommentKey;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void save() {
        Topic topic = topicRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        Comment comment = new Comment();
        comment.setTopic(topic);
        comment.setUser(user);
        comment.setLocation(commentRepository.getMaxLocationByTopicId(1L) + 1);

        comment.setContent(RandomString.make());
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));

        entityManager.persist(comment);
        entityManager.flush();

        assertEquals(comment.getTopic().getId(), topic.getId());
        assertEquals(comment.getUser().getId(), user.getId());
        System.out.println(comment);
    }

    @Test
    public void findById() {
        CommentKey commentKey = new CommentKey(1, 1L);
        Comment comment = commentRepository.findById(commentKey).orElseThrow(NoSuchElementException::new);

        assertEquals(comment.getLocation(), new Integer(1));
        assertEquals(comment.getTopic().getId(), new Long(1));
        assertEquals(comment.getUser().getId(), new Long(1));

        System.out.println(comment);
    }

    @Test
    public void findAll() {
        Iterable<Comment> iterator = commentRepository.findAll();

        assertNotNull(iterator);
        iterator.forEach(System.out::println);
    }

    @Test
    public void getMaxLocationByTopicId() {
        Integer result = commentRepository.getMaxLocationByTopicId(1L);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void findAllByTopic() {
        Topic topic = new Topic();
        topic.setId(1L);

        Page<Comment> comments = commentRepository.findAllByTopic(topic, PageRequest.of(0, 20));

        assertNotNull(comments);
        comments.forEach(System.out::println);
    }
}
