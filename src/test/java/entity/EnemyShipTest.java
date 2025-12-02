package entity;

import java.awt.Color;
import org.junit.Test;
import engine.DrawManager.SpriteType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class EnemyShipTest {

    @Test
    public void constructor_standardShip_initializesCorrectly() {
        EnemyShip shipA = new EnemyShip(10, 20, SpriteType.EnemyShipA1);
        assertEquals(10, shipA.getPointValue()); // A type = 10 points
        assertEquals("enemyA", shipA.getEnemyType());
        
        EnemyShip shipB = new EnemyShip(10, 20, SpriteType.EnemyShipB1);
        assertEquals(20, shipB.getPointValue()); // B type = 20 points
        assertEquals("enemyB", shipB.getEnemyType());
        
        EnemyShip shipC = new EnemyShip(10, 20, SpriteType.EnemyShipC1);
        assertEquals(30, shipC.getPointValue()); // C type = 30 points
        assertEquals("enemyC", shipC.getEnemyType());
    }

    @Test
    public void constructor_specialShip_initializesCorrectly() {
        EnemyShip specialShip = new EnemyShip(Color.RED, EnemyShip.Direction.RIGHT, 5);
        assertEquals(100, specialShip.getPointValue()); // Bonus type = 100 points
        assertEquals(EnemyShip.Direction.RIGHT, specialShip.getDirection());
        assertEquals(5, specialShip.getXSpeed());
    }

    @Test
    public void move_updatesPosition() {
        EnemyShip ship = new EnemyShip(100, 100, SpriteType.EnemyShipA1);
        ship.move(10, -5);
        assertEquals(110, ship.getPositionX());
        assertEquals(95, ship.getPositionY());
    }

    @Test
    public void destroy_setsDestroyedState() {
        EnemyShip ship = new EnemyShip(0, 0, SpriteType.EnemyShipA1);
        assertFalse(ship.isDestroyed());
        
        ship.destroy();
        assertTrue(ship.isDestroyed());
        assertEquals(SpriteType.Explosion, ship.getSpriteType());
    }
    
    @Test
    public void gettersSetters_workCorrectly() {
        EnemyShip ship = new EnemyShip(0, 0, SpriteType.EnemyShipA1);
        
        ship.setDirection(EnemyShip.Direction.LEFT);
        assertEquals(EnemyShip.Direction.LEFT, ship.getDirection());
        
        ship.setXSpeed(10);
        assertEquals(10, ship.getXSpeed());
    }
}
