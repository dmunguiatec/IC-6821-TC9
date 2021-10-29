package edu.tec.ic6821.blog.posts;

import edu.tec.ic6821.blog.model.posts.Post;
import edu.tec.ic6821.blog.model.posts.PostDao;
import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class PostServiceTest {

    @TestConfiguration
    static class PostServiceTestConfiguration {
        @MockBean
        private UserDao userDao;

        @MockBean
        private PostDao postDao;

        @Bean
        public PostService postService() {
            return new DefaultPostService(userDao, postDao);
        }
    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostService postService;

    @Test
    public void findByExtId() {
        // given
        final String extId = UUID.randomUUID().toString();
        final Post post = new Post(1L, 100L, 200L, 50L, "title1", "body1",
            extId, LocalDateTime.now(), LocalDateTime.now());

        given(postDao.findByExtId(extId)).willReturn(Optional.of(post));

        // when
        final Optional<Post> actual = postService.findByExtId(extId);

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(post);
    }

    @Test
    public void findNotExistingByExtId() {
        // given
        final String extId = UUID.randomUUID().toString();

        given(postDao.findByExtId(extId)).willReturn(Optional.empty());

        // when
        final Optional<Post> actual = postService.findByExtId(extId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByUserExtId() {
        // given
        final String userExtId = UUID.randomUUID().toString();
        final Long userId = 1L;
        final User user = new User(userId, 100L, "username1", "name1", "email1", "zip1",
            userExtId, LocalDateTime.now(), LocalDateTime.now());

        final List<Post> posts = List.of(
            new Post(2L, 200L, 100L, userId, "title1", "body1",
                UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now()),
            new Post(3L, 201L, 100L, userId, "title2", "body2",
                UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now())
        );

        given(userDao.findByExtId(userExtId)).willReturn(Optional.of(user));
        given(postDao.findByUserId(userId)).willReturn(posts);

        // when
        final Optional<List<Post>> actual = postService.findByUserExtId(userExtId);

        // then
        assertThat(actual).isNotEmpty();
        final List<Post> actualPosts = actual.get();
        assertThat(actualPosts).hasSize(2);
        assertThat(actualPosts).containsAll(posts);
    }

    @Test
    public void findByUserExtIdNoResults() {
        // given
        final String userExtId = UUID.randomUUID().toString();
        final Long userId = 1L;
        final User user = new User(userId, 100L, "username1", "name1", "email1", "zip1",
            userExtId, LocalDateTime.now(), LocalDateTime.now());

        final List<Post> posts = Collections.emptyList();

        given(userDao.findByExtId(userExtId)).willReturn(Optional.of(user));
        given(postDao.findByUserId(userId)).willReturn(posts);

        // when
        final Optional<List<Post>> actual = postService.findByUserExtId(userExtId);

        // then
        assertThat(actual).isNotEmpty();
        final List<Post> actualPosts = actual.get();
        assertThat(actualPosts).isEmpty();
    }

    @Test
    public void findByNotExistingUserExtId() {
        // given
        final String userExtId = UUID.randomUUID().toString();

        given(userDao.findByExtId(userExtId)).willReturn(Optional.empty());

        // when
        final Optional<List<Post>> actual = postService.findByUserExtId(userExtId);

        // then
        assertThat(actual).isEmpty();
    }

}
