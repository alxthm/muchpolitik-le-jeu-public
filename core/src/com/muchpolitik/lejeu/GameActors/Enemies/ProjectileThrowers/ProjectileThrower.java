package com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.ProjectileThrowerState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * Parent class for enemies who throw projectiles.
 */
public class ProjectileThrower extends Enemy {

    public StateMachine<ProjectileThrower, ProjectileThrowerState> livingStateMachine;
    /**
     * Time between attacks.
     */
    public float timeBetweenAttacks;
    /**
     * Time between the beginning of the attack animation and the projectile throw.
     */
    public float attackDelay;
    /**
     * If the enemy has started the attack but did not throw the projectile yet.
     */
    public boolean throwingProjectile = false;

    public ProjectileThrower(String name, float startX, float startY, float range, GameStage gameStage) {
        super(startX, startY, range, gameStage);

        // for all pacific enemies
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), name, true);
        facingRight = MathUtils.randomBoolean();
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
        livingStateMachine.changeState(ProjectileThrowerState.WANDER);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to projectile throwers
        livingStateMachine = new DefaultStateMachine<>(this);
        livingStateMachine.changeState(ProjectileThrowerState.WANDER);
    }

    @Override
    public void turnAround() {
        switch (livingStateMachine.getCurrentState()) {
            case WANDER:
                float newX = facingRight? (startX + rangeOfAction) : (startX - rangeOfAction);
                setX(newX);

                facingRight = !facingRight;
                if (facingRight)
                    currentAnimation = walkRightAnimation;
                else
                    currentAnimation = walkLeftAnimation;
                break;

            case ATTACK:
                facingRight = !facingRight;
                if (facingRight)
                    currentAnimation = attackingRightAnimation;
                else
                    currentAnimation = attackingLeftAnimation;
                break;
        }
    }

    /**
     * Start attack animation and wait a little (attackDelay) before throwing projectile.
     */
    public void startAttack() {
        // start attack animation from beginning
        currentAnimation = facingRight? attackingRightAnimation : attackingLeftAnimation;
        stateTime = 0;

        // the enemy has not thrown its projectile yet
        throwingProjectile = true;

        // tell enemy to throw projectile after some time
        MessageManager.getInstance().dispatchMessage(attackDelay, this, this, MessageType.THROW_PROJECTILE);
    }

    /**
     * Send a delayed message so that enemy is ready to attack again after some time.
     * Needs to be overridden by each specific enemy class to actually send a projectile !
     */
    public void throwProjectile() {
        // the projectile has been thrown
        throwingProjectile = false;

        // message to attack again after some time (if still in ATTACK state)
        MessageManager.getInstance().dispatchMessage(timeBetweenAttacks, this, this, MessageType.READY_TO_ATTACK);
    }

    /**
     * @return if enemy is on screen
     */
    public boolean isPlayerVisible() {
        return (isOnScreen() // enemy is on screen
                && player.getY() >= getY() // player isn't below enemy
                && player.getY() < getY() + 1 // player is roughly at the same heigh                 /
                // / enemy is facing player
                && ((facingRight && getX() < player.getX() - 1) || (!facingRight && getX() > player.getX() + 1)));
    }

    /**
     * @return if player is out of scope
     */
    public boolean isPlayerOutOfScope() {
        // player is out of scope if
        return (!isOnScreen() // enemy is out of screen
                || player.getY() < getY() // or player is below enemy
                || player.getY() > (getY() + 5)); // or player is too high
    }

    public boolean isPlayerBehind() {
        if (facingRight)
            return player.getX() < getX();
        else
            return player.getX() > getX();
    }
}
