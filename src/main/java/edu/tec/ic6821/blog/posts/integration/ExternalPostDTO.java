package edu.tec.ic6821.blog.posts.integration;

import java.util.Objects;

public final class ExternalPostDTO {

    private Long userId;
    private Long id;
    private String title;
    private String body;

    public ExternalPostDTO(final Long userId,
                           final Long id,
                           final String title,
                           final String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    private ExternalPostDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
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
        final ExternalPostDTO that = (ExternalPostDTO) o;
        return userId.equals(that.userId) && id.equals(that.id) && title.equals(that.title) && body.equals(that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, id, title, body);
    }

    @Override
    public String toString() {
        return "ExternalPostDTO{"
            + "userId=" + userId
            + ", id=" + id
            + ", title='" + title + '\''
            + ", body='" + body + '\''
            + '}';
    }
}
