package engine;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AchievementManagerTest {

    private AchievementManager achievementManager;

    @Before
    public void setUp() {
        // Create a new instance for each test to ensure isolation
        achievementManager = new AchievementManager();
        // Reset its internal state before each test
        achievementManager.resetAchievementsForTest();
    }

    @Test
    public void onLevelFinished_level1_unlocksBeginner() {
        String unlockedName = achievementManager.onLevelFinished(1);
        assertEquals("Beginner", unlockedName);
    }

    @Test
    public void onLevelFinished_level1_unlocksBeginnerOnlyOnce() {
        achievementManager.onLevelFinished(1); // First time
        String unlockedName = achievementManager.onLevelFinished(1); // Second time
        assertNull("Achievement should only be unlocked once.", unlockedName);
    }

    @Test
    public void onLevelFinished_level3_unlocksIntermediate() {
        String unlockedName = achievementManager.onLevelFinished(3);
        assertEquals("Intermediate", unlockedName);
    }

    @Test
    public void onLevelFinished_level7_unlocksConqueror() {
        String unlockedName = achievementManager.onLevelFinished(7);
        assertEquals("Conqueror", unlockedName);
    }

    @Test
    public void onTimeElapsedSeconds_lessThan60_doesNotUnlockBearGrylls() {
        String unlockedName = achievementManager.onTimeElapsedSeconds(59);
        assertNull("Achievement should not be unlocked before 60 seconds.", unlockedName);
    }

    @Test
    public void onTimeElapsedSeconds_atLeast60_unlocksBearGrylls() {
        String unlockedName = achievementManager.onTimeElapsedSeconds(60);
        assertEquals("Achievement should be unlocked at 60 seconds.", "Bear Grylls", unlockedName);
    }
    
    @Test
    public void onTimeElapsedSeconds_atLeast60_unlocksBearGryllsOnlyOnce() {
        achievementManager.onTimeElapsedSeconds(60); // First time
        String unlockedName = achievementManager.onTimeElapsedSeconds(61); // Second time
        assertNull("Achievement should only be unlocked once.", unlockedName);
    }

    @Test
    public void onEnemyDefeated_firstKill_unlocksFirstBlood() {
        String unlockedName = achievementManager.onEnemyDefeated();
        assertEquals("The first kill should unlock 'First Blood'.", "First Blood", unlockedName);
    }

    @Test
    public void onEnemyDefeated_secondKill_doesNotUnlockFirstBlood() {
        achievementManager.onEnemyDefeated(); // First kill
        String unlockedName = achievementManager.onEnemyDefeated(); // Second kill
        assertNull("Subsequent kills should not unlock 'First Blood' again.", unlockedName);
    }

    @Test
    public void onEnemyDefeated_shotsLessThan6_doesNotCheckSniperAchievement() {
        // 5 shots, 1 hit (20% accuracy)
        achievementManager.onShotFired();
        achievementManager.onShotFired();
        achievementManager.onShotFired();
        achievementManager.onShotFired();
        achievementManager.onShotFired();
        String unlockedName = achievementManager.onEnemyDefeated(); // This is the 1st hit
        
        // Even with low accuracy, "Bad Sniper" should not unlock before 6 shots are fired.
        // The return here is "First Blood", which is correct.
        assertEquals("First Blood", unlockedName);
    }

    @Test
    public void onEnemyDefeated_accuracyBelowThreshold_unlocksBadSniper() {
        // Simulate firing 6 shots before the first hit.
        for (int i = 0; i < 6; i++) {
            achievementManager.onShotFired();
        }
        // shotsFired is now 6.

        // Simulate the first enemy being hit.
        // This increments shotsHit to 1. Accuracy is 1/6 = 16.7%.
        // The check for "Bad Sniper" should pass (6 > 5 and 16.7% <= 80%).
        // The "Bad Sniper" name should be returned, overwriting "First Blood".
        String unlockedName = achievementManager.onEnemyDefeated();

        assertEquals("'Bad Sniper' should unlock when accuracy is low.", "Bad Sniper", unlockedName);
    }

    @Test
    public void onEnemyDefeated_accuracyAboveThreshold_doesNotUnlockBadSniper() {
        // 6 shots, 6 hits (100% accuracy, > 80%)
        achievementManager.onEnemyDefeated(); // First Blood
        for (int i = 0; i < 5; i++) {
            achievementManager.onShotFired();
            achievementManager.onEnemyDefeated();
        }
        
        String unlockedName = achievementManager.onEnemyDefeated();
        assertNull("'Bad Sniper' should not unlock when accuracy is high.", unlockedName);
    }
}
