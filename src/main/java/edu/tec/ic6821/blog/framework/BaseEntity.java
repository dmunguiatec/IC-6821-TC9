package edu.tec.ic6821.blog.framework;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class BaseEntity {

    protected Long id;
    protected String extId;
    protected LocalDateTime createdOn;
    protected LocalDateTime lastUpdatedOn;

    public final Long getId() {
        return id;
    }

    public final String getExtId() {
        return extId;
    }

    public final LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public final LocalDateTime getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    /**
     * Overriding methods should include a call to this method
     * <pre>if (!super.equals(o)) return false;</pre>
     *
     * @param o the object to compare with the current instance
     * @return true if the equality criteria is met, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id)
            && Objects.equals(extId, that.extId)
            && Objects.equals(createdOn, that.createdOn)
            && Objects.equals(lastUpdatedOn, that.lastUpdatedOn);
    }

    /**
     * Overriding methods should include a call to this method to use as part of its calculation
     * <pre>super.hashCode()</pre>
     *
     * @return int hash code of the properties defined in this class
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, extId, createdOn, lastUpdatedOn);
    }
}
