package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages all game achievements (including their state, unlocking logic, and persistence).
 */
public class AchievementManager {
    /** Stores the single instance of the AchievementManager. */
    private static AchievementManager instance;
    /** List of all achievements in the game. */
    private List<Achievement> achievements;
    /** Counter for the total number of shots fired by the player. */
    private int shotsFired = 0;
    /** Counter for the total number of shots that hit an enemy. */
    private int shotsHit = 0;
    /** Flag to ensure the 'First Blood' achievement is unlocked only once. */
    /** solo achievement */
    private boolean firstKillUnlocked = false;
    /** Flag to ensure the 'Bad Sniper' achievement is unlocked only once. */
    private boolean sniperUnlocked = false;
    /** Flag to ensure the 'Bear Grylls' achievement is unlocked only once. */
    private boolean beginnerUnlocked = false;
    /** Flag to ensure the 'Beginner' achievement is unlocked only once. */
    private boolean intermediateUnlocked = false;
    /** Flag to ensure the 'Intermediate' achievement is unlocked only once. */
    private boolean survivorUnlocked = false;
    private boolean conquerorUnlocked = false;
    private boolean bossSlayerUnlocked = false;
    private boolean mrGreedyUnlocked = false;
    /** co-op achievement */
    private boolean survivorDuoUnlocked = false; // Reach Lv.3 with partner alive
    private boolean perfectDuoUnlocked = false;  // Clear Lv.3 with full lives
    private boolean theMomUnlocked = false;      // Carry your partner (80% score)
    private boolean bestPartnerUnlocked = false; // Score difference < 100
    private boolean brotherhoodUnlocked = false; // Save your dying partner
    private boolean tycoonCoupleUnlocked = false; // Collect 750 coins together
    private boolean pacifistUnlocked = false;    // Clear level with 0 score
    /**
     * Private constructor to initialize the achievement list and load their status.
     * Part of the Singleton pattern.
     */
    public AchievementManager() {
        achievements = new ArrayList<>();
        /** solo achievement */
        achievements.add(new Achievement("Beginner", "Clear level 1"));
        achievements.add(new Achievement("Intermediate", "Clear level 3"));
        achievements.add(new Achievement("Boss Slayer", "Defeat a boss"));
        achievements.add(new Achievement("Mr. Greedy", "Have more than 500 coins"));
        achievements.add(new Achievement("First Blood", "Defeat your first enemy"));
        achievements.add(new Achievement("Bear Grylls", "Survive for 60 seconds"));
        achievements.add(new Achievement("Bad Sniper", "Under 80% accuracy"));
        achievements.add(new Achievement("Conqueror", "Clear the final level"));
        /** co-op achievement */
        achievements.add(new Achievement("Survivor Duo", "Reach Lv.3 with partner alive"));
        achievements.add(new Achievement("Perfect Duo", "Clear Lv.3 with full lives"));
        achievements.add(new Achievement("The Mom", "Carry your partner (80% score)"));
        achievements.add(new Achievement("Best Partner", "Score difference < 100"));
        achievements.add(new Achievement("Brotherhood", "Save your dying partner"));
        achievements.add(new Achievement("Tycoon Couple", "Collect 750 coins together"));
        achievements.add(new Achievement("Pacifist", "Clear level with 0 score"));
        loadAchievements();
        syncFlagsWithList();
        saveAchievements();
    }

    /**
     * Provides the global access point to the AchievementManager instance.
     *
     * @return The singleton instance of AchievementManager.
     */
    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    /**
     * Gets the list of all achievements.
     *
     * @return A list of all achievements.
     */
    public List<Achievement> getAchievements() {
        return achievements;
    }

    private void syncFlagsWithList() {
        for (Achievement a : achievements) {
            if (a.isUnlocked()) {
                switch (a.getName()) {
                    case "First Blood": firstKillUnlocked = true; break;
                    case "Bad Sniper": sniperUnlocked = true; break;
                    case "Beginner": beginnerUnlocked = true; break;
                    case "Intermediate": intermediateUnlocked = true; break;
                    case "Bear Grylls": survivorUnlocked = true; break;
                    case "Boss Slayer": bossSlayerUnlocked = true; break;
                    case "Mr. Greedy": mrGreedyUnlocked = true; break;
                    case "Conqueror": conquerorUnlocked = true; break;

                    case "Survivor Duo": survivorDuoUnlocked = true; break;
                    case "Perfect Duo": perfectDuoUnlocked = true; break;
                    case "The Mom": theMomUnlocked = true; break;
                    case "Best Partner": bestPartnerUnlocked = true; break;
                    case "Brotherhood": brotherhoodUnlocked = true; break;
                    case "Tycoon Couple": tycoonCoupleUnlocked = true; break;
                    case "Pacifist": pacifistUnlocked = true; break;
                }
            }
        }
    }
    /**
     * Unlocks a specific achievement by name.
     * If the achievement is found and not already unlocked, it marks it as unlocked
     * and saves the updated status.
     *
     * @param name The name of the achievement to unlock.
     */
    public boolean unlockAchievement(String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getName().equals(name) && !achievement.isUnlocked()) {
                achievement.unlock();
                saveAchievements();

                return true;
            }
        }
        return false;
    }

    /**
     * Handles game events when an enemy is defeated.
     * Checks for and unlocks achievements related to enemy kills and accuracy.
     */
    public String onEnemyDefeated() {
        String unlockedName = null;
        if (!firstKillUnlocked) {
            if (unlockAchievement("First Blood")) {
                unlockedName = "First Blood";
                firstKillUnlocked = true;
            }
        }

        shotsHit++;

        if (!sniperUnlocked && shotsFired > 5) {
            double accuracy = (shotsHit / (double) shotsFired) * 100.0;
            if (accuracy <= 80.0) {
                if(unlockAchievement("Bad Sniper")){
                    unlockedName = "Bad Sniper";
                    sniperUnlocked = true;
                }
            }
        }
        return unlockedName;
    }

    /**
     * Handles game events related to elapsed time.
     * Checks for and unlocks achievements related to survival time.
     *
     * @param elapsedSeconds The total number of seconds elapsed in the game.
     */
    public String onTimeElapsedSeconds(int elapsedSeconds) {
        if (!survivorUnlocked && elapsedSeconds >= 60) {
            if (unlockAchievement("Bear Grylls")) {
                survivorUnlocked = true;
                return "Bear Grylls";
            }
        }
        return null;
    }
    public String onLevelFinished(int level) {
        String unlockedName = null;

        // level 1 clear -> Beginner
        if (level == 1 && !beginnerUnlocked) {
            if (unlockAchievement("Beginner")) {
                unlockedName = "Beginner";
                beginnerUnlocked = true;
            }
        }

        // level 3 clear -> Intermediate
        if (level == 3 && !intermediateUnlocked) {
            if (unlockAchievement("Intermediate")) {
                unlockedName = "Intermediate";
                intermediateUnlocked = true;
            }
        }

        if (level == 7 && !conquerorUnlocked) {
            if (unlockAchievement("Conqueror")) {
                unlockedName = "Conqueror";
                conquerorUnlocked = true;
            }
        }

        return unlockedName;
    }

    /**
     * Handles the game event when a shot is fired.
     * Increments the counter for shots fired.
     */
    public void onShotFired() {
        shotsFired++;
    }
    public String checkCoopLevelFinished(int level, int livesP1, int livesP2, int maxLives, int scoreP1, int scoreP2) {
        // 1. Survivor Duo: 레벨 2를 클리어하고(3라운드 진입), 둘 다 살아있음
        if (level == 2 && !survivorDuoUnlocked) {
            if (livesP1 > 0 && livesP2 > 0) {
                if (unlockAchievement("Survivor Duo")) {
                    survivorDuoUnlocked = true;
                    return "Survivor Duo";
                }
            }
        }

        // 2. Perfect Duo: 레벨 3을 클리어하고, 둘 다 만피(한 번도 안 깎임)
        if (level == 3 && !perfectDuoUnlocked) {
            if (livesP1 == maxLives && livesP2 == maxLives) {
                if (unlockAchievement("Perfect Duo")) {
                    perfectDuoUnlocked = true;
                    return "Perfect Duo";
                }
            }
        }

        // 3. Best Partner: 점수 차이가 100점 이내
        if (!bestPartnerUnlocked) {
            if (Math.abs(scoreP1 - scoreP2) <= 100) {
                if (unlockAchievement("Best Partner")) {
                    bestPartnerUnlocked = true;
                    return "Best Partner";
                }
            }
        }

        // 4. Pacifist: 한 명은 점수가 0점 (버스 탑승)
        if (!pacifistUnlocked) {
            if (scoreP1 == 0 || scoreP2 == 0) {
                if (unlockAchievement("Pacifist")) {
                    pacifistUnlocked = true;
                    return "Pacifist";
                }
            }
        }

        return null;
    }

    /**
     * 점수 획득 시 호출 (The Mom)
     */
    public String checkTheMomAchievement(int scoreP1, int scoreP2) {
        if (!theMomUnlocked) {
            int total = scoreP1 + scoreP2;
            // 최소 500점(약 10마리 이상)은 쌓여야 기여도 판정 의미가 있음
            if (total >= 500) {
                double p1Ratio = (double) scoreP1 / total;
                double p2Ratio = (double) scoreP2 / total;

                if (p1Ratio >= 0.8 || p2Ratio >= 0.8) {
                    if (unlockAchievement("The Mom")) {
                        theMomUnlocked = true;
                        return "The Mom";
                    }
                }
            }
        }
        return null;
    }

    /**
     * 코인 획득 시 호출 (Tycoon Couple)
     */
    public String checkTycoonCouple(int coins) {
        if (!tycoonCoupleUnlocked && coins >= 750) {
            if (unlockAchievement("Tycoon Couple")) {
                tycoonCoupleUnlocked = true;
                return "Tycoon Couple";
            }
        }
        return null;
    }

    /**
     * 보스 처치 시 호출 (Brotherhood)
     */
    public String checkBrotherhood(int killerId, int livesP1, int livesP2) {
        if (!brotherhoodUnlocked) {
            // P1이 킬 했는데 P2가 위기(목숨 1개)
            if (killerId == 1 && livesP2 == 1) {
                if (unlockAchievement("Brotherhood")) {
                    brotherhoodUnlocked = true;
                    return "Brotherhood";
                }
            }
            // P2가 킬 했는데 P1이 위기(목숨 1개)
            if (killerId == 2 && livesP1 == 1) {
                if (unlockAchievement("Brotherhood")) {
                    brotherhoodUnlocked = true;
                    return "Brotherhood";
                }
            }
        }
        return null;
    }

    /**
     * Loads achievement status from file and updates the current achievement list.
     * <p>
     * Requests the FileManager to load saved achievement data, then updates
     * each achievement's unlocked state accordingly.
     * </p>
     *
     * @throws RuntimeException
     *             If an I/O error occurs while loading achievements.
     */
    public void loadAchievements() {
        try {
            // Ask FileManager to load saved achievement status
            java.util.Map<String, Boolean> unlockedStatus = Core.getFileManager().loadAchievements();
            // Update the state of each achievement based on the loaded data.
            for (Achievement achievement : achievements) {
                if (unlockedStatus.getOrDefault(achievement.getName(), false)) {
                    achievement.unlock();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load achievement file! Creating a new one.");
            // If file loading fails, attempt an initial save.
            saveAchievements();
        }
    }
    /**
     * Saves the current achievement status to file.
     * <p>
     * Requests the FileManager to write all current achievements to disk.
     * </p>
     *
     * @throws RuntimeException
     *             If an I/O error occurs while saving achievements.
     */
    private void saveAchievements() {
        try {
            // Ask FileManager to save all current achievement data
            Core.getFileManager().saveAchievements(achievements);
        } catch (IOException e) {
            System.err.println("Failed to save achievement file!");
            e.printStackTrace();
        }
    }
    public void resetAchievementsForTest() {
        this.beginnerUnlocked = false;
        this.intermediateUnlocked = false;
        this.survivorUnlocked = false;
        this.firstKillUnlocked = false;
        this.sniperUnlocked = false;
        this.conquerorUnlocked = false;

        survivorDuoUnlocked = false;
        perfectDuoUnlocked = false;
        theMomUnlocked = false;
        bestPartnerUnlocked = false;
        brotherhoodUnlocked = false;
        tycoonCoupleUnlocked = false;
        pacifistUnlocked = false;
        this.shotsFired = 0;
        this.shotsHit = 0;

        achievements = new ArrayList<>();
        achievements.add(new Achievement("Beginner", "Clear level 1"));
        achievements.add(new Achievement("Intermediate", "Clear level 3"));
        achievements.add(new Achievement("Bear Grylls", "Survive for 60 seconds"));
        achievements.add(new Achievement("Boss Slayer", "Defeat a boss"));
        achievements.add(new Achievement("Mr. Greedy", "Have more than 500 coins"));
        achievements.add(new Achievement("First Blood", "Defeat your first enemy"));
        achievements.add(new Achievement("Bad Sniper", "Under 80% accuracy"));
        achievements.add(new Achievement("Conqueror", "Clear the final level"));

        achievements.add(new Achievement("Survivor Duo", "Reach Lv.3 with partner alive"));
        achievements.add(new Achievement("Perfect Duo", "Clear Lv.3 with full lives"));
        achievements.add(new Achievement("The Mom", "Carry your partner (80% score)"));
        achievements.add(new Achievement("Best Partner", "Score difference < 100"));
        achievements.add(new Achievement("Brotherhood", "Save your dying partner"));
        achievements.add(new Achievement("Tycoon Couple", "Collect 750 coins together"));
        achievements.add(new Achievement("Pacifist", "Clear level with 0 score"));
    }

}
