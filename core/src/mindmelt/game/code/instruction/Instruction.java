package mindmelt.game.code.instruction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.code.Trigger;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.maps.TileType;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
    private String command;
    private List<Integer> args = new ArrayList<>();
    private String text;

    public Instruction(String fullCommand) {
        String[] parts = getParts(fullCommand);
        command = parts[0];
        if(command.equals("Message") || command.equals("Execute") ) {
            text = fullCommand.substring(command.length()+1);
            return;
        }
        if(parts.length==1) return;
        args = getNums(parts[1]);
    }

    private String[] getParts(String command) {
        return command.split(" ");
    }

    private List<Integer> getNums(String nums) {
        List<Integer> args = new ArrayList<>();
        String[] nn = nums.split(",");
        for(String n:nn) {
            args.add(Integer.parseInt(n));
        }
        return args;
    }

    public boolean run(Trigger trigger, Engine engine) {
        if(command.equals("Message")) {
            return doMessage(trigger, engine);
        } else if (command.equals("GotObject")) {
            return gotObject(trigger, engine);
        } else if (command.equals("OpenClose")) {
            return openClose(trigger, engine);
        } else if (command.equals("Open")) {
            return open(trigger, engine);
        } else if (command.equals("Close")) {
            return close(trigger, engine);        }
        return true;
    }

    private boolean doMessage(Trigger trig, Engine engine) {
        engine.addMessage(new Message(trig.getX(),trig.getY(),trig.getZ(),text, Color.MAGENTA, 1000L));
        Gdx.app.log("Message",text);
        return true;
    }

    private boolean gotObject(Trigger trigger, Engine engine) {
        int obj = args.get(0);
        return (engine.getPlayer().isHolding(obj));
    }

    private boolean openClose(Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        if(args.size()==3) {
            x = args.get(0);
            y = args.get(1);
            z = args.get(2);
        }
        openCloseIt(x,y,z,engine);
        return true;
    }

    private boolean open(Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        if(args.size()==3) {
            x = args.get(0);
            y = args.get(1);
            z = args.get(2);
        }
        openIt(x,y,z,engine);
        return true;
    }

    private boolean close(Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        if(args.size()==3) {
            x = args.get(0);
            y = args.get(1);
            z = args.get(2);
        }
        closeIt(x,y,z,engine);
        return true;
    }

    private  void openIt(int x, int y, int z, Engine engine) {
        TileType tile = engine.getTile(x,y,z);
        if (tile==TileType.lockeddoor)
            engine.changeTile(x,y,z, TileType.openlockeddoor);
        if (tile==TileType.door)
            engine.changeTile(x,y,z, TileType.opendoor);
        if (tile==TileType.gate)
            engine.changeTile(x,y,z, TileType.openlockedgate);
    }


    private void closeIt(int x, int y, int z, Engine engine) {
        TileType tile = engine.getTile(x,y,z);
        if (tile==TileType.openlockeddoor)
            engine.changeTile(x,y,z, TileType.lockeddoor);
        if (tile==TileType.opendoor)
            engine.changeTile(x,y,z, TileType.door);
        if (tile==TileType.openlockedgate)
            engine.changeTile(x,y,z, TileType.gate);
    }

    private void openCloseIt(int x, int y, int z, Engine engine) {
        TileType tile = engine.getTile(x,y,z);
        if(tile==TileType.door||tile==TileType.gate||tile==TileType.lockeddoor)
            openIt(x,y,z,engine);
        else if (tile==TileType.opendoor||tile==TileType.openlockeddoor||tile==TileType.openlockedgate)
            closeIt(x,y,z,engine);
    }
}
