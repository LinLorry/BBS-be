package cn.edu.ncu.topic;
import cn.edu.ncu.topic.model.TopTopic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopTopicServiceTest {
    @Autowired
    private TopTopicService topTopicService;
    @Test
    public void add() {
        TopTopic topTopic=new TopTopic();
        topTopic.setTopicId(2L);
        topTopicService.add(topTopic);
        System.out.println(topTopic);
    }

    @Test
    @Transactional
    public void deleteTopTopicByTopicId() {
        topTopicService.deleteTopTopicByTopicId(1L);
    }

    @Test
    public void findAll() {
        List<TopTopic> topTopics=topTopicService.findAll();
        for(TopTopic topTopic:topTopics){
            System.out.println(topTopic);
        }
    }

    @Test
    public void findByTopicId() {
        TopTopic topTopic=topTopicService.findByTopicId(1L);
        assertEquals(topTopic.getTopicId(),new Long(1));
    }
}
