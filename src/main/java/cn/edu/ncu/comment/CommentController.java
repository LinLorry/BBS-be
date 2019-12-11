package cn.edu.ncu.comment;

import cn.edu.ncu.comment.model.Comment;
import cn.edu.ncu.comment.model.CommentKey;
import cn.edu.ncu.topic.TopicService;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.util.SecurityUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    private final TopicService topicService;

    public CommentController(CommentService commentService, TopicService topicService) {
        this.commentService = commentService;
        this.topicService = topicService;
    }

    /**
     * Create Comment Api
     * @param request {
     *     "topicId": the topic id: Long[must],
     *     "content": the content: String[must]
     * }
     * @return if create success return {
     *     "status": 1,
     *     "message": "Add comment success.",
     * } else {
     *     "status": 0,
     *     "message": "message"
     * }
     * @throws MissingServletRequestParameterException if miss topicId or content
     *      throw this exception.
     */
    @ResponseBody
    @PostMapping
    @Transactional
    public JSONObject create(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        Long topicId = Optional.ofNullable(
                request.getLong("topicId")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "topicId", "Integer"
        ));

        String content = Optional.ofNullable(
                request.getString("content")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "content", "String"
        ));

        Integer location = commentService.getNextLocationByTopicId(topicId);

        Comment comment = new Comment();

        comment.setContent(content);
        comment.setUser(SecurityUtil.getUser());
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setLocation(location);

        try {
            Topic topic = topicService.loadTopicById(topicId);
            comment.setTopic(topic);

            commentService.add(comment);

            response.put("status", 1);
            response.put("message", "Add comment success.");
        } catch (DataIntegrityViolationException e) {
            CommentKey commentKey = new CommentKey(location, topicId);
            if (!commentService.checkByKey(commentKey)) {
                throw e;
            }
            response.put("status", "0");
            response.put("message", "Service is busy, please retry.");
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "This topic isn't exist.");
        }

        return response;
    }

    /**
     * Get The Topic Comments Api.
     * @param topicId the topic id: Long[must]
     * @param pageNumber the pageNumber: Integer
     * @return {
     *     "status": 1,
     *     "message": "Get comments success.",
     *     "data": [
     *         {
     *             "location": the comment location: Integer,
     *             "content": the comment content: String,
     *             "createTime": the comment createTime: Timestamp,
     *             "userName":"the create user name: String
     *         }
     *         ...
     *     ]
     * }
     */
    @ResponseBody
    @GetMapping({"/{*topicId}", "/{*topicId}/{*pageNumber}"})
    public JSONObject get(@PathVariable("*topicId") Long topicId,
                          @PathVariable(value = "*pageNumber", required = false) Integer pageNumber) {
        JSONObject response = new JSONObject();

        try {
            Topic topic = topicService.loadTopicById(topicId);
            Page<Comment> comments = commentService.loadAllByTopic(
                    topic, Optional.ofNullable(pageNumber).orElse(0));

            response.put("data", comments.getContent());
            response.put("status", 1);
            response.put("message", "Get comments success.");
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "This topic isn't exist.");
        }

        return response;
    }
}
