package mindmelt.game.talk;

import mindmelt.game.objects.Obj;

public class Talking {
    private boolean talking = false;
    private int playerX;
    private int playerY;
    private int pX;
    private int pY;
    private int pZ;
    private int otherX;
    private int otherY;
    private int oX;
    private int oY;
    private int oZ;
    private String playerTalk = "";
    private String otherTalk = "";

    public boolean isTalking() {
        return talking;
    }

    public void setTalking(boolean talking) {
        this.talking = talking;
    }

    public void setPlayerScreenCoords(int x, int y) {
        playerX = x;
        playerY = y;
    }

    public void setOtherScreenCoords(int x, int y) {
        otherX = x;
        otherY = y;
    }

    public int getPlayerScreenX() {
        return playerX;
    }

    public int getPlayerScreenY() {
        return playerY;
    }

    public int getOtherScreenX() {
        return otherX;
    }

    public int getOtherScreenY() {
        return otherY;
    }

    public void setPlayerCoords(int x, int y, int z) {
        pX = x;
        pY = y;
        pZ = z;
    }

    public void setOtherCoords(int x, int y, int z) {
        oX = x;
        oY = y;
        oZ = z;
    }

    public boolean isPlayerAt(int x, int y, int z) {
        return (pX == x && pY == y && pZ == z);
    }

    public boolean isOtherAt(int x, int y, int z) {
        return (oX == x && oY == y && oZ == z);
    }

    public String getPlayerTalk() {
        return playerTalk;
    }

    public void setPlayerTalk(String playerTalk) {
        this.playerTalk = playerTalk;
    }

    public String getOtherTalk() {
        return otherTalk;
    }

    public void setOtherTalk(String otherTalk) {
        this.otherTalk = otherTalk;
    }
}
