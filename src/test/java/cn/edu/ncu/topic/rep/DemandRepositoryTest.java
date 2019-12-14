package cn.edu.ncu.topic.rep;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.util.TestUtil;
import net.bytebuddy.utility.RandomString;
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
public class DemandRepositoryTest {
    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void save() {
        Topic topic = topicRepository.findById(testUtil.getRandomTopicId()).orElseThrow(NoSuchElementException::new);
        Demand demand = new Demand();
        demand.setTopicId(topic.getId());
        demand.setContent(RandomString.make());

        demand = demandRepository.save(demand);

        assertEquals(demand.getTopicId(), topic.getId());
        System.out.println(demand);
    }

    @Test
    @Transactional
    public void findById() {
        Long id = testUtil.getRandomDemandId();
        Demand demand = demandRepository.findById(id).orElseThrow(NoSuchElementException::new);

        assertEquals(demand.getTopicId(), id);
        System.out.println(demand);
    }

    @Test
    public void findAll() {
        Iterable<Demand> iterator = demandRepository.findAll();

        assertNotNull(iterator);
        iterator.forEach(System.out::println);
    }
}
