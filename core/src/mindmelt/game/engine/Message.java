package mindmelt.game.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

public class Message {
    private int x;
    private int y;
    private int z;
    private String message;
    private Color colour;
    private long expiry;

    public Message(int x, int y, int z, String message, Color colour, long expiry) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.message = message;
        this.colour = colour;
        this.expiry = TimeUtils.nanoTime()/1000000L + expiry;
    }

    public boolean isAt(int x, int y, int z) {
        return (this.x==x && this.y==y && this.z==z);
    }

    public boolean isTimeToExpire(long timeNow) {
        return (timeNow>=expiry);
    }

    public String getMessage() {
        return message;
    }
}
