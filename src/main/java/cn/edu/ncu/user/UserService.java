package cn.edu.ncu.user;

import cn.edu.ncu.user.model.User;
import cn.edu.ncu.user.rep.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * User Service
 * @author lorry
 * @author lin864464995@163.com
 * @see org.springframework.security.core.userdetails.UserDetailsService
 */
@Service
public class UserService implements UserDetailsService {

    @Value("${bbs.secret.password}")
    private String salt;

    private final UserRepository userRepository;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Add User Service
     * @param user the new user have raw password.
     * @return new user.
     */
    @Caching(
            put = {
                    @CachePut(value = "userCache", key = "#user.id"),
                    @CachePut(value = "userCache", key = "#user.username")
            }
    )
    public User add(User user) {
        String hash = encoder.encode(salt + user.getPassword().trim() + salt);
        user.setPassword(hash);

        return userRepository.save(user);
    }

    /**
     * Update User
     * @param user the user will be update.
     */
    @Caching(
            put = {
                    @CachePut(value = "userCache", key = "#user.id"),
                    @CachePut(value = "userCache", key = "#user.username")
            }
    )
    public User update(User user) {
        return userRepository.save(user);
    }

    /**
     * Update User Password
     * @param user the user with new raw password.
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "userCache", key = "#user.id"),
                    @CacheEvict(value = "userCache", key = "#user.username")
            }
    )
    public void updatePassword(User user) {
        String hash = encoder.encode(salt + user.getPassword().trim() + salt);
        user.setPassword(hash);

        userRepository.save(user);
    }

    /**
     * Load User By Id
     * @param id the user id.
     * @return the user.
     * @throws NoSuchElementException if the user is not exits throw this exception.
     */
    @Cacheable(value= "userCache", key= "#id")
    public User loadById(Long id) throws NoSuchElementException {
        return loadByIdNoCache(id);
    }

    public User loadByIdNoCache(Long id) throws NoSuchElementException {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Load User By Username.
     * @param username the username.
     * @return the user.
     * @throws UsernameNotFoundException if user is not exits throw UsernameNotFoundException.
     */
    @Override
    @Cacheable(value = "userCache", key = "#username")
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exits."));
    }

    /**
     * Check User Exists By Username
     * @param username the username
     * @return if user exists return true else return false.
     */
    @Cacheable(value = "userExistCache", key = "#username")
    public boolean checkByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check User Password
     * @param user be checked user.
     * @param password be checked password.
     * @return if password correct return true else return false.
     */
    boolean checkPassword(User user, String password) {
        return encoder.matches(salt + password + salt, user.getPassword());
    }
}
