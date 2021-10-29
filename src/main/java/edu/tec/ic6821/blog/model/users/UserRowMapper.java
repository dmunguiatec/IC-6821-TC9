package edu.tec.ic6821.blog.model.users;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    private static final String FLD_ID = "id";
    private static final String FLD_INTEGRATION_ID = "integration_id";
    private static final String FLD_USERNAME = "username";
    private static final String FLD_NAME = "name";
    private static final String FLD_EMAIL = "email";
    private static final String FLD_ZIP_CODE = "zip_code";
    private static final String FLD_EXT_ID = "ext_id";
    private static final String FLD_CREATED_ON = "created_on";
    private static final String FLD_LAST_UPDATED_ON = "last_updated_on";

    @Override
    public final User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
            rs.getLong(FLD_ID),
            rs.getLong(FLD_INTEGRATION_ID),
            rs.getString(FLD_USERNAME),
            rs.getString(FLD_NAME),
            rs.getString(FLD_EMAIL),
            rs.getString(FLD_ZIP_CODE),
            rs.getString(FLD_EXT_ID),
            rs.getTimestamp(FLD_CREATED_ON).toLocalDateTime(),
            rs.getTimestamp(FLD_LAST_UPDATED_ON).toLocalDateTime()
        );
    }
}
