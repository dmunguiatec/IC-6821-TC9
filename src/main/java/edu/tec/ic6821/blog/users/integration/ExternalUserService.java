package edu.tec.ic6821.blog.users.integration;

import edu.tec.ic6821.blog.model.users.User;

import java.util.List;

public interface ExternalUserService {
    List<User> pull();
}
