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
    /**
     * 1. Tycoon Couple
     * - 조건: 코인 750개 이상
     */
    @Test
    public void testTycoonCoupleAchievement() {
        // 749개일 때 -> 실패 (null)
        assertNull(achievementManager.checkTycoonCouple(749));

        // 750개일 때 -> 성공 ("Tycoon Couple")
        assertEquals("Tycoon Couple", achievementManager.checkTycoonCouple(750));
    }

    /**
     * 2. The Mom
     * - 조건: 총점 500점 이상이고, 한 명의 기여도가 80% 이상
     */
    @Test
    public void testTheMomAchievement() {
        // [상황 1] 총점 부족 (400점) -> 실패
        assertNull(achievementManager.checkTheMomAchievement(400, 0));

        // [상황 2] 5:5 비율 (500점 vs 500점) -> 실패
        assertNull(achievementManager.checkTheMomAchievement(500, 500));

        // [상황 3] P1이 90% 기여 (900점 vs 100점) -> 성공
        assertEquals("The Mom", achievementManager.checkTheMomAchievement(900, 100));

        // [상황 4] P2가 90% 기여 (초기화 후 재시도)
        achievementManager.resetAchievementsForTest();
        assertEquals("The Mom", achievementManager.checkTheMomAchievement(100, 900));
    }

    /**
     * 3. Brotherhood
     * - 조건: 친구가 목숨 1개일 때,다른 한 명의 플레이어가 보스 처치
     */
    @Test
    public void testBrotherhoodAchievement() {
        // [상황 1] P1이 킬, P2 목숨 2개 (위기 아님) -> 실패
        assertNull(achievementManager.checkBrotherhood(1, 3, 2));

        // [상황 2] P1이 킬, P2 목숨 1개 -> 성공
        assertEquals("Brotherhood", achievementManager.checkBrotherhood(1, 3, 1));

        // [상황 3] P2가 킬, P1 목숨 1개 -> 성공
        achievementManager.resetAchievementsForTest();
        assertEquals("Brotherhood", achievementManager.checkBrotherhood(2, 1, 3));
    }

    /**
     * 4. Survivor Duo
     * - 조건: 레벨 2 클리어 시(3라운드 진입), 둘 다 생존(lives > 0)
     */
    @Test
    public void testSurvivorDuoAchievement() {
        // 레벨 2 클리어, P1(1개), P2(1개), Max(3개)
        String result = achievementManager.checkCoopLevelFinished(2, 1, 1, 3, 100, 1000);
        assertEquals("Survivor Duo", result);

        // 한 명이라도 죽어있으면(0개) 안 뜸
        achievementManager.resetAchievementsForTest();
        assertNull(achievementManager.checkCoopLevelFinished(2, 1, 0, 3, 100, 1000));
    }

    /**
     * 5. Perfect Duo
     * - 조건: 레벨 3 클리어 시, 둘 다 풀피(Max Lives)
     */
    @Test
    public void testPerfectDuoAchievement() {
        // 레벨 3 클리어, P1(3/3), P2(3/3) -> 성공
        String result = achievementManager.checkCoopLevelFinished(3, 3, 3, 3, 500, 500);
        assertEquals("Perfect Duo", result);

        // 한 명이라도 깎였으면(2/3) -> 실패
        achievementManager.resetAchievementsForTest();
        assertNull(achievementManager.checkCoopLevelFinished(3, 3, 2, 3, 500, 100));
    }

    /**
     * 6. Best Partner
     * - 조건: 점수 차이 100점 이내
     */
    @Test
    public void testBestPartnerAchievement() {
        // 점수 차이 50점 (100점 이내) -> 성공
        String result = achievementManager.checkCoopLevelFinished(1, 3, 3, 3, 1050, 1000);
        assertEquals("Best Partner", result);

        // 점수 차이 200점 -> 실패
        achievementManager.resetAchievementsForTest();
        assertNull(achievementManager.checkCoopLevelFinished(1, 3, 3, 3, 1200, 1000));
    }

    /**
     * 7. Pacifist
     * - 조건: 한 명의 점수가 0점
     */
    @Test
    public void testPacifistAchievement() {
        // P1: 1000점, P2: 0점 -> 성공
        String result = achievementManager.checkCoopLevelFinished(1, 3, 3, 3, 1000, 0);
        assertEquals("Pacifist", result);

        // 둘 다 점수 있음 -> 실패
        achievementManager.resetAchievementsForTest();
        assertNull(achievementManager.checkCoopLevelFinished(1, 3, 3, 3, 1000, 10));
    }

}

