package cn.edu.ncu.comment.rep;

import cn.edu.ncu.comment.model.Comment;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import cn.edu.ncu.util.TestUtil;
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

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void save() {
        Topic topic = topicRepository.findById(testUtil.getRandomTopicId()).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(testUtil.getRandomUserId()).orElseThrow(NoSuchElementException::new);
        Integer location = commentRepository.getMaxLocationByTopicId(topic.getId());

        if (location == null) {
            location = 0;
        }

        Comment comment = new Comment();
        comment.setTopic(topic);
        comment.setUser(user);
        comment.setLocation(location + 1);

        comment.setContent(RandomString.make());
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));

        entityManager.persist(comment);
        entityManager.flush();

        assertEquals(comment.getTopic().getId(), topic.getId());
        assertEquals(comment.getUser().getId(), user.getId());
        System.out.println(comment);
    }

    @Test
    public void getMaxLocationByTopicId() {
        Integer result = commentRepository.getMaxLocationByTopicId(testUtil.getRandomTopicId());
        System.out.println(result);
    }

    @Test
    public void findAllByTopic() {
        Topic topic = new Topic();
        topic.setId(testUtil.getRandomTopicId());

        Page<Comment> comments = commentRepository.findAllByTopic(topic, PageRequest.of(0, 20));

        assertNotNull(comments);
        comments.forEach(System.out::println);
    }
}
