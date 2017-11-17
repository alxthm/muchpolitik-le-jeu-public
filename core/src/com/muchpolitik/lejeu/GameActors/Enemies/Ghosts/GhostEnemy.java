package com.muchpolitik.lejeu.GameActors.Enemies.Ghosts;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.GhostEnemyState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

public class GhostEnemy extends Enemy {

    public StateMachine<GhostEnemy, GhostEnemyState> livingStateMachine;
    public float timeBetweenAttacks, attackDuration;
    public Enemy.DefenseType initialDefenseType;

    public GhostEnemy(String name, float startX, float startY, float range, GameStage gameStage) {
        super(startX, startY, range, gameStage);

        // for all sprinter enemies
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), name, false);
        facingRight = MathUtils.randomBoolean();
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
        livingStateMachine.changeState(GhostEnemyState.WANDER);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to ghosts
        livingStateMachine = new DefaultStateMachine<>(this);
        livingStateMachine.changeState(GhostEnemyState.WANDER);
    }
}
