package entity;

import java.awt.Color;
import org.junit.Test;
import entity.DropItem.ItemType;
import engine.DrawManager.SpriteType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DropItemTest {

    @Test
    public void constructor_initializesCorrectly() {
        DropItem item = new DropItem(10, 20, 5, ItemType.Heal);
        assertEquals(10, item.getPositionX());
        assertEquals(20, item.getPositionY());
        assertEquals(5, item.getSpeed());
        assertEquals(ItemType.Heal, item.getItemType());
        assertEquals(SpriteType.Item_Heal, item.getSpriteType());
    }

    @Test
    public void setSprite_updatesSpriteBasedOnType() {
        DropItem item = new DropItem(0, 0, 0, ItemType.Heal);
        
        item.setItemType(ItemType.Explode);
        assertEquals(SpriteType.Item_Explode, item.getSpriteType());
        assertEquals(Color.RED, item.getColor());
        
        item.setItemType(ItemType.Shield);
        assertEquals(SpriteType.Item_Shield, item.getSpriteType());
        assertEquals(Color.CYAN, item.getColor());
    }

    @Test
    public void fromString_returnsCorrectType() {
        assertEquals(ItemType.Heal, DropItem.fromString("Heal"));
        assertEquals(ItemType.Explode, DropItem.fromString("Explode"));
        assertNull(DropItem.fromString("Invalid"));
    }
    
    @Test
    public void getRandomItemType_returnsTypeOrNull() {
        // Probability 1.0 should always return a type
        assertNotNull(DropItem.getRandomItemType(1.0));
        
        // Probability 0.0 should always return null
        assertNull(DropItem.getRandomItemType(0.0));
    }
    
    @Test
    public void timeFreeze_logicWorks() {
        // Initially inactive
        assertFalse(DropItem.isTimeFreezeActive());
        
        // Activate for 1000ms
        DropItem.applyTimeFreezeItem(1000);
        assertTrue(DropItem.isTimeFreezeActive());
        
        // We can't easily wait 1s in a unit test without slowing it down
        // but we verified activation works
    }
    
    @Test
    public void update_movesItem() {
        DropItem item = new DropItem(0, 0, 5, ItemType.Heal);
        item.update();
        assertEquals(5, item.getPositionY());
    }
}
