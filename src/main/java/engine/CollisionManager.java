package engine;

import entity.*;
import screen.GameScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages collisions between game entities.
 */
public class CollisionManager {

    private final GameScreen gameScreen;
    private final java.util.logging.Logger logger;

    public CollisionManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.logger = gameScreen.getLogger();
    }

    /**
     * Manages collisions between bullets and ships.
     */
    public void manageCollisions() {
        Set<Bullet> recyclableBullets = new HashSet<>();
        manageBulletShipCollisions(recyclableBullets);
        manageShipEnemyCollisions();
        manageItemCollisions();
        gameScreen.getBullets().removeAll(recyclableBullets);
        BulletPool.recycle(recyclableBullets);
    }

    private void manageBulletShipCollisions(Set<Bullet> recyclable) {
        for (Bullet bullet : gameScreen.getBullets()) {
            if (bullet.getSpeed() > 0) { // Enemy bullet
                handleEnemyBulletCollision(bullet, recyclable);
            } else { // Player bullet
                handlePlayerBulletCollision(bullet, recyclable);
            }
        }
    }

    private void handleEnemyBulletCollision(Bullet bullet, Set<Bullet> recyclable) {
        if (!gameScreen.getShip().isPermanentlyDestroyed() && checkCollision(bullet, gameScreen.getShip()) && !gameScreen.isLevelFinished()) {
            recyclable.add(bullet);
            if (!gameScreen.getShip().isInvincible() && !gameScreen.getShip().isDestroyed()) {
                gameScreen.getShip().destroy();
                gameScreen.decreaseLives();
                gameScreen.showHealthPopup(GameConfig.P1_HEALTH_LOSS_MESSAGE);
                logger.info("Hit on player 1 ship, " + gameScreen.getLives() + " lives remaining.");
            }
        }
        if (gameScreen.isTwoPlayer() && !gameScreen.getShip2().isPermanentlyDestroyed() && checkCollision(bullet, gameScreen.getShip2()) && !gameScreen.isLevelFinished()) {
            recyclable.add(bullet);
            if (!gameScreen.getShip2().isInvincible() && !gameScreen.getShip2().isDestroyed()) {
                gameScreen.getShip2().destroy();
                gameScreen.decreaseLivesP2();
                gameScreen.showHealthPopup(GameConfig.P2_HEALTH_LOSS_MESSAGE);
                logger.info("Hit on player 2 ship, " + gameScreen.getLivesP2() + " lives remaining.");
            }
        }
    }

    private void handlePlayerBulletCollision(Bullet bullet, Set<Bullet> recyclable) {
        Integer ownerId = bullet.getOwnerId();

        // Player bullet vs EnemyShipFormation
        for (EnemyShip enemyShip : gameScreen.getEnemyShipFormation()) {
            if (!enemyShip.isDestroyed() && checkCollision(bullet, enemyShip)) {
                handleEnemyHit(enemyShip, ownerId, bullet.penetration());
                if (!bullet.penetration()) {
                    recyclable.add(bullet);
                    return; // Bullet is used up
                }
            }
        }

        // Player bullet vs EnemyShipSpecialFormation
        for (EnemyShip enemyShipSpecial : gameScreen.getEnemyShipSpecialFormation()) {
            if (enemyShipSpecial != null && !enemyShipSpecial.isDestroyed() && checkCollision(bullet, enemyShipSpecial)) {
                handleSpecialEnemyHit(enemyShipSpecial, ownerId);
                recyclable.add(bullet);
                return; // Bullet is used up
            }
        }

        // Player bullet vs Bosses
        if (gameScreen.getOmegaBoss() != null && !gameScreen.getOmegaBoss().isDestroyed() && checkCollision(bullet, gameScreen.getOmegaBoss())) {
            handleOmegaBossHit(gameScreen.getOmegaBoss(), ownerId);
            recyclable.add(bullet);
        } else if (gameScreen.getFinalBoss() != null && !gameScreen.getFinalBoss().isDestroyed() && checkCollision(bullet, gameScreen.getFinalBoss())) {
            handleFinalBossHit(gameScreen.getFinalBoss(), ownerId);
            recyclable.add(bullet);
        }
    }

    private void handleEnemyHit(EnemyShip enemyShip, Integer ownerId, boolean penetration) {
        int pts = enemyShip.getPointValue();
        if (ownerId != null && ownerId == 2) {
            gameScreen.addScoreP2(pts);
        } else {
            gameScreen.addScore(pts);
        }
        gameScreen.addCoin(pts / GameConfig.COIN_FROM_POINTS_RATIO);
        gameScreen.increaseShipsDestroyed();

        String unlockAchievementName = AchievementManager.getInstance().onEnemyDefeated();
        if (unlockAchievementName != null) {
            gameScreen.showAchievement(unlockAchievementName);
            logger.info("Achievement unlocked:" + unlockAchievementName);
        }

        String enemyType = enemyShip.getEnemyType();
        gameScreen.getEnemyShipFormation().destroy(enemyShip);

        if (enemyType != null && gameScreen.getCurrentLevel().getItemDrops() != null) {
            tryDropItem(enemyShip);
        }
    }

    private void handleSpecialEnemyHit(EnemyShip enemyShipSpecial, Integer ownerId) {
        int pts = enemyShipSpecial.getPointValue();
        if (ownerId != null && ownerId == 2) {
            gameScreen.addScoreP2(pts);
        } else {
            gameScreen.addScore(pts);
        }
        gameScreen.addCoin(pts / GameConfig.COIN_FROM_POINTS_RATIO);
        gameScreen.increaseShipsDestroyed();
        gameScreen.getEnemyShipSpecialFormation().destroy(enemyShipSpecial);
    }

    private void handleOmegaBossHit(MidBoss omegaBoss, Integer ownerId) {
        omegaBoss.takeDamage(GameConfig.OMEGA_BOSS_DAMAGE);
        if (omegaBoss.getHealPoint() <= 0) {
            int pts = omegaBoss.getPointValue();
            if (ownerId != null && ownerId == 2) {
                gameScreen.addScoreP2(pts);
            } else {
                gameScreen.addScore(pts);
            }
            gameScreen.addCoin(pts / GameConfig.COIN_FROM_POINTS_RATIO);
            gameScreen.increaseShipsDestroyed();
            omegaBoss.destroy();
            if (AchievementManager.getInstance().unlockAchievement("Boss Slayer")) {
                gameScreen.showAchievement("Boss Slayer");
            }
            gameScreen.getBossExplosionCooldown().reset();
        }
    }

    private void handleFinalBossHit(FinalBoss finalBoss, Integer ownerId) {
        finalBoss.takeDamage(GameConfig.FINAL_BOSS_DAMAGE);
        if (finalBoss.getHealPoint() <= 0) {
            int pts = finalBoss.getPointValue();
            if (ownerId != null && ownerId == 2) {
                gameScreen.addScoreP2(pts);
            } else {
                gameScreen.addScore(pts);
            }
            gameScreen.addCoin(pts / GameConfig.COIN_FROM_POINTS_RATIO);
            finalBoss.destroy();
            if (AchievementManager.getInstance().unlockAchievement("Boss Slayer")) {
                gameScreen.showAchievement("Boss Slayer");
            }
        }
    }


    private void tryDropItem(EnemyShip enemyShip) {
        List<engine.level.ItemDrop> potentialDrops = new ArrayList<>();
        for (engine.level.ItemDrop itemDrop : gameScreen.getCurrentLevel().getItemDrops()) {
            if (enemyShip.getEnemyType() != null && enemyShip.getEnemyType().equals(itemDrop.getEnemyType())) {
                potentialDrops.add(itemDrop);
            }
        }

        List<engine.level.ItemDrop> successfulDrops = new ArrayList<>();
        for (engine.level.ItemDrop itemDrop : potentialDrops) {
            if (Math.random() < itemDrop.getDropChance()) {
                successfulDrops.add(itemDrop);
            }
        }

        if (!successfulDrops.isEmpty()) {
            engine.level.ItemDrop selectedDrop = successfulDrops.get((int) (Math.random() * successfulDrops.size()));
            DropItem.ItemType droppedType = DropItem.fromString(selectedDrop.getItemId());
            if (droppedType != null) {
                DropItem newDropItem = ItemPool.getItem(
                        enemyShip.getPositionX() + enemyShip.getWidth() / 2,
                        enemyShip.getPositionY() + enemyShip.getHeight() / 2,
                        GameConfig.ITEM_DROP_SPEED, // ITEM_DROP_SPEED
                        droppedType
                );
                gameScreen.getDropItems().add(newDropItem);
                logger.info("An item (" + droppedType + ") dropped");
            }
        }
    }

    /**
     * Manages collisions between player ship and enemy ships.
     */
    private void manageShipEnemyCollisions() {
        handleShipCollision(gameScreen.getShip());
        if (gameScreen.isTwoPlayer()) {
            handleShipCollision(gameScreen.getShip2());
        }
    }

    private void handleShipCollision(Ship ship) {
        if (ship.isPermanentlyDestroyed() || ship.isDestroyed() || ship.isInvincible()) {
            return;
        }

        String logMessage = null;

        for (EnemyShip enemyShip : gameScreen.getEnemyShipFormation()) {
            if (!enemyShip.isDestroyed() && checkCollision(ship, enemyShip)) {
                gameScreen.getEnemyShipFormation().destroy(enemyShip);
                logMessage = "collided with enemy!";
                break;
            }
        }

        if (logMessage == null) {
            for (EnemyShip enemyShipSpecial : gameScreen.getEnemyShipSpecialFormation()) {
                if (enemyShipSpecial != null && !enemyShipSpecial.isDestroyed() && checkCollision(ship, enemyShipSpecial)) {
                    gameScreen.getEnemyShipSpecialFormation().destroy(enemyShipSpecial);
                    logMessage = "collided with special enemy!";
                    break;
                }
            }
        }

        if (logMessage == null && gameScreen.getOmegaBoss() != null && !gameScreen.getOmegaBoss().isDestroyed() && checkCollision(ship, gameScreen.getOmegaBoss())) {
            logMessage = "collided with omega boss!";
        }

        if (logMessage == null && gameScreen.getFinalBoss() != null && !gameScreen.getFinalBoss().isDestroyed() && checkCollision(ship, gameScreen.getFinalBoss())) {
            logMessage = "collided with final boss!";
        }

        if (logMessage != null) {
            ship.destroy();
            if (ship.getPlayerId() == 1) {
                gameScreen.decreaseLives();
                gameScreen.showHealthPopup(GameConfig.P1_COLLISION_MESSAGE);
                logger.info("Ship 1 " + logMessage + " " + gameScreen.getLives() + " lives remaining.");
            } else {
                gameScreen.decreaseLivesP2();
                gameScreen.showHealthPopup(GameConfig.P2_COLLISION_MESSAGE);
                logger.info("Ship 2 " + logMessage + " " + gameScreen.getLivesP2() + " lives remaining.");
            }
        }
    }


    /**
     * Manages collisions between player ship and dropped items.
     */
    private void manageItemCollisions() {
        Set<DropItem> acquiredDropItems = new HashSet<>();
        for (DropItem dropItem : gameScreen.getDropItems()) {
            boolean p1Collision = !gameScreen.getShip().isPermanentlyDestroyed() && !gameScreen.getShip().isDestroyed() && checkCollision(gameScreen.getShip(), dropItem);
            boolean p2Collision = gameScreen.isTwoPlayer() && gameScreen.getShip2() != null && !gameScreen.getShip2().isPermanentlyDestroyed() && !gameScreen.getShip2().isDestroyed() && checkCollision(gameScreen.getShip2(), dropItem);

            Ship acquiringShip = null;
            if (p1Collision && p2Collision) {
                acquiringShip = (distance(gameScreen.getShip(), dropItem) <= distance(gameScreen.getShip2(), dropItem)) ? gameScreen.getShip() : gameScreen.getShip2();
            } else if (p1Collision) {
                acquiringShip = gameScreen.getShip();
            } else if (p2Collision) {
                acquiringShip = gameScreen.getShip2();
            }

            if (acquiringShip != null) {
                logger.info("Player " + acquiringShip.getPlayerId() + " acquired dropItem: " + dropItem.getItemType());
                ItemHUDManager.getInstance().addDroppedItem(dropItem.getItemType());
                gameScreen.applyItemEffect(dropItem, acquiringShip);
                acquiredDropItems.add(dropItem);
            }
        }
        gameScreen.getDropItems().removeAll(acquiredDropItems);
        ItemPool.recycle(acquiredDropItems);
    }

    private int distance(final Entity a, final Entity b) {
        int centerAX = a.getPositionX() + a.getWidth() / 2;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;

        double distanceX = centerAX - centerBX;
        double distanceY = centerAY - centerBY;

        return (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    /**
     * Checks if two entities are colliding.
     */
    public boolean checkCollision(final Entity a, final Entity b) {
        if (a == null || b == null) return false;
        int centerAX = a.getPositionX() + a.getWidth() / 2;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
        int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
        int distanceX = Math.abs(centerAX - centerBX);
        int distanceY = Math.abs(centerAY - centerBY);
        return distanceX < maxDistanceX && distanceY < maxDistanceY;
    }
}