package com.muchpolitik.lejeu.AI;

public class MessageType {
    // for regular enemies
    public final static int DEAD = 0;
    public final static int STUN_FINISHED = 1;
    // for projectile throwers
    public final static int READY_TO_ATTACK = 2;
    public final static int THROW_PROJECTILE = 3;
    // for ghost enemies
    public final static int READY_TO_SHADOW = 4;
    public final static int SHADOW_FINISHED = 5;
    // for blinking obstacles
    public final static int TIME_TO_BLINK = 6;
    public final static int TIME_TO_GO_ON = 7;
    public final static int TIME_TO_GO_OFF = 8;
    // for bosses
    public final static int ATTACK_FINISHED = 9;

}
