package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Before
    public void save() {
        Topic topic = new Topic();
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        topic.setTitle(RandomString.make());
        topic.setContent(RandomString.make());
        topic.setCreateUser(user);
        topic.setCreateTime(new Timestamp(System.currentTimeMillis()));

        topic = topicRepository.save(topic);

        assertNotNull(topic.getId());
        System.out.println(topic);
    }

    @Test
    @Transactional
    public void findById() {
        Topic topic = topicRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        assertEquals(topic.getId(), new Long(1));
        System.out.println(topic);
    }

    @Test
    @Transactional
    public void findAll() {
        Iterable<Topic> iterator = topicRepository.findAll();

        assertNotNull(iterator);
        iterator.forEach(System.out::println);
    }

    @Test
    @Transactional
    public void findAllByCountCommentsMax() {
        Page<Topic> topicPage = topicRepository.findAllOrderByCountComment(PageRequest.of(0, 5));

        assertNotNull(topicPage);

        topicPage.getContent().forEach(System.out::println);
    }

    @Test
    @Transactional
    public void findAllByDemandExists() {
        Page<Topic> topicPage = topicRepository.findAllByDemandExists(PageRequest.of(0, 5));

        assertNotNull(topicPage);
        System.out.println(topicPage.getContent().size());

        topicPage.getContent().forEach(System.out::println);
    }
}
