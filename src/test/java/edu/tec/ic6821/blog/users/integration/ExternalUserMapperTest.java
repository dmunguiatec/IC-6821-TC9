package edu.tec.ic6821.blog.users.integration;

import edu.tec.ic6821.blog.model.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ExternalUserMapperTest {
    
    @Autowired
    private ExternalUserMapper mapper;

    @Test
    public void map() {
        // given
        final ExternalUserDTO dto = new ExternalUserDTO(
                10L, "name1", "username1", "email1",
                new ExternalUserAddressDTO("street1", "suite1", "city1",
                        "zipcode1", new ExternalUserGeoDTO("lat1", "lng1")),
                "phone1", "website1", new ExternalUserCompanyDTO("name1",
                "catchphrase1", "bs1")
        );
        
        // when
        final User actual = mapper.map(dto);
        
        // then
        assertThat(actual.getName()).isEqualTo("name1");
        assertThat(actual.getEmail()).isEqualTo("email1");
        assertThat(actual.getUsername()).isEqualTo("username1");
        assertThat(actual.getZipCode()).isEqualTo("zipcode1");
        assertThat(actual.getIntegrationId()).isEqualTo(10L);
    }
}
