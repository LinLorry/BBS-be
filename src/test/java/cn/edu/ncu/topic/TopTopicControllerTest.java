package cn.edu.ncu.topic;

import cn.edu.ncu.util.TestUtil;
import com.alibaba.fastjson.JSONObject;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopTopicControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;
    private static final String baseUrl = "/api/topTopic";

    @Test
    public void add() throws URISyntaxException {
        URI uri=new URI(baseUrl);

        JSONObject requestBody=new JSONObject();
        requestBody.put("id", testUtil.getRandomTopicId());
        System.out.println(requestBody);

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody,testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);
        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }


    @Test
    public void delete() throws URISyntaxException {
        URI uri = new URI(baseUrl + "?id=" + testUtil.getRandomTopTopicId());

        HttpEntity<JSONObject> request = new HttpEntity<>(testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate.exchange(
                uri, HttpMethod.DELETE, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get() throws URISyntaxException {
        URI uri=new URI(baseUrl);
        HttpEntity<JSONObject> request = new HttpEntity<>(testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, JSONObject.class);

        System.out.println(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }
}
