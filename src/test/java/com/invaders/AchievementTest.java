package test.java.com.invaders;

import engine.Achievement;
import engine.AchievementManager;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class AchievementTest {
    private AchievementManager achievementManager;
    @Before
    public void setUp() {
        achievementManager = new AchievementManager();
        achievementManager.resetAchievementsForTest();
    }

    @Test
    public void testAchievementConstructor() {
        Achievement achievement = new Achievement("Test Name", "Test Description");
        assertEquals("Test Name", achievement.getName());
        assertEquals("Test Description", achievement.getDescription());
        assertFalse(achievement.isUnlocked());
    }
    //test1
    @Test
    public void testUnlock() {
        Achievement achievement = new Achievement("Test Name", "Test Description");
        assertFalse(achievement.isUnlocked());
        achievement.unlock();
        assertTrue(achievement.isUnlocked());
    }

    // when level 1 clear, Beginner achievement check
    @Test
    public void testBeginnerAchievementUnlock() {
        int level = 1;
        String unlockedName = achievementManager.onLevelFinished(level);
        assertEquals("Beginner", unlockedName);
    }

    // when level 3 clear, Intermediate achievement check
    @Test
    public void testIntermediateAchievementUnlock() {
        int level = 3;
        String unlockedName = achievementManager.onLevelFinished(level);


        assertEquals("Intermediate", unlockedName);
    }
    // when level 7 clear, Conquer achievement check
    @Test
    public void testConquerAchievementUnlock() {
        int level = 7;
        String unlockedName = achievementManager.onLevelFinished(level);
        assertEquals("Conqueror", unlockedName);
    }
    // Bear Grylls achievement test
    @Test
    public void testBearGryllsAchievementUnlock() {
        // 1. 60s haven’t passed yet, so the achievement hasn’t been unlocked
        String result59 = achievementManager.onTimeElapsedSeconds(59);
        assertNull("59초에는 업적이 달성되면 안 됩니다.", result59);

        // 2. 60s have passed, so the achievement is now unlocked (‘Bear Grylls’ returned)
        String result60 = achievementManager.onTimeElapsedSeconds(60);
        assertEquals("60초가 되면 Bear Grylls 업적이 달성되어야 합니다.", "Bear Grylls", result60);
    }
    @Test
    public void testFirstBloodAchievement() {
        //* 1. first kill -> achievement clear!/
        String result1 = achievementManager.onEnemyDefeated();
        assertEquals("First Blood 업적이 떠야 합니다.", "First Blood", result1);

        //* 2. second kill -> achievement null!/
        String result2 = achievementManager.onEnemyDefeated();
        assertNull("두 번째 킬부터는 업적이 뜨면 안 됩니다.", result2);
    }
    @Test
    public void testBadSniperAchievement() {
        achievementManager.onEnemyDefeated();
        for (int i = 0; i < 10; i++) {
            achievementManager.onShotFired();
        }
        String result = achievementManager.onEnemyDefeated();
        assertEquals("명중률이 낮으면 Bad Sniper가 떠야 합니다.", "Bad Sniper", result);
    }
    @Test
    public void testGoodSniper() {
        achievementManager.onEnemyDefeated();

        achievementManager.onShotFired();
        achievementManager.onShotFired();
        achievementManager.onShotFired();
        achievementManager.onShotFired();
        achievementManager.onShotFired();
        achievementManager.onShotFired();

        for(int i=0; i<6; i++) {
            achievementManager.onEnemyDefeated();
        }

        String result = achievementManager.onEnemyDefeated();
        assertNull("명중률이 높으면 Bad Sniper가 뜨면 안 됩니다.", result);
    }
}
