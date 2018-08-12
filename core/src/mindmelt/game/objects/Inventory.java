package mindmelt.game.objects;

import mindmelt.game.engine.Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 29/05/2017.
 */
public class Inventory {
    private int inventorySize;
    private Obj hand = null;
    private Obj slot[];
    private List<Obj> objects = new ArrayList<>();

    public Inventory() {
    }

    public Inventory(int inventorySize) {
        this.inventorySize = inventorySize;
        slot = new Obj[inventorySize];
    }

    public boolean hasObjects() {
        return objects!=null || objects.size()>0;
    }

    public void remove(Obj obj) {
        if(hand==obj)
            hand = null;
        for(int s=0; s<inventorySize; s++) {
            if (slot[s]==obj)
                slot[s] = null;
        }
        objects.remove(obj);
    }

    public void objToInventory(Obj obj) {
        objects.add(obj);
        if(hand==obj) return;
        for(int s=0; s<inventorySize; s++) {
            if (slot[s]==obj)
                return;
        }
        for(int s=0; s<inventorySize; s++) {
            if (slot[s]==null) {
                slot[s] = obj;
                return;
            }
        }
    }

    public List<Obj> getObjects() {
        return objects;
    }

    public void objToHand(Obj obj, Engine engine) {
        hand = obj;
        obj.moveToObject(engine.getPlayer(),engine.getWorld());
    }

    public void handToInventory(int s) {
        if(s>=inventorySize) return;
        slot[s] = hand;
        hand = null;
    }

    public void inventoryToHand(int s) {
        if(s>=inventorySize) return;
        hand = slot[s];
        slot[s] = null;
    }

    public Obj getHandObject() {
        return hand;
    }

    public Obj getObject(int s) {
        if(s>=0 && s<inventorySize && slot[s]!=null) {
            return slot[s];
        }
        return null;
    }

    public void handToMap(int x, int y, int z, Engine engine) {
        hand.moveToMap(x,y,z,engine);
        hand = null;
    }

    public void initSlots() {
        int s=0;
        for(Obj ob:objects) {
            slot[s++] = ob;
        }
    }
}
