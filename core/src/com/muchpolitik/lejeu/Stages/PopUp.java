package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * A popup displayed on top of current screen.
 */

public class PopUp extends CustomStage {

    /**
     * A flag to make sure the removePopUp() method is only called once.
     */
    private boolean open = false, needToMoveOut = false;

    public PopUp(Skin skin, String title, String text, String labelStyle) {
        super(new FitViewport(2560, 1440));

        // create widgets
        Window popupWindow = new Window(title, skin);
        Label textLabel = new Label(text, skin, labelStyle);

        // set up table
        float w = 800, h = 500;
        popupWindow.setBounds((getWidth()-w)/2, getHeight()-h-200, w, h);
        textLabel.setWrap(true);
        popupWindow.add(textLabel).pad(50).grow();

        addActor(popupWindow);
    }

    /**
     * Move in and make sure the popup moves out automatically after a certain delay.
     */
    public void displayPopUp() {
        // start move in transition
        moveIn();
        open = true;

        // move out after a certain duration
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
     * Move out and add stop being open once the transition is over.
     */
    private void removePopUp() {
        Action lastAction = new Action() {
            @Override
            public boolean act(float delta) {
                open = false;
                return true;
            }
        };

        moveOut(false, lastAction);
    }

    @Override
    public void act() {
        super.act();

        Gdx.app.debug("popup test", "open : " + open);

        if (needToMoveOut) {
            removePopUp();
            needToMoveOut = false;
        }
    }

    public boolean isOpen() {
        return open;
    }
}
