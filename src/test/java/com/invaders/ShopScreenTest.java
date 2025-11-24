package com.invaders;

import engine.GameState;
import screen.ShopScreen;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;

public class ShopScreenTest {

    private ShopScreen shopScreen;
    private GameState gameState;

    @Before
    public void setUp() {
        gameState = new GameState(1, 0, 3, 0, 0, 100); // 100 coins
        shopScreen = new ShopScreen(gameState, 800, 600, 60, false);
    }

    private void setSelectedItem(int value) throws Exception {
        Field selectedItemField = ShopScreen.class.getDeclaredField("selectedItem");
        selectedItemField.setAccessible(true);
        selectedItemField.set(shopScreen, value);
    }

    private int getSelectedItem() throws Exception {
        Field selectedItemField = ShopScreen.class.getDeclaredField("selectedItem");
        selectedItemField.setAccessible(true);
        return (int) selectedItemField.get(shopScreen);
    }

    private void callPrivateMethod(String methodName) throws Exception {
        java.lang.reflect.Method method = ShopScreen.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(shopScreen);
    }
    
    private void callPrivateMethod(String methodName, Class<?>[] pTypes, Object[] pValues) throws Exception {
        java.lang.reflect.Method method = ShopScreen.class.getDeclaredMethod(methodName, pTypes);
        method.setAccessible(true);
        method.invoke(shopScreen, pValues);
    }

    @Test
    public void testNextItem() throws Exception {
        setSelectedItem(0);
        callPrivateMethod("nextItem");
        assertEquals(1, getSelectedItem());

        // Test wrapping around
        setSelectedItem(5); // TOTAL_ITEMS is 5 (0-4 items + exit)
        callPrivateMethod("nextItem");
        assertEquals(0, getSelectedItem());
    }

    @Test
    public void testPreviousItem() throws Exception {
        setSelectedItem(1);
        callPrivateMethod("previousItem");
        assertEquals(0, getSelectedItem());
        
        // Test wrapping around
        setSelectedItem(0);
        callPrivateMethod("previousItem");
        assertEquals(5, getSelectedItem()); // TOTAL_ITEMS
    }

    @Test
    public void testPurchaseItem_NotEnoughCoins() throws Exception {
        // MultiShot level 1 costs 30. Player has 100. Should be able to afford.
        // Let's try to buy something expensive. Penetration level 2 costs 80.
        gameState.setCoins(70);
        
        Class<?>[] pTypes = {int.class, int.class};
        Object[] pValues = {2, 2}; // itemIndex 2 (Penetration), level 2

        callPrivateMethod("purchaseItem", pTypes, pValues);

        // Coins should not be deducted.
        assertEquals(70, gameState.getCoin());
        
        // Check feedback message
        Field feedbackMessageField = ShopScreen.class.getDeclaredField("feedbackMessage");
        feedbackMessageField.setAccessible(true);
        String feedbackMessage = (String) feedbackMessageField.get(shopScreen);
        assertEquals("Not enough coins!", feedbackMessage);
    }
}
