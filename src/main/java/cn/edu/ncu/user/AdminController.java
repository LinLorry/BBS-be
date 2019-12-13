package cn.edu.ncu.user;

import cn.edu.ncu.topic.TopicService;
import cn.edu.ncu.topic.model.Topic;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final TopicService topicService;
    public AdminController(TopicService topicService) {
        this.topicService = topicService;
    }

    @ResponseBody
    @DeleteMapping
    public JSONObject deleteTopic(@RequestParam long id){
        JSONObject response =new JSONObject();
        topicService.deleteById(id);
        response.put("status",1);
        response.put("message","Delete meeting success.");

        return  response;
    }
   


}
