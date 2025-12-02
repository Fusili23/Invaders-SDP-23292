package entity;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class BulletPoolTest {

    @Test
    public void getBullet_returnsNewBulletWhenPoolEmpty() {
        Bullet bullet = BulletPool.getBullet(10, 20, 5);
        assertEquals(10, bullet.getPositionX()); // Adjusted for width in getBullet
        assertEquals(20, bullet.getPositionY());
        assertEquals(5, bullet.getSpeed());
    }

    @Test
    public void recycle_addsBulletToPool() {
        Bullet bullet1 = new Bullet(0, 0, 0);
        Set<Bullet> bulletsToRecycle = new HashSet<>();
        bulletsToRecycle.add(bullet1);
        
        BulletPool.recycle(bulletsToRecycle);
        
        // Should get the same instance back
        Bullet recycledBullet = BulletPool.getBullet(100, 200, 10);
        assertSame(bullet1, recycledBullet);
        
        // Properties should be reset/updated
        assertEquals(100 - recycledBullet.getWidth()/2, recycledBullet.getPositionX());
        assertEquals(200, recycledBullet.getPositionY());
        assertEquals(10, recycledBullet.getSpeed());
    }
    
    @Test
    public void getBullet_returnsDifferentInstances() {
        Bullet b1 = BulletPool.getBullet(0, 0, 0);
        Bullet b2 = BulletPool.getBullet(0, 0, 0);
        assertNotSame(b1, b2);
    }
}
