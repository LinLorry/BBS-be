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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        Long id=request.getLong("id");
        //Topic topic=(Topic)request.get("topic");
        TopTopic topTopic=new TopTopic();
        topTopic.setTopicId(id);
        try {
            response.put("data", topTopicService.add(topTopic));
            response.put("status", 1);
            response.put("message", "Add Success.");
        } catch (DataIntegrityViolationException e) {
            /*if (topTopicService.findByTopicId(id)!=null) {
                throw e;
            }*/
            response.put("status", 0);
            response.put("message", "The topic has been placed at the top.");
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
    public JSONObject deleteTopTopicByTopicId(@RequestBody JSONObject request){
        JSONObject response = new JSONObject();
        Long id=request.getLong("id");
        try{
            topTopicService.deleteTopTopicByTopicId(id);
            response.put("status", 1);
            response.put("message", "delete toptopic success.");

        }catch (NoSuchElementException e){
            response.put("status", 0);
            response.put("message", "The toptopic isn't exist.");
        }
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

        List<TopTopic>topTopics=topTopicService.findAll();

        List<Topic> topics = new ArrayList<>();

        for (TopTopic topTopic : topTopics) {
            topics.add(topTopic.getTopic());
        }

        response.put("status", 1);
        response.put("message", "Get toptopic success.");
        response.put("data", topics);
        return response;
    }
/*
    @ResponseBody
    @RequestMapping("/findById")
    public JSONObject findById(@RequestParam(required = true)Long id){
        JSONObject response=new JSONObject();

        try{
            TopTopic topTopic=topTopicService.findByTopicId(id);
            response.put("data",topTopic);
            response.put("status",1);
            response.put("message","Find toptopic by id success");

        }catch (NoSuchElementException e){
            response.put("status", 0);
            response.put("message", "The toptopic isn't exist.");
        }
        return response;

    }*/
}
