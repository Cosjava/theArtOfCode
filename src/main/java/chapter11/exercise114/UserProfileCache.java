package chapter11.exercise114;

import chapter11.domain.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Exercise - Optimizing cache memory management
 *
 * This class illustrates how a static cache can lead to memory retention
 * when entries are never removed. It is intentionally simplified and does not
 * include a real database implementation.</p>
 */
public class UserProfileCache {
  private static final Map<String, UserProfile> CACHE = new HashMap<>();

  /**
   * Retrieves a user profile from the cache or loads it from the database
   * if it is not already cached.
   *
   * @param userId the identifier of the user
   * @return the user's profile
   */
  public static UserProfile getUserProfile(String userId) {
    if (CACHE.containsKey(userId)) {
      return CACHE.get(userId);
    }
    UserProfile profile = loadProfileFromDatabase(userId);
    CACHE.put(userId, profile);
    return profile;
  }

  /**
   * Loads a user profile from the database.
   *
   * <p>This is a placeholder method for demonstration purposes. In a real
   * application, this method would execute a database query and return the
   * corresponding user profile.</p>
   */
  private static UserProfile loadProfileFromDatabase(String userId) {
    //Mock
    return null;
  }
}