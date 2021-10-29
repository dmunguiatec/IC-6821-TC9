package edu.tec.ic6821.blog.sync;

import edu.tec.ic6821.blog.model.posts.Post;
import edu.tec.ic6821.blog.model.posts.PostDao;
import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import edu.tec.ic6821.blog.posts.integration.ExternalPostService;
import edu.tec.ic6821.blog.users.integration.ExternalUserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SyncDataServiceTest {

    @TestConfiguration
    static class SyncDataServiceTestConfiguration {
        @MockBean
        private ExternalUserService externalUserService;

        @MockBean
        private ExternalPostService externalPostService;

        @MockBean
        private UserDao userDao;

        @MockBean
        private PostDao postDao;

        @Bean
        public SyncDataService syncDataService() {
            return new DefaultSyncDataService(externalUserService, externalPostService, userDao, postDao);
        }
    }

    @Autowired
    private ExternalUserService externalUserService;

    @Autowired
    private ExternalPostService externalPostService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private SyncDataService syncDataService;

    @Test
    public void sync() {
        // given
        final User user1 = new User(10L, "username1", "name1", "email1", "zip1");
        final User user2 = new User(11L, "username2", "name2", "email2", "zip2");
        final User user3 = new User(12L, "username3", "name3", "email3", "zip3");

        List<User> externalUsers = List.of(user1, user2, user3);

        when(externalUserService.pull()).thenReturn(externalUsers);

        final Post post1 = new Post(100L, 10L, "title1", "body1");
        final Post post2 = new Post(101L, 10L, "title2", "body2");
        final Post post3 = new Post(102L, 11L, "title3", "body3");
        final Post post4 = new Post(103L, 10L, "title4", "body4");
        final Post post5 = new Post(104L, 12L, "title5", "body5");

        List<Post> externalPosts = List.of(post1, post2, post3, post4, post5);

        when(externalPostService.pull()).thenReturn(externalPosts);
        when(userDao.findByIntegrationIds(any())).thenReturn(
            List.of(user3),
            List.of(
                new User(1L, user1.getIntegrationId(), user1.getUsername(), user1.getName(),
                    user1.getEmail(), user1.getZipCode(),
                    UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now()),
                new User(2L, user2.getIntegrationId(), user2.getUsername(), user2.getName(),
                    user2.getEmail(), user2.getZipCode(),
                    UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now()),
                new User(3L, user3.getIntegrationId(), user3.getUsername(), user3.getName(),
                    user3.getEmail(), user3.getZipCode(),
                    UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now())
           )
        );
        when(userDao.create(ArgumentMatchers.<List<User>>any())).thenAnswer(invocation -> {
            final List<User> list = invocation.getArgument(0);
            return list.size();
        });
        when(postDao.findByIntegrationIds(any())).thenReturn(List.of(post5));
        when(postDao.create(ArgumentMatchers.<List<Post>>any())).thenAnswer(invocation -> {
            final List<Post> list = invocation.getArgument(0);
            return list.size();
        });

        // when
        final SyncDataResult actual = syncDataService.sync();

        // then
        assertThat(actual.getCreatedUsersCount()).isEqualTo(2);
        assertThat(actual.getCreatedPostsCount()).isEqualTo(4);
    }
}
