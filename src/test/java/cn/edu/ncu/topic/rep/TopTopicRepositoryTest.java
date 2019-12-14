package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.TopTopic;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopTopicRepositoryTest {
    @Autowired
    private TopTopicRepository topTopicRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void save() {
        Long id = testUtil.getRandomTopicId();
        while (topTopicRepository.existsById(id)) id = testUtil.getRandomTopicId();

        Topic topic = topicRepository.findById(id).orElseThrow(NoSuchElementException::new);
        TopTopic topTopic = new TopTopic();
        topTopic.setTopicId(topic.getId());

        topTopic = topTopicRepository.save(topTopic);

        assertNotNull(topTopic);
        System.out.println(topTopic);
    }

    @Test
    public void findById() {
        Long id = testUtil.getRandomTopTopicId();
        TopTopic topTopic = topTopicRepository.findById(id).orElseThrow(NoSuchElementException::new);
        assertEquals(topTopic.getTopicId(), id);
        System.out.println(topTopic);
    }

    @Test
    @Transactional
    public void findAll() {
        Iterable<Topic> iterator = topicRepository.findAll();

        assertNotNull(iterator);
        iterator.forEach(System.out::println);
    }
}
