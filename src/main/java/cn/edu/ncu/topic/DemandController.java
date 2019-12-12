package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
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

    public DemandController(DemandService demandService) {
        this.demandService = demandService;
    }

    /**
     *
     * @param request {
     *     "topicId": topicId: Long[must],
     *     "content": content: Sting[must],
     *     "reward": reward: Integer
     * }
     * @return {
     *     "status", 1，
     *     "message", "Add Success"
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

        Demand demand = new Demand();
        demand.setTopicId(topicId);
        demand.setContent(content);
        demand.setReward(reward);

        response.put("data", demandService.addOrUpdate(demand));
        response.put("status", 1);
        response.put("message", "Add Success");

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
                    request.getString("reward")
            ).ifPresent(demand::setContent);

            if(demand.getTopic().getCreateUser().getId().equals(SecurityUtil.getUserId())){
                response.put("data", demandService.addOrUpdate(demand));
                response.put("status", 1);
                response.put("message", "Update Success");
            }
            else {
                response.put("status", 0);
                response.put("message", "No permission to update.");
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

            if(demand.getTopic().getCreateUser().getId().equals(SecurityUtil.getUserId())){
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
}
