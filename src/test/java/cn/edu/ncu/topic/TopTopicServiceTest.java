package cn.edu.ncu.topic;
import cn.edu.ncu.topic.model.TopTopic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopTopicServiceTest {
    @Autowired
    private TopTopicService topTopicService;

    @Test
    @Transactional
    public void add() {
        TopTopic topTopic=new TopTopic();
        topTopic.setTopicId(3L);

        topTopicService.add(topTopic);

        assertNotNull(topTopic);
        System.out.println(topTopic);
    }

    @Test
    public void deleteByTopicId() {
        topTopicService.deleteByTopicId(1L);
    }

    @Test
    public void findAll() {
        List<TopTopic> topTopics=topTopicService.findAll();

        topTopics.forEach(System.out::println);
    }
}
