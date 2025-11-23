package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Core;
import engine.Score;

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class HighScoreScreen extends Screen {

	/** List of past 1P high scores. */
	private List<Score> highScores1P;
	/** List of past 2P high scores. */
	private List<Score> highScores2P;
	/** Check if showing 2P scores. */
	private boolean showing2P;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public HighScoreScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = 1;
		this.showing2P = false;

		try {
			this.highScores1P = Core.getFileManager().loadHighScores(false);
			this.highScores2P = Core.getFileManager().loadHighScores(true);
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				this.isRunning = false;
			}
			if (inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
				this.showing2P = !this.showing2P;
				this.inputDelay.reset();
			}
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawHighScoreMenu(this, this.showing2P);
		if (this.showing2P) {
			drawManager.drawHighScores(this, this.highScores2P);
		} else {
			drawManager.drawHighScores(this, this.highScores1P);
		}

		drawManager.completeDrawing(this);
	}
}
