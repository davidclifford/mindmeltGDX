package mindmelt.game.talk;

import mindmelt.game.engine.Engine;
import mindmelt.game.objects.Obj;

public class Talking {
    private boolean talking = false;
    private long timeout = 0L;

    private int playerX;
    private int playerY;
    private int pX;
    private int pY;
    private int pZ;
    private String playerTalk = "";

    private int other;
    private int otherX;
    private int otherY;
    private int oX;
    private int oY;
    private int oZ;
    private String otherTalk = "";

    public boolean isTalking() {
        return talking;
    }
    public boolean isReplying(Engine engine) {
        return talking || (engine.getSystemTime() < timeout);
    }

    public void setTimeout(Engine engine, long timeout) {
        this.timeout = engine.getSystemTime() + timeout*1000000L;
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

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public int getOtherX() {
        return oX;
    }

    public int getOtherY() {
        return oY;
    }

    public int getOtherZ() {
        return oZ;
    }
}
