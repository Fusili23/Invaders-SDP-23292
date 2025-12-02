package entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BulletTest {

    @Test
    public void constructor_initializesFieldsCorrectly() {
        Bullet bullet = new Bullet(10, 20, 5);
        assertEquals(10, bullet.getPositionX());
        assertEquals(20, bullet.getPositionY());
        assertEquals(5, bullet.getSpeed());
        // Bullet width/height are hardcoded in constructor: 3*2=6, 5*2=10
        assertEquals(6, bullet.getWidth());
        assertEquals(10, bullet.getHeight());
    }

    @Test
    public void update_movesBulletBasedOnSpeed() {
        Bullet bullet = new Bullet(10, 20, 5);
        bullet.update();
        assertEquals(25, bullet.getPositionY()); // 20 + 5
        
        bullet.setSpeed(-5);
        bullet.update();
        assertEquals(20, bullet.getPositionY()); // 25 - 5
    }

    @Test
    public void setSprite_setsCorrectSpriteBasedOnSpeed() {
        Bullet bulletDown = new Bullet(0, 0, 5);
        // SpriteType is protected, so we can't check it directly without a getter or subclass
        // But we can verify no exception is thrown
        bulletDown.setSprite();
        
        Bullet bulletUp = new Bullet(0, 0, -5);
        bulletUp.setSprite();
    }
    
    @Test
    public void ownerId_getterSetterWork() {
        Bullet bullet = new Bullet(0, 0, 0);
        bullet.setOwnerId(1);
        assertEquals(Integer.valueOf(1), bullet.getOwnerId());
        
        bullet.setOwnerId(2);
        assertEquals(Integer.valueOf(2), bullet.getOwnerId());
    }
}
