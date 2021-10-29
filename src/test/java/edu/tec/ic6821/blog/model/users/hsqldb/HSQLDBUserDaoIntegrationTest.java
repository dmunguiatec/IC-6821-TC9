package edu.tec.ic6821.blog.model.users.hsqldb;

import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HSQLDBUserDaoIntegrationTest {

    @Autowired
    private UserDao userDao;

    private final AtomicLong integrationIdSequence = new AtomicLong(100L);

    @Test
    public void create() {
        // given
        final LocalDateTime momentBeforeCall = LocalDateTime.now();

        final User user = new User(
            integrationIdSequence.getAndIncrement(),
            "username|" + UUID.randomUUID(),
            "name1",
            "email1",
            "zipcode1"
        );

        // when
        final User actual = userDao.create(user);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getIntegrationId()).isEqualTo(user.getIntegrationId());
        assertThat(actual.getUsername()).isEqualTo(user.getUsername());
        assertThat(actual.getName()).isEqualTo(user.getName());
        assertThat(actual.getEmail()).isEqualTo(user.getEmail());
        assertThat(actual.getZipCode()).isEqualTo(user.getZipCode());
        assertThat(actual.getExtId()).isNotNull();
        assertThat(UUID.fromString(actual.getExtId())).isNotNull();
        assertThat(actual.getCreatedOn()).isAfter(momentBeforeCall);
        assertThat(actual.getLastUpdatedOn()).isAfter(momentBeforeCall);
    }

    @Test
    public void batchCreate() {
        // given
        final long userIntegrationId1 = integrationIdSequence.getAndIncrement();
        final long userIntegrationId2 = integrationIdSequence.getAndIncrement();
        final long userIntegrationId3 = integrationIdSequence.getAndIncrement();

        final String username1 = "username|" + UUID.randomUUID();
        final String username2 = "username|" + UUID.randomUUID();
        final String username3 = "username|" + UUID.randomUUID();

        final User user1 = new User(userIntegrationId1, username1, "name1", "email1", "zipcode1");
        final User user2 = new User(userIntegrationId2, username2, "name2", "email2", "zipcode2");
        final User user3 = new User(userIntegrationId3, username3, "name3", "email3", "zipcode3");

        // when
        int count = userDao.create(List.of(user1, user2, user3));

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void findByIntegrationIds() {
        // given
        final long userIntegrationId1 = integrationIdSequence.getAndIncrement();
        final long userIntegrationId2 = integrationIdSequence.getAndIncrement();
        final long userIntegrationId3 = integrationIdSequence.getAndIncrement();

        final User user1 = new User(userIntegrationId1, "username|" + UUID.randomUUID(), "name1", "email1", "zipcode1");
        final User user2 = new User(userIntegrationId2, "username|" + UUID.randomUUID(), "name2", "email2", "zipcode2");
        final User user3 = new User(userIntegrationId3, "username|" + UUID.randomUUID(), "name3", "email3", "zipcode3");

        final User savedUser1 = userDao.create(user1);
        final User savedUser2 = userDao.create(user2);
        final User savedUser3 = userDao.create(user3);

        // when
        final List<User> actual = userDao.findByIntegrationIds(Arrays.asList(userIntegrationId1, userIntegrationId3));

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSizeGreaterThanOrEqualTo(2);
        assertThat(actual).contains(savedUser1);
        assertThat(actual).contains(savedUser3);
        assertThat(actual).doesNotContain(savedUser2);
    }

    @Test
    public void findByIntegrationIdsNoResults() {
        // given
        final long nonExistingIntegrationId1 = -1L;
        final long nonExistingIntegrationId2 = -2L;

        // when
        final List<User> actual = userDao.findByIntegrationIds(
            Arrays.asList(
                nonExistingIntegrationId1,
                nonExistingIntegrationId2
            )
        );

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isEmpty();
    }

    @Test
    public void findById() {
        // given
        final User user1 = new User(integrationIdSequence.getAndIncrement(), "username|" + UUID.randomUUID(), "name1", "email1", "zipcode1");
        final User savedUser1 = userDao.create(user1);

        // when
        final Optional<User> actual = userDao.findById(savedUser1.getId());

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(savedUser1);
    }

    @Test
    public void findByIdNoResult() {
        // given
        Long nonExistingId = -1L;

        // when
        final Optional<User> actual = userDao.findById(nonExistingId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByExtId() {
        // given
        final User user1 = new User(integrationIdSequence.getAndIncrement(), "username|" + UUID.randomUUID(), "name1", "email1", "zipcode1");
        final User savedUser1 = userDao.create(user1);

        // when
        final Optional<User> actual = userDao.findByExtId(savedUser1.getExtId());

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(savedUser1);
    }

    @Test
    public void findByExtIdNoResult() {
        // given
        final String nonExistingId = UUID.randomUUID().toString();

        // when
        final Optional<User> actual = userDao.findByExtId(nonExistingId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByUsername() {
        // given
        final String username = "username1";
        final User user = new User(integrationIdSequence.getAndIncrement(), username, "name1", "email1", "zip1");
        final User savedUser = userDao.create(user);

        // when
        final Optional<User> actual = userDao.findByUsername(username);

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(savedUser);
    }

    @Test
    public void findNotExistingByUsername() {
        // given
        final String username = UUID.randomUUID().toString();

        // when
        final Optional<User> actual = userDao.findByUsername(username);

        // then
        assertThat(actual).isEmpty();
    }
}
