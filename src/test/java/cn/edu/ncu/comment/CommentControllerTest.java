package cn.edu.ncu.comment;

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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {

    private static final String baseUrl = "/api/comment";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void create() throws URISyntaxException {
        URI uri = new URI(baseUrl);

        JSONObject requestBody = new JSONObject();
        requestBody.put("topicId", 1L);
        requestBody.put("content", RandomString.make());

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get() throws URISyntaxException {
        final String url = baseUrl + "/1/0";
        URI uri = new URI(url);

        ResponseEntity<JSONObject> response = restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(testUtil.getTokenHeader()), JSONObject.class
        );

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
