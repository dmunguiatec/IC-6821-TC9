package edu.tec.ic6821.blog.model.posts;

import edu.tec.ic6821.blog.framework.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Post extends BaseEntity {
    private final Long integrationId;
    private final Long userIntegrationId;
    private final Long userId;
    private final String title;
    private final String body;

    public Post(final Long integrationId,
                final Long userIntegrationId,
                final String title,
                final String body) {
        this.integrationId = integrationId;
        this.userIntegrationId = userIntegrationId;
        this.title = title;
        this.body = body;
        this.userId = null;
    }

    public Post(final Long integrationId,
                final Long userIntegrationId,
                final Long userId,
                final String title,
                final String body) {
        this.integrationId = integrationId;
        this.userIntegrationId = userIntegrationId;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public Post(final Long id,
                final Long integrationId,
                final Long userIntegrationId,
                final Long userId,
                final String title,
                final String body,
                final String extId,
                final LocalDateTime createdOn,
                final LocalDateTime lastUpdatedOn) {
        this.id = id;
        this.integrationId = integrationId;
        this.userIntegrationId = userIntegrationId;
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.extId = extId;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Long getIntegrationId() {
        return integrationId;
    }

    public Long getUserIntegrationId() {
        return userIntegrationId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Post post = (Post) o;
        return Objects.equals(integrationId, post.integrationId)
            && Objects.equals(userIntegrationId, post.userIntegrationId)
            && Objects.equals(userId, post.userId)
            && Objects.equals(title, post.title)
            && Objects.equals(body, post.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), integrationId, userIntegrationId, userId, title, body);
    }

    @Override
    public String toString() {
        return String.format("Post{id=%d, extId='%s', createdOn=%s, lastUpdatedOn=%s, integrationId=%d, "
                + "userIntegrationId=%d, userId=%d, title='%s', body='%s'}",
            id,
            extId,
            createdOn,
            lastUpdatedOn,
            integrationId,
            userIntegrationId,
            userId,
            title,
            body);
    }
}
