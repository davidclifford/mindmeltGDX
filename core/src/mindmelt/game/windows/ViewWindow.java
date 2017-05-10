package mindmelt.game.windows;

import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;
import mindmelt.game.objects.Obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/05/2017.
 */
public class ViewWindow extends Window {

    private final int SIZE = 9;
    private final int HALF = SIZE/2;
    private int tile[][];
    private List<DispXY> dispList;
    private boolean xray = false;
    private boolean light = false;
    private boolean see_all = false;
    private boolean cheat = false;

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
            xf=x1;
            yf=y1;
            xt=x2;
            yt=y2;
        }
    }

    private class DispXYString {
        public int x;
        public int y;
        public String string;
        DispXYString(int x, int y, String string) {
            this.x = x;
            this.y = y;
            this.string = string;
        }
    }

    private int sgn(int a) { return a<0 ? -1 : (a>0 ? 1 : 0); }

    private void dispInit() {
        dispList = new ArrayList<>();
        for(int i=1; i<=HALF; i++) { //distance out
            for(int j=-i;j<=i;j++) { //go
                int k = sgn(j);
                int l = sgn(i);
                dispList.add(new DispXY(j, -i, j-k, l-i)); //across right top
                dispList.add(new DispXY(j, i, j-k, i-l)); //across left bottom
                dispList.add(new DispXY(i, j, i-l, j-k)); //right down
                dispList.add(new DispXY(-i, j, l-i, j-k)); // left up
            }
        }
    }

    private void displayPosition(int px, int py, int dir, MindmeltGDX game) {
        int mask[][] = new int[SIZE][SIZE]; //0 = see & thru, 1 = see & not thru, 2 = not see & not thru
        List<Obj> obj[][] = new ArrayList[SIZE][SIZE];
        Obj top[][] = new Obj[SIZE][SIZE];
        boolean dark = !game.world.getLight() && !light;

        for (DispXY xy : dispList) {
            boolean canSee = game.world.getTile(px+xy.xf, py+xy.yf, 0).isSeeThru();
            if (mask[xy.xt+HALF][xy.yt+HALF]==0 && !canSee) {
                mask[xy.xf+HALF][xy.yf+HALF] = 1;
            } else if (mask[xy.xt+HALF][xy.yt+HALF]>=1) {
                mask[xy.xf+HALF][xy.yf+HALF] = 2;
            }
            if(xy.xf <= 1 && xy.xf >= -1 && xy.yf <= 1 && xy.yf >= -1 && xray) {
                mask[xy.xf+HALF][xy.yf+HALF] = 0;
            }
            if((xy.xf > 1 || xy.xf < -1 || xy.yf > 1 || xy.yf < -1) && dark) {
                mask[xy.xf+HALF][xy.yf+HALF] = 2;
            }
        }

        // Draw landscape
        List<DispXYString> xyStrings = new ArrayList<>();
        for (int y=-HALF;y<=HALF;y++) {
            for (int x=-HALF;x<=HALF;x++) {
                float bright = (float)(6f-Math.sqrt(x*x+y*y))/6f;
                int tile = game.world.getTile(px+x, py+y, 0).getIcon();
                int xx = dir==0 ? x : dir==1 ? y : dir==2 ? -x : -y;
                int yy = dir==0 ? y : dir==1 ? -x : dir==2 ? -y : x;
                if (mask[x+HALF][y+HALF] < 2 || see_all) {
                    drawTile(HALF+xx, HALF+yy, tile,bright,game);
                    List<Obj> objects = game.world.getObjects(px+x, py+y, 0);
                    if (objects!=null) {
                        for (Obj ob : objects) {
                            drawTile(HALF+xx, HALF+yy, ob.getIcon(),bright, game);
                            DispXYString xys = new DispXYString( HALF+xx, HALF+yy,ob.getMessage());
                            xyStrings.add(xys);
                        }
                    }
                } else {
                    drawTile(HALF+xx, HALF+yy, 0, bright, game);
                }
            }
        }

        //Display any strings
//        for(DispXYString xys:xyStrings ) {
//            drawString(xys.x, xys.y, xys.string);
//        }
    }

    @Override
    protected void renderThis(MindmeltGDX game, float delta) {
        super.renderThis(game,delta);

        int playerX = game.player.getX();
        int playerY = game.player.getY();
        int direction = game.player.getDirection();

        displayPosition(playerX,playerY,direction,game);

    }
}
