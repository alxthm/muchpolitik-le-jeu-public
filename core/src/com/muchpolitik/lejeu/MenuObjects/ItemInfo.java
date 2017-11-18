package com.muchpolitik.lejeu.MenuObjects;

/**
 * An object storing info in JSON for shop items (costumes, powers, etc) and level buttons in LevelMap screen.
 */
public class ItemInfo {
    String pictureName, itemName, titleText, text, precedingCutscene;
    int price;

    public String getPictureName() {
        return pictureName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getText() {
        return text;
    }

    public String getPrecedingCutscene() {
        return precedingCutscene;
    }

    public int getPrice() {
        return price;
    }
}
