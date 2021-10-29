package edu.tec.ic6821.blog.model.users;

import edu.tec.ic6821.blog.framework.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public final class User extends BaseEntity {
    private final Long integrationId;
    private final String username;
    private final String name;
    private final String email;
    private final String zipCode;

    public User(final Long integrationId,
                final String username,
                final String name,
                final String email,
                final String zipCode) {
        
        this.integrationId = integrationId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.zipCode = zipCode;
    }

    public User(final Long id,
                final Long integrationId,
                final String username,
                final String name,
                final String email,
                final String zipCode,
                final String extId,
                final LocalDateTime createdOn,
                final LocalDateTime lastUpdatedOn) {

        this.id = id;
        this.integrationId = integrationId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.zipCode = zipCode;
        this.extId = extId;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Long getIntegrationId() {
        return integrationId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final User user = (User) o;
        return Objects.equals(integrationId, user.integrationId)
            && Objects.equals(username, user.username)
            && Objects.equals(name, user.name)
            && Objects.equals(email, user.email)
            && Objects.equals(zipCode, user.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), integrationId, username, name, email, zipCode);
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, extId='%s', createdOn=%s, lastUpdatedOn=%s, integrationId=%d, "
                + "username='%s', name='%s', email='%s', zipCode='%s'}",
            id,
            extId,
            createdOn,
            lastUpdatedOn,
            integrationId,
            username,
            name,
            email,
            zipCode);
    }
}

