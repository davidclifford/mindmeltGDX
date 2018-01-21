/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmelt.game.objects;


import mindmelt.game.engine.Engine;

public class ObjPlayer extends Obj {

    private int level = 1;
    private float light = 1f;

    public ObjPlayer() {
        inventory = new Inventory(24);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void update(Engine engine, float delta) {
        //auto stuff?
    }

    public boolean isHolding(int obj) {
        Obj holding = inventory.getHandObject();
        if(holding==null) return false;
        return (holding.getId()==obj);
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }
}
