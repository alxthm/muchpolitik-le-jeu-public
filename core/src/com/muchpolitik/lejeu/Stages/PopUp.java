package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.muchpolitik.lejeu.LeJeu;

/**
 * A popup displayed on top of current screen.
 */

public class PopUp extends CustomStage {

    public enum Style {
        NotifPopup,
        ImagePopup
    }

    private Style popupStyle;

    private Texture imageTexture;

    /**
     * A flag to make sure the removePopUp() method is only called once.
     */
    private boolean open = false, needToMoveOut = false, movingOut = false;


    /**
     * Create an empty stage for a popup.
     */
    public PopUp(Skin skin) {
        super(new ExtendViewport(LeJeu.minWidth, LeJeu.minHeight, LeJeu.maxWidth, LeJeu.maxHeight));
    }

    /**
     * Create a popup that contains text and disappears automatically after a certain delay.
     */
    public PopUp(Skin skin, String title, String text, String labelStyle) {
        super(new ExtendViewport(LeJeu.minWidth, LeJeu.minHeight, LeJeu.maxWidth, LeJeu.maxHeight));
        popupStyle = Style.NotifPopup;

        // create widgets
        Window popupWindow = new Window(title, skin);
        Label textLabel = new Label(text, skin, labelStyle);

        // set up table
        float w = 800, h = 500;
        popupWindow.setBounds((getWidth() - w) / 2, getHeight() - h - 200, w, h);
        textLabel.setWrap(true);
        popupWindow.add(textLabel).pad(50).grow();

        addActor(popupWindow);
    }

    /**
     * Create a popup that contains an image and disappears when removePopup method is called.
     */
    public PopUp(String imageName) {
        super(new ExtendViewport(LeJeu.minWidth, LeJeu.minHeight, LeJeu.maxWidth, LeJeu.maxHeight));
        popupStyle = Style.ImagePopup;

        // load and create image
        imageTexture = new Texture("graphics/dialogs/" + imageName);
        Image image = new Image(imageTexture);

        // create a table so the image is automatically centered
        Table table = new Table();
        table.setFillParent(true);
        table.add(image).center();

        addActor(table);
    }

    /**
     * Move in (and can make sure the popup moves out automatically).
     */
    public void displayPopUp() {
        // start move in transition
        moveIn();
        open = true;

        if (popupStyle == Style.NotifPopup)
            // move out automatically after a certain duration
            addAction(Actions.delay(2, new Action() {
                @Override
                public boolean act(float delta) {
                    // not too safe to call the method here (may be called more than once)
                    needToMoveOut = true;
                    return true;
                }
            }));
    }

    /**
     * Move out and stop being open once the transition is over.
     */
    public void removePopUp() {
        Action lastAction = new Action() {
            @Override
            public boolean act(float delta) {
                open = false;
                return true;
            }
        };

        moveOut(false, lastAction);
        movingOut = true;
    }

    @Override
    public void act() {
        super.act();

        if (needToMoveOut) {
            removePopUp();
            needToMoveOut = false;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (imageTexture != null)
            imageTexture.dispose();
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isMovingOut() {
        return movingOut;
    }
}
