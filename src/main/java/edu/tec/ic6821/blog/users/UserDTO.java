package edu.tec.ic6821.blog.users;

import edu.tec.ic6821.blog.model.users.User;

public class UserDTO {
    private final String extId;
    private final String username;
    private final String name;
    private final String email;
    private final String zipCode;

    public UserDTO(final String extId,
                   final String username,
                   final String name,
                   final String email,
                   final String zipCode) {

        this.extId = extId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.zipCode = zipCode;
    }

    public static UserDTO from(User user) {
        return new UserDTO(
            user.getExtId(),
            user.getUsername(),
            user.getName(),
            user.getEmail(),
            user.getZipCode()
        );
    }

    public final String getExtId() {
        return extId;
    }

    public final String getUsername() {
        return username;
    }

    public final String getName() {
        return name;
    }

    public final String getEmail() {
        return email;
    }

    public final String getZipCode() {
        return zipCode;
    }
}
