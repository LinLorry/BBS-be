package cn.edu.ncu.user;

import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import cn.edu.ncu.util.TestUtil;
import com.alibaba.fastjson.JSONObject;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private static final String baseUrl = "/api/user";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void registry() throws URISyntaxException {
        final String url = baseUrl + "/registry";
        URI uri = new URI(url);

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", RandomString.make());
        requestBody.put("password", "test");

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody);

        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void token() throws URISyntaxException {
        final String url = baseUrl + "/token";
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        URI uri = new URI(url);

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", user.getUsername());
        requestBody.put("password", "test");

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody);

        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void
    getProfile() throws URISyntaxException {
        final String url = baseUrl + "/profile";
        URI uri = new URI(url);

        ResponseEntity<JSONObject> response = restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(testUtil.getTokenHeader()), JSONObject.class
        );

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void editProfile() throws URISyntaxException {
        final String url = baseUrl + "/profile";
        URI uri = new URI(url);

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", RandomString.make());
        requestBody.put("contact", RandomString.make());
        requestBody.put("nature", RandomString.make());

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void editPassword() throws URISyntaxException {
        final String url = baseUrl + "/password";
        URI uri = new URI(url);

        JSONObject requestBody = new JSONObject();
        requestBody.put("oldPassword", "test");
        requestBody.put("newPassword", "test");

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
