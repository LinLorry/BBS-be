package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import cn.edu.ncu.user.UserService;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.SecurityUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/demand")
public class DemandController {

    private final DemandService demandService;

    private final UserService userService;

    public DemandController(DemandService demandService, UserService userService) {
        this.demandService = demandService;
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
    @ResponseBody
    @PostMapping("/add")
    public JSONObject add(@RequestBody JSONObject request) throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));
        String content =Optional.of(request.getString("content"))
                .orElseThrow(() -> new MissingServletRequestParameterException("content", "String"));
        Integer reward = request.getInteger("reward");

        User user = SecurityUtil.getUser();
        Integer score = user.getScore();

        if (score >= reward){
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
        }
        else {
            response.put("status", 0);
            response.put("message", "Lack of score");
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
    @ResponseBody
    @PostMapping("/update")
    public JSONObject update(@RequestBody JSONObject request) throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));

        try {
            Demand demand = demandService.loadById(topicId);
            Optional.ofNullable(
                    request.getString("content")
            ).ifPresent(demand::setContent);
            Optional.ofNullable(
                    request.getInteger("reward")
            ).ifPresent(reward -> {
                User user = SecurityUtil.getUser();
                Integer score = user.getScore();
                Integer lastReward = demand.getReward();

                score = score - reward + lastReward;
                user.setScore(score);

                if (score >= 0){
                    userService.update(user);
                    demand.setReward(reward);

                    if(demand.getTopic().getCreateUser().getId().equals(SecurityUtil.getUserId())){
                        response.put("data", demandService.addOrUpdate(demand));
                        response.put("status", 1);
                        response.put("message", "Update Success");
                    }
                    else {
                        response.put("status", 0);
                        response.put("message", "No permission to update.");
                    }
                }else {
                    response.put("status", 0);
                    response.put("message", "Lack of score");
                }
            });
        }catch (NoSuchElementException e){
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }
        return response;
    }

    /**
     *
     * @param request "topicId": topicId: Long[must],
     * @return {
     *      "status", 1，
     *      "message", "The demand has been delete"
     * }
     */
    @ResponseBody
    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject request) throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));

        try {
            Demand demand = demandService.loadById(topicId);
            Integer reward = demand.getReward();

            if(demand.getTopic().getCreateUser().getId().equals(SecurityUtil.getUserId())){
                User user = SecurityUtil.getUser();
                Integer score = user.getScore();
                score += reward;
                user.setScore(score);

                userService.update(user);
                demandService.deleteDemandByTopicId(topicId);

                response.put("status", 1);
                response.put("message", "The demand has been delete");
            }
            else {
                response.put("status", 0);
                response.put("message", "No permission to delete.");
            }
        }catch (NoSuchElementException e){
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }
        return response;
    }

    /**
     *
     * @param request "topicId": topicId: Long[must],
     * @return {
     *      "status", 1，
     *      "message", "Load Success"
     * }
     */
    @ResponseBody
    @PostMapping("/loadById")
    public JSONObject loadById(@RequestBody JSONObject request) throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));

        response.put("data", demandService.loadById(topicId));
        response.put("status", 1);
        response.put("message", "Load Success");

        return response;
    }

    @ResponseBody
    @PostMapping("/setWinner")
    public JSONObject setWinner(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));
        Long userId = Optional.of(request.getLong("userId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("userId", "Long"));

        try {
            Demand demand = demandService.loadById(topicId);
            User user = userService.loadById(userId);

            if(demand.getTopic().getCreateUser().getId().equals(SecurityUtil.getUserId())){
                if(userId.equals(SecurityUtil.getUserId())){
                    response.put("status", 0);
                    response.put("message", "The winner can't be yourself.");
                }
                else {
                    demand.setWinner(user);
                    Integer reward = demand.getReward();
                    Integer score = user.getScore();

                    score += reward;
                    user.setScore(score);
                    userService.update(user);

                    response.put("status", 1);
                    response.put("message", "The winner has been set");
                }
            }
            else {
                response.put("status", 0);
                response.put("message", "No permission to set winner.");
            }
        }catch (NoSuchElementException e){
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }
        return response;
    }
}
