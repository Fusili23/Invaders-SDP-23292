package screen;

import java.awt.event.KeyEvent;
import java.util.List;

import engine.Achievement;
import engine.AchievementManager;
import engine.Core;

/**
 * Implements the achievement screen, which displays the player's achievements.
 */
public class AchievementScreen extends Screen {

    /**
     * Constructor for the AchievementScreen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second.
     */
    /** current page number (0: single achievement page, 1: 2p achievement page) */
    private int currentPage = 0;


    public AchievementScreen(int width, int height, int fps) {
        super(width, height, fps);
        this.returnCode = 1; // Default return code

    }

    /**
     * Initializes the screen elements.
     */
    @Override
    public void initialize() {
        super.initialize();
        this.currentPage = 0;
    }

    /**
     * Runs the screen's main loop.
     *
     * @return The screen's return code.
     */
    @Override
    public int run() {
        super.run();
        return this.returnCode;
    }

    /**
     * Updates the screen's state.
     */
    @Override
    protected void update() {
        super.update();
        draw();
        if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
            this.isRunning = false;
        }
        if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
            this.currentPage = 1;
        }
        if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
            this.currentPage = 0;
        }
    }

    /**
     * Draws the achievements on the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawAchievements(this, this.currentPage);
        drawManager.completeDrawing(this);
    }
}
