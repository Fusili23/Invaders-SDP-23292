package com.invaders;

import engine.Cooldown;
import engine.Core;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CooldownTest {

    @Test
    public void testCooldownInitialization() {
        Cooldown cd = Core.getCooldown(100);
        // A new cooldown should be finished initially.
        assertTrue(cd.checkFinished());
    }

    @Test
    public void testResetAndCheck() throws InterruptedException {
        Cooldown cd = Core.getCooldown(200);
        cd.reset();
        assertFalse(cd.checkFinished());
        Thread.sleep(250); // Wait for more than the cooldown duration
        assertTrue(cd.checkFinished());
    }

    @Test
    public void testSetMilliseconds() throws InterruptedException {
        Cooldown cd = Core.getCooldown(1000);
        cd.setMilliseconds(200);
        cd.reset();
        assertFalse(cd.checkFinished());
        Thread.sleep(250);
        assertTrue(cd.checkFinished());
    }

    @Test
    public void testVariableCooldown() {
        Cooldown cd = Core.getVariableCooldown(100, 20);
        // This is hard to test deterministically without mocking Math.random().
        // We can at least check that it doesn't crash and seems to work.
        cd.reset();
        assertFalse(cd.checkFinished());
    }
}
