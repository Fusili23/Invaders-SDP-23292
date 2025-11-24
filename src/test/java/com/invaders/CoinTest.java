package test.java.com.invaders;

import engine.Coin;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoinTest {

    @Test
    public void testCoinConstructorAndGetters() {
        Coin coin = new Coin("Player1", 100);
        assertEquals("Player1", coin.getName());
        assertEquals(100, coin.getCoin());
    }

    @Test
    public void testCoinWithZeroCoins() {
        Coin coin = new Coin("Player2", 0);
        assertEquals("Player2", coin.getName());
        assertEquals(0, coin.getCoin());
    }

    @Test
    public void testCoinWithDifferentValues() {
        Coin coin = new Coin("Player3", 9999);
        assertEquals("Player3", coin.getName());
        assertEquals(9999, coin.getCoin());
    }
}
