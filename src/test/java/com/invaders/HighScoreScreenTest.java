package com.invaders;

import screen.HighScoreScreen;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.assertFalse;

public class HighScoreScreenTest {

    @Test
    public void testHighScoreScreenConstructor() throws Exception {
        // This test is very limited due to file I/O operations in the constructor.
        // We can only test the initial state of the 'showing2P' flag.
        HighScoreScreen highScoreScreen = new HighScoreScreen(800, 600, 60);

        Field showing2PField = HighScoreScreen.class.getDeclaredField("showing2P");
        showing2PField.setAccessible(true);
        boolean showing2P = (boolean) showing2PField.get(highScoreScreen);

        assertFalse(showing2P);
    }
}
