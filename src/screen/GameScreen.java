package screen;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.GameTimer;
import engine.AchievementManager;
import engine.ItemHUDManager;
import entity.*;
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

	/** Milliseconds until the screen accepts user input. */
	private static final int INPUT_DELAY = 6000;
	/** Bonus score for each life remaining at the end of the level. */
	private static final int LIFE_SCORE = 100;
	/** Minimum time between bonus ship's appearances. */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/** Maximum variance in the time between bonus ship's appearances. */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/** Time until bonus ship explosion disappears. */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/** Time until bonus ship explosion disappears. */
	private static final int BOSS_EXPLOSION = 600;
	/** Time from finishing the level to screen change. */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/** Height of the interface separation line. */
	private static final int SEPARATION_LINE_HEIGHT = 45;
	/** Height of the items separation line (above items). */
	private static final int ITEMS_SEPARATION_LINE_HEIGHT = 400;
    /** Returns the Y-coordinate of the bottom boundary for enemies (above items HUD) */
    public static int getItemsSeparationLineHeight() {
        return ITEMS_SEPARATION_LINE_HEIGHT;
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

	    private GameState gameState;

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
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();
        /** Initialize the bullet Boss fired */
		this.bossBullets = new HashSet<>();
        enemyShipFormation = new EnemyShipFormation(this.currentLevel);
		enemyShipFormation.attach(this);
        this.enemyShipFormation.applyEnemyColorByLevel(this.currentLevel);
		if (this.isTwoPlayer) {
			this.ship = new Ship(this.width / 3, ITEMS_SEPARATION_LINE_HEIGHT - 20, Color.GREEN);
			this.ship.setPlayerId(1);
			this.ship2 = new Ship(this.width * 2 / 3, ITEMS_SEPARATION_LINE_HEIGHT - 20, Color.CYAN);
			this.ship2.setPlayerId(2);
		} else {
			this.ship = new Ship(this.width / 2, ITEMS_SEPARATION_LINE_HEIGHT - 20, Color.GREEN);
		}

        // special enemy initial
		enemyShipSpecialFormation = new EnemyShipSpecialFormation(this.currentLevel,
				Core.getVariableCooldown(BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE),
				Core.getCooldown(BONUS_SHIP_EXPLOSION));
		enemyShipSpecialFormation.attach(this);
		this.bossExplosionCooldown = Core.getCooldown(BOSS_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();
        this.dropItems = new HashSet<DropItem>();

		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
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

		this.score += LIFE_SCORE * (this.lives - 1);
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

			// Player 1 controls
			if (!this.ship.isPermanentlyDestroyed() && !this.ship.isDestroyed()) {
				boolean right = inputManager.isKeyDown(KeyEvent.VK_D);
				boolean left  = inputManager.isKeyDown(KeyEvent.VK_A);
				boolean up    = inputManager.isKeyDown(KeyEvent.VK_W);
				boolean down  = inputManager.isKeyDown(KeyEvent.VK_S);
				boolean fire  = inputManager.isKeyDown(KeyEvent.VK_SPACE);

				boolean isRightBorder = this.ship.getPositionX()
						+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship.getPositionX() - this.ship.getSpeed() < 1;
				boolean isUpBorder = this.ship.getPositionY() - this.ship.getSpeed() < SEPARATION_LINE_HEIGHT;
				boolean isDownBorder = this.ship.getPositionY()
						+ this.ship.getHeight() + this.ship.getSpeed() > ITEMS_SEPARATION_LINE_HEIGHT;

				if (right && !isRightBorder) this.ship.moveRight();
				if (left  && !isLeftBorder)  this.ship.moveLeft();
				if (up    && !isUpBorder)    this.ship.moveUp();
				if (down  && !isDownBorder)  this.ship.moveDown();

				if (fire && this.ship.shoot(this.bullets)) {
					this.bulletsShot++;
					AchievementManager.getInstance().onShotFired();
				}
			}

			// Player 2 controls
			if (this.isTwoPlayer && !this.ship2.isPermanentlyDestroyed() && !this.ship2.isDestroyed()) {
				boolean right = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
				boolean left  = inputManager.isKeyDown(KeyEvent.VK_LEFT);
				boolean up    = inputManager.isKeyDown(KeyEvent.VK_UP);
				boolean down  = inputManager.isKeyDown(KeyEvent.VK_DOWN);
				boolean fire  = inputManager.isKeyDown(KeyEvent.VK_ENTER);

				boolean isRightBorder = this.ship2.getPositionX()
						+ this.ship2.getWidth() + this.ship2.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship2.getPositionX() - this.ship2.getSpeed() < 1;
				boolean isUpBorder = this.ship2.getPositionY() - this.ship2.getSpeed() < SEPARATION_LINE_HEIGHT;
				boolean isDownBorder = this.ship2.getPositionY()
						+ this.ship2.getHeight() + this.ship2.getSpeed() > ITEMS_SEPARATION_LINE_HEIGHT;

				if (right && !isRightBorder) this.ship2.moveRight();
				if (left  && !isLeftBorder)  this.ship2.moveLeft();
				if (up    && !isUpBorder)    this.ship2.moveUp();
				if (down  && !isDownBorder)  this.ship2.moveDown();

				if (fire && this.ship2.shoot(this.bullets)) {
					this.bulletsShot++; // Shared for now
					AchievementManager.getInstance().onShotFired();
				}
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
                                this.finalBoss = new FinalBoss(this.width / 2 - 50, 50, this.width, this.height);
                                this.logger.info("Final Boss has spawned!");
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
				AchievementManager.getInstance().onTimeElapsedSeconds((int)(this.elapsedTime / 1000));
        }
        cleanItems();
        manageBulletShipCollisions();
        manageShipEnemyCollisions();
        manageItemCollisions();
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

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished()) {
			boolean anyPlayerWon = this.lives > 0 || (this.isTwoPlayer && this.livesP2 > 0);
			if (anyPlayerWon) { // Check for win condition
				if (this.currentLevel.getCompletionBonus() != null) {
					this.coin += this.currentLevel.getCompletionBonus().getCurrency();
					this.logger.info("Awarded " + this.currentLevel.getCompletionBonus().getCurrency() + " coins for level completion.");
				}

				String achievement = this.currentLevel.getAchievementTrigger();
				if (achievement != null && !achievement.isEmpty()) {
					AchievementManager.getInstance().unlockAchievement(achievement);
					this.logger.info("Unlocked achievement: " + achievement);
				}
			}
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
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
		drawManager.drawHorizontalLine(this, ITEMS_SEPARATION_LINE_HEIGHT);

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
			int countdown = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.gameState.getLevel(), countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
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
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
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
            if (dropItem.getPositionY() < SEPARATION_LINE_HEIGHT
                    || dropItem.getPositionY() > this.height)
                recyclable.add(dropItem);
        }
        this.dropItems.removeAll(recyclable);
        ItemPool.recycle(recyclable);
    }

	/**
	 * Manages collisions between bullets and ships.
	 */
	private void manageBulletShipCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets)
			if (bullet.getSpeed() > 0) { // Enemy bullet
				if (!this.ship.isPermanentlyDestroyed() && checkCollision(bullet, this.ship) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.ship.isInvincible()) {
						if (!this.ship.isDestroyed()) {
							this.ship.destroy();
							this.lives--;
							if (this.lives <= 0) {
								this.ship.permanentlyDestroy();
							}
							showHealthPopup("-1 Health (P1)");
							this.logger.info("Hit on player 1 ship, " + this.lives
									+ " lives remaining.");
						}
					}
				}
				if (this.isTwoPlayer && !this.ship2.isPermanentlyDestroyed() && this.ship2 != null && checkCollision(bullet, this.ship2) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.ship2.isInvincible()) {
						if (!this.ship2.isDestroyed()) {
							this.ship2.destroy();
							this.livesP2--;
							if (this.livesP2 <= 0) {
								this.ship2.permanentlyDestroy();
							}
							showHealthPopup("-1 Health (P2)");
							this.logger.info("Hit on player 2 ship, " + this.livesP2
									+ " lives remaining.");
						}
					}
				}
			} else { // Player bullet
				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed()
							&& checkCollision(bullet, enemyShip)) {
						int pts = enemyShip.getPointValue();
						Integer ownerId = bullet.getOwnerId();
						if (ownerId != null && ownerId == 2) {
							this.scoreP2 += pts;
						} else {
							this.score += pts;
						}
						this.coin += (pts / 10);
						this.shipsDestroyed++;

						String enemyType = enemyShip.getEnemyType();
						this.enemyShipFormation.destroy(enemyShip);
						AchievementManager.getInstance().onEnemyDefeated();
						if (enemyType != null && this.currentLevel.getItemDrops() != null) {
							List<engine.level.ItemDrop> potentialDrops = new ArrayList<>();
							for (engine.level.ItemDrop itemDrop : this.currentLevel.getItemDrops()) {
								if (enemyType.equals(itemDrop.getEnemyType())) {
									potentialDrops.add(itemDrop);
								}
							}

							List<engine.level.ItemDrop> successfulDrops = new ArrayList<>();
							for (engine.level.ItemDrop itemDrop : potentialDrops) {
								if (Math.random() < itemDrop.getDropChance()) {
									successfulDrops.add(itemDrop);
								}
							}

							if (!successfulDrops.isEmpty()) {
								engine.level.ItemDrop selectedDrop = successfulDrops.get((int) (Math.random() * successfulDrops.size()));
								DropItem.ItemType droppedType = DropItem.fromString(selectedDrop.getItemId());
								if (droppedType != null) {
									final int ITEM_DROP_SPEED = 2;

									DropItem newDropItem = ItemPool.getItem(
											enemyShip.getPositionX() + enemyShip.getWidth() / 2,
											enemyShip.getPositionY() + enemyShip.getHeight() / 2,
											ITEM_DROP_SPEED,
											droppedType
									);
									this.dropItems.add(newDropItem);
									this.logger.info("An item (" + droppedType + ") dropped");
								}
							}
						}
						if (!bullet.penetration()) {
							recyclable.add(bullet);
							break;
						}
					}

				for (EnemyShip enemyShipSpecial : this.enemyShipSpecialFormation)
					if (enemyShipSpecial != null && !enemyShipSpecial.isDestroyed()
							&& checkCollision(bullet, enemyShipSpecial)) {
						int pts = enemyShipSpecial.getPointValue();
						Integer ownerId = bullet.getOwnerId();
						if (ownerId != null && ownerId == 2) {
							this.scoreP2 += pts;
						} else {
							this.score += pts;
						}
						this.coin += (pts / 10);
						this.shipsDestroyed++;
						this.enemyShipSpecialFormation.destroy(enemyShipSpecial);
						recyclable.add(bullet);
					}
				if (this.omegaBoss != null
						&& !this.omegaBoss.isDestroyed()
						&& checkCollision(bullet, this.omegaBoss)) {
					this.omegaBoss.takeDamage(2);
					if(this.omegaBoss.getHealPoint() <= 0) {
						this.shipsDestroyed++;
						int pts = this.omegaBoss.getPointValue();
						Integer ownerId = bullet.getOwnerId();
						if (ownerId != null && ownerId == 2) {
							this.scoreP2 += pts;
						} else {
							this.score += pts;
						}
						this.coin += (pts / 10);
						this.omegaBoss.destroy();
						AchievementManager.getInstance().unlockAchievement("Boss Slayer");
						this.bossExplosionCooldown.reset();
					}
					recyclable.add(bullet);
				}

				if(this.finalBoss != null && !this.finalBoss.isDestroyed() && checkCollision(bullet,this.finalBoss)){
					this.finalBoss.takeDamage(1);
					if(this.finalBoss.getHealPoint() <= 0){
						int pts = this.finalBoss.getPointValue();
						Integer ownerId = bullet.getOwnerId();
						if (ownerId != null && ownerId == 2) {
							this.scoreP2 += pts;
						} else {
							this.score += pts;
						}
						this.coin += (pts / 10);
						this.finalBoss.destroy();
                        AchievementManager.getInstance().unlockAchievement("Boss Slayer");
					}
					recyclable.add(bullet);
				}
            }
        this.bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    /**
     * Manages collisions between player ship and enemy ships.
     */
    private void manageShipEnemyCollisions() {
        // Player 1 collision checks
        if (!this.ship.isPermanentlyDestroyed() && !this.ship.isDestroyed() && !this.ship.isInvincible()) {
            for (EnemyShip enemyShip : this.enemyShipFormation) {
                if (checkCollision(this.ship, enemyShip)) {
                    this.enemyShipFormation.destroy(enemyShip);
                    this.ship.destroy();
                    this.lives--;
					if (this.lives <= 0) {
						this.ship.permanentlyDestroy();
					}
                    showHealthPopup("-1 Life (P1 Collision!)");
                    this.logger.info("Ship 1 collided with enemy! " + this.lives + " lives remaining.");
                    return;
                }
            }
            for (EnemyShip enemyShipSpecial : this.enemyShipSpecialFormation) {
                if (enemyShipSpecial != null && !enemyShipSpecial.isDestroyed() && checkCollision(this.ship, enemyShipSpecial)) {
                    this.enemyShipSpecialFormation.destroy(enemyShipSpecial);
                    this.ship.destroy();
                    this.lives--;
					if (this.lives <= 0) {
						this.ship.permanentlyDestroy();
					}
                    showHealthPopup("-1 Life (P1 Collision!)");
                    this.logger.info("Ship 1 collided with special enemy! " + this.lives + " lives remaining.");
                    return;
                }
            }
            if (this.omegaBoss != null && !this.omegaBoss.isDestroyed() && checkCollision(this.ship, this.omegaBoss)) {
                this.ship.destroy();
                this.lives--;
				if (this.lives <= 0) {
					this.ship.permanentlyDestroy();
				}
                showHealthPopup("-1 Life (P1 Boss Collision!)");
                this.logger.info("Ship 1 collided with omega boss! " + this.lives + " lives remaining.");
                return;
            }
            if (this.finalBoss != null && !this.finalBoss.isDestroyed() && checkCollision(this.ship, this.finalBoss)) {
                this.ship.destroy();
                this.lives--;
				if (this.lives <= 0) {
					this.ship.permanentlyDestroy();
				}
                showHealthPopup("-1 Life (P1 Boss Collision!)");
                this.logger.info("Ship 1 collided with final boss! " + this.lives + " lives remaining.");
                return;
            }
        }

        // Player 2 collision checks
        if (this.isTwoPlayer && !this.ship2.isPermanentlyDestroyed() && this.ship2 != null && !this.ship2.isDestroyed() && !this.ship2.isInvincible()) {
            for (EnemyShip enemyShip : this.enemyShipFormation) {
                if (checkCollision(this.ship2, enemyShip)) {
                    this.enemyShipFormation.destroy(enemyShip);
                    this.ship2.destroy();
                    this.livesP2--;
					if (this.livesP2 <= 0) {
						this.ship2.permanentlyDestroy();
					}
                    showHealthPopup("-1 Life (P2 Collision!)");
                    this.logger.info("Ship 2 collided with enemy! " + this.livesP2 + " lives remaining.");
                    return;
                }
            }
            for (EnemyShip enemyShipSpecial : this.enemyShipSpecialFormation) {
                if (enemyShipSpecial != null && !enemyShipSpecial.isDestroyed() && checkCollision(this.ship2, enemyShipSpecial)) {
                    this.enemyShipSpecialFormation.destroy(enemyShipSpecial);
                    this.ship2.destroy();
                    this.livesP2--;
					if (this.livesP2 <= 0) {
						this.ship2.permanentlyDestroy();
					}
                    showHealthPopup("-1 Life (P2 Collision!)");
                    this.logger.info("Ship 2 collided with special enemy! " + this.livesP2 + " lives remaining.");
                    return;
                }
            }
            if (this.omegaBoss != null && !this.omegaBoss.isDestroyed() && checkCollision(this.ship2, this.omegaBoss)) {
                this.ship2.destroy();
                this.livesP2--;
				if (this.livesP2 <= 0) {
					this.ship2.permanentlyDestroy();
				}
                showHealthPopup("-1 Life (P2 Boss Collision!)");
                this.logger.info("Ship 2 collided with omega boss! " + this.livesP2 + " lives remaining.");
                return;
            }
            if (this.finalBoss != null && !this.finalBoss.isDestroyed() && checkCollision(this.ship2, this.finalBoss)) {
                this.ship2.destroy();
                this.livesP2--;
				if (this.livesP2 <= 0) {
					this.ship2.permanentlyDestroy();
				}
                showHealthPopup("-1 Life (P2 Boss Collision!)");
                this.logger.info("Ship 2 collided with final boss! " + this.livesP2 + " lives remaining.");
                return;
            }
        }
    }

    /**
     * Manages collisions between player ship and dropped items.
     */
    private void manageItemCollisions() {
        Set<DropItem> acquiredDropItems = new HashSet<DropItem>();
        for (DropItem dropItem : this.dropItems) {
            boolean p1Collision = !this.ship.isPermanentlyDestroyed() && !this.ship.isDestroyed() && checkCollision(this.ship, dropItem);
            boolean p2Collision = this.isTwoPlayer && this.ship2 != null && !this.ship2.isPermanentlyDestroyed() && !this.ship2.isDestroyed() && checkCollision(this.ship2, dropItem);

            if (p1Collision && p2Collision) {
                int p1Distance = distance(this.ship, dropItem);
                int p2Distance = distance(this.ship2, dropItem);

                if (p1Distance <= p2Distance) { // P1 is closer or equidistant
                    this.logger.info("Player 1 acquired dropItem: " + dropItem.getItemType());
                    ItemHUDManager.getInstance().addDroppedItem(dropItem.getItemType());
                    applyItemEffect(dropItem, this.ship);
                    acquiredDropItems.add(dropItem);
                } else { // P2 is closer
                    this.logger.info("Player 2 acquired dropItem: " + dropItem.getItemType());
                    ItemHUDManager.getInstance().addDroppedItem(dropItem.getItemType());
                    applyItemEffect(dropItem, this.ship2);
                    acquiredDropItems.add(dropItem);
                }
            } else if (p1Collision) {
                this.logger.info("Player 1 acquired dropItem: " + dropItem.getItemType());
                ItemHUDManager.getInstance().addDroppedItem(dropItem.getItemType());
                applyItemEffect(dropItem, this.ship);
                acquiredDropItems.add(dropItem);
            } else if (p2Collision) {
                this.logger.info("Player 2 acquired dropItem: " + dropItem.getItemType());
                ItemHUDManager.getInstance().addDroppedItem(dropItem.getItemType());
                applyItemEffect(dropItem, this.ship2);
                acquiredDropItems.add(dropItem);
            }
        }
        this.dropItems.removeAll(acquiredDropItems);
        ItemPool.recycle(acquiredDropItems);
    }

    private int distance(final Entity a, final Entity b) {
        int centerAX = a.getPositionX() + a.getWidth() / 2;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        
        double distanceX = centerAX - centerBX;
        double distanceY = centerAY - centerBY;
        
        return (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }


	/**
	 * Checks if two entities are colliding.
	 */
	private boolean checkCollision(final Entity a, final Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

    /**
     * Shows an achievement popup message on the HUD.
     */
    public void showAchievement(String message) {
        this.achievementText = message;
        this.achievementPopupCooldown = Core.getCooldown(2500); // Show for 2.5 seconds
        this.achievementPopupCooldown.reset();
    }

    /**
     * Displays a notification popup when the player gains or loses health
     *
     *  @param message
     *          Text to display in the popup
     */

    public void showHealthPopup(String message) {
        this.healthPopupText = message;
        this.healthPopupCooldown = Core.getCooldown(500);
        this.healthPopupCooldown.reset();
    }

    /**
	 * Returns a GameState object representing the status of the game.
     *
     * @return Current game state.
	 */
	    public final GameState getGameState() {
		if (this.coin > 2000) {
			AchievementManager.getInstance().unlockAchievement("Mr. Greedy");
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
			showHealthPopup("+1 Life (P1)");
		}
	}

	/**
	 * Adds one life to player 2.
	 */
	public final void gainLifeP2() {
		if (this.livesP2 < this.maxLives) {
			this.livesP2++;
			showHealthPopup("+1 Life (P2)");
		}
	}

	private void applyItemEffect(DropItem item, Ship targetShip) {
		switch (item.getItemType()) {
			case Heal:
				if (targetShip.getPlayerId() == 1) gainLife(); else gainLifeP2();
				break;
			case Shield:
				targetShip.activateInvincibility(5000);
				break;
			case Stop:
				DropItem.applyTimeFreezeItem(3000);
				break;
			case Push:
				DropItem.PushbackItem(this.enemyShipFormation,20);
				break;
			case Explode:
				int destroyedEnemy = this.enemyShipFormation.destroyAll();
				int pts = destroyedEnemy * 5;
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
				this.finalBoss = new FinalBoss(this.width / 2 - 50, 50, this.width, this.height);
				this.logger.info("Final Boss has spawned!");
				break;
			case "omegaBoss":
			case "omegaAndFinal":
				this.omegaBoss = new OmegaBoss(Color.ORANGE, ITEMS_SEPARATION_LINE_HEIGHT);
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
				else if (!this.ship.isPermanentlyDestroyed() && this.checkCollision(b, this.ship)) {
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
				else if (this.isTwoPlayer && !this.ship2.isPermanentlyDestroyed() && this.ship2 != null && this.checkCollision(b, this.ship2)) {
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
}