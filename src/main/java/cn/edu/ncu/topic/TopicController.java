package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.util.SecurityUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * User Controller
 * @author LYW
 * @author 2354640364@qq.com
 */
@RestController
@RequestMapping("/api/topic")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Create Topic Api
     * @param request {
     *      "title": String, not null
     *      "content": String, not null
     * }
     * @return if create topic success return {
     *     "status": 1,
     *     "message": "Create Topic Success."
     *     "data": {
     *         topic data
     *     }
     * } else return {
     *     "status": 0,
     *     "message": "message"
     * }
     */
    @PutMapping
    public JSONObject create(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        String title = Optional.ofNullable(
                request.getString("title")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "title", "String"
        ));
        String content = Optional.ofNullable(
                request.getString("content")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "content", "String"
        ));

        Topic topic = new Topic();

        topic.setTitle(title);
        topic.setContent(content);
        topic.setCreateUser(SecurityUtil.getUser());
        topic.setCreateTime(new Timestamp(System.currentTimeMillis()));

        response.put("data", topicService.addOrUpdate(topic));
        response.put("status", 1);
        response.put("message", "Create Topic Success.");

        return  response;
    }

    @DeleteMapping
    public JSONObject delete(@RequestParam Long id){
        JSONObject response=new JSONObject();
        try{
            topicService.deleteById(id);
            response.put("status",1);
            response.put("message","Delete topic success");
        }catch (EmptyResultDataAccessException e){
            response.put("status",0);
            response.put("message","The topic isn't exist");
        }
        return response;
    }

    @GetMapping("/find")
    public JSONObject find(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(defaultValue = "0")Integer pageNumber){
        JSONObject response=new JSONObject();
        Map<String,Object> equalMap=new HashMap<>();
        Map<String,Object> likeMap=new HashMap<>();
        if(id !=null){
            equalMap.put("id",id);
        }
        if(title !=null){
            likeMap.put("title",title);
        }
        if(content !=null){
            likeMap.put("context",content);
        }
        Page<Topic> topics=topicService.load(equalMap,likeMap,pageNumber);
        JSONObject data = new JSONObject();
        data.put("total", topics.getTotalPages());
        data.put("works", topics.getContent());

        response.put("data", data);
        response.put("status",1);
        response.put("message","Get Topic success");

        return response;
    }
}
