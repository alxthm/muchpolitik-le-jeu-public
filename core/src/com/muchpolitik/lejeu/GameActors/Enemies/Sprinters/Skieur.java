package com.muchpolitik.lejeu.GameActors.Enemies.Sprinters;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Skieur extends  SprinterEnemy {

    public Skieur(float startX, float startY, float range, GameStage gameStage) {
        super("skieur", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Basic;
        speed = 1;
        attackSpeed = 5;
        deathTime = 4;
        setSize(1, 1);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
