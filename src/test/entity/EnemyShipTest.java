package java.entity;

import main.engine.DrawManager.SpriteType;
import main.entity.EnemyShip;
import org.junit.jupiter.api.*;

import java.awt.Color;

class EnemyShipTest {

    private EnemyShip enemyA;
    private EnemyShip enemyB;
    private EnemyShip enemyC;
    private EnemyShip specialEnemy;

    @BeforeAll
    static void beforeAll() {
        System.out.println("[START] EnemyShipTest - All tests begin");
    }

    @BeforeEach
    void setUp() {
        System.out.println("[SETUP] Creating EnemyShip instances for each test...");
        // Normal enemies (A/B/C types)
        enemyA = new EnemyShip(100, 200, SpriteType.EnemyShipA1);
        enemyB = new EnemyShip(120, 200, SpriteType.EnemyShipB1);
        enemyC = new EnemyShip(140, 200, SpriteType.EnemyShipC1);

        // Special enemy (bonus points)
        specialEnemy = new EnemyShip(Color.RED, EnemyShip.Direction.RIGHT, 5);
    }

    @Test
    void testPointValueInitialization() {
        System.out.println("Running: testPointValueInitialization()");
        Assertions.assertEquals(10, enemyA.getPointValue(), "Type A enemy should have 10 points");
        Assertions.assertEquals(20, enemyB.getPointValue(), "Type B enemy should have 20 points");
        Assertions.assertEquals(30, enemyC.getPointValue(), "Type C enemy should have 30 points");
        Assertions.assertEquals(100, specialEnemy.getPointValue(), "Bonus enemy should have 100 points");
    }

    @Test
    void testMoveUpdatesPosition() {
        System.out.println("Running: testMoveUpdatesPosition()");
        int initialX = enemyA.getPositionX();
        int initialY = enemyA.getPositionY();

        enemyA.move(5, 10);

        Assertions.assertEquals(initialX + 5, enemyA.getPositionX());
        Assertions.assertEquals(initialY + 10, enemyA.getPositionY());
    }

    @Test
    void testDirectionAndSpeedSetters() {
        System.out.println("Running: testDirectionAndSpeedSetters()");
        specialEnemy.setDirection(EnemyShip.Direction.LEFT);
        specialEnemy.setXSpeed(10);

        Assertions.assertEquals(EnemyShip.Direction.LEFT, specialEnemy.getDirection());
        Assertions.assertEquals(10, specialEnemy.getXSpeed());
    }

    @Test
    void testDestroySetsDestroyedFlagAndExplosionSprite() {
        System.out.println("Running: testDestroySetsDestroyedFlagAndExplosionSprite()");
        Assertions.assertFalse(enemyB.isDestroyed(), "Enemy should not be destroyed initially");
        enemyB.destroy();
        Assertions.assertTrue(enemyB.isDestroyed(), "Enemy should be destroyed after destroy() call");
        Assertions.assertEquals(SpriteType.Explosion, enemyB.getSpriteType(), "Sprite should change to Explosion");
    }

    @Test
    void testGetEnemyTypeReturnsCorrectType() {
        System.out.println("Running: testGetEnemyTypeReturnsCorrectType()");
        Assertions.assertEquals("enemyA", enemyA.getEnemyType());
        Assertions.assertEquals("enemyB", enemyB.getEnemyType());
        Assertions.assertEquals("enemyC", enemyC.getEnemyType());
        Assertions.assertNull(specialEnemy.getEnemyType(), "Special enemy should return null type");
    }

    @Test
    void testExplosionFinishedAfterCooldown() throws InterruptedException {
        System.out.println("Running: testExplosionFinishedAfterCooldown()");
        enemyA.destroy();
        Assertions.assertFalse(enemyA.isExplosionFinished(), "Explosion should not finish immediately after destroy()");

        // Simulate 500ms cooldown delay
        Thread.sleep(600);
        Assertions.assertTrue(enemyA.isExplosionFinished(), "Explosion should finish after cooldown");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("[END] EnemyShipTest - All tests completed");
    }
}