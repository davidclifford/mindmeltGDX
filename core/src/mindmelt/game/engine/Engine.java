package mindmelt.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.buttons.SpellButton;
import mindmelt.game.code.Trigger;
import mindmelt.game.maps.EntryExit;
import mindmelt.game.maps.TileType;
import mindmelt.game.maps.World;
import mindmelt.game.objects.Inventory;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;
import mindmelt.game.objects.ObjectStore;
import mindmelt.game.talk.Talking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine {
    private MindmeltGDX game;
    private World world;
    private ObjectStore objects;
    private Map<String,ChangeTile> changeTiles;
    private boolean xray = false;
    private boolean light = false;
    private boolean seeall = false;
    private boolean cheat = false;
    private boolean debugView = false;
    private float lighting = 0f;
    private String toolTip = "";
    private List<Trigger> triggerQueue = new ArrayList<>();
    private Messages messages = new Messages();
    private List<SpellButton> activeButtons;
    private Journal journal = new Journal();
    private Talking talking = new Talking();

    public Engine(MindmeltGDX game) {
        changeTiles = new HashMap<>();
        this.game = game;
        this.world = game.world;
        this.objects = game.objects;
    }

    public boolean isDebugView() {
        return debugView;
    }

    public void setDebugView(boolean debugView) {
        this.debugView = debugView;
    }

    public Journal getJournal() {
        return journal;
    }

    public boolean isXray() {
        return xray;
    }

    public void setXray(boolean xray) {
        this.xray = xray;
    }

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

    public boolean isSeeall() {
        return seeall;
    }

    public void setSeeall(boolean seeall) {
        this.seeall = seeall;
    }

    public boolean isCheat() {
        return cheat;
    }

    public void setCheat(boolean cheat) {
        this.cheat = cheat;
    }

    public float getLighting() {
        return lighting;
    }

    public void setLighting(float lighting) {
        this.lighting = lighting;
    }

    public World getWorld() {
        return world;
    }
    
    public void setWorld(World world) {
        this.world = world;
    }

    public ObjectStore getObjects() {
        return objects;
    }

    public void setButtons(List<SpellButton> activeButtons) {
        this.activeButtons = activeButtons;
    }

    public void moveObjToMap(Obj object, int tx, int ty, int tz) {
        int fx = object.getX();
        int fy = object.getY();
        int fz = object.getZ();
        object.moveToMap(tx, ty, tz, this);
        fromTo(object, fx, fy, fz, tx, ty, tz);
    }


    public void moveObjToMap(int ob, int tx, int ty, int tz) {
        Obj object = getObjects().getObject(ob);
        if(object==null) return;
        moveObjToMap(object,tx,ty,tz);
    }

    public void fromTo(Obj ob, int fx, int fy, int fz, int tx, int ty, int tz) {
        TileType fromTile = world.getTile(fx, fy, fz);
        TileType toTile = world.getTile(tx, ty, tz);
        fromTo(ob, fromTile, toTile, fx, fy, fz, tx, ty, tz);
    }

    public void fromTo(Obj ob, TileType fromTile, TileType toTile, int fx, int fy, int fz, int tx, int ty, int tz) {
        //From
        if((ob == null || !ob.inAir()) && (fromTile == TileType.presurepad || fromTile==TileType.hiddenpp)) {
            if(world.getObjects(fx,fy,fz)==null) //empty
                addTrigger("PadOff",fx,fy,fz);
        }

        //To
        if((ob == null || !ob.inAir()) && (toTile == TileType.presurepad || toTile==TileType.hiddenpp)) {
            if(world.getObjects(tx,ty,tz)!= null && world.getObjects(tx,ty,tz).size()==1) //first object to land on pad
                addTrigger("PadOn",tx,ty,tz);
        }
        if(toTile == TileType.teleport || toTile == TileType.hiddentele) {
            addTrigger("Teleport",tx,ty,tz);
        }
        if((ob == null || !ob.inAir()) && (toTile == TileType.pit || toTile == TileType.hiddenpit)) {
            moveAllToMap(tx,ty,tz,tx,ty,tz+1);
        }

        if(ob == null || !ob.isPlayer())
            return;

        //Player only
        if(toTile == TileType.leftturn)
            ob.rotate(-1);
        if(toTile == TileType.rightturn)
            ob.rotate(1);
        if(toTile == TileType.uturn)
            ob.rotate(2);
   }

    public void moveAllToMap(int xf, int yf, int zf, int xt, int yt, int zt) {
        List<Obj> all = world.removeAllObjects(xf,yf,zf);
        world.addAllObjects(all,xt,yt,zt);
        fromTo(world.getTopObject(xf,yf,zf),xf,yf,zf, xt,yt,zt);
    }

    public boolean canEnter(Obj ob, int x, int y, int z) {
        return world.canEnter(ob, x,y,z);
    }

    public boolean canSmell(int x, int y, int z) {
        return world.canSmell(x, y, z);
    }
    
    public boolean isAnEntryExit(int x, int y, int z) {
        return world.isEntryExit(x, y, z);
    }    
    
    public void doEntryExit(int x, int y, int z) {
        EntryExit entry = getEntryExit(x, y, z);
        moveToMap(entry);
    }

    public void moveToMap(EntryExit entry) {
        if (entry!=null) {
            if(!entry.getToMap().equals(world.getFilename())) {
                world = new World();
                world.loadMap(entry.getToMap());
                overlay();
                objects.initMap(world);
            }
            objects.getPlayer().moveToMap(entry.getToX(),entry.getToY(),entry.getToZ(), this);
            game.world = world;
            game.objects = objects;
        }
    }

    public void overlay() {
        for(ChangeTile tile : changeTiles.values()) {
            if(tile.getMap()==world.getId()) {
                world.setIcon(tile.getX(),tile.getY(),tile.getZ(),tile.getChange());
            }
        }
    }

    public long getSystemTime() {
        return TimeUtils.nanoTime();
    }

    public TileType getTile(int x, int y, int z) {
        return world.getTile(x,y,z);
    }
    public EntryExit getEntryExit(int x, int y, int z) {
        return world.getEntryExit(x, y, z);
    }

    public ObjPlayer getPlayer() {
        return (ObjPlayer) objects.getPlayer();
    }

    //Player stuff
    public void setPlayerWait() {
        getPlayer().setWait(getPlayer().getSpeed());
    }

    public Inventory getPlayerInventory() {
        return objects.getPlayer().inventory;
    }

    public Obj getPlayerHandObject() {
        return objects.getPlayer().inventory.getHandObject();
    }

    public int getPlayerX() { return objects.getPlayer().getX(); }
    public int getPlayerY() { return objects.getPlayer().getY(); }
    public int getPlayerZ() { return objects.getPlayer().getZ(); }
    public int getPlayerDirection() { return objects.getPlayer().getDirection(); }
    public boolean isPlayerReady(Engine engine) { return objects.getPlayer().isReady(engine); }

    public void activateTile(int x, int y, int z) {
        Obj player = getPlayer();
        TileType tile = world.getTile(x, y, z);
        if(tile==TileType.space) {
            return;
        }
        // Doors
        if (tile==TileType.door) {
            changeTile(x,y,z,TileType.opendoor);
        } else if(tile==TileType.opendoor) {
            changeTile(x,y,z,TileType.door);         
        } else if(tile==TileType.uplever) {
            changeTile(x,y,z,TileType.downlever);
            addTrigger("LeverOn",x,y,z);
        } else if(tile==TileType.downlever) {
            changeTile(x,y,z,TileType.uplever);
            addTrigger("LeverOff",x,y,z);
        } else {
            addTrigger("Activate",x,y,z);
        }
        //Entry/Exit
        if(isAnEntryExit(x, y, z)) {
            if(player.isAt(x,y,z)) {
                doEntryExit(x, y, z);
            } else {
                EntryExit entry = getEntryExit(x, y, z);
                player.setMessage(this, entry.getDescription());
            }
        }
        
    }

    public void addTrigger(String trigger, int x, int y, int z) {
        Trigger trigg = new Trigger(trigger,x,y,z);
        triggerQueue.add(trigg);
    }

    public void addTrigger(String trigger) {
        Trigger trigg = new Trigger(trigger);
        triggerQueue.add(trigg);
    }

    public void runTriggers() {
        while (!triggerQueue.isEmpty()) {
            Trigger trigger = triggerQueue.get(0);
            world.getCodeStore().runTriggerCode(trigger, this);
            triggerQueue.remove(trigger);
        }
    }

    public void changeTile(int x, int y, int z, TileType newTile) {
        TileType oldTile = world.getTile(x,y,z);
        int current = oldTile.getId();
        world.changeTile(x, y, z, newTile);
        String coord = String.format("%d:%d:%d:%d", x, y, z, world.getId());
        ChangeTile original = changeTiles.get(coord);
        if (original != null) {
            changeTiles.remove(coord);
        }
        if (original == null || original.getId() != newTile.getId()) {
            changeTiles.put(coord, new ChangeTile(x, y, z, world.getId(), current, newTile.getId()));
        }
        // CODE
        fromTo(world.getTopObject(x,y,z), oldTile, newTile, x,y,z, x,y,z);
    }

    public void debugChangeTiles() {
        changeTiles.entrySet().forEach(
                changeTile -> {
                    ChangeTile tile = changeTile.getValue();
                    Gdx.app.log("debugChangeTiles",String.format("%d,%d,%d,%d = %d",tile.getX(),tile.getY(),tile.getZ(),tile.getMap(),tile.getId()));
                }
        );

    }

    public Obj getTopObject(int x, int y, int z) {
        List<Obj> objs = world.getObjects(x, y, z);
        if (objs==null) return null;
        if (objs.isEmpty()) return null;
        Obj ob = objs.get(objs.size()-1);
        if (ob.isPlayer() && objs.size()>1) {
            ob = objs.get(objs.size()-2);
        }
        return ob;
    }

    public boolean isObjectAt(int x, int y, int z, int id) {
        return objects.isObjectAt(x,y,z,id);
    }

    public void moveObjToObj(Obj from, Obj to) {
        from.moveToObject(to, world);
    }

    public String addToJournal(String text) {
        String parts[] = text.split(":");
        if(parts.length<2) return text;
        getJournal().addLine(parts[1],Color.YELLOW);
        return parts[0];
    }

    public void addMessage(Message message) {
        if(message.getMessage().contains(":")) {
            String pretext = addToJournal(message.getMessage());
            message.updateMessage(pretext);
        }
        messages.add(message);
    }

    public void addMessage(String text) {
        ObjPlayer player = getPlayer();
        Message message = new Message(player.getX(),player.getY(),player.getZ(),text, Color.MAGENTA,1000L);
        addMessage(message);
    }

    public Message getMessage(int x, int y, int z) {
        return messages.getMessage(x,y,z);
    }

    public void expireMessages() {
        messages.expireMessages();
    }

    public Talking getTalking() {
        return talking;
    }

    public void talkAdjacent() {
        int px = getPlayerX();
        int py = getPlayerY();
        int pz = getPlayerZ();
        for(int y=py-1;y<=py+1;y++) {
            for(int x=px-1;x<=px+1;x++) {
                Obj topObj = getTopObject(x, y, pz);
                if (topObj != null && topObj.isPerson()){
                    getWorld().talkTo(this, topObj.getId(), x, y, pz);
                    return;
                }
            }
        }
    }

    public void saveChangeTiles(String name) {
        String filename = "data/" + name + ".ovr";
        StringBuilder line = null;
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(filename));

            for(ChangeTile ct : changeTiles.values()) {
                line = new StringBuilder();
                line.append(ct.getX() + ",");
                line.append(ct.getY() + ",");
                line.append(ct.getZ() + ",");
                line.append(ct.getMap() + ",");
                line.append(ct.getId() + ",");
                line.append(ct.getChange() + "\n");
                output.write(line.toString());
            }
            output.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void loadChangeTiles(String name) {
        String filename = "data/"+name+".ovr";
        String line;
        BufferedReader input = null;

        try {
            input = new BufferedReader((new FileReader(filename)));

            while ((line = input.readLine()) != null) {
                if (line.length() == 0) continue;
                if (line.startsWith("//")) continue;

                String p[] = line.split(",");
                int x = Integer.parseInt(p[0]);
                int y = Integer.parseInt(p[1]);
                int z = Integer.parseInt(p[2]);
                int map = Integer.parseInt(p[3]);
                int original = Integer.parseInt(p[4]);
                int id = Integer.parseInt(p[5]);
                String coord = String.format("%d:%d:%d:%d", x, y, z, map);
                changeTiles.put(coord, new ChangeTile(x, y, z, map, original, id));
            }
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

}
