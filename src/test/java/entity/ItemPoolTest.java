package entity;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import entity.DropItem.ItemType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ItemPoolTest {

    @Test
    public void getItem_returnsNewItemWhenPoolEmpty() {
        DropItem item = ItemPool.getItem(10, 20, 5, ItemType.Heal);
        assertEquals(10, item.getPositionX()); // Adjusted for width
        assertEquals(20, item.getPositionY());
        assertEquals(5, item.getSpeed());
        assertEquals(ItemType.Heal, item.getItemType());
    }

    @Test
    public void recycle_addsItemToPool() {
        DropItem item1 = new DropItem(0, 0, 0, ItemType.Explode);
        Set<DropItem> itemsToRecycle = new HashSet<>();
        itemsToRecycle.add(item1);
        
        ItemPool.recycle(itemsToRecycle);
        
        // Should get the same instance back
        DropItem recycledItem = ItemPool.getItem(100, 200, 10, ItemType.Shield);
        assertSame(item1, recycledItem);
        
        // Properties should be reset/updated
        assertEquals(100 - recycledItem.getWidth()/2, recycledItem.getPositionX());
        assertEquals(200, recycledItem.getPositionY());
        assertEquals(10, recycledItem.getSpeed());
        assertEquals(ItemType.Shield, recycledItem.getItemType());
    }
}
