package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Topic;
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
import java.time.LocalDate;
import java.util.NoSuchElementException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public  void add(){
        Topic topic=new Topic();
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        topic.setId(10L);
        topic.setTitle(RandomString.make());
        topic.setContent(RandomString.make());
        topic.setCreateUser(user);
        topic.setBoutique(false);
        topic.setDemand(null);
        LocalDate today = LocalDate.now();
        topic.setCreateTime(Timestamp.valueOf(today.atStartOfDay()));
        topicService.addOrUpdate(topic);
        System.out.println(topic);
    }

    @Test
    public void deleteById() {
        topicService.deleteById(1L);
    }

    @Test
    @Transactional
    public void findAll() {
        Integer pageNumber=0;
        Page<Topic> topics = topicService.loadAll(pageNumber);
        for (Topic topic:topics) {
            System.out.println(topic);
        }
    }

    @Test
    public void loadTopicById() {
        Topic topic=topicService.loadById(1L);
    }
}