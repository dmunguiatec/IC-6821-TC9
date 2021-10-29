package edu.tec.ic6821.blog.users.integration.jsonplaceholder;

import edu.tec.ic6821.blog.framework.BeanMapper;
import edu.tec.ic6821.blog.users.integration.ExternalUserAddressDTO;
import edu.tec.ic6821.blog.users.integration.ExternalUserCompanyDTO;
import edu.tec.ic6821.blog.users.integration.ExternalUserDTO;
import edu.tec.ic6821.blog.users.integration.ExternalUserGeoDTO;
import edu.tec.ic6821.blog.users.integration.ExternalUserService;
import edu.tec.ic6821.blog.model.users.User;
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
public class JsonPlaceholderExternalUserServiceTest {

    @TestConfiguration
    static class TestConf {
        @Value("${blog.externalservice.jsonplaceholder.url}")
        private String baseUrl;

        @MockBean
        private BeanMapper<ExternalUserDTO, User> externalUserMapper;

        @MockBean
        private RestTemplate restTemplate;

        @Bean
        ExternalUserService externalUserService() {
            return new JsonPlaceholderExternalUserService(baseUrl, externalUserMapper, restTemplate);
        }
    }

    @Value("${blog.externalservice.jsonplaceholder.url}")
    private String baseUrl;

    @Autowired
    private ExternalUserService externalUserService;

    @Autowired
    private BeanMapper<ExternalUserDTO, User> externalUserMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void pull() {
        // given
        final ExternalUserDTO dto1 = new ExternalUserDTO(
                1L, "name1", "username1", "email1",
                new ExternalUserAddressDTO("street1", "suite1", "city1", "zipcode1",
                        new ExternalUserGeoDTO("lat1", "lng1")),
                "phone1", "website1",
                new ExternalUserCompanyDTO("name1", "cp1", "bs1")
        );

        final ExternalUserDTO dto2 = new ExternalUserDTO(
                2L, "name2", "username2", "email2",
                new ExternalUserAddressDTO("street2", "suite2", "city2", "zipcode2",
                        new ExternalUserGeoDTO("lat2", "lng2")),
                "phone2", "website2",
                new ExternalUserCompanyDTO("name2", "cp2", "bs2")
        );

        final ResponseEntity<ExternalUserDTO[]> response = new ResponseEntity<>(
                new ExternalUserDTO[] {dto1, dto2}, HttpStatus.OK
        );

        final ArgumentCaptor<String> resourceUrlCaptor = ArgumentCaptor.forClass(String.class);
        when(restTemplate.getForEntity(resourceUrlCaptor.capture(), eq(ExternalUserDTO[].class))).thenReturn(response);

        User user1 = new User(1L, "username1", "name1", "email1", "zipcode1");
        User user2 = new User(2L, "username2", "name2", "email2", "zipcode2");
        when(externalUserMapper.map(dto1)).thenReturn(user1);
        when(externalUserMapper.map(dto2)).thenReturn(user2);

        // when
        final List<User> actual = externalUserService.pull();

        // then
        assertThat(resourceUrlCaptor.getValue()).isEqualTo(String.format("%s/users", baseUrl));
        assertThat(actual).hasSize(2);
        assertThat(actual).contains(user1);
        assertThat(actual).contains(user2);
    }

    @Test
    public void pullWithFailedRequest() {
        // given
        final ResponseEntity<ExternalUserDTO[]> response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        final ArgumentCaptor<String> resourceUrlCaptor = ArgumentCaptor.forClass(String.class);
        when(restTemplate.getForEntity(resourceUrlCaptor.capture(), eq(ExternalUserDTO[].class))).thenReturn(response);

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> externalUserService.pull());

        // then
        assertThat(exception.getMessage()).contains("/users");
        assertThat(exception.getMessage()).contains("500");
    }

}
