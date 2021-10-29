package edu.tec.ic6821.blog.model.posts.hsqldb;

import edu.tec.ic6821.blog.model.posts.Post;
import edu.tec.ic6821.blog.model.posts.PostDao;
import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HSQLDBPostDaoIntegrationTest {

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void create() {
        // given
        final LocalDateTime momentBeforeCall = LocalDateTime.now();

        final User user = new User(
            101L,
            "username|" + UUID.randomUUID(),
            "name1",
            "email1",
            "zipcode1"
        );
        final User savedUser = userDao.create(user);

        final Post post = new Post(150L, 101L, savedUser.getId(), "title1", "body1");

        // when
        final Post actual = postDao.create(post);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getIntegrationId()).isEqualTo(post.getIntegrationId());
        assertThat(actual.getUserIntegrationId()).isEqualTo(post.getUserIntegrationId());
        assertThat(actual.getUserId()).isEqualTo(post.getUserId());
        assertThat(actual.getTitle()).isEqualTo(post.getTitle());
        assertThat(actual.getBody()).isEqualTo(post.getBody());
        assertThat(actual.getExtId()).isNotNull();
        assertThat(UUID.fromString(actual.getExtId())).isNotNull();
        assertThat(actual.getCreatedOn()).isAfter(momentBeforeCall);
        assertThat(actual.getLastUpdatedOn()).isAfter(momentBeforeCall);
    }

    @Test
    public void batchCreate() {
        // given
        final User user = new User(
            101L,
            "username|" + UUID.randomUUID(),
            "name1",
            "email1",
            "zipcode1"
        );
        final User savedUser = userDao.create(user);

        final List<Post> posts = List.of(
            new Post(150L, 101L, savedUser.getId(), "title1", "body1"),
            new Post(151L, 101L, savedUser.getId(), "title2", "body2"),
            new Post(152L, 101L, savedUser.getId(), "title3", "body3")
        );

        // when
        final int count = postDao.create(posts);

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void findById() {
        // given
        final User user = new User(
            101L,
            "username|" + UUID.randomUUID(),
            "name1",
            "email1",
            "zipcode1"
        );
        final User savedUser = userDao.create(user);
        final Post post = new Post(150L, 101L, savedUser.getId(), "title1", "body1");
        final Post savedPost = postDao.create(post);

        // when
        final Optional<Post> actual = postDao.findById(savedPost.getId());

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(savedPost);
    }

    @Test
    public void findByIdNoResult() {
        // given
        final Long nonExistingId = -1L;

        // when
        final Optional<Post> actual = postDao.findById(nonExistingId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByExtId() {
        // given
        final User user = new User(
            101L,
            "username|" + UUID.randomUUID(),
            "name1",
            "email1",
            "zipcode1"
        );
        final User savedUser = userDao.create(user);
        final Post post = new Post(150L, 101L, savedUser.getId(), "title1", "body1");
        final Post savedPost = postDao.create(post);

        // when
        final Optional<Post> actual = postDao.findByExtId(savedPost.getExtId());

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(savedPost);
    }

    @Test
    public void findByExtIdNoResult() {
        // given
        final String nonExistingId = UUID.randomUUID().toString();

        // when
        final Optional<Post> actual = postDao.findByExtId(nonExistingId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void getAll() {
        // given
        final User user = new User(
            101L,
            "username|" + UUID.randomUUID(),
            "name1",
            "email1",
            "zipcode1"
        );
        final User savedUser = userDao.create(user);

        final List<Post> posts = List.of(
            new Post(150L, 101L, savedUser.getId(), "title1", "body1"),
            new Post(151L, 101L, savedUser.getId(), "title2", "body2"),
            new Post(152L, 101L, savedUser.getId(), "title3", "body3")
        );
        int count = postDao.create(posts);

        // when
        final List<Post> actual = postDao.getAll();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    public void findByUserId() {
        // given
        final User user = new User(
            101L,
            "username|" + UUID.randomUUID(),
            "name1",
            "email1",
            "zipcode1"
        );
        final User savedUser = userDao.create(user);

        final Post savedPost1 = postDao.create(new Post(150L, 101L, savedUser.getId(), "title1", "body1"));
        final Post savedPost2 = postDao.create(new Post(151L, 101L, savedUser.getId(), "title2", "body2"));
        final Post savedPost3 = postDao.create(new Post(152L, 101L, savedUser.getId(), "title3", "body3"));

        // when
        final List<Post> actual = postDao.findByUserId(savedUser.getId());

        // then
        assertThat(actual).containsAll(List.of(savedPost1, savedPost2, savedPost3));
    }

    @Test
    public void findByUserIdNoResults() {
        // given
        final Long nonExistingId = -1L;

        // when
        final List<Post> actual = postDao.findByUserId(nonExistingId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByIntegrationIds() {
        // given
        final User savedUser1 = userDao.create(
            new User(100L, "username|" + UUID.randomUUID(), "name1", "email1", "zip1")
        );
        final User savedUser2 = userDao.create(
            new User(101L, "username|" + UUID.randomUUID(), "name2", "email2", "zip2")
        );

        final Post savedPost1 = postDao.create(new Post(200L, 100L, savedUser1.getId(),"title1", "body1"));
        final Post savedPost2 = postDao.create(new Post(201L, 101L, savedUser2.getId(),"title2", "body2"));
        final Post savedPost3 = postDao.create(new Post(202L, 100L, savedUser1.getId(),"title3", "body3"));

        // when
        final List<Post> actual = postDao.findByIntegrationIds(List.of(200L, 201L));

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(savedPost1, savedPost2);
    }
}
