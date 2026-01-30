package game.scenery.characters.types;

public enum State {
    //Global
    IDLE,


    //Knight
    RUNNING, JUMPING, FALLING,



    //Global Enemies
    TURNING, ANTICIPATION, ATTACK, DEATH,


    //Mobs
    STARTLED,
    //Boss
    JUMP, LAND, JUMP_ATTACK, LAND_DEATH, FALL_DEATH,
}
