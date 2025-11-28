package test.java.com.invaders;

import engine.level.LevelManager;
import engine.level.Level;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LevelManagerTest {

    @Test
    public void testGetNumberOfLevels() {
        LevelManager levelManager = new LevelManager();
        // Assuming maps.json has 7 levels
        assertEquals(7, levelManager.getNumberOfLevels());
    }

    @Test
    public void testGetLevel_ValidLevel() {
        LevelManager levelManager = new LevelManager();
        Level level1 = levelManager.getLevel(1);
        assertNotNull(level1);
        assertEquals(1, level1.getLevel());
        assertEquals("1. Contact", level1.getLevelName());
        assertEquals(5, level1.getFormationWidth());
        assertEquals(4, level1.getFormationHeight());
        assertEquals(60, level1.getBaseSpeed());
        assertEquals(2000, level1.getShootingFrecuency());
    }

    @Test
    public void testGetLevel_InvalidLevel() {
        LevelManager levelManager = new LevelManager();
        Level level0 = levelManager.getLevel(0);
        assertNull(level0);
        Level level8 = levelManager.getLevel(8);
        assertNull(level8);
    }
}
