package mindmelt.game.engine;

import java.util.List;
import java.util.Map.Entry;

import mindmelt.game.MindmeltGDX;
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
    
    public Engine(MindmeltGDX game) {
        this.game = game;
        this.world = game.world;
        this.objects = game.objects;
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
        } else if (tile==TileType.lockeddoor) {
            changeTile(x,y,z,TileType.openlockeddoor);
        } else if(tile==TileType.openlockeddoor) {
            changeTile(x,y,z,TileType.lockeddoor);    
        } else if (tile==TileType.gate) {
            changeTile(x,y,z,TileType.openlockedgate);
        } else if(tile==TileType.openlockedgate) {
            changeTile(x,y,z,TileType.gate);        
        } else if(tile==TileType.uplever) {
            changeTile(x,y,z,TileType.downlever);
        } else if(tile==TileType.downlever) {
            changeTile(x,y,z,TileType.uplever);
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
    
    public void changeTile(int x, int y, int z, TileType tile) {
        world.changeTile(x,y,z,tile);
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

}
