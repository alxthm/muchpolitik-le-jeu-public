package com.muchpolitik.lejeu.GameActors.Enemies.FallingObstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.muchpolitik.lejeu.AI.EnemyState;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.FallingObstacleState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

public class FallingObstacle extends Enemy {

    public StateMachine<FallingObstacle, FallingObstacleState> livingStateMachine;
    public TiledMapTileLayer tiledMapTileLayer;
    public Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };
    public Array<Rectangle> tiles = new Array<>();

    public FallingObstacle(String name, float startX, float startY, float range, GameStage gameStage) {
        super(startX, startY, range == 0? 1.5f : range, gameStage);

        // for all obstacles
        tiledMapTileLayer = (TiledMapTileLayer) gameStage.getLevel().getMap().getLayers().get("tiles");
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), name, false);
        defenseType = DefenseType.Immortal;
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
        livingStateMachine.changeState(FallingObstacleState.WAITING);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create state machine specific to falling obstacles
        livingStateMachine = new DefaultStateMachine<>(this);
        livingStateMachine.changeState(FallingObstacleState.WAITING);
    }

    @Override
    public void loadAnimations(TextureAtlas atlas, String name, boolean loadAtkAnimations) {
        stateTime = 0;

        // regular animation
        Array<TextureAtlas.AtlasRegion> frame = atlas.findRegions(name);
        currentAnimation = new Animation<>(0.1f, frame);

        // dying animation
        Array<TextureAtlas.AtlasRegion> dyingFrames = atlas.findRegions(name + "-dead");
        dyingAnimation = new Animation<>(0.1f, dyingFrames);
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
                // 'kill' the obstacle
                globalStateMachine.changeState(EnemyState.DYING);
            }
        }
    }

    public boolean noTilesProtectingPlayer() {
        // get tiles under the obstacle and over the player
        int startX = (int) getX();
        int startY = (int) Math.min(getY(), player.getY());
        int endX = (int) getX();
        int endY = (int) Math.max(getY(), player.getY());
        getTiles(startX, startY, endX, endY, tiles);

        // true if the are no tiles under the obstacle
        return tiles.size == 0;
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

    /**
     * Check if player is in range and, if it's the case, check if there is any tile protecting player
     */
    public boolean isPlayerInRange() {
        // if player is close enough to the obstacle (on X axis)
        if (Math.abs(getX() - player.getX()) < rangeOfAction) {
            if (noTilesProtectingPlayer())
                return true;
        }
        return false;
    }

    public void moveDown() {
        moveBy(0, -speed * Gdx.graphics.getDeltaTime());
    }
}
