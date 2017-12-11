package mindmelt.game.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.maps.TileType;

import java.util.ArrayList;
import java.util.List;

public class InstructionX {
    private String command;
    private List<Integer> args = new ArrayList<>();
    private String text;

    public InstructionX(String fullCommand) {
        String[] parts = getParts(fullCommand);
        command = parts[0];
        text = "";
        if (parts.length>1) text = fullCommand.substring(command.length()+1);
        if(command.equals("Message") || command.equals("Execute") ) {
//            text = fullCommand.substring(command.length()+1);
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
        Gdx.app.log("Command",String.format("%s %s",command,text));
        if(command.equals("Message")) {
            return doMessage(trigger, engine);
        } else if(command.equals("Execute")) {
            return executeRoutine(trigger, engine);
        } else if (command.equals("GotObject")) {
            return gotObject(trigger, engine);
        } else if (command.equals("OpenClose")) {
            return openClose(trigger, engine);
        } else if (command.equals("Open")) {
            return open(trigger, engine);
        } else if (command.equals("Close")) {
            return close(trigger, engine);
        } else if (command.equals("ChangeSquare")) {
            return changeSquare(trigger, engine);
        } else if (command.equals("MoveAll")) {
            return moveAll(trigger, engine);
        } else if (command.equals("IsType")) {
            return isType(trigger, engine);
        } else if (command.equals("ObjectAt")) {
            return objectAt(trigger, engine);        }
        return true;
    }

    private boolean moveAll(Trigger trigger, Engine engine) {
        int xf = trigger.getX();
        int yf = trigger.getY();
        int zf = trigger.getZ();
        int xt=0;
        int yt=0;
        int zt=0;
        if(args.size()==3) {
            xt = args.get(0);
            yt = args.get(1);
            zt = args.get(2);
        } else if(args.size()==6) {
            xf = args.get(0);
            yf = args.get(1);
            zf = args.get(2);
            xt = args.get(3);
            yt = args.get(4);
            zt = args.get(5);
        }
        engine.moveAllToMap(xf,yf,zf,xt,yt,zt);
        return true;
    }

    private boolean executeRoutine(Trigger trigger, Engine engine) {
        engine.addTrigger(String.format("Routine %s",text));
        return true;
    }

    private boolean isType(Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        int id = args.get(0);
        if(args.size()==4) {
            x = args.get(0);
            y = args.get(1);
            z = args.get(2);
            id = args.get(3);
        }
        return engine.getTile(x,y,z).getId()==id;
    }

    private boolean objectAt(Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        int id = args.get(0);
        if(args.size()==4) {
            x = args.get(0);
            y = args.get(1);
            z = args.get(2);
            id = args.get(3);
        }
        return engine.isObjectAt(x,y,z,id);
    }

    private boolean changeSquare(Trigger trigger, Engine engine) {
        int x = trigger.getX();
        int y = trigger.getY();
        int z = trigger.getZ();
        int id = args.get(0);
        if(args.size()==4) {
            x = args.get(0);
            y = args.get(1);
            z = args.get(2);
            id = args.get(3);
        }
        engine.changeTile(x,y,z,TileType.getTileType(id));
        return true;
    }

    private boolean doMessage(Trigger trig, Engine engine) {
        engine.addMessage(new Message(trig.getX(),trig.getY(),trig.getZ(),text, Color.MAGENTA, 1000L));
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
