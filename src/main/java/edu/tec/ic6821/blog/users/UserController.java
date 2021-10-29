package edu.tec.ic6821.blog.users;

import edu.tec.ic6821.blog.model.posts.Post;
import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.posts.PostDTO;
import edu.tec.ic6821.blog.posts.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(final UserService userService,
                          final PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/{userExtId}/posts")
    public final List<PostDTO> findPostsByUserExtId(@PathVariable String userExtId) {
        try {
            logger.info(String.format("[findPostsByUserExtId(%s)] Retrieving posts", userExtId));
            final Optional<List<Post>> optionalPosts = postService.findByUserExtId(userExtId);
            if (optionalPosts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            final List<Post> posts = optionalPosts.get();

            logger.info(String.format("[findPostsByUserExtId(%s)] Found %,d posts", userExtId, posts.size()));

            return posts.stream()
                .map(PostDTO::from)
                .collect(Collectors.toList());

        } catch (ResponseStatusException rse) {
            throw rse;
        } catch (Exception e) {
            logger.error(String.format("[findPostsByUserExtId(%s)] Unexpected error", userExtId), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("%s: %s", e.getClass().getName(), e.getMessage()));
        }
    }

    @GetMapping("/{username}")
    public final UserDTO findByUsername(@PathVariable String username) {
        try {
            logger.info(String.format("[findByUsername(%s)] Retrieving user by username", username));
            final Optional<User> optionalUser = userService.findByUsername(username);

            if (optionalUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return UserDTO.from(optionalUser.get());

        } catch (ResponseStatusException rse) {
            throw rse;
        } catch (Exception e) {
            logger.error(String.format("[findByUsername(%s)] Unexpected error", username), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("%s: %s", e.getClass().getName(), e.getMessage()));
        }
    }

}
