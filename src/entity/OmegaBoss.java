package entity;

import engine.DrawManager;
import engine.GameState;             // For restoring game state
import entity.HiddenStageManager;    // For hidden stage/Easter egg feature
import java.awt.*;

/**
 * Omega - Middle Boss with hidden stage restore (Easter egg) support.
 */
public class OmegaBoss extends MidBoss {

	/** Initial position in the x-axis. */
	private static final int INIT_POS_X = 224;
	/** Initial position in the y-axis. */
	private static final int INIT_POS_Y = 50;
	/** Width of Omega */
	private static final int OMEGA_WIDTH = 64;
	/** Height of Omega */
	private static final int OMEGA_HEIGHT = 28;
	/** Health of Omega */
	private static final int OMEGA_HEALTH = 45;
	/** Point of Omega when destroyed */
	private static final int OMEGA_POINT_VALUE = 500;
	/** Speed of x in pattern 1 */
	private static final int PATTERN_1_X_SPEED = 1;
	/** Speed of x in pattern 2 */
	private static final int PATTERN_2_X_SPEED = 4;
	/** Speed of y in pattern 2 */
	private static final int PATTERN_2_Y_SPEED = 3;
	/** Color of pattern 2 */
	private static final Color PATTERN_2_COLOR = Color.MAGENTA;
	/** Current horizontal movement direction. true for right, false for left. */
	private boolean isRight = true;
	/** Current vertical movement direction. true for down, false for up. */
	private boolean isDown = true;
	/** Boss cannot move below this boundary. */
	private final int bottomBoundary;
	/** Manages hidden stage transitions (Easter egg feature) */
	private HiddenStageManager hiddenStageManager;
	/** Reference to current game state (for restore) */
	private GameState gameState;

	/**
	 * Constructor, establishes the boss entity's properties and Easter egg handles.
	 * @param color Color of the boss entity.
	 * @param bottomBoundary Y boundary.
	 * @param hiddenStageManager Hidden stage transition manager.
	 * @param gameState Reference to current game state.
	 */
	public OmegaBoss(Color color, int bottomBoundary,
					 HiddenStageManager hiddenStageManager, GameState gameState) {
		super(INIT_POS_X, INIT_POS_Y, OMEGA_WIDTH, OMEGA_HEIGHT, OMEGA_HEALTH, OMEGA_POINT_VALUE, color);
		this.bottomBoundary = bottomBoundary;
		this.spriteType = DrawManager.SpriteType.OmegaBoss1;
		this.logger.info("OMEGA : Initializing Boss OMEGA");
		this.logger.info("OMEGA : move using the default pattern");
		this.hiddenStageManager = hiddenStageManager;
		this.gameState = gameState;
	}

	/** Apply a relative movement to the boss. */
	@Override
	public void move(int distanceX, int distanceY) {
		this.positionX += distanceX;
		this.positionY += distanceY;
	}

	/**
	 * Executes the boss's motion patterns, switching when health drops below half.
	 */
	private void movePatterns() {
		if (this.pattern != 2 && this.healPoint < this.maxHp / 2) {
			this.pattern = 2;
			this.color = PATTERN_2_COLOR;
			this.spriteType = DrawManager.SpriteType.OmegaBoss2;
			logger.info("OMEGA : move using second pattern");
		}
		switch (pattern) {
			case 1:
				this.patternFirst();
				break;
			case 2:
				this.patternSecond();
				break;
		}
	}

	/** First pattern: horizontal movement only. */
	private void patternFirst() {
		int dx = this.isRight ? PATTERN_1_X_SPEED : -PATTERN_1_X_SPEED;
		this.move(dx, 0);

		if (this.positionX <= 0) {
			this.isRight = true;
		} else if (this.positionX + this.width >= screen.getWidth()) {
			this.isRight = false;
		}
	}

	/** Second pattern: horizontal + vertical movement. */
	private void patternSecond() {
		int dx = this.isRight ? PATTERN_2_X_SPEED : -PATTERN_2_X_SPEED;
		int dy = this.isDown ? PATTERN_2_Y_SPEED : -PATTERN_2_Y_SPEED;

		this.move(dx, dy);

		if (this.positionX <= 0) {
			this.positionX = 0;
			this.isRight = true;
		} else if (this.positionX + this.width >= screen.getWidth()) {
			this.positionX = screen.getWidth() - this.width;
			this.isRight = false;
		}

		if (this.positionY <= INIT_POS_Y) {
			this.positionY = INIT_POS_Y;
			this.isDown = true;
		} else if (this.positionY + this.height >= bottomBoundary) {
			this.positionY = bottomBoundary - this.height;
			this.isDown = false;
		}
	}

	/**
	 * Marks the entity as destroyed and triggers hidden stage restore if applicable.
	 */
	@Override
	public void destroy() {
		this.isDestroyed = true;
		this.spriteType = DrawManager.SpriteType.OmegaBossDeath;
		this.logger.info("OMEGA : Boss OMEGA destroyed!");

		// Restore to original stage only if currently in hidden stage (Easter egg feature)
		if (hiddenStageManager != null && hiddenStageManager.isInHiddenStage()) {
			hiddenStageManager.exitHiddenStage(gameState);
		}
	}

	/**
	 * Reduces health, and destroys the entity if health is zero or less.
	 * @param damage The amount of damage to inflict.
	 */
	@Override
	public void takeDamage(int damage) {
		this.healPoint -= damage;
		if (this.healPoint <= 0 && !this.isDestroyed) {
			this.destroy();
		}
	}

	/**
	 * Updates the entity's state for the current game frame.
	 */
	@Override
	public void update() {
		this.movePatterns();
	}

	/**
	 * Renders the entity at its current position.
	 * @param drawManager Rendering manager.
	 */
	@Override
	public void draw(DrawManager drawManager) {
		drawManager.drawEntity(this, this.positionX, this.positionY);
	}
}
