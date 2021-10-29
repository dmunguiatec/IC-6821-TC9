package edu.tec.ic6821.blog.posts.integration;

import edu.tec.ic6821.blog.framework.BeanMapper;
import edu.tec.ic6821.blog.model.posts.Post;
import org.springframework.stereotype.Component;

@Component
public final class ExternalPostMapper implements BeanMapper<ExternalPostDTO, Post> {

    @Override
    public Post map(ExternalPostDTO from) {
        return new Post(
            from.getId(),
            from.getUserId(),
            from.getTitle(),
            from.getBody()
        );
    }
}
