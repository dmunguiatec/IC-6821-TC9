package edu.tec.ic6821.blog.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.tec.ic6821.blog.model.posts.Post;

import java.time.LocalDateTime;

public class PostDTO {

    private final String extId;
    private final String title;
    private final String body;
    private final LocalDateTime date;

    public static PostDTO from(Post post) {
        return new PostDTO(
            post.getExtId(),
            post.getTitle(),
            post.getBody(),
            post.getCreatedOn());
    }

    public PostDTO(final String extId,
                   final String title,
                   final String body,
                   final LocalDateTime date) {

        this.extId = extId;
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public final String getExtId() {
        return extId;
    }

    public final String getTitle() {
        return title;
    }

    public final String getBody() {
        return body;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]'Z'")
    public final LocalDateTime getDate() {
        return this.date;
    }
}
