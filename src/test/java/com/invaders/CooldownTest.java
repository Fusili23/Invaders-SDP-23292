package com.invaders;

import engine.Clock;
import engine.Cooldown;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CooldownTest {

    private MockClock mockClock;

    /**
     * A mock clock that allows manual control over time for testing.
     */
    private static class MockClock implements Clock {
        private long currentTime = 0;

        @Override
        public long now() {
            return currentTime;
        }

        public void advance(long milliseconds) {
            currentTime += milliseconds;
        }

        public void setTime(long milliseconds) {
            currentTime = milliseconds;
        }
    }

    @Before
    public void setUp() {
        mockClock = new MockClock();
        mockClock.setTime(1000000L); // Start with a non-zero time
    }

    @Test
    public void constructor_initialState_isFinished() {
        Cooldown cd = new Cooldown(100, mockClock);
        assertTrue("A newly created cooldown should be finished.", cd.checkFinished());
    }

    @Test
    public void reset_makesCooldownActive() {
        Cooldown cd = new Cooldown(100, mockClock);
        cd.reset();
        assertFalse("After reset, the cooldown should be active.", cd.checkFinished());
    }

    @Test
    public void checkFinished_beforeDuration_isActive() {
        Cooldown cd = new Cooldown(100, mockClock);
        cd.reset();
        mockClock.advance(50); // Advance time, but not enough to finish
        assertFalse("Cooldown should be active before the duration has passed.", cd.checkFinished());
    }

    @Test
    public void checkFinished_atDuration_isFinished() {
        Cooldown cd = new Cooldown(100, mockClock);
        cd.reset();
        mockClock.advance(100); // Advance time exactly to the duration
        assertTrue("Cooldown should be finished exactly when the duration has passed.", cd.checkFinished());
    }

    @Test
    public void checkFinished_afterDuration_isFinished() {
        Cooldown cd = new Cooldown(100, mockClock);
        cd.reset();
        mockClock.advance(101); // Advance time past the duration
        assertTrue("Cooldown should be finished after the duration has passed.", cd.checkFinished());
    }

    @Test
    public void setMilliseconds_updatesCooldownDuration() {
        Cooldown cd = new Cooldown(100, mockClock);
        cd.setMilliseconds(500);
        cd.reset();
        assertFalse(cd.checkFinished());

        mockClock.advance(499);
        assertFalse("Should still be active before the new duration.", cd.checkFinished());

        mockClock.advance(1);
        assertTrue("Should be finished after the new duration.", cd.checkFinished());
    }

    @Test
    public void reset_withVariance_usesRandomGeneratorDeterministically() {
        // Use a fixed seed for predictable "random" numbers.
        Random seededRandom = new Random(12345L);
        // With a seed of 12345L, the first nextInt(41) call will be 16.
        // Duration = min + random_val = 80 + 16 = 96
        
        Cooldown cd = new Cooldown(100, 20, mockClock, seededRandom);
        cd.reset();

        mockClock.advance(95);
        assertFalse("Should be active before the random duration (96ms).", cd.checkFinished());
        
        mockClock.advance(1);
        assertTrue("Should be finished at the random duration (96ms).", cd.checkFinished());
    }
}

