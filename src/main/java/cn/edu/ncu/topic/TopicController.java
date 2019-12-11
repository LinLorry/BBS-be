package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.UserService;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.SecurityUtil;
import cn.edu.ncu.util.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

}
