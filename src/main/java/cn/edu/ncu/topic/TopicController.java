package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.UserService;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.SecurityUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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

    private final UserService userService;

    public TopicController(TopicService topicService, UserService userService) {
        this.topicService = topicService;
        this.userService = userService;
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
        User user = userService.loadByIdNoCache(SecurityUtil.getUserId());

        topic.setTitle(title);
        topic.setContent(content);
        topic.setCreateUser(user);
        topic.setCreateTime(new Timestamp(System.currentTimeMillis()));

        String question = request.getString("question");
        if(question != null) {
            Demand demand = new Demand();
            Integer reward = Optional.ofNullable(
                    request.getInteger("reward")
            ).orElse(0);
            demand.setTopic(topic);
            demand.setReward(reward);
            demand.setContent(question);
            topic.setDemand(demand);
        }

        response.put("data", topicService.addOrUpdate(topic));
        response.put("status", 1);
        response.put("message", "Create Topic Success.");

        return  response;
    }

    @PostMapping
    @Transactional
    public JSONObject update(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        Topic topic = topicService.loadByIdNoCache(
                Optional.ofNullable(
                        request.getLong("id")
                ).orElseThrow(() -> new MissingServletRequestParameterException(
                        "id", "Long"
                ))
        );

        if (topic.getCreateUser().getId().equals(SecurityUtil.getUserId())) {
            Optional.ofNullable(
                    request.getString("title")
            ).ifPresent(topic::setTitle);

            Optional.ofNullable(
                    request.getString("content")
            ).ifPresent(topic::setContent);

            response.put("data", topicService.addOrUpdate(topic));
            response.put("status", 1);
            response.put("message", "Update topic success.");
        } else {
            response.put("status", 0);
            response.put("message", "You can't update this topic.");
        }

        return response;
    }

    @DeleteMapping
    public JSONObject delete(@RequestParam Long id){
        JSONObject response=new JSONObject();
        try {
            Topic topic = topicService.loadById(id);

            if (topic.getCreateUser().getId().equals(SecurityUtil.getUserId())) {
                topicService.deleteById(id);
                response.put("status", 1);
                response.put("message", "Delete topic success");
            } else {
                response.put("status", 0);
                response.put("message", "You can't delete this topic.");
            }
        } catch (NoSuchElementException e) {

            response.put("status", 0);
            response.put("message", "The topic isn't exist");
        }
        return response;
    }

    @GetMapping("/{*id}")
    public JSONObject get(@PathVariable("*id") Long id) {
        JSONObject response = new JSONObject();
        try {
            Topic topic = topicService.loadById(id);
            Demand demand = topic.getDemand();

            JSONObject data = new JSONObject();

            data.put("id", topic.getId());
            data.put("title", topic.getTitle());
            data.put("content", topic.getContent());
            data.put("createTime", topic.getCreateTime());
            data.put("boutique", topic.getBoutique());

            if (demand != null) {
                data.put("demandContent", topic.getDemand().getContent());
                data.put("demandReward", topic.getDemand().getReward());
                Optional.ofNullable(
                        demand.getWinner()
                ).ifPresent(winner -> data.put("winnerUsername", winner.getUsername()));
            }
            data.putAll(topic.getInfo());

            response.put("status", 1);
            response.put("message", "Get topic success.");
            response.put("data", data);
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "This topic isn't exist.");
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
        data.put("topics", topics.getContent());

        response.put("data", data);
        response.put("status",1);
        response.put("message","Get Topic success");

        return response;
    }

    @GetMapping
    public JSONObject getAll(@RequestParam(defaultValue = "0") Integer pageNumber) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        Page<Topic> topics = topicService.loadAll(pageNumber);
        data.put("total", topics.getTotalPages());
        data.put("topics", topics.getContent());

        response.put("status", 1);
        response.put("message", "Get topics success");
        response.put("data", data);

        return response;
    }

    @GetMapping("/boutique")
    public JSONObject getBoutique(@RequestParam(defaultValue = "0") Integer pageNumber) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        Page<Topic> topics = topicService.loadAllBoutique(pageNumber);
        data.put("total", topics.getTotalPages());
        data.put("topics", topics.getContent());

        response.put("status", 1);
        response.put("message", "Get all boutique topics success.");
        response.put("data", data);

        return response;
    }

    @GetMapping("/hot")
    public JSONObject getHot(@RequestParam(defaultValue = "0") Integer pageNumber) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        Page<Topic> topics = topicService.loadAllOrderByCountComment(pageNumber);
        data.put("total", topics.getTotalPages());
        data.put("topics", topics.getContent());

        response.put("status", 1);
        response.put("message", "Get all hot topics success.");
        response.put("data", data);

        return response;
    }

    @GetMapping("/demand")
    public JSONObject getHaveDemand(@RequestParam(defaultValue = "0") Integer pageNumber) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        Page<Topic> topics = topicService.loadAllByDemandExists(pageNumber);
        data.put("total", topics.getTotalPages());
        data.put("topics", topics.getContent());

        response.put("status", 1);
        response.put("message", "Get all have demand topics success.");
        response.put("data", data);

        return response;
    }
}
