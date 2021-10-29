package edu.tec.ic6821.blog.posts.integration;

import edu.tec.ic6821.blog.model.posts.Post;

import java.util.List;

public interface ExternalPostService {
    List<Post> pull();
}
