package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;

public class BackgroundStage extends Stage {

    private Texture backgroundTexture;
    private Image background;
    private float rangeX, rangeY;

    private final float maxMoveX = 5, maxMoveY = 5;

    public BackgroundStage(Texture backgroundTexture) {
        super(new ExtendViewport(LeJeu.minWidth, LeJeu.minHeight, LeJeu.maxWidth, LeJeu.maxHeight));
        this.backgroundTexture = backgroundTexture;

        // create background
        background = new Image(backgroundTexture);
        addActor(background);

        // how much the image should move on x and y axis
        rangeX = background.getWidth() - getWidth();
        rangeY = background.getHeight() - getHeight();
    }

    /**
     * Translate background image
     *
     * @param deltaX in percents of map width
     * @param deltaY in percents of map height
     */
    public void translate(float deltaX, float deltaY) {
        // prevent the background from moving too fast
        float moveX = Math.abs(deltaX * rangeX) > maxMoveX ?
                Math.signum(deltaX) * maxMoveX : deltaX * rangeX;
        float moveY = Math.abs(deltaY * rangeY) > maxMoveY ?
                Math.signum(deltaY) * maxMoveY : deltaY * rangeY;

        background.moveBy(moveX, moveY);

        // make sure the background stays fully on the screen
        if (background.getX() > 0)
            background.setX(0);
        else if (background.getX() + background.getWidth() < getWidth())
            background.setX(getWidth() - background.getWidth());
        if (background.getY() > 0)
            background.setY(0);
        else if (background.getY() + background.getHeight() < getHeight())
            background.setY(getHeight() - background.getHeight());
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
    }
}
