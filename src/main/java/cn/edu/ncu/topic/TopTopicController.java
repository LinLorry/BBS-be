package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.TopTopic;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.util.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/topTopic")
public class TopTopicController {

    private final TopTopicService topTopicService;

    public TopTopicController(TopTopicService topTopicService) {
        this.topTopicService = topTopicService;
    }

    
}
