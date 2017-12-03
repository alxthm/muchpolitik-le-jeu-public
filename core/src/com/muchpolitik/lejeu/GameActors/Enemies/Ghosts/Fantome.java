package com.muchpolitik.lejeu.GameActors.Enemies.Ghosts;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Fantome extends GhostEnemy {

    public Fantome(float startX, float startY, float range, GameStage gameStage) {
        super("fantome", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Basic;
        timeBetweenAttacks = 2;
        attackDuration = 4;
        speed = 1;
        deathTime = 4;
        setSize(1, 1);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
