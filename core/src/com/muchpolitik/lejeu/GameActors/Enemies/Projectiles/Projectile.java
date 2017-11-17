package com.muchpolitik.lejeu.GameActors.Enemies.Projectiles;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.muchpolitik.lejeu.AI.EnemyState;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.ProjectileState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * Parent class for projectiles.
 */
public class Projectile extends Enemy {

    public StateMachine<Projectile, ProjectileState> livingStateMachine;
    public TiledMapTileLayer tiledMapTileLayer;
    public Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };
    public Array<Rectangle> tiles = new Array<>();

    public Projectile(String name, float startX, float startY, boolean facingRight, GameStage gameStage) {
        super(startX, startY, 0, gameStage);

        // for all projectiles
        tiledMapTileLayer = (TiledMapTileLayer) gameStage.getLevel().getMap().getLayers().get("tiles");
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), name, false);
        this.facingRight = facingRight;
        currentAnimation = facingRight? walkRightAnimation : walkLeftAnimation;
    }

    @Override
    public void updateLivingStateMachine() {
        livingStateMachine.update();
    }

    @Override
    public boolean handleLivingStateMachineMessage(Telegram telegram) {
        return livingStateMachine.handleMessage(telegram);
    }

    @Override
    public void returnToDefaultState() {
        livingStateMachine.changeState(ProjectileState.FLYING);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to projectiles
        livingStateMachine = new DefaultStateMachine<>(this);
        livingStateMachine.changeState(ProjectileState.FLYING);
    }

    @Override
    public void checkPlayerCollision() {
        updateBounds();
        if (collideWithPlayer() && !player.isHurt()) {
            // remove a life for the player, and 'kill' the projectile
            if (player.loseOneLife())
                globalStateMachine.changeState(EnemyState.DYING);
        }
    }

    /**
     * Check collision with surrounding tiles, and 'die' if this hits a tile.
     */
    public void checkTilesCollision() {
        // get tiles around the projectile
        int startX = (int) getX();
        int startY = (int) getY();
        int endX = (int) (getX() + getWidth());
        int endY = (int) (getY() + getHeight());
        getTiles(startX, startY, endX, endY, tiles);

        // check collision with projectile
        for (Rectangle tile : tiles) {
            if (tile.overlaps(bounds)) {
                // 'kill' the projectile
                globalStateMachine.changeState(EnemyState.DYING);
            }
        }
    }

    /**
     * Get tiles inside the rectangle of parameters.
     * Rectangles are obtained from a pool, and added to the tiles Array.
     */
    private void getTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
    }
}
