package com.invaders;

import engine.Core;
import screen.Screen;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ScreenTest {

    @Test
    public void testScreenConstructorAndGetters() {
        // Core.getDrawManager() and other Core methods might throw exceptions if not initialized.
        // This is a risk of testing without being able to run the code.
        // Assuming Core can be initialized in a test environment.
        try {
            Core.initialize(448, 520, 60);
        } catch (Exception e) {
            // If it fails, we can't test screens.
        }

        Screen screen = new Screen(800, 600, 60);
        assertEquals(800, screen.getWidth());
        assertEquals(600, screen.getHeight());
    }
}
