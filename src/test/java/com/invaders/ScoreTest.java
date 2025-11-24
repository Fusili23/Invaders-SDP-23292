package test.java.com.invaders;

import engine.Score;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ScoreTest {

    @Test
    public void testScoreConstructorFull() {
        Score score = new Score("ABC", 1000, 1, 50, 100, 0.5f);
        assertEquals("ABC", score.getName());
        assertEquals(1000, score.getScore());
        assertEquals(1, score.getStage());
        assertEquals(50, score.getKilled());
        assertEquals(100, score.getBullets());
        assertEquals(0.5f, score.getAccuracy(), 0.001f); // Delta for float comparison
    }

    @Test
    public void testScoreConstructorNameAndScore() {
        Score score = new Score("DEF", 500);
        assertEquals("DEF", score.getName());
        assertEquals(500, score.getScore());
        // Other fields should be default values (0 or null) or not applicable based on this constructor
        assertEquals(0, score.getStage());
        assertEquals(0, score.getKilled());
        assertEquals(0, score.getBullets());
        assertEquals(0.0f, score.getAccuracy(), 0.001f);
    }

    @Test
    public void testCompareTo() {
        // Test cases based on the compareTo logic:
        // 1st priority: score (descending)
        // 2nd priority: accuracy (descending)

        // Case 1: Different scores, same accuracy
        Score score1 = new Score("AAA", 1000, 1, 0, 0, 0.8f);
        Score score2 = new Score("BBB", 500, 1, 0, 0, 0.8f);
        assertTrue(score1.compareTo(score2) < 0); // score1 > score2, so negative (this.score > score.getScore() -> -1)

        // Case 2: Same scores, different accuracy
        Score score3 = new Score("CCC", 700, 1, 0, 0, 0.9f);
        Score score4 = new Score("DDD", 700, 1, 0, 0, 0.7f);
        assertTrue(score3.compareTo(score4) < 0); // score3 > score4 by accuracy, so negative (this.accuracy > score.getAccuracy() -> -1)

        // Case 3: Same scores, same accuracy
        Score score5 = new Score("EEE", 800, 1, 0, 0, 0.6f);
        Score score6 = new Score("FFF", 800, 1, 0, 0, 0.6f);
        assertEquals(0, score5.compareTo(score6)); // Scores and accuracy are same, so zero
    }
}
