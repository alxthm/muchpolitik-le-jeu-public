package com.muchpolitik.lejeu.GameActors.Enemies.DangerousObstacle;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.AI.NormalEnemiesStates.BlinkingObstacleState;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * Enemy blinking to make platform dangerous in boss levels.
 */

public class BlinkingObstacle extends Enemy {

    public StateMachine<BlinkingObstacle, BlinkingObstacleState> livingStateMachine;

    public float onDuration = 6, offDuration = 8, blinkDuration = 1, apparitionDelay;

    public BlinkingObstacle(float startX, float startY, float apparitionDelay, GameStage gameStage) {
        super(startX, startY, 0, gameStage); // create enemy with range = 0
        this.apparitionDelay = apparitionDelay;

        // for all obstacles
        String name = "blinking-obstacle";
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
        livingStateMachine.changeState(BlinkingObstacleState.OFF);
    }

    /**
     * Obstacles only use one animation, so there is no walk/atk/stunned animations to load.
     * @param atlas
     * @param name
     */
    @Override
    public void loadAnimations(TextureAtlas atlas, String name, boolean loadAtkAnimations) {
        stateTime = 0;

        Array<TextureAtlas.AtlasRegion> frame = atlas.findRegions(name);
        currentAnimation = new Animation<>(0.1f, frame);
    }


    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to static obstacles
        livingStateMachine = new DefaultStateMachine<>(this);

        // make the obstacle go off until the delay is over
        livingStateMachine.changeState(BlinkingObstacleState.WAITING_BEFORE_APPARITION);
        MessageManager.getInstance().dispatchMessage(apparitionDelay, this, this, MessageType.TIME_TO_BLINK);
    }
}
