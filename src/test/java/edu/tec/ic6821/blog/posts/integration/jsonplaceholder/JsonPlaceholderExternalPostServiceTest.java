package edu.tec.ic6821.blog.posts.integration.jsonplaceholder;

import edu.tec.ic6821.blog.framework.BeanMapper;
import edu.tec.ic6821.blog.posts.integration.ExternalPostDTO;
import edu.tec.ic6821.blog.posts.integration.ExternalPostService;
import edu.tec.ic6821.blog.model.posts.Post;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JsonPlaceholderExternalPostServiceTest {

    @TestConfiguration
    static class TestConf {
        @Value("${blog.externalservice.jsonplaceholder.url}")
        private String baseUrl;

        @MockBean
        private BeanMapper<ExternalPostDTO, Post> externalPostMapper;

        @MockBean
        private RestTemplate restTemplate;

        @Bean
        ExternalPostService externalPostService() {
            return new JsonPlaceholderExternalPostService(baseUrl, externalPostMapper, restTemplate);
        }
    }

    @Value("${blog.externalservice.jsonplaceholder.url}")
    private String baseUrl;

    @Autowired
    private ExternalPostService externalPostService;

    @Autowired
    private BeanMapper<ExternalPostDTO, Post> externalPostMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void pull() {
        // given
        final ExternalPostDTO dto1 = new ExternalPostDTO(
            27L, 115L, "title1", "body1"
        );

        final ExternalPostDTO dto2 = new ExternalPostDTO(
            28L, 117L, "title2", "body2"
        );

        final ResponseEntity<ExternalPostDTO[]> response = new ResponseEntity<>(
            new ExternalPostDTO[] {dto1, dto2}, HttpStatus.OK
        );

        final ArgumentCaptor<String> resourceUrlCaptor = ArgumentCaptor.forClass(String.class);
        when(restTemplate.getForEntity(resourceUrlCaptor.capture(), eq(ExternalPostDTO[].class))).thenReturn(response);

        final Post post1 = new Post(115L, 27L, "title1", "body1");
        final Post post2 = new Post(117L, 28L, "title2", "body2");
        when(externalPostMapper.map(dto1)).thenReturn(post1);
        when(externalPostMapper.map(dto2)).thenReturn(post2);

        // when
        final List<Post> actual = externalPostService.pull();

        // then
        assertThat(resourceUrlCaptor.getValue()).isEqualTo(String.format("%s/posts", baseUrl));
        assertThat(actual).hasSize(2);
        assertThat(actual).contains(post1);
        assertThat(actual).contains(post2);
    }

    @Test
    public void pullWithFailedRequest() {
        // given
        final ResponseEntity<ExternalPostDTO[]> response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        final ArgumentCaptor<String> resourceUrlCaptor = ArgumentCaptor.forClass(String.class);
        when(restTemplate.getForEntity(resourceUrlCaptor.capture(), eq(ExternalPostDTO[].class))).thenReturn(response);

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> externalPostService.pull());

        // then
        assertThat(exception.getMessage()).contains("/posts");
        assertThat(exception.getMessage()).contains("500");
    }

}
