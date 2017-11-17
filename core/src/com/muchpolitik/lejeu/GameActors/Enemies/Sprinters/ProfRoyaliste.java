package com.muchpolitik.lejeu.GameActors.Enemies.Sprinters;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class ProfRoyaliste extends  SprinterEnemy {

    public ProfRoyaliste(float startX, float startY, float range, GameStage gameStage) {
        super("skieur", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Tank;
        loadStunnedAnimations(gameStage.getLevel().getGameObjectsAtlas(), "skieur");
        speed = 1;
        attackSpeed = 4;
        deathTime = 4;
        setSize(1, 1);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX() + (getWidth() - hitboxWidth) / 2f, getY(), hitboxWidth, getHeight());

        createStateMachines();
    }
}

