package mindmelt.game.maps;

import java.awt.Color;

public class TileType {
    public int id = 0;
    public int icon = 0;
    public String name = "space";
    public boolean canEnter = false;
    public boolean seeThru = false;
    public int speed = 1;
    public int ch = '$';
    public Color color = Color.BLACK;
    
    public static TileType tileType[] = new TileType[256];

    public static final TileType space = new TileType(0).name("space").ch('$');
    public static final TileType floor = new TileType(1).name("floor").enter().seeThru().ch(' ').icon(1);
    public static final TileType presurepad = new TileType(2).name("pressure pad").enter().seeThru().ch('o').icon(2);
    public static final TileType hiddenpp = new TileType(3).name("hidden presure pad").enter().seeThru().ch('.').icon(3);
    public static final TileType hiddentele = new TileType(4).name("hidden teleport").enter().seeThru().ch('t').icon(4);
    public static final TileType leftturn = new TileType(5).name("left turn").enter().seeThru().ch(')').icon(5);
    public static final TileType rightturn = new TileType(6).name("right turn").enter().seeThru().ch('(').icon(6);
    public static final TileType uturn = new TileType(7).name("U turn").enter().seeThru().ch('U').icon(7);
    public static final TileType hiddenff = new TileType(8).name("hidden force field").seeThru().ch('z').icon(8);
    public static final TileType pit = new TileType(9).name("pit").enter().seeThru().ch('X').icon(9);
    public static final TileType wall = new TileType(10).name("wall").ch('#').icon(10);
    public static final TileType uplever = new TileType(11).name("up lever").ch('\\').icon(11);
    public static final TileType downlever = new TileType(12).name("down lever").ch('/').icon(12);
    public static final TileType message = new TileType(13).name("message").ch('m').icon(13);
    public static final TileType secretdoor = new TileType(14).name("secret door").enter().ch('S').icon(14);
    public static final TileType secretbutton = new TileType(15).name("secret button").ch('b').icon(15);
    public static final TileType button = new TileType(16).name("button").ch('B').icon(16);  
    public static final TileType door = new TileType(17).name("door").ch('[').icon(17);
    public static final TileType lockeddoor = new TileType(18).name("locked door").ch(']').icon(18);
    public static final TileType opendoor = new TileType(19).name("open door").ch('_').enter().seeThru().icon(19);
    public static final TileType gate = new TileType(20).name("gate").ch('G').seeThru().icon(20);
    public static final TileType forcefield = new TileType(21).name("force field").ch('Z').seeThru().icon(21);
    public static final TileType exit = new TileType(22).name("exit").ch('@').seeThru().enter().icon(22);
    public static final TileType teleport = new TileType(23).name("teleport").ch('T').seeThru().enter().icon(23);
    public static final TileType stairs = new TileType(24).name("stairs").ch('%').seeThru().enter().icon(24);
    public static final TileType window = new TileType(25).name("window").ch('+').seeThru().icon(25);
    public static final TileType grass = new TileType(26).name("grass").ch('\'').seeThru().enter().icon(26);
    public static final TileType bush = new TileType(27).name("bush").ch('*').seeThru().enter().speed(2).icon(27);
    public static final TileType tree = new TileType(28).name("tree").ch('|').enter().speed(2).icon(28);
    public static final TileType ytree = new TileType(29).name("y tree").ch('Y').enter().speed(2).icon(29);
    public static final TileType mountain = new TileType(30).name("mountain").ch('M').icon(30);
    public static final TileType hill = new TileType(31).name("hill").ch('n').seeThru().enter().speed(3).icon(31);
    public static final TileType water = new TileType(32).name("water").ch('~').seeThru().icon(32);
    public static final TileType deepwater = new TileType(33).name("deep water").ch('^').seeThru().icon(33);
    public static final TileType marsh = new TileType(34).name("marsh").ch(':').seeThru().enter().speed(2).icon(34);
    public static final TileType sign = new TileType(35).name("sign").ch('?').seeThru().enter().icon(35);
    public static final TileType crops = new TileType(36).name("crops").ch('!').seeThru().enter().icon(36);
    public static final TileType path = new TileType(37).name("path").ch('=').seeThru().enter().icon(37);
    public static final TileType bridge = new TileType(38).name("bridge").ch('-').seeThru().enter().icon(38);
    public static final TileType waterfall = new TileType(39).name("waterfall").ch('W').seeThru().icon(39);
    public static final TileType hiddenpit = new TileType(40).name("hidden pit").ch('x').seeThru().enter().icon(1);
    public static final TileType rocks = new TileType(41).name("rocks").ch('O').icon(41);
    public static final TileType fence = new TileType(42).name("fence").ch('F').seeThru().icon(42);
    public static final TileType house = new TileType(43).name("house").ch('H').seeThru().enter().icon(43);
    public static final TileType village = new TileType(44).name("village").ch('V').seeThru().enter().icon(44);
    public static final TileType openlockeddoor = new TileType(45).name("opened locked door").ch('<').seeThru().enter().icon(45);
    public static final TileType openlockedgate = new TileType(46).name("opened locked gate").ch('>').seeThru().enter().icon(46);
    public static final TileType castle = new TileType(47).name("omgra's castle").ch('&').seeThru().enter().icon(47);
    public static final TileType chair = new TileType(48).name("chair").ch('h').seeThru().enter().icon(48);
    public static final TileType table = new TileType(49).name("table").ch('A').seeThru().enter().icon(49);
    public static final TileType plant = new TileType(50).name("plant").ch('p').seeThru().enter().icon(50);
    public static final TileType barrel = new TileType(51).name("barrel").ch('D').seeThru().icon(51);
    public static final TileType wardrobe = new TileType(52).name("wardrobe").ch('I').seeThru().icon(52);
    public static final TileType drawers = new TileType(53).name("drawers").ch('d').seeThru().icon(53);
    public static final TileType lamp = new TileType(54).name("lamp").ch('l').seeThru().enter().icon(54);
    public static final TileType bed = new TileType(55).name("bed").ch('L').seeThru().enter().icon(55);
    public static final TileType fireplace = new TileType(56).name("firepace").ch('f').icon(56);
    public static final TileType books = new TileType(57).name("books").ch('E').icon(57);
    public static final TileType picture = new TileType(58).name("picture").ch('P').icon(58);
    public static final TileType rug = new TileType(59).name("rug").ch('r').seeThru().enter().icon(59);
    public static final TileType stone = new TileType(60).name("stone").ch('Q').icon(60);
       
    public TileType(int id) {
        if (tileType[id] != null) {
            System.out.println("Tile already registered");
            System.exit(1);
        }
        tileType[id] = this;
        this.id = id;
    }

    public TileType icon(int icon) {
        this.icon = icon;
        return this;
    }
    public TileType name(String name) {
        this.name = name;
        return this;
    }
    public TileType enter() {
        canEnter = true;
        return this;
    }
    public TileType seeThru() {
        this.seeThru = true;
        return this;
    }
    public TileType speed(int speed) {
        this.speed = speed;
        return this;
    }
    public TileType ch(char c) {
        ch = c;
        return this;
    }
    public TileType color(Color color) {
        this.color = color;
        return this;
    }

    static public TileType getFromChar(char ch)
    {
        for (TileType tile : tileType) {
            if(tile.ch == ch)
                return tile;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean canEnter() {
        return canEnter;
    }

    public void setCanEnter(boolean canEnter) {
        this.canEnter = canEnter;
    }

    public boolean isSeeThru() {
        return seeThru;
    }

    public void setSeeThru(boolean seeThru) {
        this.seeThru = seeThru;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
