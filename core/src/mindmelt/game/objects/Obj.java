package mindmelt.game.objects;
// basic objects

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.maps.World;

public class Obj {
    public static final int DEAD_ICON = 183;
    public int id = 0;
    public int x = 0;
    public int y = 0;
    public int z = 0;
    public int mapId = 0;
    public int strength;
    public int order;
    public int dir = 0;
    public long speed = 1000000000L/3L;
    public long wait;
    public String message = "";
    public Color messColour;
    private long expiry = 0;

    public Obj inside = null;

    public Inventory inventory = new Inventory();
    
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
        return (inventory.hasObjects()) ;
    }

    public boolean isAt(int x, int y, int z) {
        return (this.x == x && this.y == y && this.z == z);
    }

    public boolean isInside(int container) {
        return isInObject() && inside.getId()==container;
    }

    public void moveToObject(Obj obTo, World world) {
        if (isInMap()) unlink(world);
        if (isInObject()) unlink();
        inside = obTo;
        obTo.addObject(this);
    }
    
    private void addObject(Obj ob) {
        inventory.objToInventory(ob);
    }
    
    public Obj moveToMap(World world) {
        world.setTop(x, y, z, this);
        return this;
    }
    
    public void moveToMap(int x, int y, int z, Engine engine) {
        Obj ob = this;
        int mapId = engine.getWorld().getId();
        if (isInMap()) { 
            unlink(engine.getWorld());
        }
        else if (isInObject()) {
            unlink();
        }
        engine.getWorld().setTop(x, y, z, ob);
        setCoords(x, y, z);
        setMapId(mapId);
        engine.getObjects().addToActiveObjects(ob);
    }

    private void unlink() {
        if (inside==null) 
            return;
        inside.inventory.remove(this);
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

    public Obj strength(int strength) {
        this.strength = strength;
        return this;
    }

    public Obj order(int order) {
        this.order = order;
        return this;
    }

    public Obj id(int id) {
        this.id = id;
        return this;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed*100000000L;
    }

    public long getWait() {
        return wait;
    }

    public void setWait(long wait) {
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

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<Obj> getObjects() {
        return inventory.getObjects();
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

    public boolean isPlayerBlocked() {
        return (isMonster() && !isDead());
    }

    public String getMessage() {
        return message;
    }

    protected void setExpiry(Engine engine) {
        expiry = engine.getSystemTime()+1000000000L;
    }

    protected void setExpiry(Engine engine, int secs) {
        expiry = engine.getSystemTime()+(secs*1000000000L);
    }

    public void setMessage(Engine engine, String message) {
        this.message = message;
        setExpiry(engine);
    }

    public void setMessage(Engine engine, String message, Color colour) {
        setMessage(engine, message);
        this.messColour = colour;
    }

    public void updateMessage(Engine engine) {
        if(engine.getSystemTime()>=expiry) {
            message = "";
        }
    }
    public boolean isReady(Engine engine) {
        return (engine.getSystemTime()>wait);
    }

    public void rotate(int d) {
        dir+=d;
        dir = dir>3 ? dir-4 : dir<0 ? dir+4 : dir;
    }

    public void update(Engine engine, float delta) {
        updateMessage(engine);

        //don't move if next to player
        ObjPlayer player = engine.getPlayer();
        if(abs(x-player.getX())<=1 && abs(y-player.getY())<=1 ) return;

        int dx = x;
        int dy = y;

        if (rand.nextInt(2)==0) {
            dx += rand.nextInt(2)*2-1;
        } else {
            dy += rand.nextInt(2)*2-1;
        }

        if (isReady(engine)) {
            if (engine.canEnter(this, dx, dy, z)) {
                engine.moveObjToMap(this, dx, dy, z);
                resetWait(engine);
            }
        }
    }

    private int abs(int i) { return (i<0) ? -i : i; }

    public void resetWait(Engine engine) {
        wait = engine.getSystemTime() + speed;
    }

    protected int sign(int s) {
        return (s<0) ? -1 : (s>0) ? 1 : 0;
    }

    public void attack(Engine engine, int hits) {
        hits(engine, hits);
    }

    public void attack(Engine engine) {}

    public void talkTo(Engine engine) {
        engine.getPlayer().startTalking(engine, this);
    }

    public void replyTo(Engine engine, String saying) {
        String reply = engine.getWorld().talkTo(id, saying, engine);
        setMessage(engine,reply,Color.GREEN);
        setExpiry(engine,1000);
    }

    public void hits(Engine engine, int hits) {
        if(strength<=0) return; //already dead
        strength -= hits;
        strength = (strength<0) ? 0 : strength;
        Color colour = Color.GREEN;
        if(strength<20) colour = Color.YELLOW;
        if(strength<10) colour = Color.RED;
        String damage = ""+hits;
        setMessage(engine,damage,colour);
    }

    public Color getMessageColour() {
        return messColour;
    }

    public boolean isDead() {
        return (strength<=0);
    }
}
