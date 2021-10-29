package edu.tec.ic6821.blog.model.users;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(User user);
    int create(List<User> users);
    Optional<User> findById(Long id);
    Optional<User> findByExtId(String extId);
    List<User> findByIntegrationIds(List<Long> userIntegrationIds);
    Optional<User> findByUsername(String username);
}
