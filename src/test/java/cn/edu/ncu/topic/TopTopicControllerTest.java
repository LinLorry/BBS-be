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
import java.util.Random;

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
        final String url=baseUrl+"/add";
        URI uri=new URI(url);

        JSONObject requestBody=new JSONObject();
        requestBody.put("id", 3L);

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody,testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);
        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }


    @Test
    public void deleteTopTopicByTopicId() throws URISyntaxException {
        final String url=baseUrl+"/delete";
        URI uri=new URI(url);
        JSONObject requestBody=new JSONObject();
        requestBody.put("id",2L);
        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody,testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);
        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void find() throws URISyntaxException {
        final String url=baseUrl+"/get";
        URI uri=new URI(url);
        HttpEntity<JSONObject> request = new HttpEntity<>(testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, JSONObject.class);

        System.out.println(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }



}
