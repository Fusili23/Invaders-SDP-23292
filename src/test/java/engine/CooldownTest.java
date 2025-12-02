package engine;

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
        
        // Calculate the expected duration using the same logic as Cooldown.reset()
        int milliseconds = 100;
        int variance = 20;
        int min = milliseconds - variance;  // 80
        int max = milliseconds + variance;  // 120
        
        // Generate the same random value that the Cooldown will use
        int randomValue = seededRandom.nextInt(max - min + 1);
        int expectedDuration = min + randomValue;
        
        // Create a new Random with the same seed for the Cooldown
        Random cooldownRandom = new Random(12345L);
        Cooldown cd = new Cooldown(milliseconds, variance, mockClock, cooldownRandom);
        cd.reset();

        // Test that it's still active just before the expected duration
        mockClock.advance(expectedDuration - 1);
        assertFalse("Should be active before the random duration (" + expectedDuration + "ms).", cd.checkFinished());
        
        // Test that it's finished exactly at the expected duration
        mockClock.advance(1);
        assertTrue("Should be finished at the random duration (" + expectedDuration + "ms).", cd.checkFinished());
    }
}
