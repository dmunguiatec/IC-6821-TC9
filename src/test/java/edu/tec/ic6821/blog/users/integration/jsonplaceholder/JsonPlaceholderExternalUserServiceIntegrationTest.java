package edu.tec.ic6821.blog.users.integration.jsonplaceholder;

import edu.tec.ic6821.blog.users.integration.ExternalUserService;
import edu.tec.ic6821.blog.model.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JsonPlaceholderExternalUserServiceIntegrationTest {

    @Autowired
    private ExternalUserService externalUserService;

    @Test
    public void pullUsersFromJsonPlaceholder() {
        // given

        // when
        final List<User> actual = externalUserService.pull();

        // then
        assertThat(actual).isNotEmpty();
        actual.forEach(user -> {
            assertThat(user.getIntegrationId()).isNotNull();
            assertThat(user.getUsername()).isNotBlank();
            assertThat(user.getName()).isNotBlank();
            assertThat(user.getEmail()).isNotBlank();
            assertThat(user.getZipCode()).isNotBlank();
        });
    }
}
