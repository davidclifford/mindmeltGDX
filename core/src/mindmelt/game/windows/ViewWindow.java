package mindmelt.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.engine.Engine;
import mindmelt.game.engine.Message;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/05/2017.
 */
public class ViewWindow extends Window {

    private final int SIZE = 9;
    private final int HALF = SIZE / 2;
    private List<DispXY> dispList;

    public ViewWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
        dispInit();
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

    private class DispXYString {
        public int x;
        public int y;
        public String string;
        public Color colour;

        DispXYString(int x, int y, String string, Color colour) {
            this.x = x;
            this.y = y;
            this.string = string;
            this.colour = colour;
        }
    }

    private int sgn(int a) {
        return a < 0 ? -1 : (a > 0 ? 1 : 0);
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
        List<DispXYString> xyStrings = new ArrayList<>();
        for (int y = -HALF; y <= HALF; y++) {
            for (int x = -HALF; x <= HALF; x++) {
                int sx = px + x;
                int sy = py + y;
                int sz = pz;
                //float bright = 1f-darkness*(float)(Math.sqrt(x*x+y*y)/Math.sqrt(HALF*HALF*2));
                float bright = 1f - darkness * Math.max(Math.abs(x), Math.abs(y)) / HALF;
                int tile = game.world.getTile(sx, sy, sz).getIcon();
                int xx = dir == 0 ? x : dir == 1 ? y : dir == 2 ? -x : -y;
                int yy = dir == 0 ? y : dir == 1 ? -x : dir == 2 ? -y : x;
                if (mask[x + HALF][y + HALF] < 2 || engine.isSeeall()) {
                    drawTile(HALF + xx, HALF + yy, tile, bright, game);
                    List<Obj> objects = game.world.getObjects(sx, sy, sz);
                    if (objects != null) {
                        for (Obj ob : objects) {
                            displayObject(HALF + xx, HALF + yy, ob, bright, game)
//                            drawTile(HALF+xx, HALF+yy, ob.getIcon(),bright, game);
                            DispXYString xys = new DispXYString(HALF + xx, HALF + yy, ob.getMessage(), Color.BLUE);
                            if (xys.string != "")
                                xyStrings.add(xys);
                        }
                    }
                    //messages
                    Message message = engine.getMessage(sx, sy, sz);
                    if (message != null) {
                        DispXYString xys = new DispXYString(HALF + xx, HALF + yy, message.getMessage(), message.getColour());
                        xyStrings.add(xys);
                    }
                } else {
                    drawTile(HALF + xx, HALF + yy, 0, bright, game);
                }
            }
        }

        //Display any strings
        for (DispXYString xys : xyStrings) {
            drawMidString(xys.x, xys.y, xys.colour, xys.string, game);
        }
    }

    private void drawObject(int x, int y, Obj ob, float bright, MindmeltGDX game) {
        drawTile(HALF+xx, HALF+yy, ob.getIcon(),bright,game);
    }

    @Override
    protected void renderThis(MindmeltGDX game, float delta) {
        //lighting += delta;
        super.renderThis(game,delta);

        int playerX = game.player.getX();
        int playerY = game.player.getY();
        int playerZ = game.player.getZ();
        int direction = game.player.getDirection();

        displayPosition(playerX,playerY,playerZ,direction,game);

    }

    @Override
    public void activate(int x, int y, Engine engine) {
        Gdx.app.log("ViewWindow",String.format("%d,%d",x,y));
    }
}
