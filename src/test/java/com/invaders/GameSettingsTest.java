package com.invaders;

import engine.GameSettings;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void constructor_initializesAllSettingsCorrectly() {
        // Test with standard values
        GameSettings settings1 = new GameSettings(10, 5, 100, 500);
        assertEquals(10, settings1.getFormationWidth());
        assertEquals(5, settings1.getFormationHeight());
        assertEquals(100, settings1.getBaseSpeed());
        assertEquals(500, settings1.getShootingFrecuency());

        // Test with zero values
        GameSettings settings2 = new GameSettings(0, 0, 0, 0);
        assertEquals(0, settings2.getFormationWidth());
        assertEquals(0, settings2.getFormationHeight());
        assertEquals(0, settings2.getBaseSpeed());
        assertEquals(0, settings2.getShootingFrecuency());

        // Test with different values
        GameSettings settings3 = new GameSettings(20, 10, 200, 1000);
        assertEquals(20, settings3.getFormationWidth());
        assertEquals(10, settings3.getFormationHeight());
        assertEquals(200, settings3.getBaseSpeed());
        assertEquals(1000, settings3.getShootingFrecuency());
    }
}
