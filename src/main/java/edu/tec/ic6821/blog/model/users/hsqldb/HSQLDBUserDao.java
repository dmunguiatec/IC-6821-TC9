package edu.tec.ic6821.blog.model.users.hsqldb;

import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import edu.tec.ic6821.blog.model.users.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class HSQLDBUserDao extends NamedParameterJdbcDaoSupport implements UserDao {

    private UserRowMapper userRowMapper;

    @Autowired
    public HSQLDBUserDao(final DataSource dataSource,
                         final UserRowMapper userRowMapper) {
        super.setDataSource(dataSource);
        this.userRowMapper = userRowMapper;
    }

    @Override
    public User create(User user) {
        final String sql =
            "INSERT INTO users "
                + "(integration_id, username, name, email, zip_code, ext_id, created_on, last_updated_on) "
                + "VALUES "
                + "(:integrationId, :username, :name, :email, :zipCode, :extId, :createdOn, :lastUpdatedOn)";

        final String extId = UUID.randomUUID().toString();
        final LocalDateTime createdOn = LocalDateTime.now();
        final LocalDateTime lastUpdatedOn = LocalDateTime.now();

        final KeyHolder holder = new GeneratedKeyHolder();
        final SqlParameterSource params = new MapSqlParameterSource(Map.of(
            "integrationId", user.getIntegrationId(),
            "username", user.getUsername(),
            "name", user.getName(),
            "email", user.getEmail(),
            "zipCode", user.getZipCode(),
            "extId", extId,
            "createdOn", createdOn,
            "lastUpdatedOn", lastUpdatedOn
        ));

        getNamedParameterJdbcTemplate().update(sql, params, holder);

        return new User(
            holder.getKey().longValue(),
            user.getIntegrationId(),
            user.getUsername(),
            user.getName(),
            user.getEmail(),
            user.getZipCode(),
            extId,
            createdOn,
            lastUpdatedOn
        );
    }

    @Override
    public int create(List<User> users) {
        final String sql =
            "INSERT INTO users "
                + "(integration_id, username, name, email, zip_code, ext_id, created_on, last_updated_on) "
                + "VALUES "
                + "(:integrationId, :username, :name, :email, :zipCode, :extId, :createdOn, :lastUpdatedOn)";

        final SqlParameterSource[] params = users.stream().map(user -> {
            final String extId = UUID.randomUUID().toString();
            final LocalDateTime createdOn = LocalDateTime.now();
            final LocalDateTime lastUpdatedOn = LocalDateTime.now();

            return new MapSqlParameterSource(Map.of(
                "integrationId", user.getIntegrationId(),
                "username", user.getUsername(),
                "name", user.getName(),
                "email", user.getEmail(),
                "zipCode", user.getZipCode(),
                "extId", extId,
                "createdOn", createdOn,
                "lastUpdatedOn", lastUpdatedOn
            ));
        }).toArray(SqlParameterSource[]::new);

        final int[] counts = getNamedParameterJdbcTemplate().batchUpdate(sql, params);
        return Arrays.stream(counts).filter(i -> i > 0).sum();
    }

    @Override
    public Optional<User> findById(Long id) {
        final String sql = "SELECT * FROM users WHERE id = :id";
        final User user = getNamedParameterJdbcTemplate().query(sql, Collections.singletonMap("id", id),
            resultSet -> resultSet.next() ? userRowMapper.mapRow(resultSet, 1) : null);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByExtId(String extId) {
        final String sql = "SELECT * FROM users WHERE ext_id = :extId";
        final User user = getNamedParameterJdbcTemplate().query(sql,
            Collections.singletonMap("extId", extId),
            resultSet -> resultSet.next() ? userRowMapper.mapRow(resultSet, 1) : null);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findByIntegrationIds(List<Long> userIntegrationIds) {
        final String sql = "SELECT * FROM users WHERE integration_id IN (:userIntegrationIds)";
        final SqlParameterSource queryParams = new MapSqlParameterSource("userIntegrationIds", userIntegrationIds);

        return getNamedParameterJdbcTemplate().query(sql, queryParams, userRowMapper);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final String sql = "SELECT * FROM users WHERE username = :username";
        final User user = getNamedParameterJdbcTemplate().query(sql, Collections.singletonMap("username", username),
            resultSet -> resultSet.next() ? userRowMapper.mapRow(resultSet, 1) : null);
        return Optional.ofNullable(user);
    }
}
