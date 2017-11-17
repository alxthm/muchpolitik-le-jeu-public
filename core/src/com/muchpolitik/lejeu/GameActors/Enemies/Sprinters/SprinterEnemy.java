package com.muchpolitik.lejeu.GameActors.Enemies.Sprinters;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.SprinterEnemyState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

public class SprinterEnemy extends Enemy {

    public StateMachine<SprinterEnemy, SprinterEnemyState> livingStateMachine;
    public float attackSpeed;

    public SprinterEnemy(String name, float startX, float startY, float range, GameStage gameStage) {
        super(startX, startY, range, gameStage);

        // for all sprinter enemies
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
        livingStateMachine.changeState(SprinterEnemyState.WANDER);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to sprinters
        livingStateMachine = new DefaultStateMachine<>(this);
        livingStateMachine.changeState(SprinterEnemyState.WANDER);
    }

    /**
     * Move the sprinting enemy within range of action.
     */
    public void sprintWithinRangeOfAction() {
        if (isInRangeOfAction()) {
            // move enemy
            if (facingRight)
                moveBy(attackSpeed * deltaTime, 0);
            else
                moveBy(-attackSpeed * deltaTime, 0);
        }
        else {
            livingStateMachine.changeState(SprinterEnemyState.WANDER);
            turnAround();
        }
    }

    /**
     * @return if enemy is on screen
     */
    public boolean isPlayerVisible() {
        // true if enemy is facing player
        return ((facingRight && getX() < player.getX() - 1 || !facingRight && getX() > player.getX() + 1)
                && player.getY() >= getY() // and player isn't below enemy
                && player.getY() < getY() + 1 // and player is roughly at the same height
                && Math.abs(startX - player.getX()) <= rangeOfAction); // and player is within range of action
    }

    public boolean isPlayerBehind() {
        if (facingRight)
            return player.getX() < getX() - 1;
        else
            return player.getX() > getX() + 1;
    }


}
