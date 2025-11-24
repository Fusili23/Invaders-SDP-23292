package com.invaders;

import screen.TitleScreen;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;

public class TitleScreenTest {

    private TitleScreen titleScreen;

    @Before
    public void setUp() {
        titleScreen = new TitleScreen(800, 600, 60);
    }

    private void setReturnCode(int value) throws Exception {
        Field returnCodeField = TitleScreen.class.getSuperclass().getDeclaredField("returnCode");
        returnCodeField.setAccessible(true);
        returnCodeField.set(titleScreen, value);
    }

    private int getReturnCode() throws Exception {
        Field returnCodeField = TitleScreen.class.getSuperclass().getDeclaredField("returnCode");
        returnCodeField.setAccessible(true);
        return (int) returnCodeField.get(titleScreen);
    }

    private void callPrivateMethod(String methodName) throws Exception {
        java.lang.reflect.Method method = TitleScreen.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(titleScreen);
    }

    @Test
    public void testNextMenuItem() throws Exception {
        // From Play (2) to High scores (3)
        setReturnCode(2);
        callPrivateMethod("nextMenuItem");
        assertEquals(3, getReturnCode());

        // From High scores (3) to Achievements (6)
        setReturnCode(3);
        callPrivateMethod("nextMenuItem");
        assertEquals(6, getReturnCode());
        
        // From Achievements (6) to Shop (4)
        setReturnCode(6);
        callPrivateMethod("nextMenuItem");
        assertEquals(4, getReturnCode());
        
        // From Shop (4) to Exit (0)
        setReturnCode(4);
        callPrivateMethod("nextMenuItem");
        assertEquals(0, getReturnCode());

        // From Exit (0) to Play (2)
        setReturnCode(0);
        callPrivateMethod("nextMenuItem");
        assertEquals(2, getReturnCode());
    }

    @Test
    public void testPreviousMenuItem() throws Exception {
        // From Play (2) to Exit (0)
        setReturnCode(2);
        callPrivateMethod("previousMenuItem");
        assertEquals(0, getReturnCode());
        
        // From Exit (0) to Shop (4)
        setReturnCode(0);
        callPrivateMethod("previousMenuItem");
        assertEquals(4, getReturnCode());

        // From Shop (4) to Achievements (6)
        setReturnCode(4);
        callPrivateMethod("previousMenuItem");
        assertEquals(6, getReturnCode());

        // From Achievements (6) to High scores (3)
        setReturnCode(6);
        callPrivateMethod("previousMenuItem");
        assertEquals(3, getReturnCode());
        
        // From High scores (3) to Play (2)
        setReturnCode(3);
        callPrivateMethod("previousMenuItem");
        assertEquals(2, getReturnCode());
    }
}
