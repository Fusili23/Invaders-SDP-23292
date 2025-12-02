package entity;

import java.awt.Color;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EntityTest {

    @Test
    public void constructor_initializesAllFields() {
        Entity entity = new Entity(10, 20, 30, 40, Color.RED);
        assertEquals(10, entity.getPositionX());
        assertEquals(20, entity.getPositionY());
        assertEquals(30, entity.getWidth());
        assertEquals(40, entity.getHeight());
        assertEquals(Color.RED, entity.getColor());
        assertNull(entity.getSpriteType());
    }

    @Test
    public void setters_updateFieldsCorrectly() {
        Entity entity = new Entity(0, 0, 0, 0, Color.BLACK);
        
        entity.setPositionX(100);
        assertEquals(100, entity.getPositionX());
        
        entity.setPositionY(200);
        assertEquals(200, entity.getPositionY());
        
        entity.setColor(Color.BLUE);
        assertEquals(Color.BLUE, entity.getColor());
        
        entity.setPosition(50, 60);
        assertEquals(50, entity.getPositionX());
        assertEquals(60, entity.getPositionY());
    }
}
