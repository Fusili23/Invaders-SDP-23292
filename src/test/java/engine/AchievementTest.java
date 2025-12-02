package engine;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AchievementTest {

    @Test
    public void constructor_initializesNameDescriptionAndLockedState() {
        Achievement achievement = new Achievement("Test Name", "Test Description");
        
        assertEquals("Test Name", achievement.getName());
        assertEquals("Test Description", achievement.getDescription());
        assertFalse("A new achievement should be locked by default.", achievement.isUnlocked());
    }

    @Test
    public void unlock_setsTheAchievementToUnlocked() {
        Achievement achievement = new Achievement("Test Name", "Test Description");
        
        assertFalse("Achievement should be locked initially.", achievement.isUnlocked());
        
        achievement.unlock();
        
        assertTrue("After unlocking, achievement should be unlocked.", achievement.isUnlocked());
    }

    @Test
    public void unlock_onAlreadyUnlockedAchievement_remainsUnlocked() {
        Achievement achievement = new Achievement("Test Name", "Test Description");
        
        achievement.unlock();
        assertTrue("Achievement should be unlocked.", achievement.isUnlocked());
        
        achievement.unlock();
        assertTrue("Unlocking an already unlocked achievement should have no effect.", achievement.isUnlocked());
    }
}
