package mindmelt.game.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.maps.TileType;

public class Instruction {
    private String command;

    public Instruction(String command) {
        this.command = command;
    }

    //GotObject 112
    //OpenClose

    public boolean run(Trigger trigger, Engine engine) {
        String[] parts = getParts(command);
        if(command.startsWith("Message")) {
            return doMessage(command, trigger, engine);
        } else if (command.startsWith("GotObject")) {
            return gotObject(command, trigger, engine);
        } else if (command.startsWith("OpenClose")) {
            return openClose(command, trigger, engine);
        } else if (command.startsWith("Open")) {
            return open(command, trigger, engine);
        } else if (command.startsWith("Close")) {
            return close(command, trigger, engine);        }
        return true;
    }

    private boolean doMessage(String command, Trigger trig, Engine engine) {
        String message = command.substring("Message ".length());
        engine.addMessage(new Message(trig.getX(),trig.getY(),trig.getZ(),message, Color.MAGENTA, 1000L));
        Gdx.app.log("Message",message);
        return true;
    }

    private boolean gotObject(String command, Trigger trigger, Engine engine) {
        String [] parts = getParts(command);
        if (parts.length<2) return false;
        int obj = Integer.parseInt(parts[1]);
        return (engine.getPlayer().isHolding(obj));
    }

    private boolean openClose(String command, Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        TileType tile = engine.getTile(x,y,z);
        if (tile == TileType.lockeddoor)
            engine.changeTile(x,y,z, TileType.openlockeddoor);
        else if (tile == TileType.openlockeddoor)
            engine.changeTile(x,y,z, TileType.lockeddoor);
        return true;
    }

    private boolean open(String command, Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        TileType tile = engine.getTile(x,y,z);
        engine.changeTile(x,y,z, TileType.openlockeddoor);
        return true;
    }

    private boolean close(String command, Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        TileType tile = engine.getTile(x,y,z);
        engine.changeTile(x,y,z, TileType.lockeddoor);
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
