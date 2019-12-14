package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import cn.edu.ncu.util.TestUtil;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemandServiceTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void addOrUpdate() {
        Demand demand = new Demand();
        Topic topic = topicRepository.findById(testUtil.getRandomTopicId()).orElseThrow(NoSuchElementException::new);

        demand.setTopicId(topic.getId());
        demand.setTopic(topic);
        demand.setContent(RandomString.make());
        demand.setReward(0);

        demandService.addOrUpdate(demand);
        System.out.println(demand);

        User user = userRepository.findById(testUtil.getRandomUserId()).orElseThrow(NoSuchElementException::new);
        demand = demandService.loadById(testUtil.getRandomDemandId());
        demand.setWinner(user);
        demandService.addOrUpdate(demand);
        System.out.println(demand);
    }

    @Test
    public void deleteById() {
        demandService.deleteById(testUtil.getRandomDemandId());
    }

    @Test
    public void loadById(){
        Demand demand = demandService.loadById(testUtil.getRandomDemandId());
        System.out.println(demand);
    }
}
