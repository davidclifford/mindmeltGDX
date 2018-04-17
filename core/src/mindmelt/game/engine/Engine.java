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
    private float lighting = 0f;
    private List<Trigger> triggerQueue = new ArrayList<>();
    private Messages messages = new Messages();
    private List<SpellButton> activeButtons;
    private TextLines textLines = new TextLines();
    private Talking talking = new Talking();

    public Engine(MindmeltGDX game) {
        changeTiles = new HashMap<>();
        this.game = game;
        this.world = game.world;
        this.objects = game.objects;
    }

    public TextLines getTextLines() {
        return textLines;
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
        fromTo(fx, fy, fz, tx, ty, tz);
    }


    public void moveObjToMap(int ob, int tx, int ty, int tz) {
        Obj object = getObjects().getObject(ob);
        if(object==null) return;
        moveObjToMap(object,tx,ty,tz);
    }

    public void fromTo(int fx, int fy, int fz, int tx, int ty, int tz) {
        TileType fromTile = world.getTile(fx, fy, fz);
        TileType toTile = world.getTile(tx, ty, tz);
        fromTo(fromTile, toTile, fx, fy, fz, tx, ty, tz);
    }

    public void fromTo(TileType fromTile, TileType toTile, int fx, int fy, int fz, int tx, int ty, int tz) {
        //From
        if(fromTile == TileType.presurepad || fromTile==TileType.hiddenpp) {
            if(world.getObjects(fx,fy,fz)==null) //empty
                addTrigger("PadOff",fx,fy,fz);
        }

        //To
        if(toTile == TileType.presurepad || toTile==TileType.hiddenpp) {
            if(world.getObjects(tx,ty,tz)!= null && world.getObjects(tx,ty,tz).size()==1) //first object to land on pad
                addTrigger("PadOn",tx,ty,tz);
        }
        if(toTile == TileType.teleport || toTile == TileType.hiddentele) {
            addTrigger("Teleport",tx,ty,tz);
        }
        if(toTile == TileType.pit || toTile == TileType.hiddenpit) {
            moveAllToMap(tx,ty,tz,tx,ty,tz+1);
        }
        Obj topObj = world.getTopObject(tx,ty,tz);
        if(topObj==null || !topObj.isPlayer())
            return;
        if(toTile == TileType.leftturn)
            getPlayer().rotate(-1);
        if(toTile == TileType.rightturn)
            getPlayer().rotate(1);
        if(toTile == TileType.uturn)
            getPlayer().rotate(2);
   }

    public void moveAllToMap(int xf, int yf, int zf, int xt, int yt, int zt) {
        List<Obj> all = world.removeAllObjects(xf,yf,zf);
        world.addAllObjects(all,xt,yt,zt);
        fromTo(xf,yf,zf, xt,yt,zt);
    }

    public boolean canEnter(Obj ob, int x, int y, int z) {
        return world.canEnter(ob, x,y,z);
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
                objects.initMap(world);
            }
            objects.getPlayer().moveToMap(entry.getToX(),entry.getToY(),entry.getToZ(), this);
            game.world = world;
            game.objects = objects;
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
        Gdx.app.log("addTrigger",String.format("%s: %d,%d,%d",trigger,x,y,z));
        Trigger trigg = new Trigger(trigger,x,y,z);
        triggerQueue.add(trigg);
    }

    public void addTrigger(String trigger) {
        Gdx.app.log("addTrigger",String.format("%s",trigger));
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
        String coord = String.format("%d:%d:%d,%d", x, y, z, world.getId());
        ChangeTile original = changeTiles.get(coord);
        if (original != null) {
            changeTiles.remove(coord);
        }
        if (original == null || original.getIcon() != newTile.getIcon()) {
            changeTiles.put(coord, new ChangeTile(x, y, z, world.getId(), current));
        }
        // CODE
        fromTo(oldTile, newTile, x,y,z, x,y,z);
    }

    public void debugChangeTiles() {
        changeTiles.entrySet().forEach(
                changeTile -> {
                    ChangeTile tile = changeTile.getValue();
                    Gdx.app.log("debugChangeTiles",String.format("%d,%d,%d,%d = %d",tile.getX(),tile.getY(),tile.getZ(),tile.getMap(),tile.getIcon()));
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

    public void addMessage(Message message) {
        getTextLines().addLine(message.getMessage(),message.getColour());
        messages.add(message);
    }

    public void addMessage(String text) {
        ObjPlayer player = getPlayer();
        Message message = new Message(player.getX(),player.getY(),player.getZ(),text, Color.FIREBRICK,1000L);
        addMessage(message);
    }

    public Message getMessage(int x, int y, int z) {
        return messages.getMessage(x,y,z);
    }

    public void expireMessages() {
        messages.expireMessages();
    }

    public void talkTo(int person) {
        //can also include talking pictures

    }

    public boolean isTalking() {
        return talking.isTalking();
    }

    public void setPlayerTalkCoords(int xx, int yy) {
        talking.setPlayerTalkCoords(xx,yy);
    }

    public void setPersonTalkCoords(int xx, int yy) {
        talking.setPersonTalkCoords(xx,yy);
    }
}
