/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmelt.game.objects;

import com.badlogic.gdx.Input;
import mindmelt.game.engine.Engine;
import mindmelt.game.maps.EntryExit;
import mindmelt.game.spells.Spell;
import mindmelt.game.talk.Talking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ObjPlayer extends Obj {

    private static final int SIZE = 9;
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
    private Talking talking;
    private int smellMap[][] = new int[SIZE][SIZE];

    public ObjPlayer() {
        inventory = new Inventory(24);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isTalking(Engine engine) {
        return engine.getTalking().isTalking();
    }

    public void setTalking(Engine engine, boolean talking) {
        engine.getTalking().setTalking(talking);
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
        updateSmellMap(engine);
    }

    private void updateSmellMap(Engine engine) {
        for(int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                smellMap[i][j] = SIZE*SIZE;
            }
        }
        adjacent(engine, x, y, 1);
        // display
//        for(int i=0; i<SIZE; i++) {
//            String line = "";
//            for (int j=0; j<SIZE; j++) {
//                line += smellMap[i][j];
//            }
//            System.out.println(line);
//        }
//        System.out.println("---------");
    }

    private void adjacent(Engine engine, int x1, int y1, int dist) {
        if((x1!=x || y1!=y) && !engine.canSmell(x1, y1, z))
            return;
        int sx = x1 - x + SIZE/2;
        int sy = y1 - y + SIZE/2;
        if(sx>=0 && sx<SIZE && sy>=0 && sy<SIZE) {
            if(smellMap[sx][sy] > dist) {
                smellMap[sx][sy] = dist;
                adjacent(engine, x1 + 1, y1, dist + 1);
                adjacent(engine, x1, y1 + 1, dist + 1);
                adjacent(engine, x1 - 1, y1, dist + 1);
                adjacent(engine, x1, y1 - 1, dist + 1);
            }
        }
    }

    public int getSmellDist(int mx, int my, int mz) {
        if(mz != z) return 0;
        int sx = mx - x + SIZE/2;
        int sy = my - y + SIZE/2;
        if(sx>=0 && sx<SIZE && sy>=0 && sy<SIZE) {
            return smellMap[sx][sy];
        }
        return SIZE*SIZE;
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
        setSpells(level);
    }
        //set spells

    public void setSpells(int level) {
/*
100,scroll,X-ray scroll,thing,44,64,0,0,44,0,0,102
101,scroll,Stun scroll,thing,68,64,0,0,68,0,0,102
102,scroll,Zap scroll,thing,67,64,0,0,67,0,0,102
103,scroll,Heal scroll,thing,58,64,0,0,58,0,0,102
104,scroll,Back scroll,thing,45,64,0,0,45,0,0,102
105,scroll,Overview scroll,thing,69,64,0,0,69,0,0,102
 */
        switch (level) {
            case 2:
                spells.get(2).setLearned(true); //dir
                break;
            case 3:
                spells.get(3).setLearned(true); //coord
                break;
            case 4:
                spells.get(4).setLearned(true); //light
                spells.get(5).setLearned(true); //xray
                break;
            case 5:
                spells.get(6).setLearned(true); //water
                spells.get(7).setLearned(true); //stun
                break;
            case 6:
                spells.get(8).setLearned(true); //jump
                spells.get(9).setLearned(true); //zap
                spells.get(10).setLearned(true); //health
                break;
            case 7:
                spells.get(11).setLearned(true); //FF
                spells.get(12).setLearned(true); //back
                spells.get(13).setLearned(true); //map
                break;
        }
    }

    public void sayThis(Engine engine, String message) {
        engine.getTalking().setPlayerTalk(message);
        String reply = engine.getWorld().talkTo(engine.getTalking().getOther(), message, engine);
        engine.getTalking().setOtherTalk(reply);
    }

    public void startTalking(Engine engine, int other, int xx, int yy, int zz) {
        ObjPlayer player = engine.getPlayer();
        talking = engine.getTalking();
        talking.setTalking(true);
        if(engine.getWorld().talkFirst(other)) {
            talking.setOtherTalk(engine.getWorld().talkTo(other, "greeting", engine));
        } else {
            talking.setOtherTalk("");
        }
        talking.setPlayerTalk("");
        talking.setPlayerCoords(player.getX(), player.getY(), player.getZ() );
        talking.setOtherCoords(xx, yy, zz);
        talking.setOther(other);
    }

    public void stopTalking(Engine engine) {
        talking.setTalking(false);
        talking.setTimeout(engine,2000L);
    }

    public void talkInput(Engine engine, int input) {
        String in = Input.Keys.toString(input).toLowerCase();
        String saying = talking.getPlayerTalk();
        if(input==Input.Keys.BACKSPACE ) {
            if (saying.length() > 0) {
                saying = saying.substring(0, saying.length() - 1);
            }
        } else if (input==Input.Keys.ENTER) {
            if(saying.length()==0) {
                saying = "bye";
            }
            sayThis(engine, saying);
            if(saying.equals("bye"))
                stopTalking(engine);
            saying = "";
        } else {
            saying += in;
        }
        talking.setPlayerTalk(saying);
    }

    public void saveStatus(Engine engine, String name) {
        String filename = "data/"+name+".pla";
        String line = "";
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(filename));

            output.write("map="+engine.getWorld().getFilename()+"\n");
            output.write("level="+getLevel()+"\n");
            output.write("light="+getLight()+"\n");
            output.write("ff="+isForcefield()+"\n");
            output.write("water="+isWater()+"\n");
            output.write("stun="+isStun()+"\n");
            output.write("xray="+isXray()+"\n");
            output.write("zap="+isZap()+"\n");
            output.write("backmap="+backMap+"\n");
            output.write("backx="+backX+"\n");
            output.write("backy="+backY+"\n");
            output.write("backz="+backZ+"\n");
            output.write("mapactive="+isMapActive()+"\n");
            //spells learned
            int spellNum = 0;
            for(Spell spell : getSpells()) {
                output.write("spell="+spellNum+","+(spell.isLearned()?1:0)+","+(spell.isActive()?1:0)+"\n");
                spellNum++;
            }
            output.close();
        } catch (Exception e) {
            System.out.println("Error "+e.getMessage());
        }
    }

    public String loadStatus(Engine engine, String name) {
        String filename = "data/"+name+".pla";
        String line;
        String mapName = null;
        BufferedReader input = null;

        try {
            input = new BufferedReader((new FileReader(filename)));

            while((line = input.readLine()) != null) {
                if(line.length()==0) continue;
                if(line.startsWith("//")) continue;

                String parts[] = line.split("=");
                if(parts.length != 2) continue;
                String key = parts[0];
                String value = parts[1];
                switch (key) {
                    case "map":
                        mapName = value;
                        break;
                    case "spell":
                        String spellParts[] = value.split(",");
                        int sp = Integer.parseInt(spellParts[0]);
                        Spell spell = spells.get(sp);
                        spell.setLearned(spellParts[1].equals("1"));
                        if (spellParts[2].equals("1")) spell.activate(engine);
                        break;
                    case "level":
                        level = Integer.parseInt(value);
                        break;
                    case "light":
                        light = Float.parseFloat(value);
                        break;
                    case "ff":
                        forcefield = Boolean.parseBoolean(value);
                        break;
                    case "water":
                        water = Boolean.parseBoolean(value);
                        break;
                    case "stun":
                        stun = Boolean.parseBoolean(value);
                        break;
                    case "xray":
                        xray = Boolean.parseBoolean(value);
                        break;
                    case "zap":
                        zap = Boolean.parseBoolean(value);
                        break;
                    case "mapactive":
                        mapActive = Boolean.parseBoolean(value);
                        break;
                    case "backmap":
                        backMap = value;
                        break;
                    case "backx":
                        backX = Integer.parseInt(value);
                        break;
                    case "backy":
                        backY = Integer.parseInt(value);
                        break;
                    case "backz":
                        backZ = Integer.parseInt(value);
                        break;
                    default:
                        System.out.println("Unknown key: "+line);
                }
            }
            input.close();
        } catch (Exception e) {
            System.out.println("Error: "+e);
            e.printStackTrace();
            System.exit(1);
        }
        return mapName;
    }

    public void initInventory() {
        inventory.initSlots();
    }
}
