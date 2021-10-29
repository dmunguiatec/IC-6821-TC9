package edu.tec.ic6821.blog.users.integration;

import java.util.Objects;

public final class ExternalUserCompanyDTO {
    private String name;
    private String catchPhrase;
    private String bs;

    public ExternalUserCompanyDTO(final String name,
                                  final String catchPhrase,
                                  final String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public ExternalUserCompanyDTO() {
    }

    public String getName() {
        return name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public String getBs() {
        return bs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExternalUserCompanyDTO that = (ExternalUserCompanyDTO) o;
        return name.equals(that.name) && catchPhrase.equals(that.catchPhrase) && bs.equals(that.bs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, catchPhrase, bs);
    }

    @Override
    public String toString() {
        return "ExternalUserCompanyDTO{"
            + "name='" + name + '\''
            + ", catchPhrase='" + catchPhrase + '\''
            + ", bs='" + bs + '\''
            + '}';
    }
}
