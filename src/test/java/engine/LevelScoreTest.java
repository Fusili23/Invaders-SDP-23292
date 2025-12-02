package engine;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LevelScoreTest {

    @Test
    public void constructor_initializesAllFields() {
        LevelScore levelScore = new LevelScore(5, 1000);
        assertEquals(5, levelScore.getLevel());
        assertEquals(1000, levelScore.getScore());
    }

    @Test
    public void constructor_withZeroValues() {
        LevelScore levelScore = new LevelScore(0, 0);
        assertEquals(0, levelScore.getLevel());
        assertEquals(0, levelScore.getScore());
    }

    @Test
    public void constructor_withNegativeScore() {
        // Edge case: negative score
        LevelScore levelScore = new LevelScore(1, -100);
        assertEquals(1, levelScore.getLevel());
        assertEquals(-100, levelScore.getScore());
    }
}
