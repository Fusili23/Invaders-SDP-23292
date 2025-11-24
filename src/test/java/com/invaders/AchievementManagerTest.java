package com.invaders;

import engine.Achievement;
import engine.AchievementManager;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.List;
import static org.junit.Assert.*;

public class AchievementManagerTest {

    @Before
    public void setUp() throws Exception {
        // Reset the singleton instance before each test
        Field instance = AchievementManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void testGetInstance() {
        AchievementManager instance1 = AchievementManager.getInstance();
        AchievementManager instance2 = AchievementManager.getInstance();
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetAchievements() {
        AchievementManager am = AchievementManager.getInstance();
        List<Achievement> achievements = am.getAchievements();
        assertNotNull(achievements);
        // Assuming there are 8 achievements defined in the constructor
        assertEquals(8, achievements.size());
    }

    @Test
    public void testUnlockAchievement() {
        AchievementManager am = AchievementManager.getInstance();
        boolean unlocked = am.unlockAchievement("Beginner");
        assertTrue(unlocked);

        Achievement beginner = null;
        for (Achievement a : am.getAchievements()) {
            if ("Beginner".equals(a.getName())) {
                beginner = a;
                break;
            }
        }
        assertNotNull(beginner);
        assertTrue(beginner.isUnlocked());

        // Try to unlock again
        boolean unlockedAgain = am.unlockAchievement("Beginner");
        assertFalse(unlockedAgain);
    }
    
    @Test
    public void testUnlockNonExistentAchievement() {
        AchievementManager am = AchievementManager.getInstance();
        boolean unlocked = am.unlockAchievement("Non Existent");
        assertFalse(unlocked);
    }

    @Test
    public void testOnShotFired() {
        AchievementManager am = AchievementManager.getInstance();
        // This test is tricky without being able to see the private fields.
        // We can test its effect on onEnemyDefeated.
        for (int i = 0; i < 10; i++) {
            am.onShotFired();
        }
        // No direct assert, but we assume shotsFired is 10.
    }

    @Test
    public void testOnEnemyDefeated_FirstBlood() {
        AchievementManager am = AchievementManager.getInstance();
        String unlocked = am.onEnemyDefeated();
        assertEquals("First Blood", unlocked);

        String unlockedAgain = am.onEnemyDefeated();
        assertNull(unlockedAgain); // Should not unlock again
    }
    
    @Test
    public void testOnTimeElapsedSeconds_Survivor() {
        AchievementManager am = AchievementManager.getInstance();
        String unlocked = am.onTimeElapsedSeconds(59);
        assertNull(unlocked);
        
        unlocked = am.onTimeElapsedSeconds(60);
        assertEquals("Bear Grylls", unlocked);

        unlocked = am.onTimeElapsedSeconds(100);
        assertNull(unlocked); // Should not unlock again
    }
}
