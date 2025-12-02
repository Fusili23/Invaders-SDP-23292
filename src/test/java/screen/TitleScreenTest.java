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

    private TitleScreen titleScreen;

    @Before
    public void setUp() {
        titleScreen = new TitleScreen(800, 600, 60);
    }
    
    /**
     * Helper method to set the internal state of the screen for testing.
     * This is acceptable for tests as it avoids reflection and sets up a precondition.
     * @param code The return code to set.
     */
    private void setReturnCode(int code) {
        // The field 'returnCode' is protected in the 'Screen' superclass,
        // so we can access it directly from a test in the same package.
        // To make it explicit, we create a subclass for testing.
        class TestTitleScreen extends TitleScreen {
            TestTitleScreen() { super(800, 600, 60); }
            void setCode(int c) { this.returnCode = c; }
        }
        TestTitleScreen testScreen = (TestTitleScreen) this.titleScreen;
        testScreen.setCode(code);
    }

    @Test
    public void nextMenuItem_fromPlay_goesToHighScores() {
        setReturnCode(PLAY_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(HIGH_SCORES_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void nextMenuItem_fromHighScores_goesToAchievements() {
        setReturnCode(HIGH_SCORES_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(ACHIEVEMENTS_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void nextMenuItem_fromAchievements_goesToShop() {
        setReturnCode(ACHIEVEMENTS_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(SHOP_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void nextMenuItem_fromShop_goesToExit() {
        setReturnCode(SHOP_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(EXIT_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void nextMenuItem_fromExit_wrapsToPlay() {
        setReturnCode(EXIT_RETURN_CODE);
        titleScreen.nextMenuItem();
        assertEquals(PLAY_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void previousMenuItem_fromPlay_wrapsToExit() {
        setReturnCode(PLAY_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(EXIT_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void previousMenuItem_fromExit_goesToShop() {
        setReturnCode(EXIT_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(SHOP_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void previousMenuItem_fromShop_goesToAchievements() {
        setReturnCode(SHOP_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(ACHIEVEMENTS_RETURN_CODE, titleScreen.getReturnCode());
    }
    
    @Test
    public void previousMenuItem_fromAchievements_goesToHighScores() {
        setReturnCode(ACHIEVEMENTS_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(HIGH_SCORES_RETURN_CODE, titleScreen.getReturnCode());
    }

    @Test
    public void previousMenuItem_fromHighScores_goesToPlay() {
        setReturnCode(HIGH_SCORES_RETURN_CODE);
        titleScreen.previousMenuItem();
        assertEquals(PLAY_RETURN_CODE, titleScreen.getReturnCode());
    }
}
