package edu.tec.ic6821.blog.model.posts;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Post create(Post post);
    int create(List<Post> posts);
    Optional<Post> findById(Long id);
    Optional<Post> findByExtId(String extId);
    List<Post> getAll();
    List<Post> findByUserId(Long userId);
    List<Post> findByIntegrationIds(List<Long> postIntegrationIds);
}
