package com.invaders;

import engine.Coin;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoinTest {

    @Test
    public void constructor_initializesNameAndCoinCorrectly() {
        // Test with a standard value
        Coin coin1 = new Coin("Player1", 100);
        assertEquals("Player1", coin1.getName());
        assertEquals(100, coin1.getCoin());

        // Test with zero coins
        Coin coin2 = new Coin("Player2", 0);
        assertEquals("Player2", coin2.getName());
        assertEquals(0, coin2.getCoin());

        // Test with a large value
        Coin coin3 = new Coin("Player3", 9999);
        assertEquals("Player3", coin3.getName());
        assertEquals(9999, coin3.getCoin());
    }
}
