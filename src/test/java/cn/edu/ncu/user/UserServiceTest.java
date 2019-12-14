package cn.edu.ncu.user;

import cn.edu.ncu.user.model.User;
import cn.edu.ncu.util.TestUtil;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void add() {
        User user = new User();
        user.setUsername(RandomString.make());
        user.setPassword("test");

        userService.add(user);
        System.out.println(user);
    }

    @Test
    public void checkByUsername() {
        assertTrue(userService.checkByUsername(
                userService.loadById(testUtil.getRandomUserId()).getUsername()
        ));
    }

    @Test
    @Transactional
    public void update() {
        Long id = testUtil.getRandomUserId();
        User user = userService.loadByIdNoCache(id);

        String randomString = RandomString.make();
        user.setName(randomString);

        userService.update(user);

        assertEquals(userService.loadById(id).getName(), randomString);
        System.out.println(user);
    }

    @Test
    public void checkPassword() {
        User user = userService.loadById(testUtil.getRandomUserId());
        assertTrue(userService.checkPassword(user, "test"));
    }

    @Test
    @Transactional
    public void updatePassword() {
        User user = userService.loadByIdNoCache(testUtil.getRandomUserId());
        user.setPassword("test");

        userService.updatePassword(user);

        assertTrue(userService.checkPassword(
                userService.loadById(1L), "test")
        );
        System.out.println(user);
    }

    @Test
    public void loadById() {
        Long id = testUtil.getRandomUserId();
        User user = userService.loadById(id);

        assertEquals(user.getId(), id);
        System.out.println(user);
    }

    @Test
    public void loadUserByUsername() {
        User user = userService.loadById(testUtil.getRandomUserId());
        User tmp = userService.loadUserByUsername(user.getUsername());

        assertEquals(user.getId(), tmp.getId());
        System.out.println(user);
    }
}
