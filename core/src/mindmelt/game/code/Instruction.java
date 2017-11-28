package mindmelt.game.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;

public class Instruction {
    private String command;

    public Instruction(String command) {
        this.command = command;
    }

    public boolean run(Trigger trigger, Engine engine) {
        String[] parts = getParts(command);
        if(command.startsWith("Message ")) {
            return doMessage(command,trigger,engine);
        } else {
            return true;
        }
    }

    private boolean doMessage(String command, Trigger trig, Engine engine) {
        String message = command.substring("Message ".length());
        engine.addMessage(new Message(trig.getX(),trig.getY(),trig.getZ(),message, Color.MAGENTA, 1000L));
        Gdx.app.log("Message",message);
        return true;
    }
    private String[] getParts(String command) {
        return command.split(" ");
    }

    private int[] getNums(String nums) {
        String[] n = nums.split(",");
        int[] in = new int[n.length];
        int i = 0;
        for(String nn:n) {
            in[i] = Integer.parseInt(n[i++]);
        }
        return in;
    }
}
