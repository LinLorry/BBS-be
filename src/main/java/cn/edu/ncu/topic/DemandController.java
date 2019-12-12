package cn.edu.ncu.topic;

import cn.edu.ncu.topic.model.Demand;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

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

        Demand demand = demandService.loadById(topicId);

        Optional.ofNullable(
                request.getString("content")
        ).ifPresent(demand::setContent);
        Optional.ofNullable(
                request.getString("reward")
        ).ifPresent(demand::setContent);

        response.put("data", demandService.addOrUpdate(demand));
        response.put("status", 1);
        response.put("message", "Add Success");

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

        demandService.deleteDemandByTopicId(topicId);

        response.put("status", 1);
        response.put("message", "The demand has been delete");

        return response;
    }
}
