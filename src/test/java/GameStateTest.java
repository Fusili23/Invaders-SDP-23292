import engine.GameState;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void testGameStateCreation() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        assertEquals(1, gameState.getLevel());
        assertEquals(100, gameState.getScore());
        assertEquals(3, gameState.getLivesRemaining());
        assertEquals(50, gameState.getBulletsShot());
        assertEquals(10, gameState.getShipsDestroyed());
        assertEquals(20, gameState.getCoin());
    }

    @Test
    public void testDeductCoins_Success() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        assertTrue(gameState.deductCoins(10));
        assertEquals(10, gameState.getCoin());
    }

    @Test
    public void testDeductCoins_InsufficientFunds() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        assertFalse(gameState.deductCoins(30));
        assertEquals(20, gameState.getCoin());
    }

    @Test
    public void testDeductCoins_NegativeAmount() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        assertFalse(gameState.deductCoins(-10));
        assertEquals(20, gameState.getCoin());
    }

    @Test
    public void testAddCoins() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        gameState.addCoins(10);
        assertEquals(30, gameState.getCoin());
    }

    @Test
    public void testAddCoins_NegativeAmount() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        gameState.addCoins(-10);
        assertEquals(20, gameState.getCoin());
    }

    @Test
    public void testSetCoins() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        gameState.setCoins(50);
        assertEquals(50, gameState.getCoin());
    }

    @Test
    public void testSetCoins_NegativeAmount() {
        GameState gameState = new GameState(1, 100, 3, 50, 10, 20);
        gameState.setCoins(-10);
        assertEquals(20, gameState.getCoin());
    }
}
