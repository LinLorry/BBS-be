package cn.edu.ncu.user;

import cn.edu.ncu.topic.TopicService;
import cn.edu.ncu.topic.model.Topic;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TopicService topicService;

    public AdminController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/boutique")
    @Transactional
    public JSONObject addBoutique(@RequestParam long id){
        JSONObject response=new JSONObject();

        try{
            Topic topic = topicService.loadByIdNoCache(id);

            topic.setBoutique(!topic.getBoutique());
            topicService.addOrUpdate(topic);
            response.put("status", 1);
            response.put("message", "Set topic boutique as true");
        }catch (NoSuchElementException e){
            response.put("status", 0);
            response.put("message", "The topic isn't exist");
        }

        return response;
    }

    @PostMapping("/topic")
    @Transactional
    public JSONObject update(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response=new JSONObject();

        try{
            Topic topic = topicService.loadByIdNoCache(
                    Optional.ofNullable(
                            request.getLong("id")
                    ).orElseThrow(() -> new MissingServletRequestParameterException(
                            "id", "Long"
                    ))
            );

            Optional.ofNullable(
                    request.getString("title")
            ).ifPresent(topic::setTitle);

            Optional.ofNullable(
                    request.getString("content")
            ).ifPresent(topic::setContent);

            topicService.addOrUpdate(topic);

            response.put("status",1);
            response.put("message","Update topic success");
        }catch (NoSuchElementException e){
            response.put("status",0);
            response.put("message","This topic not exist.");
        }

        return response;
    }

    @DeleteMapping("/topic")
    public JSONObject deleteTopic(@RequestParam long id){
        JSONObject response =new JSONObject();

        try {
            topicService.deleteById(id);

            response.put("status",1);
            response.put("message","Delete meeting success.");
        } catch (EmptyResultDataAccessException e) {
            response.put("status", 0);
            response.put("message", "This topic isn't exist.");
        }

        return  response;
    }
}
