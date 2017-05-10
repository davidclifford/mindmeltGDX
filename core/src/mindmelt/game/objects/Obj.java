package mindmelt.game.objects;
// basic objects

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mindmelt.game.engine.Engine;
import mindmelt.game.maps.World;

public class Obj {
    public int id = 0;
    public int x = 0;
    public int y = 0;
    public int z = 0;
    public int mapId = 0;
    public int dir = 0;
    public float speed = 0.1f;
    public float wait = 0;
    public String message = "";
    
    public Obj inside = null;
    public List<Obj> objects = null;
    
    public int icon = 0;
    public String name = "object";
    public String description = "an object";
    public String type = "object";
    
    public Random rand = new Random();
    
    public boolean isInMap() {
        return (mapId != 0);
    }
    
    public boolean isInObject() {
        return (inside!=null);
    }
    
    public boolean isInInventory() {
        return (inside!=null && type.equals("player"));
    }
    
    public boolean hasObjectInside() {
        return (objects!=null && objects.size()>0) ;
    }
    
    public boolean isAt(int x, int y, int z) {
        return (this.x == x && this.y == y && this.z == z);
    }
    public void moveToObject(Obj obTo, World world) {
        if (isInMap()) unlink(world);
        if (isInObject()) unlink();
        inside = obTo;
        obTo.addObject(this);
    }
    
    private void addObject(Obj ob) {
        if (objects==null) {
            objects = new ArrayList<Obj>();
        }        
        objects.add(ob);
    }
    
    public Obj moveToMap(World world) {
        world.setTop(x, y, z, this);
        return this;
    }
    
    public void moveToMap(int x, int y, int z, World world, ObjectStore objects) {
        Obj ob = this;
        int mapId = world.getId();
        if (isInMap()) { 
            unlink(world);
        }
        else if (isInObject()) {
            unlink();
        }
        world.setTop(x, y, z, ob);
        setCoords(x, y, z);
        setMapId(mapId);
        objects.addToActiveObjects(ob);
    }

    private void unlink() {
        if (inside==null) 
            return;
        List<Obj> objects = inside.objects;
        objects.remove(this);
        inside = null;
    }
    
    private void unlink(World world) {
        world.removeObject(this);
    }
    
    public static Obj builder(String type) {
        Obj ob;
        if (type.equals("player"))
            ob = new ObjPlayer();
        else if (type.equals("monster"))
            ob = new ObjMonster();
        else if (type.equals("person"))
            ob = new ObjPerson();
        else if (type.equals("animal"))
            ob = new ObjAnimal();
        else if (type.equals("thing"))
            ob = new ObjThing();
        else
            ob = new Obj();
        return ob.type(type);
    }
    
    public Obj setCoords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }  

    public Obj inside(Obj in) {
        this.inside = in;
        return this;
    }
    
    public Obj mapId(int mapId) {
        this.mapId = mapId;
        return this;
    }
    
    public Obj name(String name) {
        this.name = name;
        return this;
    }    
    
    public Obj description(String description) {
        this.description = description;
        return this;
    }
    
    public Obj type(String type) {
        this.type = type;
        return this;
    }
    
    public Obj icon(int icon) {
        this.icon = icon;
        return this;
    }    
    
    public Obj id(int id) {
        this.id = id;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getWait() {
        return wait;
    }

    public void setWait(float wait) {
        this.wait = wait;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public Obj getInside() {
        return inside;
    }

    public void setInside(Obj inside) {
        this.inside = inside;
        mapId = 0;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDirection() {
        return dir;
    }

    public void setDirection(int dir) {
        this.dir = dir;
    }
    
    public List<Obj> getObjects() {
        return objects;
    }
 
    public boolean isMonster() {
        return type.equals("monster");
    } 
    
    public boolean isPlayer() {
        return type.equals("player");
    } 
    
    public boolean isPerson() {
        return type.equals("person");
    } 
    
    public boolean isAnimal() {
        return type.equals("animal");
    } 
    
    public boolean isBlocked() {
        return isMonster() || isPlayer() || isPerson() || isAnimal();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isReady(float delta) {
        wait+=delta;
        if (wait>=speed) {
            wait=0;
            setMessage("");
            return true;
        }
        return false;
    }
       
    public void update(Engine engine, int delta) {
        
    }
    
}
