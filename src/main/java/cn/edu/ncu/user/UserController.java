package cn.edu.ncu.user;

import cn.edu.ncu.topic.TopicService;
import cn.edu.ncu.topic.model.Topic;
import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.SecurityUtil;
import cn.edu.ncu.util.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * User Controller
 * @author lorry
 * @author lin864464995@163.com
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final TokenUtil tokenUtil;

    private final UserService userService;

    private final TopicService topicService;

    public UserController(TokenUtil tokenUtil, UserService userService, TopicService topicService) {
        this.tokenUtil = tokenUtil;
        this.userService = userService;
        this.topicService = topicService;
    }

    /**
     * Registry Api
     * @param request {
     *      "username": username: String[must],
     *      "password": password: String[must],
     *      "name": name: String,
     *      "contact": contact: String,
     *      "nature": nature: String
     * }
     * @return if registry success return {
     *     "status": 1,
     *     "message": "Registry Success.",
     *     "data": {
     *         "id": user id: BigInteger,
     *         "username": username: String,
     *         "name": name: String
     *     }
     * } else return {
     *     "status: 0,
     *     "message": message: String
     * }
     */
    @ResponseBody
    @PostMapping("/registry")
    public JSONObject registry(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        String username = Optional.of(request.getString("username"))
                .orElseThrow(() -> new MissingServletRequestParameterException("username", "String"));
        String password = Optional.of(request.getString("password"))
                .orElseThrow(() -> new MissingServletRequestParameterException("password", "String"));
        String name = request.getString("name");
        String contact = request.getString("contact");
        String nature = request.getString("nature");

        User user = new User();

        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);
        user.setContact(contact);
        user.setNature(nature);

        try {
            response.put("data", userService.add(user));
            response.put("status", 1);
            response.put("message", "Registry Success.");
        } catch (DataIntegrityViolationException e) {
            if (userService.checkByUsername(username)) {
                response.put("status", 0);
                response.put("message", "Registry failed: username exits");
            } else {
                throw e;
            }
        }

        return response;
    }

    /**
     * User Login, Get Token Api
     * @param request {
     *      "username": username: String[must],
     *      "password": password: String[must]
     * }
     * @return if login success return {
     *     "status": 1,
     *     "message": "Login success.",
     *     "token": token: String
     * } else return {
     *     "status": 0,
     *     "message": message: String
     * }
     */
    @ResponseBody
    @PostMapping("/token")
    public JSONObject token(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject result = new JSONObject();

        String username = Optional.of(request.getString("username"))
                .orElseThrow(() -> new MissingServletRequestParameterException("username", "String"));

        String password = Optional.of(request.getString("password"))
                .orElseThrow(() -> new MissingServletRequestParameterException("password", "String"));

        try {
            User user = userService.loadUserByUsername(username);
            if (userService.checkPassword(user, password)) {
                result.put("status", 1);
                result.put("message", "Login success.");
                result.put("token", tokenUtil.generateToken(user));
            } else {
                result.put("status", 0);
                result.put("message", "Wrong password.");
            }
        } catch (UsernameNotFoundException e) {
            result.put("status", 0);
            result.put("message", "The user does not exist.");
        }

        return result;
    }

    /**
     * Get User Self Profile Api
     * @return {
     *     "status": 1,
     *     "message": "Get profile success.",
     *     "data": {
     *         "id": user id: BigInteger,
     *         "username": username: String,
     *         "name": name: String,
     *         "contact": contact: String,
     *         "nature": nature: String
     *     }
     * }
     */
    @ResponseBody
    @GetMapping("/profile")
    public JSONObject getProfile() {
        JSONObject response = new JSONObject();

        response.put("status", 1);
        response.put("message", "Get profile success.");
        response.put("data", SecurityUtil.getUser());

        return response;
    }

    /**
     * Update User Profile Api
     * @param request {
     *      "name": name: String,
     *      "contact": contact: String,
     *      "nature": nature: String
     * }
     * @return if update user profile success return {
     *     "status": 1,
     *     "message": "Update profile success.",
     *     "data": {
     *         "id": user id: BigInteger
     *         "username": username: String,
     *         "name": name: String,
     *         "contact": contact: String,
     *         "nature": nature: String
     *     }
     * } else return {
     *     "status": 0,
     *     "message": "Update profile failed."
     * }
     */
    @ResponseBody
    @PostMapping("/profile")
    @Transactional
    public JSONObject editProfile(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();

        User user = userService.loadByIdNoCache(SecurityUtil.getUserId());

        Optional<String> name = Optional.of(request.getString("name"));
        Optional<String> contact = Optional.of(request.getString("contact"));
        Optional<String> nature = Optional.of(request.getString("nature"));

        name.ifPresent(user::setName);
        contact.ifPresent(user::setContact);
        nature.ifPresent(user::setNature);

        try {
            response.put("data", userService.update(user));
            response.put("status", 1);
            response.put("message", "Update profile success.");
        } catch (Exception e) {
            response.put("status", 0);
            response.put("message", "Update profile failed.");
        }

        return response;
    }

    /**
     * Edit Password Api
     * @param request {
     *      "oldPassword": oldPassword: String[must],
     *      "newPassword": newPassword: String[must]
     * }
     * @return if edit password success return {
     *     "status": 1,
     *     "message": "Edit Password Success"
     * } else if old password wrong return {
     *     "status": 0,
     *     "message": "Old Password Wrong"
     * } else return {
     *     "status": 0,
     *     "message": "Edit Password Failed"
     * }
     */
    @ResponseBody
    @PostMapping("/password")
    @Transactional
    public JSONObject editPassword(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        String oldPassword = Optional.of(request.getString("oldPassword"))
                .orElseThrow(() -> new MissingServletRequestParameterException("oldPassword", "String"));
        String newPassword = Optional.of(request.getString("newPassword"))
                .orElseThrow(() -> new MissingServletRequestParameterException("newPassword", "String"));

        User user = userService.loadByIdNoCache(SecurityUtil.getUserId());

        if (userService.checkPassword(user, oldPassword)) {
            user.setPassword(newPassword);
            userService.updatePassword(user);
            response.put("status", 1);
            response.put("message", "Edit Password Success");
        } else {
            response.put("status", 0);
            response.put("message", "Old Password Wrong");
        }

        return response;
    }

    @GetMapping("/topics")
    public JSONObject getMyTopics(@RequestParam(defaultValue = "0") Integer pageNumber) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        Page<Topic> topics = topicService.loadAllByCreateUser(SecurityUtil.getUser(), pageNumber);
        data.put("total", topics.getTotalPages());
        data.put("topics", topics.getContent());

        response.put("status", 1);
        response.put("message", "Get the use all topics success.");
        response.put("data", data);

        return response;
    }
}