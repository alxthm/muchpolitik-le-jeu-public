package com.muchpolitik.lejeu.GameActors.Enemies.DangerousObstacle;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.StaticObstacleState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * Parent class for static and dangerous obstacles.
 */
public class StaticObstacle extends Enemy {

    public StateMachine<StaticObstacle, StaticObstacleState> livingStateMachine;

    public StaticObstacle(String name, float startX, float startY, GameStage gameStage) {
        super(startX, startY, 0, gameStage);

        // for all obstacles
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), name, false);
        defenseType = DefenseType.Immortal;

        // set hitbox
        setSize(1, 1);
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
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
        livingStateMachine.changeState(StaticObstacleState.NORMAL);
    }

    @Override
    public void loadAnimations(TextureAtlas atlas, String name, boolean loadAtkAnimations) {
        stateTime = 0;

        // obstacles only use one animation.
        Array<TextureAtlas.AtlasRegion> frame = atlas.findRegions(name);
        currentAnimation = new Animation<>(0.1f, frame);
        currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to static obstacles
        livingStateMachine = new DefaultStateMachine<>(this);
        livingStateMachine.changeState(StaticObstacleState.NORMAL);
    }
}
