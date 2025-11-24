package com.invaders;

import engine.GameSettings;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void testGameSettingsConstructorAndGetters() {
        GameSettings settings = new GameSettings(10, 5, 100, 500);
        assertEquals(10, settings.getFormationWidth());
        assertEquals(5, settings.getFormationHeight());
        assertEquals(100, settings.getBaseSpeed());
        assertEquals(500, settings.getShootingFrecuency());
    }

    @Test
    public void testGameSettingsWithZeroValues() {
        GameSettings settings = new GameSettings(0, 0, 0, 0);
        assertEquals(0, settings.getFormationWidth());
        assertEquals(0, settings.getFormationHeight());
        assertEquals(0, settings.getBaseSpeed());
        assertEquals(0, settings.getShootingFrecuency());
    }

    @Test
    public void testGameSettingsWithDifferentValues() {
        GameSettings settings = new GameSettings(20, 10, 200, 1000);
        assertEquals(20, settings.getFormationWidth());
        assertEquals(10, settings.getFormationHeight());
        assertEquals(200, settings.getBaseSpeed());
        assertEquals(1000, settings.getShootingFrecuency());
    }
}
