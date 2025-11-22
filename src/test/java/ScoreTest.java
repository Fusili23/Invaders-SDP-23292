import org.junit.Test;
import static org.junit.Assert.*;
import engine.Score;


public class ScoreTest {

    @Test
    public void testScoreCreation() {
        Score score = new Score("Player1", 100, 1, 10, 20, 50.0f);
        assertEquals("Player1", score.getName());
        assertEquals(100, score.getScore());
        assertEquals(1, score.getStage());
        assertEquals(10, score.getKilled());
        assertEquals(20, score.getBullets());
        assertEquals(50.0f, score.getAccuracy(), 0.001);
    }

    @Test
    public void testCompareTo_HigherScore() {
        Score score1 = new Score("Player1", 200, 1, 10, 20, 50.0f);
        Score score2 = new Score("Player2", 100, 1, 10, 20, 50.0f);
        assertTrue(score1.compareTo(score2) < 0);
    }

    @Test
    public void testCompareTo_LowerScore() {
        Score score1 = new Score("Player1", 100, 1, 10, 20, 50.0f);
        Score score2 = new Score("Player2", 200, 1, 10, 20, 50.0f);
        assertTrue(score1.compareTo(score2) > 0);
    }

    @Test
    public void testCompareTo_SameScore_HigherAccuracy() {
        Score score1 = new Score("Player1", 100, 1, 10, 20, 60.0f);
        Score score2 = new Score("Player2", 100, 1, 10, 20, 50.0f);
        assertTrue(score1.compareTo(score2) < 0);
    }

    @Test
    public void testCompareTo_SameScore_LowerAccuracy() {
        Score score1 = new Score("Player1", 100, 1, 10, 20, 50.0f);
        Score score2 = new Score("Player2", 100, 1, 10, 20, 60.0f);
        assertTrue(score1.compareTo(score2) > 0);
    }

    @Test
    public void testCompareTo_SameScore_SameAccuracy() {
        Score score1 = new Score("Player1", 100, 1, 10, 20, 50.0f);
        Score score2 = new Score("Player2", 100, 1, 10, 20, 50.0f);
        assertEquals(0, score1.compareTo(score2));
    }
}
