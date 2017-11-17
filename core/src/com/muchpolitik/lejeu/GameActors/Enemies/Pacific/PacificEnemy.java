package com.muchpolitik.lejeu.GameActors.Enemies.Pacific;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.PacificEnemyState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * Parent class for non agressive enemies.
 */
public class PacificEnemy extends Enemy {

    private StateMachine<PacificEnemy, PacificEnemyState> livingStateMachine;

    public PacificEnemy(String name, float startX, float startY, float range, GameStage gameStage) {
        super(startX, startY, range, gameStage);

        // for all pacific enemies
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), name, false);
        facingRight = MathUtils.randomBoolean();
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to pacific enemies
        livingStateMachine = new DefaultStateMachine<>(this);
        livingStateMachine.changeState(PacificEnemyState.WANDER);
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
        livingStateMachine.changeState(PacificEnemyState.WANDER);
    }
}
