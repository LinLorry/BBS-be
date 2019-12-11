package cn.edu.ncu.comment;

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
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void add() {
        Topic topic = topicRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        Comment comment = new Comment();
        comment.setTopic(topic);
        comment.setUser(user);
        comment.setLocation(commentService.getNextLocationByTopicId(1L));

        comment.setContent(RandomString.make());
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));

        commentService.add(comment);

        assertEquals(comment.getTopic().getId(), topic.getId());
        assertEquals(comment.getUser().getId(), user.getId());
        System.out.println(comment);
    }

    @Test
    public void loadAllByTopic() {
        Topic topic = topicRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        Page<Comment> comments = commentService.loadAllByTopic(topic, 0);

        assertNotNull(comments);
        comments.forEach(System.out::println);
    }

    @Test
    public void getNextLocationByTopicId() {
        int result = commentService.getNextLocationByTopicId(1L);
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void checkByKey() {
        assertTrue(commentService.checkByKey(new CommentKey(1, 1L)));
    }
}
