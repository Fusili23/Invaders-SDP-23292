package engine;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScoreTest {

    @Test
    public void constructor_full_initializesAllFields() {
        Score score = new Score("ABC", 1000, 1, 50, 100, 0.5f);
        assertEquals("ABC", score.getName());
        assertEquals(1000, score.getScore());
        assertEquals(1, score.getStage());
        assertEquals(50, score.getKilled());
        assertEquals(100, score.getBullets());
        assertEquals(0.5f, score.getAccuracy(), 0.001f);
    }

    @Test
    public void constructor_nameAndScore_initializesFieldsAndDefaultsOthers() {
        Score score = new Score("DEF", 500);
        assertEquals("DEF", score.getName());
        assertEquals(500, score.getScore());
        assertEquals(0, score.getStage());
        assertEquals(0, score.getKilled());
        assertEquals(0, score.getBullets());
        assertEquals(0.0f, score.getAccuracy(), 0.001f);
    }

    @Test
    public void compareTo_higherScore_returnsNegative() {
        Score score1 = new Score("A", 1000);
        Score score2 = new Score("B", 500);
        assertTrue("A higher score should result in a negative value, indicating it comes first.", score1.compareTo(score2) < 0);
    }

    @Test
    public void compareTo_lowerScore_returnsPositive() {
        Score score1 = new Score("A", 500);
        Score score2 = new Score("B", 1000);
        assertTrue("A lower score should result in a positive value, indicating it comes second.", score1.compareTo(score2) > 0);
    }

    @Test
    public void compareTo_sameScore_higherAccuracy_returnsNegative() {
        Score score1 = new Score("A", 1000, 1, 1, 1, 0.9f);
        Score score2 = new Score("B", 1000, 1, 1, 1, 0.7f);
        assertTrue("With same scores, higher accuracy should result in a negative value.", score1.compareTo(score2) < 0);
    }

    @Test
    public void compareTo_sameScore_lowerAccuracy_returnsPositive() {
        Score score1 = new Score("A", 1000, 1, 1, 1, 0.7f);
        Score score2 = new Score("B", 1000, 1, 1, 1, 0.9f);
        assertTrue("With same scores, lower accuracy should result in a positive value.", score1.compareTo(score2) > 0);
    }

    @Test
    public void compareTo_sameScore_sameAccuracy_returnsZero() {
        Score score1 = new Score("A", 1000, 1, 1, 1, 0.8f);
        Score score2 = new Score("B", 1000, 1, 1, 1, 0.8f);
        assertEquals("With same scores and accuracy, the result should be zero.", 0, score1.compareTo(score2));
    }
}
