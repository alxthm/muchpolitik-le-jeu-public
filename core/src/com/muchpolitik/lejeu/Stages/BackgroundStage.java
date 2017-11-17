package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class BackgroundStage extends Stage {

    private Texture backgroundTexture;
    private Image background;
    private float rangeX, rangeY;

    public BackgroundStage(Texture backgroundTexture) {
        super(new FitViewport(2560, 1440));
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
        background.moveBy(deltaX * rangeX, deltaY * rangeY);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
    }
}
