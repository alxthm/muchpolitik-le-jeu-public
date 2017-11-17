package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.GameActors.Enemies.DangerousObstacle.StaticObstacle;

/**
 * Contains states for static obstacles.
 */
public enum StaticObstacleState implements State<StaticObstacle> {

    NORMAL() {
        @Override
        public void update(StaticObstacle obstacle) {
            obstacle.checkPlayerCollision();
        }
    };

    @Override
    public void enter(StaticObstacle entity) {

    }

    @Override
    public void exit(StaticObstacle obstacle) {

    }

    @Override
    public boolean onMessage(StaticObstacle obstacle, Telegram telegram) {
        return false;
    }
}
