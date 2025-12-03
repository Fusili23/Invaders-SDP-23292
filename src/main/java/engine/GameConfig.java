package engine;

/**
 * Stores the global game settings.
 */
public final class GameConfig {

    /**
     * Private constructor to prevent instantiation.
     */
    private GameConfig() {

    }

    // Screen and Frame settings
    /** Width of the screen. */
    public static final int WIDTH = 448;
    /** Height of the screen. */
    public static final int HEIGHT = 520;
    /** Max fps of the screen. */
    public static final int FPS = 60;

    // Game settings
    /** Max lives. */
    public static final int MAX_LIVES = 3;
    /** Levels between extra life. */
    public static final int EXTRA_LIFE_FRECUENCY = 3;
    /** Bonus score for each life remaining at the end of the level. */
    public static final int LIFE_SCORE = 100;

    // Timings and Cooldowns
    /** Milliseconds until the screen accepts user input. */
    public static final int INPUT_DELAY = 6000;
    /** Time from finishing the level to screen change. */
    public static final int SCREEN_CHANGE_INTERVAL = 1500;

    // Bonus Ship settings
    /** Minimum time between bonus ship's appearances. */
    public static final int BONUS_SHIP_INTERVAL = 20000;
    /** Maximum variance in the time between bonus ship's appearances. */
    public static final int BONUS_SHIP_VARIANCE = 10000;
    /** Time until bonus ship explosion disappears. */
    public static final int BONUS_SHIP_EXPLOSION = 500;

    // Boss settings
    /** Time until Boss explosion disappears. */
    public static final int BOSS_EXPLOSION = 600;

    // UI settings
    /** Height of the interface separation line. */
    public static final int SEPARATION_LINE_HEIGHT = 45;
    /** Height of the items separation line (above items). */
    public static final int ITEMS_SEPARATION_LINE_HEIGHT = 400;
}