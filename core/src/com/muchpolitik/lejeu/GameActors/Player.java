package com.muchpolitik.lejeu.GameActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.muchpolitik.lejeu.GameActors.GameObjects.InvincibilityBonus;
import com.muchpolitik.lejeu.Screens.Level;
import com.muchpolitik.lejeu.Stages.BackgroundStage;

public class Player extends Actor {

    private float typicalDelta = 0.017f;
    private float MAX_SPEED_X = 5;
    private float MAX_SPEED_Y = 18; // max speed while falling
    // x axis forces
    private float ACCELERATION = 0.3f / typicalDelta;
    private float DECELERATION = 0.5f / typicalDelta;
    private float FRICTION = 0.3f / typicalDelta;
    // y axis forces
    private float FIRST_JUMP_SPEED = 13;
    private float SECOND_JUMP_SPEED = 11;
    private float GRAVITY = 0.6f / typicalDelta;
    private float TIME_HURT = 1000; // en ms.
    private int lives = 3;

    private enum State {
        Idle,
        Walking,
        Jumping,
        Dying,
        Dead
    }

    private float speedX, speedY, hitboxWidth, feetHitboxHeight, stateTime;
    private int mapWidth, mapHeight;
    private long lastTimeHurt = 0, deathTime;
    private State state;
    private boolean pressingRight, pressingLeft, facingRight, grounded, jumpButtonPressed, needToDoSecondJump,
            canDoSecondJump, hurt, invincible, lastBlinkTransparent;

    private Level level;
    private OrthographicCamera camera;
    private BackgroundStage bgStage;
    private TiledMapTileLayer tiledMapTileLayer;
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private Array<Rectangle> surroundingTiles = new Array<>();
    private Rectangle bounds, feetBounds;
    /**
     * Used in collision detection with tiles.
     * <p>
     * xPoints are for collision on x-axis : 2 on the left side, and 2 on the right side.
     * <p>
     * yPoints are for collision on y-axis : 2 on the top, 2 on the bottom.
     */
    private Vector2 xPointLeft1, xPointLeft2, xPointRight1, xPointRight2,
            yPointTop1, yPointTop2, yPointBottom1, yPointBottom2;
    private TextureAtlas atlas;
    private Sprite dead;
    private Animation<TextureRegion> currentAnimation, idleRightAnimation, idleLeftAnimation, walkRightAnimation, walkLeftAnimation,
            jumpRightAnimation, jumpLeftAnimation;
    private SequenceAction stopInvincibilityDelayed;


    public Player(Level lvl, OrthographicCamera camera, String costumeName, float startX, float startY) {
        level = lvl;
        atlas = lvl.getGameObjectsAtlas();
        bgStage = lvl.getBgStage();
        this.camera = camera;

        loadAnimations(costumeName);

        // load super power
        switch (costumeName) {
            case "armure":
                lives += 1;
                break;
            case "ninja":
                FIRST_JUMP_SPEED += 5;
                SECOND_JUMP_SPEED += 3;
                break;
            case "cosmonaute":
                GRAVITY /= 2;
                break;
        }

        // get tiled map
        tiledMapTileLayer = (TiledMapTileLayer) level.getMap().getLayers().get("tiles");
        mapWidth = level.getMap().getProperties().get("width", int.class);
        mapHeight = level.getMap().getProperties().get("height", int.class);

        // set flags
        pressingRight = false;
        pressingLeft = false;
        jumpButtonPressed = false;
        needToDoSecondJump = false;
        canDoSecondJump = true;

        // initialize state
        currentAnimation = idleRightAnimation;
        state = State.Idle;
        facingRight = true;
        grounded = true;
        hurt = false;
        invincible = false;

        // initialize speed, bounds and position
        speedX = 0;
        speedY = 0;
        setBounds(startX, startY, 1, 1);
        hitboxWidth = getWidth() * 2 / 3f;
        feetHitboxHeight = 1 / 3f;
        bounds = new Rectangle(getX() + (getWidth() - hitboxWidth) / 2f, getY(),
                hitboxWidth, getHeight());
        feetBounds = new Rectangle(getX() + (getWidth() - hitboxWidth) / 2f, getY(),
                hitboxWidth, feetHitboxHeight);
        xPointLeft1 = new Vector2();
        xPointLeft2 = new Vector2();
        xPointRight1 = new Vector2();
        xPointRight2 = new Vector2();
        yPointTop1 = new Vector2();
        yPointTop2 = new Vector2();
        yPointBottom1 = new Vector2();
        yPointBottom2 = new Vector2();

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (state != State.Dead) {
            if (state != State.Dying) {
                // apply forces
                Vector2 newPos = applyForces(delta);
                // check tiles collision
                newPos = checkTilesCollision(newPos);
                // update position
                movePlayer(newPos, delta);
            }
            updateLogic();
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        // make character blink when hurt (and not invincible)
        if (hurt && !invincible) {
            if (lastBlinkTransparent) {
                setColor(color.r, color.g, color.b, 0.8f);
                lastBlinkTransparent = false;
            } else {
                setColor(color.r, color.g, color.b, 0);
                lastBlinkTransparent = true;
            }
        }
        // remove transparency if player isn't fading out
        else if (state != State.Dying && state != State.Dead) {
            setColor(color.r, color.g, color.b, 1);
        }
        batch.setColor(getColor());

        // draw current frame scaled and at player's location
        TextureRegion currentFrame;
        if (state == State.Dying)
            currentFrame = dead;
        else
            currentFrame = currentAnimation.getKeyFrame(stateTime);

        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());


        // for debugging
//        Level.shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
//        Level.shapeRenderer.rect(feetBounds.getX(), feetBounds.getY(), feetBounds.getWidth(), feetBounds.getHeight());
//        Level.shapeRenderer.point(xPointLeft1.x, xPointLeft1.y, 0);
//        Level.shapeRenderer.point(xPointLeft2.x, xPointLeft2.y, 0);
//        Level.shapeRenderer.point(xPointRight1.x, xPointRight1.y, 0);
//        Level.shapeRenderer.point(xPointRight2.x, xPointRight2.y, 0);
//        Level.shapeRenderer.point(yPointBottom1.x, yPointBottom1.y, 0);
//        Level.shapeRenderer.point(yPointBottom2.x, yPointBottom2.y, 0);
//        Level.shapeRenderer.point(yPointTop1.x, yPointTop1.y, 0);
//        Level.shapeRenderer.point(yPointTop2.x, yPointTop2.y, 0);

        batch.setColor(1, 1, 1, 1);
    }

    public void loadAnimations(String costumeName) {
        // idle animations
        Array<TextureAtlas.AtlasRegion> idleRightFrames = atlas.findRegions("player-" + costumeName + "-idle");
        Array<TextureAtlas.AtlasRegion> idleLeftFrames = new Array<>();
        for (TextureAtlas.AtlasRegion rFrame : idleRightFrames) {
            TextureAtlas.AtlasRegion lFrame = new TextureAtlas.AtlasRegion(rFrame);
            lFrame.flip(true, false);
            idleLeftFrames.add(lFrame);
        }
        idleRightAnimation = new Animation<TextureRegion>(0.1f, idleRightFrames);
        idleRightAnimation.setPlayMode(Animation.PlayMode.LOOP);
        idleLeftAnimation = new Animation<TextureRegion>(0.1f, idleLeftFrames);
        idleLeftAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // walk animations
        Array<TextureAtlas.AtlasRegion> walkRightFrames = atlas.findRegions("player-" + costumeName + "-walk");
        Array<TextureAtlas.AtlasRegion> walkLeftFrames = new Array<>();
        for (TextureAtlas.AtlasRegion rFrame : walkRightFrames) {
            TextureAtlas.AtlasRegion lFrame = new TextureAtlas.AtlasRegion(rFrame);
            lFrame.flip(true, false);
            walkLeftFrames.add(lFrame);
        }
        walkRightAnimation = new Animation<TextureRegion>(0.08f, walkRightFrames);
        walkRightAnimation.setPlayMode(Animation.PlayMode.LOOP);
        walkLeftAnimation = new Animation<TextureRegion>(0.08f, walkLeftFrames);
        walkLeftAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // jump animations
        Array<TextureAtlas.AtlasRegion> jumpRightFrames = atlas.findRegions("player-" + costumeName + "-jump");
        Array<TextureAtlas.AtlasRegion> jumpLeftFrames = new Array<>();
        for (TextureAtlas.AtlasRegion rFrame : jumpRightFrames) {
            TextureAtlas.AtlasRegion lFrame = new TextureAtlas.AtlasRegion(rFrame);
            lFrame.flip(true, false);
            jumpLeftFrames.add(lFrame);
        }
        jumpRightAnimation = new Animation<TextureRegion>(0.1f, jumpRightFrames);
        jumpLeftAnimation = new Animation<TextureRegion>(0.1f, jumpLeftFrames);

        dead = atlas.createSprite("player-" + costumeName + "-dead");

        stateTime = 0;
    }

    /**
     * Apply all forces to the player, and return the updated position
     *
     * @return The updated position
     */
    private Vector2 applyForces(float delta) {
        // set speed on x-axis
        // v(t+dt) = v(t) + a*dt
        if (pressingRight && !pressingLeft) {
            if (speedX < 0)
                speedX += DECELERATION * delta;
            else
                speedX = Math.min(speedX + ACCELERATION * delta, MAX_SPEED_X);

            state = State.Walking;
            facingRight = true;
            currentAnimation = walkRightAnimation;
        } else if (pressingLeft && !pressingRight) {
            if (speedX > 0)
                speedX -= DECELERATION * delta;
            else
                speedX = Math.max(speedX - ACCELERATION * delta, -MAX_SPEED_X);

            state = State.Walking;
            facingRight = false;
            currentAnimation = walkLeftAnimation;
        } else {
            // if no key is pressed, add friction to slow player down
            if (Math.abs(speedX) >= FRICTION * delta)
                speedX -= Math.signum(speedX) * FRICTION * delta;
            else
                speedX = 0;

            state = State.Idle;
            currentAnimation = facingRight ? idleRightAnimation : idleLeftAnimation;
        }


        // apply gravity and jump force on y-axis
        if (speedY > -MAX_SPEED_Y)
            speedY -= GRAVITY * delta;
        if (jumpButtonPressed && grounded) {
            speedY = FIRST_JUMP_SPEED;
            stateTime = 0; // for jump animation to start from beginning
        } else if (needToDoSecondJump) {
            speedY = SECOND_JUMP_SPEED;
            stateTime = 0; // for jump animation to start from beginning
            needToDoSecondJump = false;
        }

        // return the new position of player, once all forces are applied
        // x(t+dt) = x(t) + v * dt
        // and same for y(t)
        return new Vector2(getX() + speedX * delta, getY() + speedY * delta);
    }

    /**
     * Check collision with surrounding surroundingTiles, and adjust player position
     * so he can't move into surroundingTiles.
     *
     * @return The updated position
     */
    private Vector2 checkTilesCollision(Vector2 newPos) {

        // get surroundingTiles around the player
        int startX = (int) newPos.x;
        int startY = (int) newPos.y;
        int endX = (int) (newPos.x + getWidth());
        int endY = (int) (newPos.y + getHeight());
        getTiles(startX, startY, endX, endY, surroundingTiles);

        updateTileCollisionBounds(newPos);

        grounded = false; // false by default, unless player collides with ground surroundingTiles
        for (Rectangle tile : surroundingTiles) {
            // check collision on y-axis with 2 points on top, 2 points on the bottom

            if (speedY < 0 && (tile.contains(yPointBottom1) || tile.contains(yPointBottom2))) {
                newPos.y = tile.getY() + 1;
                yPointBottom1.y = tile.getY() + 1;
                yPointBottom2.y = tile.getY() + 1;
                speedY = 0;
                grounded = true;
                canDoSecondJump = true;

            } else if (speedY > 0 && (tile.contains(yPointTop1) || tile.contains(yPointTop2))) {
                newPos.y = tile.getY() - getHeight();
                yPointTop1.y = tile.getY() - getHeight();
                yPointTop2.y = tile.getY() - getHeight();
                speedY = 0;
            }

            // important to avoid bugs
            updateTileCollisionBounds(newPos);

            // check collision on x-axis with 2 points at the right and 2 at the left to prevent going into surroundingTiles

            if (speedX > 0 && (tile.contains(xPointRight1) || tile.contains(xPointRight2)))
                // push the player to the left border of the tile
                newPos.x = Math.max(tile.getX() - getWidth(), 0);

            else if ((speedX < 0) && (tile.contains(xPointLeft1) || tile.contains(xPointLeft2)))
                // push the player to the right border of the tile
                newPos.x = tile.getX() + 1;
        }


        if (!grounded) {
            state = State.Jumping;
            currentAnimation = facingRight ? jumpRightAnimation : jumpLeftAnimation;
        }

        return newPos;
    }


    /**
     * Move player and eventually camera.
     */
    private void movePlayer(Vector2 newPos, float delta) {
        float minCamX = camera.viewportWidth / 2, maxCamX = mapWidth - camera.viewportWidth / 2;
        float minCamY = camera.viewportHeight / 2, maxCamY = mapHeight - camera.viewportHeight / 2;
        float lerp = 5; // lerp : linear interpolation to move the camera more smoothly
        // in percents of map width/height
        float deltaX = (newPos.x - getX()) / mapWidth;
        float deltaY = (newPos.y - getY()) / mapHeight;

        // move player
        setPosition(newPos.x, newPos.y);

        // make the camera follow player smoothly so he stays at the center of the screen
        camera.translate((getX() - camera.position.x) * lerp * delta,
                (getY() - camera.position.y) * lerp * delta);


        // prevent camera and player from going out of world boundaries on x-axis
        if (camera.position.x <= minCamX)
            camera.position.x = minCamX;
        else if (camera.position.x >= maxCamX)
            camera.position.x = maxCamX;
        else
            // if the camera isn't at the edge
            // (minus sign because the background goes the opposite way)
            bgStage.translate(-deltaX, 0);

        if (getX() < 0)
            setX(0);
        else if (getX() > mapWidth - 1)
            setX(mapWidth - 1);

        // prevent camera from going out of world boundaries on y-axis
        if (camera.position.y <= minCamY)
            camera.position.y = minCamY;
        else if (camera.position.y >= maxCamY)
            camera.position.y = maxCamY;
        else
            // if the camera isn't at the edge
            // (minus sign because the background goes the opposite way)
            bgStage.translate(0, -deltaY);

    }

    /**
     * Updates state time (for animation), bounds, and kill player if dying animation is over,
     * or if player felt out of the world.
     */
    private void updateLogic() {
        // update state time
        stateTime += Gdx.graphics.getDeltaTime();

        // update bounds rectangle
        bounds.setPosition(getX() + (getWidth() - hitboxWidth) / 2f, getY());
        feetBounds.setPosition(getX() + (getWidth() - hitboxWidth) / 2f, getY());

        // update hurt state
        if (TimeUtils.timeSinceMillis(lastTimeHurt) > TIME_HURT) {
            hurt = false;
        }

        // load game over screen if dying animation is finished or player falls out of the screen
        if ((state == State.Dying && TimeUtils.timeSinceMillis(deathTime) > 1000)
                || getY() < -10) {
            state = State.Dead;
            level.loadGameOverMenu(Level.GameOverCause.PlayerDead);
        }

    }

    /**
     * Remove the player a life, or kill him depending on his lives.
     *
     * @return true if player actually lost a life/died
     */
    public boolean loseOneLife() {
        if (state != State.Dead && state != State.Dying && !hurt && !invincible) {
            if (lives > 1) {
                lives -= 1;
                hurt = true;
                lastTimeHurt = TimeUtils.millis();
                return true;
            } else {
                lives -= 1;

                // set player dying
                addAction(Actions.fadeOut(1));
                state = State.Dying;
                deathTime = TimeUtils.millis();
                setY((int) getY()); // to put him down to the floor
                return true;
            }
        }
        // return true if player is invincible, so projectiles explode,
        // and false otherwise, so projectiles go through the player (if dead, dying or hurt)
        return invincible;
    }

    /**
     * Get surroundingTiles inside the rectangle of parameters.
     * Rectangles are obtained from a pool, and added to the surroundingTiles Array.
     */
    private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
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

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getFeetBounds() {
        return feetBounds;
    }

    public int getLives() {
        return lives;
    }

    public boolean isHurt() {
        return hurt;
    }

    public void addOneLife() {
        lives++;
    }

    public void setPressingRight(boolean pressingRight) {
        this.pressingRight = pressingRight;
        if (pressingRight)
            pressingLeft = false;
    }

    public void setPressingLeft(boolean pressingLeft) {
        this.pressingLeft = pressingLeft;
        if (pressingLeft)
            pressingRight = false;
    }

    public void setJumping(boolean jumpButtonPressed) {
        this.jumpButtonPressed = jumpButtonPressed;

        if (jumpButtonPressed && canDoSecondJump && !grounded) {
            needToDoSecondJump = true;
            canDoSecondJump = false;
        }
    }

    /**
     * If the player is walking, does nothing.
     * If the player is in the air (ie he jumped on the enemy), make him bounce.
     * <p>
     * Also allow the player to jump again.
     */
    public void bounce() {
        if (state != State.Walking && state != State.Idle) {
            speedY = FIRST_JUMP_SPEED * 2 / 3f;
            canDoSecondJump = true;
        }
    }

    /**
     * Change player color and make him invincible temporarily. Also, make sure
     * that no previous bonus will expire before duration time)
     **/
    public void makeInvincible() {
        // make invincible
        invincible = true;

        // if there is already an invincibility bonus running, remove it
        if (getActions().contains(stopInvincibilityDelayed, true)) {
            removeAction(stopInvincibilityDelayed);
        }

        // reset the action
        Action stopInvincibleAction = new Action() {
            @Override
            public boolean act(float delta) {
                invincible = false;
                return true;
            }
        };
        ColorAction colorAction1 = Actions.color(Color.RED,
                InvincibilityBonus.duration / 2f, Interpolation.exp5Out);
        ColorAction colorAction2 = Actions.color(Color.WHITE,
                InvincibilityBonus.duration / 2f, Interpolation.exp5In);
        stopInvincibilityDelayed = Actions.sequence(colorAction1, colorAction2, stopInvincibleAction);

        // add the action
        addAction(stopInvincibilityDelayed);
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible() {
        invincible = true;
    }

    /**
     * Update the points used for tile collision detection
     *
     * @param newPos The new position used
     */
    public void updateTileCollisionBounds(Vector2 newPos) {
        // points for x-axis collision
        xPointLeft1.set(newPos.x, newPos.y + getHeight() * 1 / 4f);
        xPointLeft2.set(newPos.x, newPos.y + getHeight() * 3 / 4f);
        xPointRight1.set(newPos.x + getWidth(), newPos.y + getHeight() * 1 / 4f);
        xPointRight2.set(newPos.x + getWidth(), newPos.y + getHeight() * 3 / 4f);

        // points for y-axis collision
        yPointTop1.set(newPos.x + getWidth() * 1 / 4f, newPos.y + getHeight());
        yPointTop2.set(newPos.x + getWidth() * 3 / 4f, newPos.y + getHeight());
        yPointBottom1.set(newPos.x + getWidth() * 1 / 4f, newPos.y);
        yPointBottom2.set(newPos.x + getWidth() * 3 / 4f, newPos.y);
    }


    //TODO: remove once testing is over
    public float getACCELERATION() {
        return ACCELERATION;
    }

    public float getFRICTION() {
        return FRICTION;
    }

    public float getDECELERATION() {
        return DECELERATION;
    }

    public float getMAX_SPEED_X() {
        return MAX_SPEED_X;
    }

    public float getFIRST_JUMP_SPEED() {
        return FIRST_JUMP_SPEED;
    }

    public float getGRAVITY() {
        return GRAVITY;
    }

    public void setACCELERATION(float ACCELERATION) {
        this.ACCELERATION = ACCELERATION;
    }

    public void setFRICTION(float FRICTION) {
        this.FRICTION = FRICTION;
    }

    public void setDECELERATION(float DECELERATION) {
        this.DECELERATION = DECELERATION;
    }

    public void setGRAVITY(float GRAVITY) {
        this.GRAVITY = GRAVITY;
    }

    public void setFIRST_JUMP_SPEED(float FIRST_JUMP_SPEED) {
        this.FIRST_JUMP_SPEED = FIRST_JUMP_SPEED;
    }

    public void setMAX_SPEED_X(float MAX_SPEED_X) {
        this.MAX_SPEED_X = MAX_SPEED_X;
    }
}
