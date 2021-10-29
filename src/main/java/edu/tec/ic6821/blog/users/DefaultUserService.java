package edu.tec.ic6821.blog.users;

import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultUserService implements UserService {

    private final UserDao userDao;

    @Autowired
    public DefaultUserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
