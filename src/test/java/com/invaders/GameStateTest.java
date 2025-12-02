package com.invaders;

import engine.GameState;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void constructor_full_initializesAllFields() {
        GameState gs = new GameState(1, 1000, 3, 50, 10, 500);
        assertEquals(1, gs.getLevel());
        assertEquals(1000, gs.getScore());
        assertEquals(3, gs.getLivesRemaining());
        assertEquals(50, gs.getBulletsShot());
        assertEquals(10, gs.getShipsDestroyed());
        assertEquals(500, gs.getCoin());
        assertFalse(gs.isTwoPlayer());
    }

    @Test
    public void constructor_fromPreviousState_copiesStateAndIncrementsLevel() {
        // Arrange: Create a previous game state for level 1
        GameState previousGs = new GameState(1, 1000, 3, 50, 10, 500);

        // Act: Create a new game state for the next level
        GameState newGs = new GameState(previousGs);

        // Assert: The level is incremented and all other stats are carried over
        assertEquals(2, newGs.getLevel());
        assertEquals(1000, newGs.getScore());
        assertEquals(3, newGs.getLivesRemaining());
        assertEquals(50, newGs.getBulletsShot());
        assertEquals(10, newGs.getShipsDestroyed());
        assertEquals(500, newGs.getCoin());
        assertFalse(newGs.isTwoPlayer()); // Should not be 2p by default
    }

    @Test
    public void constructor_fromPreviousTwoPlayerState_copiesAllPlayerData() {
        // Arrange: Create a previous game state and configure it for two players
        GameState previousGs = new GameState(1, 1000, 3, 50, 10, 500);
        int p2StartingLives = 2;
        previousGs.setTwoPlayerMode(p2StartingLives);
        previousGs.setScoreP2(800);

        // Act: Create a new game state for the next level
        GameState newGs = new GameState(previousGs);

        // Assert: All player 1 and player 2 data is carried over
        assertTrue(newGs.isTwoPlayer());
        assertEquals(800, newGs.getScoreP2());
        assertEquals(p2StartingLives, newGs.getLivesRemainingP2());
    }

    @Test
    public void addScore_updatesScoreCorrectly() {
        GameState gs = new GameState(1, 100, 3, 0, 0, 0);
        gs.addScore(50);
        assertEquals(150, gs.getScore());
    }
    
    @Test
    public void addScoreP2_updatesP2ScoreCorrectly() {
        GameState gs = new GameState(1, 100, 3, 0, 0, 0);
        gs.setTwoPlayerMode(3);
        gs.addScoreP2(70);
        assertEquals(70, gs.getScoreP2());
    }

    @Test
    public void loseLife_decrementsLivesRemaining() {
        GameState gs = new GameState(1, 100, 3, 0, 0, 0);
        gs.loseLife();
        assertEquals(2, gs.getLivesRemaining());
    }
    
    @Test
    public void loseLifeP2_decrementsP2LivesRemaining() {
        GameState gs = new GameState(1, 100, 3, 0, 0, 0);
        gs.setTwoPlayerMode(3);
        gs.loseLifeP2();
        assertEquals(2, gs.getLivesRemainingP2());
    }

    @Test
    public void deductCoins_sufficientFunds_returnsTrueAndDecrementsCoins() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        boolean result = gs.deductCoins(50);
        assertTrue(result);
        assertEquals(50, gs.getCoin());
    }

    @Test
    public void deductCoins_insufficientFunds_returnsFalseAndCoinsUnchanged() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        boolean result = gs.deductCoins(150);
        assertFalse(result);
        assertEquals(100, gs.getCoin());
    }

    @Test
    public void deductCoins_negativeAmount_returnsFalseAndCoinsUnchanged() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        boolean result = gs.deductCoins(-50);
        assertFalse(result);
        assertEquals(100, gs.getCoin());
    }

    @Test
    public void addCoins_positiveAmount_incrementsCoins() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        gs.addCoins(50);
        assertEquals(150, gs.getCoin());
    }

    @Test
    public void addCoins_negativeOrZeroAmount_coinsUnchanged() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        gs.addCoins(-50);
        assertEquals(100, gs.getCoin());
        gs.addCoins(0);
        assertEquals(100, gs.getCoin());
    }

    @Test
    public void setCoins_positiveAmount_setsCoins() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        gs.setCoins(250);
        assertEquals(250, gs.getCoin());
    }

    @Test
    public void setCoins_negativeAmount_coinsUnchanged() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 100);
        gs.setCoins(-50);
        assertEquals(100, gs.getCoin());
    }
    
    @Test
    public void setTwoPlayerMode_enablesTwoPlayerModeAndSetsP2Lives() {
        GameState gs = new GameState(1, 0, 3, 0, 0, 0);
        assertFalse(gs.isTwoPlayer()); // Verify initial state
        gs.setTwoPlayerMode(2);
        assertTrue(gs.isTwoPlayer());
        assertEquals(2, gs.getLivesRemainingP2());
    }
}
