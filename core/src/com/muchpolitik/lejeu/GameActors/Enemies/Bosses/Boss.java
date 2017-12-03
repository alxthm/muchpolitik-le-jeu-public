package com.muchpolitik.lejeu.GameActors.Enemies.Bosses;

import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.muchpolitik.lejeu.AI.BossStates.BossStates;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Faucille;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Marteau;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Projectile;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * Parent class for all bosses.
 */

public abstract class Boss extends Enemy {

    /**
     * Time between attacks.
     */
    public float timeBetweenAttacks;
    /**
     * Time between the beginning of the attack animation and the projectile throw. Also, time between the projectile throw
     * and going back to WALK state.
     */
    public float attackDelay = 0.5f;

    /**
     * The time at which the enemy entered WALK state for the last time (in milliseconds).
     */
    public long timeOfWalkBeginning;

    public StateMachine<Boss, BossStates> livingStateMachine;

    public Boss(float startX, float startY, float range, GameStage gameStage) {
        super(startX, startY, range, gameStage);
    }

    @Override
    public void startDying() {
        super.startDying();

        // avoid player to lose during the boss dying animation
        gameStage.getPlayer().setInvincible();
    }

    @Override
    public void returnToDefaultState() {
        livingStateMachine.changeState(BossStates.WALK);
    }

    public void startAttack() {
        // start attack animation from beginning
        currentAnimation = facingRight? attackingRightAnimation : attackingLeftAnimation;
        stateTime = 0;

        // tell enemy to throw projectile after some time
        MessageManager.getInstance().dispatchMessage(attackDelay, this, this, MessageType.THROW_PROJECTILE);
    }

    /**
     * Throw a projectile and send a message to go back to WALK state after.
     */
    public void throwProjectile() {
        // send a delayed message when enemy has finished the attack, to go back in WALK state.
        MessageManager.getInstance().dispatchMessage(attackDelay, this, this, MessageType.ATTACK_FINISHED);


        // create a random projectile and add it to the stage
        float x = getX();
        float y = getY() + 0.5f * getHeight();
        Projectile projectile;
        if (MathUtils.randomBoolean())
            projectile = new Marteau(x, y, facingRight, gameStage);
        else
            projectile = new Faucille(x, y, facingRight, gameStage);
        gameStage.addActor(projectile);

    }

    public boolean isPlayerBehind() {
        if (facingRight)
            return player.getX() < getX();
        else
            return player.getX() > getX();
    }

    /**
     * Make the phaseStateMachine handle the message.
     * @param telegram
     * @return
     */
    public abstract boolean handlePhasesStateMachine(Telegram telegram);

}
