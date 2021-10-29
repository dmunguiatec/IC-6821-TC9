package edu.tec.ic6821.blog.users.integration;

import java.util.Objects;

public final class ExternalUserGeoDTO {
    private String lat;
    private String lng;

    public ExternalUserGeoDTO(final String lat,
                              final String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public ExternalUserGeoDTO() {
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExternalUserGeoDTO that = (ExternalUserGeoDTO) o;
        return lat.equals(that.lat) && lng.equals(that.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }

    @Override
    public String toString() {
        return "ExternalUserGeoDTO{"
            + "lat='" + lat + '\''
            + ", lng='" + lng + '\''
            + '}';
    }
}
