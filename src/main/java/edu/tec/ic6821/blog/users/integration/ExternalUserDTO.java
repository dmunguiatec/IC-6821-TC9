package edu.tec.ic6821.blog.users.integration;

import java.util.Objects;

public final class ExternalUserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private ExternalUserAddressDTO address;
    private String phone;
    private String website;
    private ExternalUserCompanyDTO company;

    public ExternalUserDTO(final Long id,
                           final String name,
                           final String username,
                           final String email,
                           final ExternalUserAddressDTO address,
                           final String phone,
                           final String website,
                           final ExternalUserCompanyDTO company) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.company = company;
    }

    private ExternalUserDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public ExternalUserAddressDTO getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public ExternalUserCompanyDTO getCompany() {
        return company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExternalUserDTO dto = (ExternalUserDTO) o;
        return id.equals(dto.id) && name.equals(dto.name) && username.equals(dto.username) && email.equals(dto.email)
            && address.equals(dto.address) && phone.equals(dto.phone) && website.equals(dto.website)
            && company.equals(dto.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, email, address, phone, website, company);
    }

    @Override
    public String toString() {
        return "ExternalUserDTO{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", username='" + username + '\''
            + ", email='" + email + '\''
            + ", address=" + address
            + ", phone='" + phone + '\''
            + ", website='" + website + '\''
            + ", company=" + company
            + '}';
    }
}
