package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.SecurityUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

/**
 * User Controller
 * @author LYW
 * @author 2354640364@qq.com
 */
@RestController
@RequestMapping("/api/topic")
public class TopicController {
    private final Log logger = LogFactory.getLog(this.getClass());

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }


    /**
     * Create Topic Api
     * @param request {
     *      "title": String, not null
     *      "content": String, not null
     *      "time": Timestamp, not null
     *      "comment": String
     *      "demand": Demand
     *      "user": User
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
    /*
    @ResponseBody
    @PostMapping("/create")
    public JSONObject create(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        User user = SecurityUtil.getUser();
        Demand demand = new Demand();

        try {
            response.put("data", topicService.addTopic(request, user, demand));
            response.put("status", 1);
            response.put("message", "Create Topic Success.");
        } catch (Exception e) {
            response.put("status", 0);
            if (request.getString("title") == null) {
                response.put("message", "title can't be null.");
            } else if (request.getString("content") == null) {
                response.put("message", "content can't be null.");
            } else {
                logger.error(e);
                response.put("message", "Create Topic Failed.");
            }
        }

        return response;
    }
*/

    @ResponseBody
    @PostMapping("/create")
    public JSONObject create(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();
        User user = SecurityUtil.getUser();
        //Demand demand = new Demand();
        Topic topic=new Topic();
        String title= Optional.ofNullable(
                request.getString("title")
        ).orElseThrow(()->new MissingServletRequestParameterException(
                "title","String"
        ));
        String content = Optional.ofNullable(
                request.getString("content")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "content", "String"
        ));

        /*
        Long id=request.getLong("id");
        String title=request.getString("title");
        String content=request.getString("content");*/
        LocalDate today = LocalDate.now();
        topic.setCreateTime(Timestamp.valueOf(today.atStartOfDay()));
        topic.setCreateUser(user);
        topic.setContent(content);
        topic.setTitle(title);
        //topic.setId(id);
        //topic.setDemand(demand);
        try{
            response.put("data", topicService.addOrUpdate(topic));
            response.put("status", 1);
            response.put("message", "Create Topic Success.");
        }catch (DataIntegrityViolationException e){
            response.put("status",0);
            response.put("message","The topic already exists");
        }
        /*catch (Exception e) {
            response.put("status", 0);
            if (title == null) {
                response.put("message", "title can't be null.");
            } else if (content == null) {
                response.put("message", "content can't be null.");
            } else {
                logger.error(e);
                response.put("message", "Create Topic Failed.");
            }
        }*/
        return  response;
    }

    @ResponseBody
    @GetMapping("/delete")
    public JSONObject delete(@RequestParam(required = true)Long id){
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

    @ResponseBody
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
