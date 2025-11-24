package engine;

/**
 * Implements an object that stores the state of the game between levels.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameState {

	/** [Easter egg] Special value for hidden stage entry. */
	public static final int HIDDEN_STAGE = -1;

	/** Current game level. */
	private int level;
	/** Current score. */
	private int score;
	/** Lives currently remaining. */
	private int livesRemaining;
	/** Bullets shot until now. */
	private int bulletsShot;
	/** Ships destroyed until now. */
	private int shipsDestroyed;
	/** Current coin. */
	private int coin;

	// Fields for 2P mode
	private boolean isTwoPlayer = false;
	private int scoreP2 = 0;
	private int livesRemainingP2 = 0;

	/**
	 * Constructor.
	 *
	 * @param level          Current game level.
	 * @param score          Current score.
	 * @param livesRemaining Lives currently remaining.
	 * @param bulletsShot    Bullets shot until now.
	 * @param shipsDestroyed Ships destroyed until now.
	 * @param coin           Current coin.
	 */
	public GameState(final int level, final int score,
					 final int livesRemaining, final int bulletsShot,
					 final int shipsDestroyed, final int coin) {
		this.level = level;
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		this.coin = coin;
	}

	/**
	 * Constructor for transitioning to the next level.
	 * @param previousGameState The state of the previous level.
	 */
	public GameState(final GameState previousGameState) {
		this.level = previousGameState.getLevel() + 1;
		this.score = previousGameState.getScore();
		this.livesRemaining = previousGameState.getLivesRemaining();
		this.bulletsShot = previousGameState.getBulletsShot();
		this.shipsDestroyed = previousGameState.getShipsDestroyed();
		this.coin = previousGameState.getCoin();

		this.isTwoPlayer = previousGameState.isTwoPlayer();
		if (this.isTwoPlayer) {
			this.scoreP2 = previousGameState.getScoreP2();
			this.livesRemainingP2 = previousGameState.getLivesRemainingP2();
		}
	}

	/**
	 * [Easter egg] Clones current game state for hidden stage transition.
	 * @return Deep copy of the current GameState.
	 */
	public GameState clone() {
		GameState copy = new GameState(
				this.level, this.score, this.livesRemaining,
				this.bulletsShot, this.shipsDestroyed, this.coin
		);
		// Clone 2P mode data as well
		copy.isTwoPlayer = this.isTwoPlayer;
		copy.scoreP2 = this.scoreP2;
		copy.livesRemainingP2 = this.livesRemainingP2;
		return copy;
	}

	/**
	 * [Easter egg] Restores game state from a backup after hidden stage completion.
	 * @param state The GameState to restore from.
	 */
	public void restore(GameState state) {
		this.level = state.level;
		this.score = state.score;
		this.livesRemaining = state.livesRemaining;
		this.bulletsShot = state.bulletsShot;
		this.shipsDestroyed = state.shipsDestroyed;
		this.coin = state.coin;

		this.isTwoPlayer = state.isTwoPlayer;
		this.scoreP2 = state.scoreP2;
		this.livesRemainingP2 = state.livesRemainingP2;
	}

	/**
	 * Activates 2-player mode with starting lives.
	 * @param startingLivesP2 lives for player 2.
	 */
	public final void setTwoPlayerMode(int startingLivesP2) {
		this.isTwoPlayer = true;
		this.livesRemainingP2 = startingLivesP2;
	}

	/** @return True if the game is in 2-player mode. */
	public final boolean isTwoPlayer() {
		return this.isTwoPlayer;
	}

	/** @return the level */
	public final int getLevel() {
		return level;
	}

	/**
	 * [Easter egg] Sets the game level, e.g. for hidden stage transitions.
	 * @param level New game level value.
	 */
	public final void setLevel(int level) {
		this.level = level;
	}

	/** @return the score */
	public final int getScore() {
		return score;
	}

	/** @return the score for player 2 */
	public final int getScoreP2() {
		return scoreP2;
	}

	/**
	 * Adds points to player 1's score.
	 * @param amount Points to add.
	 */
	public final void addScore(final int amount) {
		this.score += amount;
	}

	/**
	 * Adds points to player 2's score.
	 * @param amount Points to add.
	 */
	public final void addScoreP2(final int amount) {
		this.scoreP2 += amount;
	}

	/**
	 * Sets the score for player 2.
	 * @param score The new score for player 2.
	 */
	public final void setScoreP2(final int score) {
		this.scoreP2 = score;
	}

	/** @return the livesRemaining */
	public final int getLivesRemaining() {
		return livesRemaining;
	}

	/** @return the livesRemaining for player 2 */
	public final int getLivesRemainingP2() {
		return livesRemainingP2;
	}

	/** Player 1 loses a life. */
	public final void loseLife() {
		this.livesRemaining--;
	}

	/** Player 2 loses a life. */
	public final void loseLifeP2() {
		this.livesRemainingP2--;
	}

	/** @return the bulletsShot */
	public final int getBulletsShot() {
		return bulletsShot;
	}

	/** @return the shipsDestroyed */
	public final int getShipsDestroyed() {
		return shipsDestroyed;
	}

	/** @return the current coin count */
	public final int getCoin() { return coin; }

	/**
	 * Deducts coins if amount is valid and there are enough coins.
	 * @param amount Amount to deduct.
	 * @return True if coins were successfully deducted.
	 */
	public final boolean deductCoins(final int amount) {
		if (amount < 0) {
			return false;
		}
		if (this.coin >= amount) {
			this.coin -= amount;
			return true;
		}
		return false;
	}

	/**
	 * Adds coins.
	 * @param amount Amount to add.
	 */
	public final void addCoins(final int amount) {
		if (amount > 0) {
			this.coin += amount;
		}
	}

	/**
	 * Sets the coin count.
	 * @param amount New coin value.
	 */
	public final void setCoins(final int amount) {
		if (amount >= 0) {
			this.coin = amount;
		}
	}
}
