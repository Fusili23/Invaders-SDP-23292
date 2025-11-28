package test.java.com.invaders;

import audio.SoundManager;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SoundManagerTest {

    private boolean getMutedState() throws Exception {
        Field mutedField = SoundManager.class.getDeclaredField("muted");
        mutedField.setAccessible(true);
        return (boolean) mutedField.get(null); // static field
    }

    @Test
    public void testMuteAndUnmute() throws Exception {
        // Initial state should be not muted
        assertFalse(getMutedState());

        SoundManager.cutAllSound();
        assertTrue(getMutedState());
        
        SoundManager.uncutAllSound();
        assertFalse(getMutedState());
    }
}
