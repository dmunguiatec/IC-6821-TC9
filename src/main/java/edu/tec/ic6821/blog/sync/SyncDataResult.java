package edu.tec.ic6821.blog.sync;

public class SyncDataResult {

    private final int createdUsersCount;
    private final int createdPostsCount;

    public SyncDataResult(final int createdUsersCount,
                          final int createdPostsCount) {
        this.createdUsersCount = createdUsersCount;
        this.createdPostsCount = createdPostsCount;
    }

    public final int getCreatedUsersCount() {
        return createdUsersCount;
    }

    public final int getCreatedPostsCount() {
        return createdPostsCount;
    }
}
