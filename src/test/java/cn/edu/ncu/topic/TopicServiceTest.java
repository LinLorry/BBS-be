package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void addTopic() {
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title","标题");
        jsonObject.put("content","内容");
        jsonObject.put("time","2019-12-12 12:12:00");
        jsonObject.put("boutique",false);

        topicService.addTopic(jsonObject,user,null);
    }

    @Test
    public void deleteById() {
        topicService.deleteById(1L);
    }

    @Test
    @Transactional
    public void findAll() {
        List<Topic> topics = topicService.findAll();
        for (Topic topic:topics) {
            System.out.println(topic);
        }
    }

    @Test
    public void loadTopicById() {
        Topic topic=topicService.loadTopicById(1L);
    }
}