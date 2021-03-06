package mindmelt.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.gui.Window;
import mindmelt.game.maps.TileType;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjMonster;
import mindmelt.game.objects.ObjPlayer;
import mindmelt.game.talk.Talking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/05/2017.
 */
public class ViewWindow extends Window {

    private final int SIZE = 9;
    private final int HALF = SIZE / 2;
    private List<DispXY> dispList;
    private List<Coords> zaps;
    private boolean debugView = false;

    public ViewWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
        dispInit();
    }

    private class Coords {
        int x;
        int y;
        Coords(int x, int y) {this.x=x; this.y=y;}
    }

    private class DispXY {
        public int xf;
        public int yf;
        public int xt;
        public int yt;

        DispXY(int x1, int y1, int x2, int y2) {
            xf = x1;
            yf = y1;
            xt = x2;
            yt = y2;
        }
    }

    private void dispInit() {
        dispList = new ArrayList<>();
        for (int i = 1; i <= HALF; i++) { //distance out
            for (int j = -i; j <= i; j++) { //go
                int k = sgn(j);
                int l = sgn(i);
                dispList.add(new DispXY(j, -i, j - k, l - i)); //across right top
                dispList.add(new DispXY(j, i, j - k, i - l)); //across left bottom
                dispList.add(new DispXY(i, j, i - l, j - k)); //right down
                dispList.add(new DispXY(-i, j, l - i, j - k)); // left up
            }
        }
    }

    private void displayPosition(int px, int py, int pz, int dir, MindmeltGDX game) {
        Engine engine = game.engine;
        ObjPlayer player = engine.getPlayer();
        Talking talking = engine.getTalking();
        DisplayStrings displayStrings = new DisplayStrings();
        boolean debugView = engine.isDebugView();

        zaps = new ArrayList<>();
        int mask[][] = new int[SIZE][SIZE]; //0 = see & thru, 1 = see & not thru, 2 = not see & not thru
        float darkness = 0;

        if (!game.world.getLight())
            darkness = 3f * player.getLight();

        for (DispXY xy : dispList) {
            boolean canSee = game.world.getTile(px + xy.xf, py + xy.yf, pz).isSeeThru();
            if (mask[xy.xt + HALF][xy.yt + HALF] == 0 && !canSee) {
                mask[xy.xf + HALF][xy.yf + HALF] = 1;
            } else if (mask[xy.xt + HALF][xy.yt + HALF] >= 1) {
                mask[xy.xf + HALF][xy.yf + HALF] = 2;
            }
            if (xy.xf <= 1 && xy.xf >= -1 && xy.yf <= 1 && xy.yf >= -1 && player.isXray()) {
                mask[xy.xf + HALF][xy.yf + HALF] = 0;
            }
        }

        // Draw landscape
        for (int y = -HALF; y <= HALF; y++) {
            for (int x = -HALF; x <= HALF; x++) {
                int sx = px + x;
                int sy = py + y;
                int sz = pz;
                //float bright = 1f-darkness*(float)(Math.sqrt(x*x+y*y)/Math.sqrt(HALF*HALF*2));
                float bright = 1f - darkness * Math.max(Math.abs(x), Math.abs(y)) / HALF;
                int tile = game.world.getTile(sx, sy, sz).getIcon(debugView);
                int xx = dir == 0 ? x : dir == 1 ? y : dir == 2 ? -x : -y;
                int yy = dir == 0 ? y : dir == 1 ? -x : dir == 2 ? -y : x;
                if (mask[x + HALF][y + HALF] < 2 || engine.isSeeall()) {
                    drawTile(HALF + xx, HALF + yy, tile, bright, game);
                    List<Obj> objects = game.world.getObjects(sx, sy, sz);
                    if (objects != null) {
                        for (Obj ob : objects) {
                            displayObject(HALF + xx, HALF + yy, ob, bright, game);
                            if(ob.getMessage()!="") {
                                displayStrings.add(HALF + xx, HALF + yy, ob.getMessage(), ob.getMessageColour());
                            }
                        }
                    }
                    //messages
                    Message message = engine.getMessage(sx, sy, sz);
                    if (message != null) {
                        displayStrings.add(HALF + xx, HALF + yy, message.getMessage(), message.getColour());
                    }
                    //talking
                    if (talking.isTalking() || talking.isReplying(engine)) {
                        // set display coords for player and talking thing
                        if(talking.isPlayerAt(sx,sy,sz)) {
                            talking.setPlayerScreenCoords(HALF + xx, HALF + yy);
                        } else if (talking.isOtherAt(sx,sy,sz)) {
                            talking.setOtherScreenCoords(HALF + xx, HALF + yy);
                        }
                    }
                } else {
                    drawTile(HALF + xx, HALF + yy, 0, bright, game);
                }
            }
        }

        //Display any strings
        for (DispXYString xys : displayStrings.getDispXYStrings() ) {
            drawMidStringInBox(xys.getX(), xys.getY(), xys.getColour(), xys.getString(), game);
        }
        //Display Zaps
        for(Coords coords : zaps) {
            zapMonster(HALF,HALF,coords.x,coords.y,game);
        }
        //Display talking
        displayTalk(engine, game, displayStrings);
    }

    private void displayTalk(Engine engine, MindmeltGDX game, DisplayStrings displayStrings) {
        Talking talking = engine.getTalking();
        if (!talking.isReplying(engine) && !talking.isTalking())
            return;
        int px = talking.getPlayerScreenX();
        int py = talking.getPlayerScreenY();
        int ox = talking.getOtherScreenX();
        int oy = talking.getOtherScreenY();
        if (py==oy) oy -= 1;
        if (talking.isTalking()) {
            drawMidStringInBox(px, py, Color.WHITE, "You say> " + talking.getPlayerTalk() + "#", game);
        }
        if( talking.isReplying(engine) && talking.getOtherTalk().length() > 0) {
            if(displayStrings.isAlreadyMessageAt(ox,oy)) {
                oy--;
            }
            String otherTalk = engine.addToJournal(talking.getOtherTalk());
            drawMidStringInBox(ox, oy, Color.GREEN, otherTalk, game);
        }
    }

    private void displayMap(int mx, int my, int mz, MindmeltGDX game) {
        int size = 50;
        int pixSize = 6;
        Engine engine = game.engine;
        long time = engine.getSystemTime()/100000000L;
        ObjPlayer player = engine.getPlayer();
        for(int y=-size/2;y<size/2;y++) {
            for (int x = -size/2; x < size/2; x++) {
                TileType tile = engine.getTile(mx+x,my+y,mz);
                plot(PIXEL6,x*pixSize+w/2,y*pixSize+h/2,tile.getColor(),game);
            }
        }
        Color pixel = Color.WHITE;
        if(time%2L==0) pixel = Color.BLACK;
        plot(PIXEL6,w/2, h/2, pixel, game);
    }

    private void displayObject(int x, int y, Obj ob, float bright, MindmeltGDX game) {
        if(ob.isMonster() && (game.player.isStun() || game.player.isZap())) {
            if(game.player.isStun()) {
                long time = game.engine.getSystemTime();
                if (time % 100000000L > 50000000L && ob.getStrength() > 0)
                    drawTile(x, y, ob.getIcon(), bright / 2f, game);
                else
                    drawTile(x, y, ob.getIcon(), bright, game);
            } else if(game.player.isZap()) {
                drawTile(x, y, ob.getIcon(), bright, game);
                if(!ob.isDead()) {
                    ((ObjMonster) ob).zap(game.engine); //not good coding
                    zaps.add(new Coords(x, y));
                }
            }
        } else {
            drawTile(x, y, ob.getIcon(), bright, game);
        }
    }

    private void zapMonster(int x1, int y1, int x2, int y2, MindmeltGDX game) {
        zap(x1,y1,x2,y2,game);
    }

    @Override
    protected void renderThis(MindmeltGDX game, float delta) {
        //lighting += delta;
        super.renderThis(game,delta);

        int playerX = game.player.getX();
        int playerY = game.player.getY();
        int playerZ = game.player.getZ();
        int direction = game.player.getDirection();

        if(game.player.isMapActive()) {
            displayMap(playerX, playerY, playerZ, game);
        } else {
            displayPosition(playerX, playerY, playerZ, direction, game);
        }

    }

    @Override
    public void activate(int x, int y, Engine engine) {
        Gdx.app.log("ViewWindow",String.format("%d,%d",x,y));
    }
}
