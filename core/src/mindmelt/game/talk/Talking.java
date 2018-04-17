package mindmelt.game.talk;

import mindmelt.game.objects.Obj;

public class Talking {
    private boolean talking = false;
    private int playerX;
    private int playerY;
    private int personX;
    private int personY;
    private boolean playerTalking;
    private int personTalking;


    public boolean isTalking() {
        return talking;
    }

    public void setTalking(boolean talking) {
        this.talking = talking;
    }

    public void setPlayerTalkCoords(int x, int y) {
        playerX = x;
        playerY = y;
    }

    public void setPersonTalkCoords(int x, int y) {
        playerX = x;
        playerY = y;
    }

}
