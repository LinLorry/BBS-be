package cn.edu.ncu.topic;

import cn.edu.ncu.exception.NoEnoughScoreException;
import cn.edu.ncu.exception.RewardInvalidException;
import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.UserService;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.SecurityUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/demand")
public class DemandController {

    private final DemandService demandService;

    private final TopicService topicService;

    private final UserService userService;

    private final static JSONObject permission;

    static {
        permission = new JSONObject();
        permission.put("status", 0);
        permission.put("message", "No permission to update.");
    }

    public DemandController(DemandService demandService, TopicService topicService, UserService userService) {
        this.demandService = demandService;
        this.topicService = topicService;
        this.userService = userService;
    }

    /**
     *
     * @param request {
     *     "topicId": topicId: Long[must],
     *     "content": content: Sting[must],
     *     "reward": reward: Integer
     * }
     * @return if(user.score is enough){
     *     "status", 1，
     *     "message", "Add Success"
     * }else{
     *     "status", 0，
     *     "message", "Lack of score"
     * }
     */
    @PutMapping
    @Transactional
    public JSONObject add(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Demand demand = new Demand();

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));
        String content =Optional.of(request.getString("content"))
                .orElseThrow(() -> new MissingServletRequestParameterException("content", "String"));
        Integer reward = request.getInteger("reward");

        try {
            Topic topic = topicService.loadByIdNoCache(topicId);

            if (judge(topic)) return permission;

            if (topic.getDemand() == null) {
                demand.setTopicId(topicId);
                demand.setTopic(topic);
                demand.setContent(content);
                demand.setReward(reward);

                response.put("data", demandService.addOrUpdate(demand));
                response.put("status", 1);
                response.put("message", "Add Success");
            } else {
                response.put("status", 0);
                response.put("message", "This topic have demand, can't add demand.");
            }
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }

        return response;
    }

    /**
     *
     * @param request{
     *     "topicId": topicId: Long[must],
     *     "content": content: Sting,
     *     "reward": reward: Integer
     * }
     * @return {
     *     "status", 1，
     *     "message", "Add Success"
     * }
     */
    @PostMapping
    @Transactional
    public JSONObject update(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Long id = Optional.ofNullable(
                request.getLong("topicId")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "topicId", "Long"
        ));

        try {
            Demand demand = demandService.loadById(id);

            if (judge(demand.getTopic())) return permission;

            Optional.ofNullable(
                    request.getString("content")
            ).ifPresent(demand::setContent);

            Optional.ofNullable(
                    request.getInteger("reward")
            ).ifPresent(demand::setReward);

            Optional.ofNullable(
                    request.getLong("winnerId")
            ).ifPresent(winnerId -> demand.setWinner(userService.loadById(winnerId)));

            demandService.addOrUpdate(demand);

            response.put("status", 1);
            response.put("message", "Update demand success.");

        } catch (NoEnoughScoreException e) {
            response.put("status", 0);
            response.put("message", "Lack of score");
        } catch (RewardInvalidException e) {
            response.put("status", 0);
            response.put("message", "Reward invalid.");
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }

        return response;
    }

    /**
     *
     * @param topicId: topicId: Long[must],
     * @return {
     *      "status", 1，
     *      "message", "The demand has been delete"
     * }
     */
    @DeleteMapping
    @Transactional
    public JSONObject delete(@RequestParam Long topicId){
        JSONObject response = new JSONObject();

        try {
            Demand demand = demandService.loadById(topicId);

            if (judge(demand.getTopic())) return permission;

            if (demand.getWinner() == null) {
                User user = demand.getTopic().getCreateUser();

                user.setScore(user.getScore() + demand.getReward());

                userService.update(user);
            }
            demandService.deleteById(topicId);

            response.put("status", 1);
            response.put("message", "The demand has been delete");

        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }

        return response;
    }

    private boolean judge(Topic topic) {
        return !topic.getCreateUser().getId().equals(SecurityUtil.getUserId());
    }
}
