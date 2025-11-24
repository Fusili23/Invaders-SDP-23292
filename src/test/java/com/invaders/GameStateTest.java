package com.invaders; // Matches the IDE's expected package

import engine.GameState;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GameStateTest {

    @Test
    public void testGameStateConstructorFull() {
        GameState gs = new GameState(1, 1000, 3, 50, 10, 500);
        assertEquals(1, gs.getLevel());
        assertEquals(1000, gs.getScore());
        assertEquals(3, gs.getLivesRemaining());
        assertEquals(50, gs.getBulletsShot());
        assertEquals(10, gs.getShipsDestroyed());
        assertEquals(500, gs.getCoin());
    }

    @Test
    public void testGameStateConstructorTransition() {
        GameState prevGs = new GameState(1, 1000, 3, 50, 10, 500);
        prevGs.setTwoPlayerMode(2); // Set two player mode for previous state
        prevGs.setScoreP2(1000);
        GameState newGs = new GameState(prevGs);
        
        assertEquals(2, newGs.getLevel()); // Level should increment
        assertEquals(1000, newGs.getScore());
        assertEquals(3, newGs.getLivesRemaining());
        assertEquals(50, newGs.getBulletsShot());
        assertEquals(10, newGs.getShipsDestroyed());
        assertEquals(500, newGs.getCoin());
        assertTrue(newGs.isTwoPlayer());
        assertEquals(1000, newGs.getScoreP2()); // Should carry over from previous state
        assertEquals(2, newGs.getLivesRemainingP2()); // Should carry over from previous state
    }

    @Test
    public void testAddScore() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 0);
        gs.addScore(100);
        assertEquals(100, gs.getScore());
        gs.addScore(50);
        assertEquals(150, gs.getScore());
    }

    @Test
    public void testLoseLife() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 0);
        gs.loseLife();
        assertEquals(2, gs.getLivesRemaining());
        gs.loseLife();
        assertEquals(1, gs.getLivesRemaining());
    }

    @Test
    public void testCoinManagement() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        
        // Deduct coins
        assertTrue(gs.deductCoins(50));
        assertEquals(50, gs.getCoin());
        assertFalse(gs.deductCoins(100)); // Not enough coins
        assertEquals(50, gs.getCoin());

        // Add coins
        gs.addCoins(25);
        assertEquals(75, gs.getCoin());
        gs.addCoins(-10); // Should not add negative
        assertEquals(75, gs.getCoin());

        // Set coins
        gs.setCoins(200);
        assertEquals(200, gs.getCoin());
        gs.setCoins(-50); // Should not set negative
        assertEquals(200, gs.getCoin());
    }

    @Test
    public void testTwoPlayerMode() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 0);
        assertFalse(gs.isTwoPlayer());
        gs.setTwoPlayerMode(2);
        assertTrue(gs.isTwoPlayer());
        assertEquals(2, gs.getLivesRemainingP2());

        gs.addScoreP2(200);
        assertEquals(200, gs.getScoreP2());

        gs.loseLifeP2();
        assertEquals(1, gs.getLivesRemainingP2());
    }
}
