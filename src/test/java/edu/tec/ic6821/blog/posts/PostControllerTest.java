package edu.tec.ic6821.blog.posts;

import edu.tec.ic6821.blog.model.posts.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @Test
    public void findByExtId() throws Exception {
        // given
        final String postExtId = UUID.randomUUID().toString();
        final Post post = new Post(1L, 100L, 200L, 20L, "title1", "body1",
            postExtId, LocalDateTime.now(), LocalDateTime.now());

        given(postService.findByExtId(postExtId)).willReturn(Optional.of(post));

        final String expected = format(
            "{'extId':'%s', 'title':'title1', 'body':'body1'}",
            postExtId
        );

        // when ... then
        mvc.perform(get(format("/api/posts/%s", postExtId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expected));
    }

    @Test
    public void findNotExistingByExtId() throws Exception {
        // given
        final String postExtId = UUID.randomUUID().toString();
        given(postService.findByExtId(postExtId)).willReturn(Optional.empty());

        // when ... then
        mvc.perform(get(format("/api/posts/%s", postExtId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
