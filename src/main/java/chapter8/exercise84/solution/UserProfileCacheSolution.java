package chapter8.exercise84.solution;

import chapter8.domain.UserProfile;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Solution - Optimizing cache memory management.
 *
 * <p>This implementation replaces the naive static {@code HashMap} cache with
 * a {@link LoadingCache} that provides automatic eviction and expiration.
 * It prevents unbounded memory growth by limiting the maximum number of
 * cached entries and removing profiles that have not been accessed for a
 * configured period.</p>
 *
 * <p>This snippet illustrates the memory efficiency pattern discussed in the
 * chapter, showing how proper cache design reduces memory retention and
 * avoids long-term performance degradation.</p>
 */
public class UserProfileCacheSolution {

  private static final LoadingCache<String, UserProfile> CACHE =
    CacheBuilder.newBuilder()
      .maximumSize(5_000)
      .expireAfterAccess(48, TimeUnit.HOURS)
      .build(new CacheLoader<>() {
        @Override
        public UserProfile load(String login) {
          return loadProfileFromDatabase(login);
        }
      });

  /**
   * Retrieves the user profile associated with the given identifier.
   *
   * <p>If the profile is present in the cache, it is returned immediately.
   * Otherwise, Guava loads it using the {@code loadProfileFromDatabase}
   * method and inserts it into the cache with the appropriate eviction and
   * expiration policies.</p>
   *
   * @param userId the identifier of the user
   * @return the user profile
   * @throws ExecutionException if the cache loader throws an exception
   */
  public static UserProfile getUserProfile(String userId)
    throws ExecutionException {
    return CACHE.get(userId);
  }

  /**
   * Loads the user profile from the database.
   *
   * <p>This is a placeholder for demonstration purposes. In a real
   * application, this method would perform a database query and return
   * the corresponding profile.</p>
   */
  private static UserProfile loadProfileFromDatabase(String userId) {
    //Mock
    return new UserProfile("demo", "01", "test@test.com");
  }

}
