package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.NoSuchElementException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemandServiceTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    public void addOrUpdate() {
        Demand demand = new Demand();
        Topic topic = topicRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        demand.setTopicId(1L);
        demand.setTopic(topic);
        demand.setContent(RandomString.make());
        demand.setReward(0);

        demandService.addOrUpdate(demand);
        System.out.println(demand);
    }

    @Test
    public void deleteByTopicId() {
        demandService.deleteById(1L);
    }

    @Test
    public void loadByTopicId(){
        Demand demand = demandService.loadById(1L);

        demandService.loadById(1L);
        System.out.println(demand);
    }
}
