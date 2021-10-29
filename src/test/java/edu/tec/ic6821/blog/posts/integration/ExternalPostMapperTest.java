package edu.tec.ic6821.blog.posts.integration;

import edu.tec.ic6821.blog.posts.integration.ExternalPostDTO;
import edu.tec.ic6821.blog.posts.integration.ExternalPostMapper;
import edu.tec.ic6821.blog.model.posts.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ExternalPostMapperTest {

    @Autowired
    private ExternalPostMapper mapper;

    @Test
    public void map() {
        // given
        final ExternalPostDTO dto = new ExternalPostDTO(
            13L, 128L, "title1", "body1"
        );

        // when
        final Post actual = mapper.map(dto);

        // then
        assertThat(actual.getIntegrationId()).isEqualTo(128L);
        assertThat(actual.getUserIntegrationId()).isEqualTo(13L);
        assertThat(actual.getTitle()).isEqualTo("title1");
        assertThat(actual.getBody()).isEqualTo("body1");
    }
}
