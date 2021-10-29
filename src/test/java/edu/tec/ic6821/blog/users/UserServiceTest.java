package edu.tec.ic6821.blog.users;

import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceTestConfiguration {
        @MockBean
        private UserDao userDao;

        @Bean
        public UserService userService() {
            return new DefaultUserService(userDao);
        }
    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Test
    public void findExistingByUsername() {
        // given
        final String username = "username1";
        final User user = new User(17L, 100L, username, "name1", "email1", "zip1",
            UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now());

        given(userDao.findByUsername(username)).willReturn(Optional.of(user));

        // when
        final Optional<User> actual = userService.findByUsername(username);

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(user);
    }

    @Test
    public void findNotExistingByUsername() {
        // given
        final String username = "username1";

        given(userDao.findByUsername(username)).willReturn(Optional.empty());

        // when
        final Optional<User> actual = userService.findByUsername(username);

        // then
        assertThat(actual).isEmpty();
    }

}
