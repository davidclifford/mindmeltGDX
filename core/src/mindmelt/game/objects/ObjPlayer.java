/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmelt.game.objects;


/**
 *
 * @author w18749
 */
public class ObjPlayer extends Obj {

    private int level = 1;

    public ObjPlayer() {
        inventory = new Inventory(24);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
