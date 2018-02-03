/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmelt.game.objects;

import mindmelt.game.engine.Engine;
import mindmelt.game.maps.EntryExit;
import mindmelt.game.spells.Spell;

import java.util.ArrayList;
import java.util.List;

public class ObjPlayer extends Obj {

    private int level = 1;
    private float light = 1f;
    private boolean xray = false;
    private boolean water = false;
    private boolean forcefield = false;
    private boolean stun = false;
    private boolean zap = false;
    private int backX = 0;
    private int backY = 0;
    private int backZ = 0;
    private String backMap = "";
    private List<Spell> spells = new ArrayList<>();

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

    public void addSpell(Spell spell) {
        spells.add(spell);
    }

    public List<Spell> getSpells() {
        return spells;
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

    public boolean isXray() {
        return xray;
    }

    public void setXray(boolean xray) {
        this.xray = xray;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isForcefield() {
        return forcefield;
    }

    public void setForcefield(boolean forcefield) {
        this.forcefield = forcefield;
    }

    public boolean isStun() {
        return stun;
    }

    public void setStun(boolean stun) {
        this.stun = stun;
    }

    public boolean isZap() {
        return zap;
    }

    public void setZap(boolean zap) {
        this.zap = zap;
    }

    public void setBack(Engine engine) {
        backX = getX();
        backY = getY();
        backZ = getZ();
        backMap = engine.getWorld().getFilename();
    }

    public void goBack(Engine engine) {
        EntryExit goBackTo = new EntryExit(0,0,0,backX,backY,backZ,backMap,"Back");
        engine.moveToMap(goBackTo);
    }

    public void setHealthMax() {
        strength = level*10 + 10;
    }
}
