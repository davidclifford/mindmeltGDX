package mindmelt.game.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.code.Trigger;
import mindmelt.game.maps.EntryExit;
import mindmelt.game.maps.TileType;
import mindmelt.game.maps.World;
import mindmelt.game.objects.Inventory;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjectStore;

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

    public Engine(MindmeltGDX game) {
        changeTiles = new HashMap<>();
        this.game = game;
        this.world = game.world;
        this.objects = game.objects;
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

    public void moveObjToMap(Obj object, int x, int y, int z) {
        object.moveToMap(x, y, z, this);
        //CODE!!!
        // do it here (need from & to)
    }

    public void moveAllToMap(int xf, int yf, int zf, int xt, int yt, int zt) {
        List<Obj> all = world.removeAllObjects(xf,yf,zf);
        world.addAllObjects(all,xt,yt,zt);
    }

    public boolean canEnter(Obj ob, int x, int y, int z) {
        return world.canEnter(ob, x,y,z);
    }
    
    public boolean isAnEntryExit(int x, int y, int z) {
        return world.isEntryExit(x, y, z);
    }    
    
    public void doEntryExit(int x, int y, int z) {
        EntryExit entry = getEntryExit(x, y, z);
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
    
    public EntryExit getEntryExit(int x, int y, int z) {
        return world.getEntryExit(x, y, z);
    }

    public Obj getPlayer() {
        return objects.getPlayer();
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
    public boolean isPlayerReady(float delta) { return objects.getPlayer().isReady(delta); }

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
//        } else if (tile==TileType.lockeddoor) {
//            changeTile(x,y,z,TileType.openlockeddoor);
//        } else if(tile==TileType.openlockeddoor) {
//            changeTile(x,y,z,TileType.lockeddoor);
//        } else if (tile==TileType.gate) {
//            changeTile(x,y,z,TileType.openlockedgate);
//        } else if(tile==TileType.openlockedgate) {
//            changeTile(x,y,z,TileType.gate);
        } else if(tile==TileType.uplever) {
            changeTile(x,y,z,TileType.downlever);
            addTrigger("LeverDown",x,y,z);
        } else if(tile==TileType.downlever) {
            changeTile(x,y,z,TileType.uplever);
            addTrigger("LeverUp",x,y,z);
        } else {
            addTrigger("Activate",x,y,z);
        }
        //Entry/Exit
        if(isAnEntryExit(x, y, z)) {
            if(player.isAt(x,y,z)) {
                doEntryExit(x, y, z);
            } else {
                EntryExit entry = getEntryExit(x, y, z);
                player.setMessage(entry.getDescription());
            }
        }
        
    }

    public void addTrigger(String trigger, int x, int y, int z) {
        Gdx.app.log("addTrigger",String.format("%d,%d,%d",x,y,z));
        Trigger trigg = new Trigger(trigger,x,y,z);
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
        int current = world.getTile(x,y,z).getId();
        world.changeTile(x, y, z, newTile);
        String coord = String.format("%d:%d:%d,%d", x, y, z, world.getId());
        ChangeTile original = changeTiles.get(coord);
        if (original != null) {
            changeTiles.remove(coord);
        }
        if (original == null || original.getIcon() != newTile.getIcon()) {
            changeTiles.put(coord, new ChangeTile(x, y, z, world.getId(), current));
        }
        //CODE !!!!
        // do it here (ie pits, pressure pads, teleports etc...)
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
        Obj ob =  objs.get(objs.size()-1);
        if (ob.isPlayer() && objs.size()>1) {
            ob = objs.get(objs.size()-2);
        }
        return ob;
    }

    public void moveObjToObj(Obj from, Obj to) {
        from.moveToObject(to, world);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public Message getMessage(int x, int y, int z) {
        return messages.getMessage(x,y,z);
    }

    public void expireMessages() {
        messages.expireMessages();
    }
}
