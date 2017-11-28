package mindmelt.game.engine;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class Messages {
    private List<Message> messages = new ArrayList<>();

    public void add(Message message) {
        messages.add(message);
    }

    public Message getMessage(int x, int y, int z) {
        for(Message message:messages) {
            if (message.isAt(x,y,z))
                return message;
        }
        return null;
    }

    public void expireMessages() {
        long timeNow = TimeUtils.nanoTime()/1000000L;
        messages.removeIf(message ->  message.isTimeToExpire(timeNow));
    }
}
