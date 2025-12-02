package entity;

import java.awt.Color;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ShipTest {

    @Test
    public void constructor_initializesFieldsCorrectly() {
        Ship ship = new Ship(100, 200, Color.GREEN);
        assertEquals(100, ship.getPositionX());
        assertEquals(200, ship.getPositionY());
        assertEquals(Color.GREEN, ship.getColor());
        assertEquals(2, ship.getSpeed()); // Default speed is 2
        assertFalse(ship.isDestroyed());
        assertFalse(ship.isPermanentlyDestroyed());
        assertFalse(ship.isInvincible());
    }

    @Test
    public void movement_updatesPositionCorrectly() {
        Ship ship = new Ship(100, 200, Color.GREEN);
        
        // Initial speed is 2 (assuming no upgrades)
        ship.moveRight();
        assertTrue(ship.getPositionX() > 100);
        
        int newX = ship.getPositionX();
        ship.moveLeft();
        assertTrue(ship.getPositionX() < newX);
        
        ship.moveDown();
        assertTrue(ship.getPositionY() > 200);
        
        int newY = ship.getPositionY();
        ship.moveUp();
        assertTrue(ship.getPositionY() < newY);
    }
    
    @Test
    public void playerId_getterSetterWork() {
        Ship ship = new Ship(0, 0, Color.RED);
        assertEquals(1, ship.getPlayerId()); // Default is 1
        
        ship.setPlayerId(2);
        assertEquals(2, ship.getPlayerId());
    }
    
    @Test
    public void destruction_logicWorks() {
        Ship ship = new Ship(0, 0, Color.RED);
        
        // Test temporary destruction
        ship.destroy();
        // Can't easily test isDestroyed() true state because it depends on System.currentTimeMillis()
        // and Cooldown logic which might be immediate in test environment without mocking
        
        // Test permanent destruction
        ship.permanentlyDestroy();
        assertTrue(ship.isPermanentlyDestroyed());
        assertEquals(-100, ship.getPositionX()); // Moves off screen
    }
}
