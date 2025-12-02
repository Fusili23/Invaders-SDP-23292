package screen;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TitleScreenTest {

    // Constants for menu items to make tests more readable and less brittle.
    private static final int PLAY_RETURN_CODE = 2;
    private static final int HIGH_SCORES_RETURN_CODE = 3;
    private static final int SHOP_RETURN_CODE = 4;
    private static final int ACHIEVEMENTS_RETURN_CODE = 6;
    private static final int EXIT_RETURN_CODE = 0;

    private TestableTitleScreen titleScreen;

    /**
     * A test-specific subclass of TitleScreen that exposes a method to set
     * the internal returnCode for testing state transitions.
     */
    private static class TestableTitleScreen extends TitleScreen {
        TestableTitleScreen() {
            super(800, 600, 60);
        }

        public void setReturnCode(int code) {
            this.returnCode = code;
        }
    }

    @Before
    public void setUp() {
        titleScreen = new TestableTitleScreen();
    }

    @Test
    public void nextMenuItem_fromPlay_goesToHighScores() {
        titleScreen.setReturnCode(PLAY_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(HIGH_SCORES_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void nextMenuItem_fromHighScores_goesToAchievements() {
        titleScreen.setReturnCode(HIGH_SCORES_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(ACHIEVEMENTS_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void nextMenuItem_fromAchievements_goesToShop() {
        titleScreen.setReturnCode(ACHIEVEMENTS_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(SHOP_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void nextMenuItem_fromShop_goesToExit() {
        titleScreen.setReturnCode(SHOP_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(EXIT_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void nextMenuItem_fromExit_wrapsToPlay() {
        titleScreen.setReturnCode(EXIT_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(PLAY_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void previousMenuItem_fromPlay_wrapsToExit() {
        titleScreen.setReturnCode(PLAY_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(EXIT_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void previousMenuItem_fromExit_goesToShop() {
        titleScreen.setReturnCode(EXIT_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(SHOP_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void previousMenuItem_fromShop_goesToAchievements() {
        titleScreen.setReturnCode(SHOP_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(ACHIEVEMENTS_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void previousMenuItem_fromAchievements_goesToHighScores() {
        titleScreen.setReturnCode(ACHIEVEMENTS_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(HIGH_SCORES_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void previousMenuItem_fromHighScores_goesToPlay() {
        titleScreen.setReturnCode(HIGH_SCORES_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(PLAY_RETURN_CODE, titleScreen.getReturnCode());
    }
}

