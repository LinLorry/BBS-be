package cn.edu.ncu.util;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.TopTopic;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.topic.rep.DemandRepository;
import cn.edu.ncu.topic.rep.TopTopicRepository;
import cn.edu.ncu.topic.rep.TopicRepository;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Random;

@Component
public class TestUtil {

    private static final Random random = new Random();

    @Value("${bbs.authentication.name}")
    private String authenticationName;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopTopicRepository topTopicRepository;

    @Autowired
    private DemandRepository demandRepository;

    public HttpHeaders getTokenHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",
                authenticationName + " " + tokenUtil.generateToken(
                        userRepository.findById(1L).orElse(new User())
                )
        );

        return headers;
    }

    public Long getRandomUserId() {
        long count = random.nextLong() % userRepository.count();
        Iterator<User> iterator = userRepository.findAll().iterator();
        while (--count > 0) {
            iterator.next();
        }
        return iterator.next().getId();
    }

    public Long getRandomTopicId() {
        long count = random.nextLong() % topicRepository.count();
        Iterator<Topic> iterator = topicRepository.findAll().iterator();
        while (--count > 0) {
            iterator.next();
        }
        return iterator.next().getId();
    }

    public Long getRandomTopTopicId() {
        long count = random.nextLong() % topTopicRepository.count();
        Iterator<TopTopic> iterator = topTopicRepository.findAll().iterator();
        while (--count > 0) {
            iterator.next();
        }
        return iterator.next().getTopicId();
    }

    public Long getRandomDemandId() {
        long count = random.nextLong() % demandRepository.count();
        Iterator<Demand> iterator = demandRepository.findAll().iterator();
        while (--count > 0) {
            iterator.next();
        }
        return iterator.next().getTopicId();
    }
}
