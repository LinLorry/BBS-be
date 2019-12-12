package cn.edu.ncu.topic;

import cn.edu.ncu.user.rep.UserRepository;
import cn.edu.ncu.util.TestUtil;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicControllerTest {

    private static final String baseUrl = "/api/topic";
    @Autowired
    private TopicController topicController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestUtil testUtil;
    @Test
    public void create() throws URISyntaxException {
        final String url = baseUrl + "/create";
        URI uri=new URI(url);
        JSONObject requestBody = new JSONObject();
        requestBody.put("id",2L);
        requestBody.put("title","测试2的title");
        requestBody.put("content","测试2的content");
        requestBody.put("time","2019-12-12 12:12:00");
        requestBody.put("boutique",false);
       // requestBody.put("demand",null);
        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);
        System.out.println(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void delete() throws URISyntaxException {
        final String url=baseUrl+"/delete";
        URI uri=new URI(url);



    }

    @Test
    public void find() {
    }
}
