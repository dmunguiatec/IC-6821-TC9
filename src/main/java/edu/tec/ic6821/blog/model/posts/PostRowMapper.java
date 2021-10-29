package edu.tec.ic6821.blog.model.posts;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public final class PostRowMapper implements RowMapper<Post> {

    private static final String FLD_ID = "id";
    private static final String FLD_INTEGRATION_ID = "integration_id";
    private static final String FLD_USER_INTEGRATION_ID = "user_integration_id";
    private static final String FLD_USER_ID = "user_id";
    private static final String FLD_TITLE = "title";
    private static final String FLD_BODY = "body";
    private static final String FLD_EXT_ID = "ext_id";
    private static final String FLD_CREATED_ON = "created_on";
    private static final String FLD_LAST_UPDATED_ON = "last_updated_on";

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
            rs.getLong(FLD_ID),
            rs.getLong(FLD_INTEGRATION_ID),
            rs.getLong(FLD_USER_INTEGRATION_ID),
            rs.getLong(FLD_USER_ID),
            rs.getString(FLD_TITLE),
            rs.getString(FLD_BODY),
            rs.getString(FLD_EXT_ID),
            rs.getTimestamp(FLD_CREATED_ON).toLocalDateTime(),
            rs.getTimestamp(FLD_LAST_UPDATED_ON).toLocalDateTime());
    }
}
