package edu.tec.ic6821.blog.sync;

import edu.tec.ic6821.blog.model.posts.Post;
import edu.tec.ic6821.blog.model.posts.PostDao;
import edu.tec.ic6821.blog.model.users.User;
import edu.tec.ic6821.blog.model.users.UserDao;
import edu.tec.ic6821.blog.posts.integration.ExternalPostService;
import edu.tec.ic6821.blog.users.integration.ExternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DefaultSyncDataService implements SyncDataService {

    private final ExternalUserService externalUserService;
    private final ExternalPostService externalPostService;

    private final UserDao userDao;
    private final PostDao postDao;

    @Autowired
    public DefaultSyncDataService(final ExternalUserService externalUserService,
                                  final ExternalPostService externalPostService,
                                  final UserDao userDao,
                                  final PostDao postDao) {

        this.externalUserService = externalUserService;
        this.externalPostService = externalPostService;
        this.userDao = userDao;
        this.postDao = postDao;
    }

    @Override
    @Transactional
    public SyncDataResult sync() {
        final List<User> externalUsers = externalUserService.pull();
        final List<Long> userIntegrationIds = getUserIntegrationIds(externalUsers);
        final List<User> usersToCreate = filterExistingUsers(externalUsers, userIntegrationIds);
        final int createdUsersCount = userDao.create(usersToCreate);

        final List<User> createdUsers = userDao.findByIntegrationIds(userIntegrationIds);

        final Map<Long, Long> createdUserIds = createdUsers.stream()
            .collect(Collectors.toMap(User::getIntegrationId, User::getId));

        final List<Post> externalPosts = externalPostService.pull();
        final List<Post> postsToCreate = filterExistingPosts(externalPosts);
        final List<Post> linkedPostsToCreate = linkPostsToUsers(postsToCreate, createdUserIds);
        final int createdPostsCount = postDao.create(linkedPostsToCreate);

        return new SyncDataResult(createdUsersCount, createdPostsCount);
    }

    private List<Long> getUserIntegrationIds(List<User> externalUsers) {
        return externalUsers.stream()
            .map(User::getIntegrationId)
            .collect(Collectors.toList());
    }

    private List<Post> linkPostsToUsers(List<Post> postsToCreate, Map<Long, Long> createdUserIds) {
        final List<Post> linkedPostsToCreate = postsToCreate.stream()
            .map(externalPost -> new Post(
                externalPost.getIntegrationId(),
                externalPost.getUserIntegrationId(),
                createdUserIds.get(externalPost.getUserIntegrationId()),
                externalPost.getTitle(),
                externalPost.getBody()
            ))
            .collect(Collectors.toList());
        return linkedPostsToCreate;
    }

    private List<User> filterExistingUsers(List<User> externalUsers, List<Long> userIntegrationIds) {
        final List<User> existingUsers = userDao.findByIntegrationIds(userIntegrationIds);

        final Map<Long, User> existingUsersMap = existingUsers.stream()
            .collect(Collectors.toMap(User::getIntegrationId, Function.identity()));

        return externalUsers.stream()
            .filter(user -> !existingUsersMap.containsKey(user.getIntegrationId()))
            .collect(Collectors.toList());
    }

    private List<Post> filterExistingPosts(List<Post> externalPosts) {
        final List<Long> postIntegrationIds = externalPosts.stream()
            .map(Post::getIntegrationId)
            .collect(Collectors.toList());

        final List<Post> existingPosts = postDao.findByIntegrationIds(postIntegrationIds);

        final Map<Long, Post> existingPostsMap = existingPosts.stream()
            .collect(Collectors.toMap(Post::getIntegrationId, Function.identity()));

        return externalPosts.stream()
            .filter(post -> !existingPostsMap.containsKey(post.getIntegrationId()))
            .collect(Collectors.toList());
    }
}
