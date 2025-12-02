package com.invaders;

import audio.SoundManager;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SoundManagerTest {

    /**
     * Ensures that the sound state is reset after each test,
     * so that tests do not interfere with each other.
     */
    @After
    public void tearDown() {
        SoundManager.uncutAllSound();
    }

    @Test
    public void muteAndUnmute_togglesMuteStateCorrectly() {
        // Initial state should be not muted
        assertFalse("SoundManager should not be muted by default.", SoundManager.isMuted());

        // Mute the sound
        SoundManager.cutAllSound();
        assertTrue("SoundManager should be muted after calling cutAllSound().", SoundManager.isMuted());

        // Unmute the sound
        SoundManager.uncutAllSound();
        assertFalse("SoundManager should not be muted after calling uncutAllSound().", SoundManager.isMuted());
    }

    @Test
    public void muteState_isIndependentAcrossToggleCycles() {
        // Cycle 1
        SoundManager.cutAllSound();
        assertTrue(SoundManager.isMuted());
        SoundManager.uncutAllSound();
        assertFalse(SoundManager.isMuted());

        // Cycle 2
        SoundManager.cutAllSound();
        assertTrue("Mute should work correctly on the second cycle.", SoundManager.isMuted());
        SoundManager.uncutAllSound();
        assertFalse("Unmute should also work correctly on the second cycle.", SoundManager.isMuted());
    }
}
