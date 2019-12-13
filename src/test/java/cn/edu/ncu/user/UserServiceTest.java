package cn.edu.ncu.user;

import cn.edu.ncu.user.model.User;
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
                userService.loadById(1L).getUsername()
        ));
    }

    @Test
    @Transactional
    public void update() {
        User user = userService.loadByIdNoCache(1L);

        String randomString = RandomString.make();
        user.setName(randomString);

        userService.update(user);

        assertEquals(userService.loadById(1L).getName(), randomString);
        System.out.println(user);
    }

    @Test
    public void checkPassword() {
        User user = userService.loadById(1L);
        assertTrue(userService.checkPassword(user, "test"));
    }

    @Test
    @Transactional
    public void updatePassword() {
        User user = userService.loadByIdNoCache(1L);
        user.setPassword("test");

        userService.updatePassword(user);

        assertTrue(userService.checkPassword(
                userService.loadById(1L), "test")
        );
        System.out.println(user);
    }

    @Test
    public void loadById() {
        User user = userService.loadById(1L);

        assertEquals(user.getId(), new Long(1));
        System.out.println(user);
    }

    @Test
    public void loadUserByUsername() {
        User user = userService.loadById(1L);
        User tmp = userService.loadUserByUsername(user.getUsername());

        assertEquals(user.getId(), tmp.getId());
        System.out.println(user);
    }
}
