package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import cn.edu.ncu.util.TestUtil;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void addOrUpdate() {
        Topic topic = new Topic();
        User user = userRepository.findById(testUtil.getRandomUserId()).orElseThrow(NoSuchElementException::new);

        topic.setTitle(RandomString.make());
        topic.setContent(RandomString.make());
        topic.setCreateUser(user);
        topic.setBoutique(false);
        topic.setCreateTime(new Timestamp(System.currentTimeMillis()));

        topicService.addOrUpdate(topic);
        System.out.println(topic);
    }

    @Test
    public void deleteById() {
        topicService.deleteById(testUtil.getRandomTopicId());
    }

    @Test
    public void findAll() {
        Page<Topic> topics = topicService.loadAll(0);

        topics.getContent().forEach(System.out::println);
    }

    @Test
    public void loadTopicById() {
        Topic topic = topicService.loadById(testUtil.getRandomTopicId());
        System.out.println(topic);
    }
}