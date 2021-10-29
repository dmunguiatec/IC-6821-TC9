package edu.tec.ic6821.blog.users;

import edu.tec.ic6821.blog.model.users.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
