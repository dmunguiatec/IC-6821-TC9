package edu.tec.ic6821.blog.users.integration.jsonplaceholder;

import edu.tec.ic6821.blog.framework.BeanMapper;
import edu.tec.ic6821.blog.users.integration.ExternalUserDTO;
import edu.tec.ic6821.blog.users.integration.ExternalUserService;
import edu.tec.ic6821.blog.model.users.User;
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
public final class JsonPlaceholderExternalUserService implements ExternalUserService {

    private final String baseUrl;
    private final BeanMapper<ExternalUserDTO, User> externalUserMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public JsonPlaceholderExternalUserService(
            final @Value("${blog.externalservice.jsonplaceholder.url}") String baseUrl,
            final BeanMapper<ExternalUserDTO, User> externalUserMapper,
            final RestTemplate restTemplate) {

        this.baseUrl = baseUrl;
        this.externalUserMapper = externalUserMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<User> pull() {
        final List<ExternalUserDTO> externalUsers = getUsers();
        return convertUsers(externalUsers);
    }

    private List<ExternalUserDTO> getUsers() {
        final String resourceUrl = String.format("%s/users", baseUrl);
        final ResponseEntity<ExternalUserDTO[]> response = restTemplate.getForEntity(resourceUrl,
                ExternalUserDTO[].class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException(String.format("Couldn't pull external users from %s: status %s",
                    resourceUrl, response.getStatusCodeValue()));
        }

        final ExternalUserDTO[] body = response.getBody();
        return (body == null) ? Collections.emptyList() : Arrays.asList(body);
    }

    private List<User> convertUsers(List<ExternalUserDTO> externalUsers) {
        return externalUsers.stream()
                .map(externalUserMapper::map)
                .collect(Collectors.toList());
    }

}
