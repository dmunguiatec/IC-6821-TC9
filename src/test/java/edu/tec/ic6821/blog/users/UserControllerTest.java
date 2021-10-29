package edu.tec.ic6821.blog.users;

import edu.tec.ic6821.blog.model.posts.Post;
import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.posts.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PostService postService;

    @Test
    public void findByUsername() throws Exception {
        // given
        String username = "username1";
        final String userExtId = UUID.randomUUID().toString();
        final User user = new User(1L, 100L, username, "name1", "email1", "zip1",
            userExtId, LocalDateTime.now(), LocalDateTime.now());

        given(userService.findByUsername(username)).willReturn(Optional.of(user));

        // when ... then
        mvc.perform(get(format("/api/users/%s", username))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(
                format("{'extId':'%s', 'username':'%s', 'name':'name1', 'email':'email1', 'zipCode':'zip1'}",
                    userExtId, username)
            ));
    }

    @Test
    public void findNotExistingByUsername() throws Exception {
        // given
        String username = "username1";
        given(userService.findByUsername(username)).willReturn(Optional.empty());

        // when ... then
        mvc.perform(get(format("/api/users/%s", username))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void findPostsByUserExtId() throws Exception {
        // given
        final String userExtId = UUID.randomUUID().toString();
        final Long userId = 1L;

        final String postExtId1 = UUID.randomUUID().toString();
        final String postExtId2 = UUID.randomUUID().toString();

        final List<Post> posts = List.of(
            new Post(2L, 200L, 100L, userId, "title1", "body1",
                postExtId1, LocalDateTime.now(), LocalDateTime.now()),
            new Post(3L, 201L, 100L, userId, "title2", "body2",
                postExtId2, LocalDateTime.now(), LocalDateTime.now())
        );

        given(postService.findByUserExtId(userExtId)).willReturn(Optional.of(posts));

        String expected = format(
            "[{'extId': %s, 'title':'title1', 'body':'body1'},{'extId':%s, 'title':'title2', 'body':'body2'}]",
            postExtId1, postExtId2);

        // when ... then
        mvc.perform(get(format("/api/users/%s/posts", userExtId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expected));
    }

    @Test
    public void findPostsByNotExistingUserExtId() throws Exception {
        // given
        final String userExtId = UUID.randomUUID().toString();
        given(postService.findByUserExtId(userExtId)).willReturn(Optional.empty());

        // when ... then
        mvc.perform(get(format("/api/users/%s/posts", userExtId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
