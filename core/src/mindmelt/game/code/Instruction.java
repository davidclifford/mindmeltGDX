package mindmelt.game.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.maps.TileType;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instruction {
    private static final String[] commands = {"Message","Execute","GotObject","OpenClose","Open","Close","ChangeSquare","MoveAll","IsType","ObjectAt"};
    private static final int[] commandArgs = {1,1,1,3,3,3,4,6,4,4};
    private String command;
    private int commandCode;
    private List<Integer> iargs = new ArrayList<>();
    private String text;
    private Trigger trigger;

    public Instruction(Trigger trigger, String fullCommand) {
        this.trigger = trigger;
        String[] parts = getParts(fullCommand);
        command = parts[0];
        text = "";
        if (parts.length>1) text = fullCommand.substring(command.length()+1);
        if(command.equals("Message") || command.equals("Execute") ) {
            return;
        }
        if(parts.length==1)
            iargs = new ArrayList<>();
        else
            iargs = getNums(parts[1]);
        commandCode = getCommandCode(command);
        if(commandCode<0) return;
        if(iargs.size()<commandArgs[commandCode]) {
            List<Integer> trigs = new ArrayList<>();
            trigs.add(trigger.getX());
            trigs.add(trigger.getY());
            trigs.add(trigger.getZ());
            trigs.addAll(iargs);
            iargs = trigs;
        }
        Gdx.app.log("Instruction code",""+commandCode);
        Gdx.app.log("Args", StringUtils.join(iargs, ","));
    }

    private int getCommandCode(String command) {
        List<String> coms = Arrays.asList(commands);
        return coms.indexOf(command);
    }
    private String[] getParts(String command) {
        return command.split(" ");
    }

    private List<Integer> getNums(String nums) {
        List<Integer> args = new ArrayList<>();
        String[] nn = nums.split(",");
        for(String n:nn) {
            if(n.matches("\\d+"))
                args.add(Integer.parseInt(n));
            else
                args.add(0);
        }
        return args;
    }

    public boolean run(Engine engine) {
        Gdx.app.log("Command",String.format("%s %s",command,text));
        if(command.equals("Message")) {
            return doMessage(trigger, engine);
        } else if(command.equals("Say")) {
            return say(trigger, engine);
        } else if(command.equals("Execute")) {
            return executeRoutine(trigger, engine);
        } else if (command.equals("GotObject")) {
            return gotObject(trigger, engine);
        } else if (command.equals("HasObject")) {
            return hasObject(trigger, engine);
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
            return objectAt(trigger, engine);
        } else if (command.equals("ObjectIn")) {
            return objectIn(trigger, engine);
        } else if (command.equals("GetObject")) {
            return getObject(trigger, engine);
        } else if (command.equals("MoveObject")) {
            return moveObject(trigger, engine);
        } else if (command.equals("MovePlayer")) {
            return movePlayer(trigger, engine);
        } else if (command.equals("AtPosition")) {
            return atPosition(trigger, engine);
        } else if (command.equals("MoveObjectInto")) {
            return moveObjectInto(trigger, engine);
        } else if (command.equals("TalkTo")) {
            return talkTo(trigger, engine);
        }
        return true;
    }

    private boolean talkTo(Trigger trigger, Engine engine) {
        int who = iargs.get(0);
        int which = iargs.get(1);
        engine.getPlayer().startTalking(engine, who, trigger.getX(), trigger.getY(), trigger.getZ());
        return true;
    }

    private boolean moveAll(Trigger trigger, Engine engine) {
        int xf = iargs.get(0);
        int yf = iargs.get(1);
        int zf = iargs.get(2);
        int xt = iargs.get(3);
        int yt = iargs.get(4);
        int zt = iargs.get(5);
        engine.moveAllToMap(xf,yf,zf,xt,yt,zt);
        return true;
    }

    private boolean moveObject(Trigger trigger, Engine engine) {
        int tx = iargs.get(0);
        int ty = iargs.get(1);
        int tz = iargs.get(2);
        int ob = iargs.get(3);
        engine.moveObjToMap(ob,tx,ty,tz);
        return true;
    }

    private boolean getObject(Trigger trigger, Engine engine) {
        Obj ob = engine.getObjects().getObject(iargs.get(0));
        ob.moveToObject(engine.getPlayer(), engine.getWorld());
        if(ob.getId()>=100 && ob.getId()<=105) { //scrolls
            ObjPlayer player = engine.getPlayer();
            player.setSpells(player.getLevel(), engine);
        }
        return true;
    }

    private boolean moveObjectInto(Trigger trigger, Engine engine) {
        engine.getObjects().getObject(iargs.get(0)).moveToObject(engine.getObjects().getObject(iargs.get(1)),engine.getWorld());
        return true;
    }

    private boolean movePlayer(Trigger trigger, Engine engine) {
        int tx = iargs.get(0);
        int ty = iargs.get(1);
        int tz = iargs.get(2);
        engine.moveObjToMap(engine.getPlayer(),tx,ty,tz);
        return true;
    }

    private boolean atPosition(Trigger trigger, Engine engine) {
        int tx = iargs.get(0);
        int ty = iargs.get(1);
        int tz = iargs.get(2);
        return engine.getPlayer().isAt(tx,ty,tz);
    }

    private boolean executeRoutine(Trigger trigger, Engine engine) {
        engine.addTrigger(String.format("Routine %s",text));
        return true;
    }

    private boolean isType(Trigger trigger, Engine engine) {
        int x = iargs.get(0);
        int y = iargs.get(1);
        int z = iargs.get(2);
        int id = iargs.get(3);
        return engine.getTile(x,y,z).getId()==id;
    }

    private boolean objectAt(Trigger trigger, Engine engine) {
        int x = iargs.get(0);
        int y = iargs.get(1);
        int z = iargs.get(2);
        int id = iargs.get(3);
        return engine.isObjectAt(x,y,z,id);
    }

    private boolean changeSquare(Trigger trigger, Engine engine) {
        int x = iargs.get(0);
        int y = iargs.get(1);
        int z = iargs.get(2);
        int id = iargs.get(3);
        engine.changeTile(x,y,z,TileType.getTileType(id));
        return true;
    }

    private boolean doMessage(Trigger trig, Engine engine) {
        engine.addMessage(new Message(trig.getX(),trig.getY(),trig.getZ(),text, Color.CYAN, 1000L));
        return true;
    }

    private boolean say(Trigger trig, Engine engine) {
        Obj person = engine.getObjects().getObject(trig.getX());
        int x = person.getX();
        int y = person.getY();
        int z = person.getZ();
        engine.addMessage(new Message(x,y,z, text, Color.CYAN, 1000L));
        return true;
    }

    private boolean hasObject(Trigger trigger, Engine engine) {
        int obj = iargs.get(0);
        return (engine.getPlayer().isHolding(obj) || engine.getObjects().isObjectIn(obj,engine.getPlayer()));
    }

    private boolean gotObject(Trigger trigger, Engine engine) {
        int obj = iargs.get(0);
        return (engine.getPlayer().isHolding(obj));
    }

    private boolean openClose(Trigger trigger, Engine engine) {
        int x = iargs.get(0);
        int y = iargs.get(1);
        int z = iargs.get(2);
        openCloseIt(x,y,z,engine);
        return true;
    }

    private boolean objectIn(Trigger trigger, Engine engine) {
        return (engine.getObjects().getObject(iargs.get(0)).isInside(iargs.get(1)));
    }

    private boolean open(Trigger trigger, Engine engine) {
        int x = iargs.get(0);
        int y = iargs.get(1);
        int z = iargs.get(2);
        openIt(x,y,z,engine);
        return true;
    }

    private boolean close(Trigger trigger, Engine engine) {
        int x = iargs.get(0);
        int y = iargs.get(1);
        int z = iargs.get(2);
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
