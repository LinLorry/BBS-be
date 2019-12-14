package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.TopTopic;

import cn.edu.ncu.util.TestUtil;
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

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void add() {
        TopTopic topTopic=new TopTopic();
        Long id = testUtil.getRandomTopicId();

        while (!topTopicService.checkById(id)) id = testUtil.getRandomTopicId();
        topTopic.setTopicId(id);
        topTopicService.add(topTopic);

        assertNotNull(topTopic);
        System.out.println(topTopic);
    }

    @Test
    @Transactional
    public void deleteByTopicId() {
        topTopicService.deleteById(testUtil.getRandomTopTopicId());
    }

    @Test
    public void loadAll() {
        List<TopTopic> topTopics = topTopicService.loadAll();

        topTopics.forEach(System.out::println);
    }
}
