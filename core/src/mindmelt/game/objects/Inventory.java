package mindmelt.game.objects;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.maps.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 29/05/2017.
 */
public class Inventory {
    private int inventorySize;
    private int hand = 0;
    private int slot[];
    List<Obj> objects = new ArrayList<>();

    public Inventory() {
    }

    public Inventory(int inventorySize) {
        this.inventorySize = inventorySize;
        slot = new int[inventorySize+1];
    }

    public boolean hasObjects() {
        return objects!=null || objects.size()>0;
    }

    public void remove(Obj obj) {
        objects.remove(obj);
    }

    public void objToInventory(Obj obj) {
        objects.add(obj);
    }

    public Obj getFirstObject() {
        Obj obj = objects.get(0);
        objects.remove(obj);
        return obj;
    }

    public List<Obj> getObjects() {
        return objects;
    }

    public void objToHand(Obj obj, MindmeltGDX game) {
        obj.moveToObject(game.player,game.world);
        hand = objects.indexOf(obj)+1;
    }

    public void handToInventory(int s) {
        slot[s] = hand;
        hand = 0;
    }

    public void inventoryToHand(int s) {
        hand = s;
        slot[s] = 0;
    }

    public Obj getHandObject() {
        if(hand==0) return null;
        return objects.get(hand-1);
    }

    public Obj getObject(int s) {
        if(s>1 && s<inventorySize && slot[s]!=0) {
            return objects.get(slot[s]-1);
        }
        return null;
    }

    public void handToMap(int x, int y, int z, World world, ObjectStore objectStore) {
        objects.get(hand-1).moveToMap(x,y,z,world,objectStore);
        hand = 0;
    }
}
