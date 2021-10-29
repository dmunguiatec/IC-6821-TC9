package edu.tec.ic6821.blog.users.integration;

import java.util.Objects;

public final class ExternalUserAddressDTO {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private ExternalUserGeoDTO geo;

    public ExternalUserAddressDTO(final String street,
                                  final String suite,
                                  final String city,
                                  final String zipcode,
                                  final ExternalUserGeoDTO geo) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = geo;
    }

    public ExternalUserAddressDTO() {
    }

    public String getStreet() {
        return street;
    }

    public String getSuite() {
        return suite;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public ExternalUserGeoDTO getGeo() {
        return geo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExternalUserAddressDTO that = (ExternalUserAddressDTO) o;
        return street.equals(that.street) && suite.equals(that.suite) && city.equals(that.city)
            && zipcode.equals(that.zipcode) && geo.equals(that.geo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, suite, city, zipcode, geo);
    }

    @Override
    public String toString() {
        return "ExternalUserAddressDTO{"
            + "street='" + street + '\''
            + ", suite='" + suite + '\''
            + ", city='" + city + '\''
            + ", zipcode='" + zipcode + '\''
            + ", geo=" + geo
            + '}';
    }
}
