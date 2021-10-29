package edu.tec.ic6821.blog.posts.integration.jsonplaceholder;

import edu.tec.ic6821.blog.framework.BeanMapper;
import edu.tec.ic6821.blog.posts.integration.ExternalPostDTO;
import edu.tec.ic6821.blog.posts.integration.ExternalPostService;
import edu.tec.ic6821.blog.model.posts.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public final class JsonPlaceholderExternalPostService implements ExternalPostService {

    private final String baseUrl;
    private final BeanMapper<ExternalPostDTO, Post> externalPostMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public JsonPlaceholderExternalPostService(
        final @Value("${blog.externalservice.jsonplaceholder.url}") String baseUrl,
        final BeanMapper<ExternalPostDTO, Post> externalPostMapper,
        final RestTemplate restTemplate) {

        this.baseUrl = baseUrl;
        this.externalPostMapper = externalPostMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Post> pull() {
        final List<ExternalPostDTO> externalPosts = getPosts();
        return convertPosts(externalPosts);
    }

    private List<ExternalPostDTO> getPosts() {
        final String resourceUrl = String.format("%s/posts", baseUrl);
        final ResponseEntity<ExternalPostDTO[]> response = restTemplate.getForEntity(resourceUrl,
            ExternalPostDTO[].class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException(String.format("Couldn't pull external posts from %s: status %s",
                resourceUrl, response.getStatusCodeValue()));
        }

        final ExternalPostDTO[] body = response.getBody();
        return (body == null) ? Collections.emptyList() : Arrays.asList(body);
    }

    private List<Post> convertPosts(List<ExternalPostDTO> externalPosts) {
        return externalPosts.stream()
            .map(externalPostMapper::map)
            .collect(Collectors.toList());
    }
}
