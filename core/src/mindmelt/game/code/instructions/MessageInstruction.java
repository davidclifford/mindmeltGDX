package mindmelt.game.code.instructions;

import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;

public class MessageInstruction extends Instruction {
    public MessageInstruction(int x, int y, int z, String text) {
        this.trigX = x;
        this.trigY = y;
        this.trigZ = z;
        this.text = text;
    }

    public boolean run(Engine engine) {
        engine.addMessage(new Message(trigX,trigY,trigZ, text, Color.MAGENTA, 1000L));
        return true;
    }
}
