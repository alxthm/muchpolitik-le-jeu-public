package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A regular stage with transition methods added. Useful for menu windows.
 */

public class CustomStage extends Stage {

    private float transitionDuration = 0.3f;

    public CustomStage(Viewport viewport) {
        super(viewport);
    }

    /**
     * Make the stage enter the screen by sliding in from the left.
     *
     * @param fadeIn
     */
    public void moveIn(boolean fadeIn) {
        if (fadeIn) {
            addAction(Actions.sequence(
                    Actions.alpha(0),
                    Actions.moveTo(-getWidth(), 0),
                    Actions.parallel(
                            Actions.fadeIn(transitionDuration, Interpolation.circle),
                            Actions.moveTo(0, 0, transitionDuration, Interpolation.circle))));
        } else {
            addAction(Actions.sequence(
                    Actions.moveTo(-getWidth(), 0),
                    Actions.moveTo(0, 0, transitionDuration, Interpolation.circle)));
        }
    }

    /**
     * Make the stage leave the screen by sliding out to the right,
     * and execute a last action afterwards.
     *
     * @param fadeOut
     * @param lastAction An action to be executed once the transition is over (may be null)
     */
    public void moveOut(boolean fadeOut, Action lastAction) {
        Action moveOutAction;
        if (fadeOut) {
            moveOutAction = Actions.parallel(
                    Actions.fadeOut(transitionDuration, Interpolation.circle),
                    Actions.moveBy(getWidth(), 0, transitionDuration, Interpolation.circle));
        } else {
            moveOutAction = Actions.moveBy(getWidth(), 0, transitionDuration, Interpolation.circle);
        }
        Action actionSequence = (lastAction == null) ?
                moveOutAction :
                Actions.sequence(moveOutAction, lastAction);
        addAction(actionSequence);
    }

    /**
     * Make the stage enter the screen by sliding in from the left.
     */
    public void moveIn() {
        moveIn(false);
    }

}
