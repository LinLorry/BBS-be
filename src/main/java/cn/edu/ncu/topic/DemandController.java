package cn.edu.ncu.topic;

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

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));
        String content =Optional.of(request.getString("content"))
                .orElseThrow(() -> new MissingServletRequestParameterException("content", "String"));
        Integer reward = request.getInteger("reward");

        try {
            Topic topic = topicService.loadTopicById(topicId);
            User user = SecurityUtil.getUser();

            if (!topic.getCreateUser().getId().equals(user.getId())) {
                response.put("status", 0);
                response.put("message", "No permission to update.");
                return response;
            }

            Integer score = user.getScore();

            if (score >= reward) {
                score -= reward;
                user.setScore(score);

                userService.update(user);

                Demand demand = new Demand();
                demand.setTopicId(topicId);
                demand.setContent(content);
                demand.setReward(reward);

                response.put("data", demandService.addOrUpdate(demand));
                response.put("status", 1);
                response.put("message", "Add Success");
            } else {
                response.put("status", 0);
                response.put("message", "Lack of score");
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
        User user = SecurityUtil.getUser();

        try {
            Demand demand = demandService.loadById(Optional.ofNullable(
                    request.getLong("topicId")
            ).orElseThrow(() -> new MissingServletRequestParameterException
                    ("topicId", "Long")
            ));
            if (judge(demand)) return permission;

            Optional.ofNullable(
                    request.getString("content")
            ).ifPresent(demand::setContent);

            Integer reward = request.getInteger("reward");

            if (reward != null) {
                Integer score = user.getScore();
                Integer lastReward = demand.getReward();

                score = score - reward + lastReward;
                user.setScore(score);

                if (score >= 0) {
                    userService.update(user);
                    demand.setReward(reward);

                    response.put("data", demandService.addOrUpdate(demand));
                    response.put("status", 1);
                    response.put("message", "Update Success");
                } else {
                    response.put("status", 0);
                    response.put("message", "Lack of score");
                }
            }
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
        User user = SecurityUtil.getUser();

        try {
            Demand demand = demandService.loadById(topicId);
            if (judge(demand)) return permission;

            if (demand.getWinner() == null) {
                Integer reward = demand.getReward();
                Integer score = user.getScore();

                score += reward;
                user.setScore(score);

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

    @PostMapping("/winner")
    @Transactional
    public JSONObject setWinner(@RequestBody JSONObject request)
            throws Exception {
        JSONObject response = new JSONObject();

        Long userId = Optional.of(request.getLong("userId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("userId", "Long"));

        try {
            Demand demand = demandService.loadById(Optional.ofNullable(
                    request.getLong("topicId")
            ).orElseThrow(() -> new MissingServletRequestParameterException
                    ("topicId", "Long")
            ));
            if (judge(demand)) return permission;

            User user = userService.loadById(userId);

            if (demand.getWinner() != null) {
                response.put("status", 0);
                response.put("message", "The winner has been already set.");
            } else if (userId.equals(SecurityUtil.getUserId())) {
                response.put("status", 0);
                response.put("message", "The winner can't be yourself.");
            } else {
                demand.setWinner(user);

                user.setScore(user.getScore() + demand.getReward());

                userService.update(user);
                demandService.addOrUpdate(demand);

                response.put("status", 1);
                response.put("message", "The winner has been set.");
            }
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }

        return response;
    }

    private boolean judge(Demand demand) {
        return !demand.getTopic().getCreateUser().getId().equals(SecurityUtil.getUserId());
    }
}
