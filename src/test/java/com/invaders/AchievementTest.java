package com.invaders;

import engine.Achievement;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AchievementTest {

    @Test
    public void testAchievementConstructor() {
        Achievement achievement = new Achievement("Test Name", "Test Description");
        assertEquals("Test Name", achievement.getName());
        assertEquals("Test Description", achievement.getDescription());
        assertFalse(achievement.isUnlocked());
    }

    @Test
    public void testUnlock() {
        Achievement achievement = new Achievement("Test Name", "Test Description");
        assertFalse(achievement.isUnlocked());
        achievement.unlock();
        assertTrue(achievement.isUnlocked());
    }
}
