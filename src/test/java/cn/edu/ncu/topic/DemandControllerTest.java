package cn.edu.ncu.topic;

import cn.edu.ncu.util.TestUtil;
import com.alibaba.fastjson.JSONObject;
import net.bytebuddy.utility.RandomString;
import org.junit.Assert;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemandControllerTest {

    private static final String baseUrl = "/api/demand";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void add() throws URISyntaxException {
        URI uri=new URI(baseUrl);

        JSONObject requestBody = new JSONObject();
        requestBody.put("topicId", 2L);
        requestBody.put("content", RandomString.make());
        requestBody.put("reward", 5);

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.exchange(
                uri, HttpMethod.PUT,request, JSONObject.class);
        System.out.println(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void update() throws URISyntaxException {
        URI uri=new URI(baseUrl);

        JSONObject requestBody = new JSONObject();
        requestBody.put("topicId", 4L);
        requestBody.put("content","测试的demand3");
        requestBody.put("reward", 0);

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);

        System.out.println(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void delete() throws URISyntaxException{
        final String url = baseUrl + "?topicId=2";
        URI uri=new URI(url);

        HttpEntity<JSONObject> request = new HttpEntity<>(testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate
                .exchange(uri, HttpMethod.DELETE, request, JSONObject.class);

        System.out.println(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void setWinner() throws URISyntaxException {
        final String url = baseUrl + "/winner";
        URI uri=new URI(url);

        JSONObject requestBody = new JSONObject();
        requestBody.put("topicId", 2L);
        requestBody.put("userId", 2L);

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);
        System.out.println(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }
}
