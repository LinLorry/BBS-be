package cn.edu.ncu.user;

import cn.edu.ncu.topic.TopicService;
import cn.edu.ncu.topic.model.Topic;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final TopicService topicService;
    public AdminController(TopicService topicService) {
        this.topicService = topicService;
    }

    @ResponseBody
    @DeleteMapping
    public JSONObject deleteTopic(@RequestParam long id){
        JSONObject response =new JSONObject();
        topicService.deleteById(id);
        response.put("status",1);
        response.put("message","Delete meeting success.");

        return  response;
    }
    @ResponseBody
    @GetMapping
    public JSONObject addBoutique(@RequestParam long id){
        JSONObject response=new JSONObject();
        Topic topic=topicService.loadById(id);

        try{
            topic.setBoutique(true);
            topicService.addOrUpdate(topic);
            response.put("status",1);
            response.put("message","Set topic boutique as true");

        }catch (NoSuchElementException e){
            response.put("status",0);
            response.put("message","The topic isn't exist");

        }
        return response;
    }

    @ResponseBody
    @PostMapping
    @Transactional
    public JSONObject update(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response=new JSONObject();
        Long id=request.getLong("id");
        try{
            Topic topic=topicService.loadById(id);
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
            topic.setTitle(title);
            topic.setContent(content);
            topicService.addOrUpdate(topic);
            response.put("status",1);
            response.put("message","Update topic success");

        }catch (NoSuchElementException e){
            response.put("status",0);
            response.put("message","This topic not exist.");


        }
        return response;
    }


}
