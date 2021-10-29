package edu.tec.ic6821.blog.sync;

public class SyncDataResultDTO {

    private final int createdUsersCount;
    private final int createdPostsCount;

    public SyncDataResultDTO(final int createdUsersCount,
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

    public static SyncDataResultDTO from(SyncDataResult syncDataResult) {
        return new SyncDataResultDTO(
            syncDataResult.getCreatedUsersCount(),
            syncDataResult.getCreatedPostsCount()
        );
    }
}
