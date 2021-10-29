package edu.tec.ic6821.blog.posts;

import edu.tec.ic6821.blog.model.posts.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<List<Post>> findByUserExtId(String userExtId);

    Optional<Post> findByExtId(String postExtId);
}
