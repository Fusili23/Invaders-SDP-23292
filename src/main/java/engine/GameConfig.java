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

    // GameScreen-specific settings
    /** Cooldown for achievement pop-ups in milliseconds. */
    public static final int ACHIEVEMENT_POPUP_COOLDOWN = 2000;
    /** Vertical offset for ship placement. */
    public static final int SHIP_VERTICAL_OFFSET = 20;
    /** X-axis offset for final boss spawning. */
    public static final int FINAL_BOSS_SPAWN_OFFSET_X = 50;
    /** Y-axis offset for final boss spawning. */
    public static final int FINAL_BOSS_SPAWN_OFFSET_Y = 50;
    /** Damage taken by the Omega Boss. */
    public static final int OMEGA_BOSS_DAMAGE = 2;
    /** Damage taken by the Final Boss. */
    public static final int FINAL_BOSS_DAMAGE = 1;
    /** Ratio for converting points to coins. */
    public static final int COIN_FROM_POINTS_RATIO = 10;
    /** Speed of dropped items. */
    public static final int ITEM_DROP_SPEED = 2;
    /** Cooldown for health pop-ups in milliseconds. */
    public static final int HEALTH_POPUP_COOLDOWN = 500;
    /** Coin threshold for the "Greedy" achievement. */
    public static final int GREEDY_COIN_THRESHOLD = 500;
    /** Duration of invincibility in milliseconds. */
    public static final int INVINCIBILITY_DURATION = 5000;
    /** Duration of time freeze in milliseconds. */
    public static final int TIME_FREEZE_DURATION = 3000;
    /** Distance for the push back item effect. */
    public static final int PUSH_BACK_DISTANCE = 20;
    /** Points awarded per enemy destroyed by the explode item. */
    public static final int EXPLODE_ITEM_POINTS_PER_ENEMY = 5;
    /** Divisor for countdown calculation. */
    public static final int COUNTDOWN_DIVISOR = 1000;
    /** UI height divisor for countdown display. */
    public static final int COUNTDOWN_UI_HEIGHT_DIVISOR = 12;

    // Health and collision pop-up messages
    public static final String P1_HEALTH_LOSS_MESSAGE = "-1 Health (P1)";
    public static final String P2_HEALTH_LOSS_MESSAGE = "-1 Health (P2)";
    public static final String P1_COLLISION_MESSAGE = "-1 Life (P1 Collision!)";
    public static final String P2_COLLISION_MESSAGE = "-1 Life (P2 Collision!)";
    public static final String P1_BOSS_COLLISION_MESSAGE = "-1 Life (P1 Boss Collision!)";
    public static final String P2_BOSS_COLLISION_MESSAGE = "-1 Life (P2 Boss Collision!)";
    public static final String P1_LIFE_GAIN_MESSAGE = "+1 Life (P1)";
    public static final String P2_LIFE_GAIN_MESSAGE = "+1 Life (P2)";
}