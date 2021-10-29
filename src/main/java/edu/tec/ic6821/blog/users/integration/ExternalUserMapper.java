package edu.tec.ic6821.blog.users.integration;

import edu.tec.ic6821.blog.framework.BeanMapper;
import edu.tec.ic6821.blog.model.users.User;
import org.springframework.stereotype.Component;

@Component
public final class ExternalUserMapper implements BeanMapper<ExternalUserDTO, User> {
    public User map(ExternalUserDTO from) {
        return new User(
            from.getId(),
            from.getUsername(),
            from.getName(),
            from.getEmail(),
            from.getAddress().getZipcode()
        );
    }
}
