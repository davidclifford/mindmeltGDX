/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmelt.game.objects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
    private List<ObjPerson> mindmeltedPeople = new ArrayList<>();
    private boolean mapActive = false;
    private boolean talking = false;
    private Obj personTalkingTo;
    private String saying = "";

    public ObjPlayer() {
        inventory = new Inventory(24);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isTalking() {
        return talking;
    }

    public void setTalking(boolean talking) {
        this.talking = talking;
    }

    @Override
    public void update(Engine engine, float delta) {
        updateMessage(engine);
        if(strength<=0) {
            //DEAD
            EntryExit death = new EntryExit(0,0,0,40,39,0,"world","You are dead");
            engine.moveToMap(death);
            engine.addMessage("You are DEAD!");
            setHealthMax();
        }
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

    public boolean isMapActive() {
        return mapActive;
    }

    public void setMapActive(boolean mapActive) {
        this.mapActive = mapActive;
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

    public boolean alreadyMindmelted(ObjPerson person) {
        return (mindmeltedPeople.contains(person));
    }

    public void setMindmelted(ObjPerson person) {
        if(mindmeltedPeople.contains(person)) return;
        mindmeltedPeople.add(person);
    }

    public void levelUp() {
        level++;
        setHealthMax();
        //set spells
    }


    public void sayThis(Engine engine, String message) {
        if(!talking) return;
        setExpiry(engine,1000);
        messColour = Color.WHITE;
        this.message = "You Say> "+message+"#";
        if(message.length()==0)
            engine.getTextLines().addLine(this.message, messColour);
        else
            engine.getTextLines().updateCurrentLine(this.message, messColour);
    }

    public void startTalking(Engine engine, Obj person) {
        talking = true;
        personTalkingTo = person;
        saying = "";
        sayThis(engine,  saying);
    }

    public void stopTalking(Engine engine) {
        talking = false;
        personTalkingTo.setExpiry(engine,1);
        personTalkingTo = null;
        saying = "";
        setMessage(engine, "");
        setExpiry(engine,0);
    }

    public void talkInput(Engine engine, int input) {
        String in = Input.Keys.toString(input).toLowerCase();
        if(input==Input.Keys.BACKSPACE ) {
            if (saying.length() > 0) {
                saying = saying.substring(0, saying.length() - 1);
            }
        } else if (input==Input.Keys.ENTER) {
            if(saying.length()==0) {
                saying = "bye";
            }
            personTalkingTo.replyTo(engine, saying);
            if(saying.equals("bye"))
                stopTalking(engine);
            saying = "";
        } else {
            saying += in;
        }
        sayThis(engine, saying );
    }
}
