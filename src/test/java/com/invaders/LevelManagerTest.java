package com.invaders;

import engine.level.Level;
import engine.level.LevelManager;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * A test class for LevelManager that uses a mock LevelManager to avoid file I/O.
 */
class MockLevelManager extends LevelManager {
    public MockLevelManager(List<Level> levels) {
        super(levels);
    }
}

public class LevelManagerTest {

    private LevelManager levelManager;
    private List<Level> mockLevels;

    @Before
    public void setUp() {
        mockLevels = new ArrayList<>();
        // Using LinkedHashMap to ensure consistent field order if needed, and then creating level
        java.util.Map<String, Object> level1Map = new java.util.LinkedHashMap<>();
        level1Map.put("level", 1.0); // Numbers are parsed as doubles by the custom parser
        level1Map.put("levelName", "Mock Level 1");
        level1Map.put("formationWidth", 10.0);
        level1Map.put("formationHeight", 2.0);
        level1Map.put("baseSpeed", 100.0);
        level1Map.put("shootingFrecuency", 3000.0);
        mockLevels.add(new Level(level1Map));

        java.util.Map<String, Object> level2Map = new java.util.LinkedHashMap<>();
        level2Map.put("level", 2.0);
        level2Map.put("levelName", "Mock Level 2");
        level2Map.put("formationWidth", 20.0);
        level2Map.put("formationHeight", 4.0);
        level2Map.put("baseSpeed", 200.0);
        level2Map.put("shootingFrecuency", 4000.0);
        mockLevels.add(new Level(level2Map));

        levelManager = new MockLevelManager(mockLevels);
    }

    @Test
    public void getNumberOfLevels_returnsCorrectCount() {
        assertEquals("Should return the count of mock levels provided.", 2, levelManager.getNumberOfLevels());
    }

    @Test
    public void getNumberOfLevels_whenNoLevels_returnsZero() {
        LevelManager emptyManager = new MockLevelManager(Collections.emptyList());
        assertEquals("Should return 0 when no levels are loaded.", 0, emptyManager.getNumberOfLevels());
    }

    @Test
    public void getLevel_forValidLevel_returnsCorrectLevel() {
        Level level1 = levelManager.getLevel(1);
        assertNotNull("Should find and return level 1.", level1);
        assertEquals(1, level1.getLevel());
        assertEquals("Mock Level 1", level1.getLevelName());
        assertEquals(10, level1.getFormationWidth());
        assertEquals(100, level1.getBaseSpeed());
    }

    @Test
    public void getLevel_forAnotherValidLevel_returnsCorrectLevel() {
        Level level2 = levelManager.getLevel(2);
        assertNotNull("Should find and return level 2.", level2);
        assertEquals(2, level2.getLevel());
        assertEquals("Mock Level 2", level2.getLevelName());
        assertEquals(20, level2.getFormationWidth());
        assertEquals(200, level2.getBaseSpeed());
    }

    @Test
    public void getLevel_forInvalidLevel_returnsNull() {
        Level level3 = levelManager.getLevel(3);
        assertNull("Should return null for a level that does not exist.", level3);
    }

    @Test
    public void getLevel_forZeroOrNegativeLevel_returnsNull() {
        Level level0 = levelManager.getLevel(0);
        assertNull("Should return null for level 0.", level0);
        Level levelNegative = levelManager.getLevel(-1);
        assertNull("Should return null for a negative level.", levelNegative);
    }
}
