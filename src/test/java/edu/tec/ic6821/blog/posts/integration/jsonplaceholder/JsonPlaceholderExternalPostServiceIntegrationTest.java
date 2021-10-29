package edu.tec.ic6821.blog.posts.integration.jsonplaceholder;

import edu.tec.ic6821.blog.posts.integration.ExternalPostService;
import edu.tec.ic6821.blog.model.posts.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JsonPlaceholderExternalPostServiceIntegrationTest {

    @Autowired
    private ExternalPostService externalPostService;

    @Test
    public void pullPostsFromJsonPlaceholder() {
        // given

        // when
        final List<Post> actual = externalPostService.pull();

        // then
        assertThat(actual).isNotEmpty();
        actual.forEach(post -> {
            assertThat(post.getIntegrationId()).isNotNull();
            assertThat(post.getUserIntegrationId()).isNotNull();
            assertThat(post.getTitle()).isNotBlank();
            assertThat(post.getBody()).isNotBlank();
        });
    }
}
