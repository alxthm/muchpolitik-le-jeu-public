package com.muchpolitik.lejeu.GameActors.Enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.muchpolitik.lejeu.AI.EnemyState;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Player;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * Parent class for all enemies.
 */
public abstract class Enemy extends Actor implements Telegraph {

    public int STARTING_LIVES = 2;

    public float startX, hitboxWidth, rangeOfAction, speed, deathTime, stateTime, deltaTime;
    /**
     * Number of hits an enemy can take before dying (only used with tank enemies).
     */
    public int lives = STARTING_LIVES;
    public boolean facingRight;
    public Animation<TextureAtlas.AtlasRegion> currentAnimation, walkRightAnimation, walkLeftAnimation, stunnedRightAnimation,
            stunnedLeftAnimation, dyingAnimation, attackingRightAnimation, attackingLeftAnimation;
    public Rectangle bounds;
    public float stunnedTime = 5;
    /**
     * Proportion of enemy height beyond which is head hitbox.
     */
    public float headHeight = 2 / 3f;


    public enum DefenseType {
        Basic,
        Tank,
        Immortal;
    }

    public Player player;
    public StateMachine<Enemy, EnemyState> globalStateMachine;
    public GameStage gameStage;
    // a generic state machine, for generic states (LIVING, STUNNED, DYING) of all types of enemies.
    // handles all defense types (basic, tank, immortal)
    public DefenseType defenseType;

    /**
     * Save parameters for later use.
     */
    public Enemy(float startX, float startY, float range, GameStage gameStage) {
        setPosition(startX, startY);
        this.gameStage = gameStage;
        this.startX = startX;
        rangeOfAction = range;
        player = gameStage.getPlayer();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        deltaTime = delta;
        stateTime += delta;

        globalStateMachine.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // tint the image if needed
        batch.setColor(getColor());

        // draw the enemy
        batch.draw(currentAnimation.getKeyFrame(stateTime), getX(), getY(), getWidth(), getHeight());

        // for debugging
        //Level.shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());

        batch.setColor(1, 1, 1, 1);
    }

    /**
     * Handles the msg by giving it to globalStateMachine of the enemy
     *
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Telegram msg) {
        return globalStateMachine.handleMessage(msg);
    }

    /**
     * Create enemy's globalStateMachine and enter LIVING state.
     * Has to be overridden to also create specific livingStateMachine
     * (and eventually a phasesStateMachine for bosses).
     */
    public void createStateMachines() {
        globalStateMachine = new DefaultStateMachine<>(this);
        globalStateMachine.changeState(EnemyState.LIVING);
    }

    /**
     * Get graphics from level TextureAtlas and load following animations :
     * walk right,
     * walk left,
     * dying
     *
     * @param atlas             gameObjects atlas
     * @param name              name of the enemy
     * @param loadAtkAnimations if attackingLeft and attackingRight animations should be loaded (for bosses, projectiles throwers and sprinters)
     */
    public void loadAnimations(TextureAtlas atlas, String name, boolean loadAtkAnimations) {
        stateTime = 0;

        // walk right animation
        Array<TextureAtlas.AtlasRegion> walkRightFrames = atlas.findRegions(name + "-walk");
        walkRightAnimation = new Animation<>(0.1f, walkRightFrames);
        walkRightAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // walk left animation
        Array<TextureAtlas.AtlasRegion> walkLeftFrames = new Array<>();
        for (TextureAtlas.AtlasRegion walkRightFrame : walkRightFrames) {
            TextureAtlas.AtlasRegion walkLeftFrame = new TextureAtlas.AtlasRegion(walkRightFrame);
            walkLeftFrame.flip(true, false);
            walkLeftFrames.add(walkLeftFrame);
        }
        walkLeftAnimation = new Animation<>(0.1f, walkLeftFrames);
        walkLeftAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // dying animation
        Array<TextureAtlas.AtlasRegion> dyingFrames = atlas.findRegions(name + "-dead");
        dyingAnimation = new Animation<>(0.1f, dyingFrames);

        if (loadAtkAnimations) {
            // attack right animation
            Array<TextureAtlas.AtlasRegion> attackingRightFrames = atlas.findRegions(name + "-atk");
            attackingRightAnimation = new Animation<>(0.1f, attackingRightFrames);

            // attack left animation
            Array<TextureAtlas.AtlasRegion> attackingLeftFrames = new Array<>();
            for (TextureAtlas.AtlasRegion attackingRightFrame : attackingRightFrames) {
                TextureAtlas.AtlasRegion attackingLeftFrame = new TextureAtlas.AtlasRegion(attackingRightFrame);
                attackingLeftFrame.flip(true, false);
                attackingLeftFrames.add(attackingLeftFrame);
            }
            attackingLeftAnimation = new Animation<>(0.15f, attackingLeftFrames);
        }
    }

    /**
     * Load stunned right and stunned left animations for tank enemies.
     */
    public void loadStunnedAnimations(TextureAtlas atlas, String name) {
        // stunned right animation
        Array<TextureAtlas.AtlasRegion> stunnedRightFrames = atlas.findRegions(name + "-stunned");
        stunnedRightAnimation = new Animation<>(0.1f, stunnedRightFrames);
        stunnedRightAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // stunned left animation
        Array<TextureAtlas.AtlasRegion> stunnedLeftFrames = new Array<>();
        for (TextureAtlas.AtlasRegion stunnedRightFrame : stunnedRightFrames) {
            TextureAtlas.AtlasRegion stunnedLeftFrame = new TextureAtlas.AtlasRegion(stunnedRightFrame);
            stunnedLeftFrame.flip(true, false);
            stunnedLeftFrames.add(stunnedLeftFrame);
        }
        stunnedLeftAnimation = new Animation<>(0.1f, stunnedLeftFrames);
        stunnedLeftAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
     * Check collision with player, to hit him or kill the enemy.
     */
    public void checkPlayerCollision() {
        updateBounds();

        if (collideWithPlayer()) {
            switch (defenseType) {

                case Basic:
                    if (isHit()) {
                        // kill enemy
                        player.bounce();
                        startDying();
                    } else {
                        player.loseOneLife();
                    }
                    break;

                case Tank:
                    if (isHit()) {
                        if (lives > 1) {
                            player.bounce();
                            loseOneLife();
                        } else {
                            player.bounce();
                            startDying();
                        }
                    } else {
                        player.loseOneLife();
                    }
                    break;

                case Immortal:
                    player.loseOneLife();
                    break;
            }
        }
    }

    /**
     * Set enemy position to the range limit and change animation.
     */
    public void turnAround() {
        float newX = facingRight ? (startX + rangeOfAction) : (startX - rangeOfAction);
        setX(newX);

        facingRight = !facingRight;
        currentAnimation = facingRight ? walkRightAnimation : walkLeftAnimation;
    }

    /**
     * Make the enemy walk around start point.
     */
    public void moveWithinRangeOfAction() {
        if (isInRangeOfAction()) {
            // move enemy
            if (facingRight)
                moveBy(speed * deltaTime, 0);
            else
                moveBy(-speed * deltaTime, 0);
        } else {
            turnAround();
        }
    }

    /**
     * Update the living state machine of the enemy.
     */
    public abstract void updateLivingStateMachine();

    /**
     * Make the livingStateMachine handle the message.
     */
    public abstract boolean handleLivingStateMachineMessage(Telegram telegram);

    /**
     * Change livingStateMachine state to the current state, so the state is entered again
     * (to be called after stun is finished).
     */
    public abstract void returnToDefaultState();

    protected void updateBounds() {
        bounds.setPosition(getX() + (getWidth() - hitboxWidth) / 2f, getY());
    }

    /**
     * Lose a life and get stunned
     */
    public void loseOneLife() {
        lives -= 1;
        globalStateMachine.changeState(EnemyState.STUNNED);
    }

    /**
     * Enter DYING state.
     */
    public void startDying() {
        MessageManager.getInstance().dispatchMessage(this, this, MessageType.START_DYING);
        globalStateMachine.changeState(EnemyState.DYING);
    }

    protected boolean isInRangeOfAction() {
        return Math.abs(startX - getX()) <= rangeOfAction;
    }

    protected boolean collideWithPlayer() {
        return bounds.overlaps(player.getBounds());
    }

    /**
     * Check if the enemy is hit in the head, or if the player is in invincible mode (and every contact is a hit).
     *
     * @return if the ennemy should take a hit
     */
    private boolean isHit() {
        // true if player's feet hit the enemy's head
        Rectangle head = new Rectangle(getX(), getY() + getHeight() * headHeight,
                getWidth(), getHeight() * (1 - headHeight));
        boolean isHitInTheHead = player.getFeetBounds().overlaps(head);
        return (isHitInTheHead && !player.isHurt()) || player.isInvincible();
    }

    protected boolean isOnScreen() {
        return gameStage.getCamera().frustum.boundsInFrustum(getX(), getY(), 0, getWidth() / 2f, getHeight() / 2f, 0);
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public int getLives() {
        return lives;
    }
}
