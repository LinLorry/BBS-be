package cn.edu.ncu.user.rep;

import cn.edu.ncu.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * User Repository
 * @author lorry
 * @author lin864464995@163.com
 * @see org.springframework.data.repository.CrudRepository
 */
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
