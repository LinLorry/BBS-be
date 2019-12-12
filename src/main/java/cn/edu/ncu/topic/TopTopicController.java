package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.TopTopic;
import cn.edu.ncu.topic.model.Topic;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/topTopic")
public class TopTopicController {

    private final TopTopicService topTopicService;

    public TopTopicController(TopTopicService topTopicService) {
        this.topTopicService = topTopicService;
    }

    /**
     *
     * @param request
     * @return 返回json串，data,status,message
     * @throws MissingServletRequestParameterException
     */
    @ResponseBody
    @RequestMapping("/add")
    public JSONObject add(@RequestBody JSONObject request)throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Long topicId = Optional.ofNullable(request.getLong("id"))
                .orElseThrow(() -> new MissingServletRequestParameterException(
                        "id", "Long"));
        TopTopic topTopic = new TopTopic();
        topTopic.setTopicId(topicId);

        try {
            response.put("data", topTopicService.add(topTopic));
            response.put("status", 1);
            response.put("message", "Add Success.");
        }catch (NoSuchElementException e){
            response.put("status",0);
            response.put("message","The topic isn't exist");
        }

        return response;
    }
    /**
     *
     * @param request
     * @return 返回status,message
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/delete")
    public JSONObject deleteTopTopicByTopicId(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();
        Long topicId = Optional.ofNullable(request.getLong("id"))
                .orElseThrow(() -> new MissingServletRequestParameterException(
                        "id", "Long"));
        topTopicService.deleteTopTopicByTopicId(topicId);
        response.put("status", 1);
        response.put("message", "Delete top topic success.");

        return response;
    }

    /**
     *
     * @return 返回json，包括status,message,data
     */
    @ResponseBody
    @GetMapping("/get")
    public JSONObject find() {
        JSONObject response = new JSONObject();

        List<TopTopic> topTopics = topTopicService.findAll();

        List<Topic> topics = new ArrayList<>();

        for (TopTopic topTopic : topTopics) {
            topics.add(topTopic.getTopic());
        }

        response.put("status", 1);
        response.put("message", "Get top topics success.");
        response.put("data", topics);

        return response;
    }
}