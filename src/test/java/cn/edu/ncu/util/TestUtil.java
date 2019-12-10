package cn.edu.ncu.util;

import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {
    @Value("${bbs.authentication.name}")
    private String authenticationName;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserRepository userRepository;

    public HttpHeaders getTokenHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",
                authenticationName + " " + tokenUtil.generateToken(
                        userRepository.findById(1L).orElse(new User())
                )
        );

        return headers;
    }
}
