package cn.edu.ncu.topic;

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
public class TopicControllerTest {

    private static final String baseUrl = "/api/topic";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void create() throws URISyntaxException {
        URI uri=new URI(baseUrl);
        JSONObject requestBody = new JSONObject();

        requestBody.put("title", RandomString.make());
        requestBody.put("content", RandomString.make());

        HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, testUtil.getTokenHeader());
        ResponseEntity<JSONObject> response = restTemplate.exchange(
                uri, HttpMethod.PUT, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void delete() throws URISyntaxException {
        URI uri=new URI(baseUrl + "?id=1");
        HttpEntity<JSONObject> request = new HttpEntity<>(testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate
                .exchange(uri, HttpMethod.DELETE, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void find() throws URISyntaxException {
        final String url = baseUrl + "/find";

        URI uri = new URI(url);

        HttpEntity<JSONObject> request = new HttpEntity<>(testUtil.getTokenHeader());

        ResponseEntity<JSONObject> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, JSONObject.class);

        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
