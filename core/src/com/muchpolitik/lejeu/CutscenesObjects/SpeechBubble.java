package com.muchpolitik.lejeu.CutscenesObjects;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * A speech bubble displayed on top of characters heads in cut-scenes.
 */
public class SpeechBubble extends Window {

    private int DELAY_BETWEEN_LETTERS = 50, SPEECH_BUBBLE_WIDTH = 800, SPEECH_BUBBLE_HEIGHT = 400;
    private long lastLetterDispTime;
    private String totalText, currentText;
    private boolean isTextFullyDisplayed;

    private Label label;

    public SpeechBubble(boolean isBubble1, Skin skin) {
        super("", skin, "speechBubble");
        setClip(false); // if the text goes out of bounds, it is still displayed outside
        setColor(1, 1, 1, 0); // invisible at the beginning
        isTextFullyDisplayed = true; // so the text doesn't show unless startDispText() is called

        if (isBubble1)
            setBounds(500, 500, SPEECH_BUBBLE_WIDTH, SPEECH_BUBBLE_HEIGHT);
        else
            setBounds(1350, 500, SPEECH_BUBBLE_WIDTH, SPEECH_BUBBLE_HEIGHT);

        // place the logical table containing the label at the top left of the window
        top().left().pad(5, 15, 0, 15);

        label = new Label("", skin, "dialog");
        label.setWrap(true);
        add(label).prefWidth(SPEECH_BUBBLE_WIDTH - 30);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!isTextFullyDisplayed &&
                TimeUtils.timeSinceMillis(lastLetterDispTime) > DELAY_BETWEEN_LETTERS) {
            updateText();
        }
    }

    /**
     * Make the speech bubble visible and start displaying text letter by letter.
     * @param txt the total text to be displayed
     */
    public void startDispText(String txt) {
        addAction(Actions.fadeIn(0.3f));
        totalText = txt;
        currentText = "";
        isTextFullyDisplayed = false;

        currentText += totalText.charAt(0);
        lastLetterDispTime = TimeUtils.millis();
        label.setText(currentText);
    }

    /**
     * Display all the text in the label
     */
    public void finishDispText() {
        currentText = totalText;
        label.setText(currentText);
        isTextFullyDisplayed = true;
    }

    /**
     * Add a letter to the text, or set the bubble to fullyDisplayed
     */
    public void updateText() {
        // check if text is fully displayed
        if (currentText.equals(totalText))
            isTextFullyDisplayed = true;

        else {
            // add text letter by letter
            currentText += totalText.charAt(currentText.length());
            lastLetterDispTime = TimeUtils.millis();
            label.setText(currentText);
        }
    }

    public void hide() {
        addAction(Actions.fadeOut(0.3f));
    }

    public boolean isTextFullyDisplayed() {
        return isTextFullyDisplayed;
    }
}
