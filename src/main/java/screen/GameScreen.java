package screen;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import engine.*;
import entity.*;
import audio.SoundManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import engine.level.Level;
import engine.level.LevelManager;

/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {

    /**
     * Returns the Y-coordinate of the bottom boundary for enemies (above items HUD)
     */
    public static int getItemsSeparationLineHeight() {
        return GameConfig.ITEMS_SEPARATION_LINE_HEIGHT;
    }

    /** Current level data (direct from Level system). */
    private Level currentLevel;
    /** Current difficulty level number. */
    private int level;
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;
	/** Formation of special enemy ships. */
	private EnemyShipSpecialFormation enemyShipSpecialFormation;
	/** Player's ship. */
	private Ship ship;
	/** Player 2's ship. */
	private Ship ship2;
	/** Bonus enemy ship that appears sometimes. */
	private EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** team drawing may implement */
	private FinalBoss finalBoss;
	/** Time until bonus ship explosion disappears. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time until Boss explosion disappears. */
	private Cooldown bossExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;
	/** OmegaBoss */
	private MidBoss omegaBoss;
	/** Set of all bullets fired by on-screen ships. */
	private Set<Bullet> bullets;
	/** Set of all dropItems dropped by on screen ships. */
	private Set<DropItem> dropItems;
	/** Current score. */
	private int score;
	/** Current score for player 2. */
	private int scoreP2;
    /** Player lives left. */
	private int lives;
	/** Player 2 lives left. */
	private int livesP2;
	/** Total bullets shot by the player. */
	private int bulletsShot;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed;
	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	private boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;
  /** Maximum number of lives. */
	private int maxLives;
	/** Current coin. */
	private int coin;
	/** Is the game in two-player mode? */
	private boolean isTwoPlayer;

    /** bossBullets carry bullets which Boss fires */
	private Set<BossBullet> bossBullets;
	/** Is the bullet on the screen erased */
  private boolean is_cleared = false;
  /** Timer to track elapsed time. */
  private GameTimer gameTimer;
  /** Elapsed time since the game started. */
  private long elapsedTime;
  // Achievement popup
  private String achievementText;
  private Cooldown achievementPopupCooldown;
  private enum StagePhase{wave, boss_wave};
  private StagePhase currentPhase;
  /** Health change popup. */
  private String healthPopupText;
  private Cooldown healthPopupCooldown;
  private boolean achievementTriggered = false;

	    private GameState gameState;
		private CollisionManager collisionManager;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param gameState
     *            Current game state.	 * @param level
     *            Current level settings.
     * @param bonusLife
     *            Checks if a bonus life is awarded this level.
     * @param maxLives
     *            Maximum number of lives.
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
	private String achievementPopuptext;


	public GameScreen(final GameState gameState,
			final Level level, final boolean bonusLife, final int maxLives,
			final int width, final int height, final int fps) {
		super(width, height, fps);

        this.currentLevel = level;
		this.level = gameState.getLevel();
		this.bonusLife = bonusLife;
		this.maxLives = maxLives;
        this.score = gameState.getScore();
        this.coin = gameState.getCoin();
		this.lives = gameState.getLivesRemaining();
		if (this.bonusLife) {
			this.lives++;
		}
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.isTwoPlayer = gameState.isTwoPlayer();
		if (this.isTwoPlayer) {
			this.livesP2 = gameState.getLivesRemainingP2();
			this.scoreP2 = gameState.getScoreP2();
			if (this.bonusLife) {
				this.livesP2++;
			}
		}
		this.gameState = gameState;
		this.achievementPopupCooldown = Core.getCooldown(GameConfig.ACHIEVEMENT_POPUP_COOLDOWN);
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();
		this.collisionManager = new CollisionManager(this);
        /** Initialize the bullet Boss fired */
		this.bossBullets = new HashSet<>();
        enemyShipFormation = new EnemyShipFormation(this.currentLevel);
		enemyShipFormation.attach(this);
        this.enemyShipFormation.applyEnemyColorByLevel(this.currentLevel);
		if (this.isTwoPlayer) {
			this.ship = new Ship(this.width / 3, GameConfig.ITEMS_SEPARATION_LINE_HEIGHT - GameConfig.SHIP_VERTICAL_OFFSET, Color.GREEN);
			this.ship.setPlayerId(1);
			this.ship2 = new Ship(this.width * 2 / 3, GameConfig.ITEMS_SEPARATION_LINE_HEIGHT - GameConfig.SHIP_VERTICAL_OFFSET, Color.CYAN);
			this.ship2.setPlayerId(2);
		} else {
			this.ship = new Ship(this.width / 2, GameConfig.ITEMS_SEPARATION_LINE_HEIGHT - GameConfig.SHIP_VERTICAL_OFFSET, Color.GREEN);
		}

        // special enemy initial
		enemyShipSpecialFormation = new EnemyShipSpecialFormation(this.currentLevel,
				Core.getVariableCooldown(GameConfig.BONUS_SHIP_INTERVAL, GameConfig.BONUS_SHIP_VARIANCE),
				Core.getCooldown(GameConfig.BONUS_SHIP_EXPLOSION));
		enemyShipSpecialFormation.attach(this);
		this.bossExplosionCooldown = Core.getCooldown(GameConfig.BOSS_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(GameConfig.SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();
        this.dropItems = new HashSet<DropItem>();

		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(GameConfig.INPUT_DELAY);
		this.inputDelay.reset();

		this.gameTimer = new GameTimer();
        this.elapsedTime = 0;
		this.finalBoss = null;
		this.omegaBoss = null;
		this.currentPhase = StagePhase.wave;
	}

	/**
	 * Starts the action.
     *
     * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += GameConfig.LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		if (this.inputDelay.checkFinished() && !this.levelFinished) {

			if (!this.gameTimer.isRunning()) {
				this.gameTimer.start();
			}

			// Player controls
			handlePlayerInput(this.ship, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
			if (this.isTwoPlayer) {
				handlePlayerInput(this.ship2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
			}


			switch (this.currentPhase) {
				case wave:
					if (!DropItem.isTimeFreezeActive()) {
						this.enemyShipFormation.update();
						this.enemyShipFormation.shoot(this.bullets);
					}
					if (this.enemyShipFormation.isEmpty()) {
						this.currentPhase = StagePhase.boss_wave;
					}
					break;
				case boss_wave:
					if (this.finalBoss == null && this.omegaBoss == null){
						bossReveal();
						this.enemyShipFormation.clear();
					}
					if(this.finalBoss != null){
						finalbossManage();
					}
					else if (this.omegaBoss != null){
						this.omegaBoss.update();
						if (this.omegaBoss.isDestroyed()) {
							if ("omegaAndFinal".equals(this.currentLevel.getBossId())) {
								this.omegaBoss = null;
                                this.finalBoss = new FinalBoss(this.width / 2 - GameConfig.FINAL_BOSS_SPAWN_OFFSET_X, GameConfig.FINAL_BOSS_SPAWN_OFFSET_Y, this.width, this.height);
                                this.logger.info("Final Boss has spawned!");
								SoundManager.play("sfx/pikachu.wav");
							} else {
								this.levelFinished = true;
								this.screenFinishedCooldown.reset();
							}
						}
					}
					else{
						if(!this.levelFinished){
							this.levelFinished = true;
							this.screenFinishedCooldown.reset();
						}
					}
					break;
			}
			this.ship.update();
			if (this.isTwoPlayer) {
				this.ship2.update();
			}
			this.enemyShipSpecialFormation.update();
		}

		if (this.gameTimer.isRunning()) {
            this.elapsedTime = this.gameTimer.getElapsedTime();
			/**calculate the time*/
			int seconds = (int)(this.elapsedTime/GameConfig.COUNTDOWN_DIVISOR);
			/**get achievement name what they achieve*/
			String unlockedAchievement = AchievementManager.getInstance().onTimeElapsedSeconds(seconds);
			if (unlockedAchievement != null) {
				showAchievement(unlockedAchievement);
			}
        }
        cleanItems();
        collisionManager.manageCollisions();
		cleanBullets();
		draw();

		boolean allPlayersDead = this.ship.isPermanentlyDestroyed() && (!this.isTwoPlayer || this.ship2.isPermanentlyDestroyed());
		if (allPlayersDead && !this.levelFinished) {
			this.levelFinished = true;
			this.screenFinishedCooldown.reset();
			if (this.gameTimer.isRunning()) {
				this.gameTimer.stop();
			}
		}
		if (this.levelFinished && !this.achievementTriggered) {
			this.achievementTriggered = true;

			if (this.gameTimer.isRunning()) {
				this.gameTimer.stop();
			}

			boolean anyPlayerWon = this.lives > 0 || (this.isTwoPlayer && this.livesP2 > 0);
			if (anyPlayerWon) {

				if (this.currentLevel.getCompletionBonus() != null) {
					this.coin += this.currentLevel.getCompletionBonus().getCurrency();
					this.logger.info("Awarded coins for level completion.");
				}

				/** popup achievement at gamescrean when player clear level**/
				String unlockedLevelAchievement = AchievementManager.getInstance().onLevelFinished(this.level);
				if (unlockedLevelAchievement != null) {
					showAchievement(unlockedLevelAchievement);
					this.logger.info("Achievement unlocked: " + unlockedLevelAchievement);
				}
				if (this.isTwoPlayer) {
					String coop = AchievementManager.getInstance().checkCoopLevelFinished(
							this.level,
							this.lives,
							this.livesP2,
							this.maxLives,
							this.score,
							this.scoreP2
					);
					if (coop != null) showAchievement(coop);
				}
			}
		}
		if (this.levelFinished && this.screenFinishedCooldown.checkFinished()) {
			this.isRunning = false;
		}

	}


	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		if (!this.ship.isPermanentlyDestroyed()) {
			drawManager.drawEntity(this.ship, this.ship.getPositionX(),
					this.ship.getPositionY());
		}
		if (this.isTwoPlayer && !this.ship2.isPermanentlyDestroyed()) {
			drawManager.drawEntity(this.ship2, this.ship2.getPositionX(),
					this.ship2.getPositionY());
		}

		enemyShipSpecialFormation.draw();

        /** draw final boss at the field */
        /** draw final boss bullets */
		if(this.finalBoss != null && !this.finalBoss.isDestroyed()){
			for (BossBullet bossBullet : bossBullets) {
				drawManager.drawEntity(bossBullet, bossBullet.getPositionX(), bossBullet.getPositionY());
			}
			drawManager.drawEntity(finalBoss, finalBoss.getPositionX(), finalBoss.getPositionY());
		}

		enemyShipFormation.draw();

		if(this.omegaBoss != null) {
			this.omegaBoss.draw(drawManager);
		}

		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());

		for (DropItem dropItem : this.dropItems)
			drawManager.drawEntity(dropItem, dropItem.getPositionX(), dropItem.getPositionY());

		// Interface.
		if (this.isTwoPlayer) {
			drawManager.drawScore(this, this.score, 1);
			drawManager.drawLives(this, this.lives, 1);
			drawManager.drawScore(this, this.scoreP2, 2);
			drawManager.drawLives(this, this.livesP2, 2);
		} else {
			drawManager.drawScore(this, this.score);
			drawManager.drawLives(this, this.lives);
		}
        drawManager.drawCoin(this,this.coin);
		drawManager.drawTime(this, this.elapsedTime);
		drawManager.drawItemsHUD(this);
		drawManager.drawLevel(this, this.currentLevel.getLevelName());
		drawManager.drawHorizontalLine(this, GameConfig.SEPARATION_LINE_HEIGHT - 1);
		drawManager.drawHorizontalLine(this, GameConfig.ITEMS_SEPARATION_LINE_HEIGHT);

		if (this.achievementText != null && !this.achievementPopupCooldown.checkFinished()) {
			drawManager.drawAchievementPopup(this, this.achievementText);
		} else {
			this.achievementText = null; // clear once expired
		}

		// Health notification popup
		if(this.healthPopupText != null && !this.healthPopupCooldown.checkFinished()) {
			drawManager.drawHealthPopup(this, this.healthPopupText);
		} else {
			this.healthPopupText = null;
		}

		// Countdown to game start.
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((GameConfig.INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.gameState.getLevel(), countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ GameConfig.COUNTDOWN_UI_HEIGHT_DIVISOR);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ GameConfig.COUNTDOWN_UI_HEIGHT_DIVISOR);
		}

		drawManager.completeDrawing(this);
	}


	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets) {
			bullet.update();
			if (bullet.getPositionY() < GameConfig.SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

    /**
     * Cleans Items that go off screen.
     */

    private void cleanItems() {
        Set<DropItem> recyclable = new HashSet<DropItem>();
        for (DropItem dropItem : this.dropItems) {
            dropItem.update();
            if (dropItem.getPositionY() < GameConfig.SEPARATION_LINE_HEIGHT
                    || dropItem.getPositionY() > this.height)
                recyclable.add(dropItem);
        }
        this.dropItems.removeAll(recyclable);
        ItemPool.recycle(recyclable);
    }


	/**
     * Shows an achievement popup message on the HUD.
     */
    public void showAchievement(String message) {
        this.achievementText = message;
        this.achievementPopupCooldown.reset();
		SoundManager.stop("sfx/achievement.wav");
		SoundManager.play("sfx/achievement.wav");
    }

    /**
     * Displays a notification popup when the player gains or loses health
     *
     *  @param message
     *          Text to display in the popup
     */

    public void showHealthPopup(String message) {
        this.healthPopupText = message;
        this.healthPopupCooldown = Core.getCooldown(GameConfig.HEALTH_POPUP_COOLDOWN);
        this.healthPopupCooldown.reset();
    }

    /**
	 * Returns a GameState object representing the status of the game.
     *
     * @return Current game state.
	 */
	    public final GameState getGameState() {
		if (this.coin > GameConfig.GREEDY_COIN_THRESHOLD) {
			if (AchievementManager.getInstance().unlockAchievement("Mr. Greedy")){
				showAchievement("Mr.Greedy");
			}/**popup achievement at gamescrean when player get over 500 coins **/
		}
		GameState newGameState = new GameState(this.level, this.score, this.lives,
				this.bulletsShot, this.shipsDestroyed,this.coin);
		if (this.isTwoPlayer) {
			newGameState.setTwoPlayerMode(this.livesP2);
			newGameState.setScoreP2(this.scoreP2);
		}
		return newGameState;
	}
	/**
	 * Adds one life to the player.
	 */
	public final void gainLife() {
		if (this.lives < this.maxLives) {
			this.lives++;
			showHealthPopup(GameConfig.P1_LIFE_GAIN_MESSAGE);
		}
	}

	/**
	 * Adds one life to player 2.
	 */
	public final void gainLifeP2() {
		if (this.livesP2 < this.maxLives) {
			this.livesP2++;
			showHealthPopup(GameConfig.P2_LIFE_GAIN_MESSAGE);
		}
	}

	public void applyItemEffect(DropItem item, Ship targetShip) {
		switch (item.getItemType()) {
			case Heal:
				if (targetShip.getPlayerId() == 1) gainLife(); else gainLifeP2();
				break;
			case Shield:
				targetShip.activateInvincibility(GameConfig.INVINCIBILITY_DURATION);
				break;
			case Stop:
				DropItem.applyTimeFreezeItem(GameConfig.TIME_FREEZE_DURATION);
				break;
			case Push:
				DropItem.PushbackItem(this.enemyShipFormation,GameConfig.PUSH_BACK_DISTANCE);
				break;
			case Explode:
				int destroyedEnemy = this.enemyShipFormation.destroyAll();
				int pts = destroyedEnemy * GameConfig.EXPLODE_ITEM_POINTS_PER_ENEMY;
				if (targetShip.getPlayerId() == 1) this.score += pts; else this.scoreP2 += pts;
				break;
			case Slow:
				enemyShipFormation.activateSlowdown();
				this.logger.info("Enemy formation slowed down!");
				break;
			default:
				break;
		}
	}

	private void bossReveal() {
		String bossName = this.currentLevel.getBossId();

		if (bossName == null || bossName.isEmpty()) {
			this.logger.info("No boss for this level. Proceeding to finish.");
			return;
		}

		this.logger.info("Spawning boss: " + bossName);
		switch (bossName) {
			case "finalBoss":
				this.finalBoss = new FinalBoss(this.width / 2 - GameConfig.FINAL_BOSS_SPAWN_OFFSET_X, GameConfig.FINAL_BOSS_SPAWN_OFFSET_Y, this.width, this.height);
				this.logger.info("Final Boss has spawned!");
				SoundManager.play("sfx/pikachu.wav");
				break;
			case "omegaBoss":
			case "omegaAndFinal":
				this.omegaBoss = new OmegaBoss(Color.ORANGE, GameConfig.ITEMS_SEPARATION_LINE_HEIGHT);
				omegaBoss.attach(this);
				this.logger.info("Omega Boss has spawned!");
				break;
			default:
				this.logger.warning("Unknown bossId: " + bossName);
				break;
		}
	}


	public void finalbossManage(){
		if (this.finalBoss != null && !this.finalBoss.isDestroyed()) {
			this.finalBoss.update();
			/** called the boss shoot logic */
			if (this.finalBoss.getHealPoint() > this.finalBoss.getMaxHp() / 4) {
				bossBullets.addAll(this.finalBoss.shoot1());
				bossBullets.addAll(this.finalBoss.shoot2());
			} else {
				/** Is the bullet on the screen erased */
				if (!is_cleared) {
					bossBullets.clear();
					is_cleared = true;
					logger.info("boss is angry");
				} else {
					bossBullets.addAll(this.finalBoss.shoot3());
				}
			}

			/** bullets to erase */
			Set<BossBullet> bulletsToRemove = new HashSet<>();

			for (BossBullet b : bossBullets) {
				b.update();
				/** If the bullet goes off the screen */
				if (b.isOffScreen(width, height)) {
                    /** bulletsToRemove carry bullet */
					bulletsToRemove.add(b);
				}
				/** If the bullet collides with ship */
				else if (!this.ship.isPermanentlyDestroyed() && this.collisionManager.checkCollision(b, this.ship)) {
					if (!this.ship.isDestroyed()) {
						this.ship.destroy();
						this.lives--;
						if (this.lives <= 0) {
							this.ship.permanentlyDestroy();
						}
						this.logger.info("Hit on player ship, " + this.lives + " lives remaining.");
					}
					bulletsToRemove.add(b);
				}
				/** If the bullet collides with ship2 */
				else if (this.isTwoPlayer && !this.ship2.isPermanentlyDestroyed() && this.collisionManager.checkCollision(b, this.ship2)) {
					if (!this.ship2.isDestroyed()) {
						this.ship2.destroy();
						this.livesP2--;
						if (this.livesP2 <= 0) {
							this.ship2.permanentlyDestroy();
						}
						this.logger.info("Hit on player 2 ship, " + this.livesP2 + " lives remaining.");
					}
					bulletsToRemove.add(b);
				}
			}
			/** all bullets are removed */
			bossBullets.removeAll(bulletsToRemove);

		}
		if (this.finalBoss != null && this.finalBoss.isDestroyed()) {
			this.levelFinished = true;
			this.screenFinishedCooldown.reset();
		}
	}

	/**
	 * Handles the input for a player's ship.
	 *
	 * @param playerShip The ship to control.
	 * @param upKey The key for moving up.
	 * @param downKey The key for moving down.
	 * @param leftKey The key for moving left.
	 * @param rightKey The key for moving right.
	 * @param fireKey The key for firing.
	 */
	private void handlePlayerInput(final Ship playerShip, final int upKey, final int downKey, final int leftKey, final int rightKey, final int fireKey) {
		if (!playerShip.isPermanentlyDestroyed() && !playerShip.isDestroyed()) {
			boolean right = inputManager.isKeyDown(rightKey);
			boolean left  = inputManager.isKeyDown(leftKey);
			boolean up    = inputManager.isKeyDown(upKey);
			boolean down  = inputManager.isKeyDown(downKey);
			boolean fire  = inputManager.isKeyDown(fireKey);

			boolean isRightBorder = playerShip.getPositionX()
					+ playerShip.getWidth() + playerShip.getSpeed() > this.width - 1;
			boolean isLeftBorder = playerShip.getPositionX() - playerShip.getSpeed() < 1;
			boolean isUpBorder = playerShip.getPositionY() - playerShip.getSpeed() < GameConfig.SEPARATION_LINE_HEIGHT;
			boolean isDownBorder = playerShip.getPositionY()
					+ playerShip.getHeight() + playerShip.getSpeed() > GameConfig.ITEMS_SEPARATION_LINE_HEIGHT;

			if (right && !isRightBorder) playerShip.moveRight();
			if (left  && !isLeftBorder)  playerShip.moveLeft();
			if (up    && !isUpBorder)    playerShip.moveUp();
			if (down  && !isDownBorder)  playerShip.moveDown();

			if (fire && playerShip.shoot(this.bullets)) {
				this.bulletsShot++;
				AchievementManager.getInstance().onShotFired();
			}
		}
	}

	// GETTERS AND SETTERS FOR COLLISION MANAGER
	public Set<Bullet> getBullets() { return this.bullets; }
	public Set<DropItem> getDropItems() { return this.dropItems; }
	public Ship getShip() { return this.ship; }
	public Ship getShip2() { return this.ship2; }
	public boolean isTwoPlayer() { return this.isTwoPlayer; }
	public boolean isLevelFinished() { return this.levelFinished; }
	public EnemyShipFormation getEnemyShipFormation() { return this.enemyShipFormation; }
	public EnemyShipSpecialFormation getEnemyShipSpecialFormation() { return this.enemyShipSpecialFormation; }
	public MidBoss getOmegaBoss() { return this.omegaBoss; }
	public FinalBoss getFinalBoss() { return this.finalBoss; }
	public Cooldown getBossExplosionCooldown() { return this.bossExplosionCooldown; }
	public Logger getLogger() { return this.logger; }
	public Level getCurrentLevel() { return this.currentLevel; }
	public int getLives() { return this.lives; }
	public int getLivesP2() { return this.livesP2; }

	public void addScore(int amount) { this.score += amount; }
	public void addScoreP2(int amount) { this.scoreP2 += amount; }
	public void addCoin(int amount) { this.coin += amount; }
	public void increaseShipsDestroyed() { this.shipsDestroyed++; }
	public void decreaseLives() {
		this.lives--;
		if (this.lives <= 0) {
			this.ship.permanentlyDestroy();
		}
	}
	public void decreaseLivesP2() {
		this.livesP2--;
		if (this.livesP2 <= 0) {
			this.ship2.permanentlyDestroy();
		}
	}
}