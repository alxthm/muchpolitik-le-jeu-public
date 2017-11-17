package com.muchpolitik.lejeu.GameActors.Enemies.Bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;

/**
 * A progress bar on top of boss head to show its life remaining
 */

public class HealthBar extends ProgressBar {

    private Table table; // a parent to set healthbar size
    private Enemy enemy;

    public HealthBar(Enemy enemy) {
        // FIXME: 30/07/2017
        //TODO: include health bar as a progress bar in the skin
        super(0, 1, 0.01f, false, enemy.getGameStage().getLevel().getGame().getSkin(), "default-horizontal");
        this.enemy = enemy;

        // set size
        table = new Table();
        table.add(this).width(enemy.getWidth()).height(enemy.getWidth()/8);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // set progress and position (on top of enemy head)
        float progress = enemy.getLives() / enemy.STARTING_LIVES;
        setX(enemy.getX());
        setY(enemy.getY() + enemy.getHeight() + 0.1f);
        setValue(progress);
        Gdx.app.debug("hb width", String.valueOf(getWidth()));



    }
}
