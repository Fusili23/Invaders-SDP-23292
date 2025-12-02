package com.invaders;

import engine.GameState;
import entity.ShopItem;
import screen.ShopScreen;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShopScreenTest {

    private GameState gameState;
    private ShopScreen shopScreen;
    private static final int TOTAL_ITEMS_PLUS_EXIT = 6; // 5 items + 1 exit option

    @Before
    public void setUp() {
        // Reset the static item states before each test to ensure isolation.
        ShopItem.resetAllItems();
        // Create a new game state with 500 coins for each test.
        gameState = new GameState(1, 0, 3, 0, 0, 500);
        shopScreen = new ShopScreen(gameState, 800, 600, 60, false);
    }

    @Test
    public void nextItem_incrementsSelectedItem() {
        assertEquals(0, shopScreen.getSelectedItem());
        shopScreen.nextItem();
        assertEquals(1, shopScreen.getSelectedItem());
    }

    @Test
    public void nextItem_wrapsAroundFromEnd() {
        // Set selected item to the last option (exit button)
        for (int i = 0; i < TOTAL_ITEMS_PLUS_EXIT - 1; i++) {
            shopScreen.nextItem();
        }
        assertEquals(TOTAL_ITEMS_PLUS_EXIT - 1, shopScreen.getSelectedItem());

        shopScreen.nextItem();
        assertEquals(0, shopScreen.getSelectedItem()); // Should wrap around to the first item
    }

    @Test
    public void previousItem_decrementsSelectedItem() {
        shopScreen.nextItem(); // selected is now 1
        assertEquals(1, shopScreen.getSelectedItem());

        shopScreen.previousItem();
        assertEquals(0, shopScreen.getSelectedItem());
    }

    @Test
    public void previousItem_wrapsAroundFromStart() {
        assertEquals(0, shopScreen.getSelectedItem());
        shopScreen.previousItem();
        assertEquals(TOTAL_ITEMS_PLUS_EXIT - 1, shopScreen.getSelectedItem()); // Should wrap around to the last item
    }

    @Test
    public void purchaseItem_successfulPurchase_deductsCoinsAndUpgradesItem() {
        // MultiShot Level 1 costs 30 coins. Player has 500.
        int itemIndex = 0; // MultiShot
        int levelToPurchase = 1;
        int initialCoins = gameState.getCoin();
        int price = 30;

        shopScreen.purchaseItem(itemIndex, levelToPurchase);

        assertEquals("Coins should be deducted.", initialCoins - price, gameState.getCoin());
        assertEquals("Item level should be upgraded.", levelToPurchase, ShopItem.getMultiShotLevel());
        assertTrue("Feedback message should indicate success.", shopScreen.getFeedbackMessage().contains("Purchased"));
    }

    @Test
    public void purchaseItem_insufficientFunds_doesNotPurchaseAndShowsMessage() {
        gameState.setCoins(20); // Not enough for a 30 coin item.
        int itemIndex = 0; // MultiShot
        int levelToPurchase = 1;

        shopScreen.purchaseItem(itemIndex, levelToPurchase);

        assertEquals("Coins should not be deducted.", 20, gameState.getCoin());
        assertEquals("Item level should not change.", 0, ShopItem.getMultiShotLevel());
        assertEquals("Feedback message should indicate not enough coins.", "Not enough coins!", shopScreen.getFeedbackMessage());
    }

    @Test
    public void purchaseItem_itemAlreadyOwned_doesNotPurchaseAndShowsMessage() {
        ShopItem.setMultiShotLevel(1); // Item is already level 1.
        int initialCoins = gameState.getCoin();
        int itemIndex = 0; // MultiShot
        int levelToPurchase = 1; // Attempting to buy level 1 again.

        shopScreen.purchaseItem(itemIndex, levelToPurchase);

        assertEquals("Coins should not be deducted.", initialCoins, gameState.getCoin());
        assertEquals("Item level should not change.", 1, ShopItem.getMultiShotLevel());
        assertEquals("Feedback message should indicate item is already owned.", "Already owned!", shopScreen.getFeedbackMessage());
    }

    @Test
    public void purchaseItem_higherLevel_successfulPurchase() {
        ShopItem.setMultiShotLevel(1);
        gameState.setCoins(100);
        int initialCoins = gameState.getCoin();
        int price = 60; // Price for level 2

        shopScreen.purchaseItem(0, 2); // Purchase MultiShot Level 2

        assertEquals("Coins should be deducted for the new level.", initialCoins - price, gameState.getCoin());
        assertEquals("Item should be upgraded to the new level.", 2, ShopItem.getMultiShotLevel());
        assertTrue("Feedback message should indicate success.", shopScreen.getFeedbackMessage().contains("Purchased"));
    }
}
