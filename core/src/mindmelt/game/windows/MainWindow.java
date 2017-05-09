package mindmelt.game.windows;

import com.badlogic.gdx.graphics.Color;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Window;

/**
 * Created by David on 8/05/2017.
 */
public class MainWindow extends Window {

    public MainWindow(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void renderThis(MindmeltGDX game, float delta) {
        //super.renderThis(game,delta);
        game.batch.setColor(Color.WHITE);
        game.batch.draw(game.mainWindow, getAbsX(), height-getAbsY()-game.mainWindow.getHeight()+32);
        //super.render(game,delta);
    }
/*
        //Window
        for(int i=0;i<20;i++) {
            drawTile(i, 0, SQUARE, Color.LIGHT_GRAY, game);
            drawTile(i, 10, SQUARE, Color.LIGHT_GRAY, game);
            if(i>10 && i<19) {
                drawTile(i, 4, SQUARE, Color.LIGHT_GRAY, game);
                drawTile(i, 7, SQUARE, Color.LIGHT_GRAY, game);
            }
            drawTile(i,0,TOP,Color.WHITE,game);
            drawTile(i,10,BOTTOM,Color.DARK_GRAY,game);
        }
        for(int i=1; i<10; i++) {
            drawTile(0,i,SQUARE,Color.LIGHT_GRAY,game);
            drawTile(10,i,SQUARE,Color.LIGHT_GRAY,game);
            drawTile(19,i,SQUARE,Color.LIGHT_GRAY,game);

            drawTile(0,i,LEFT,Color.WHITE,game);
        }
        for(int i=0; i<11; i++) {
            drawTile(0,i,LEFT,Color.WHITE,game);
            drawTile(19,i,RIGHT,Color.DARK_GRAY,game);
        }
        //Highlights
        //View
        for(int x=1; x<10;x++) {
            drawTile(x,0,BOTTOM,Color.DARK_GRAY,game);
            drawTile(x,10,TOP,Color.WHITE,game);
            drawTile(0,x,RIGHT,Color.DARK_GRAY,game);
            drawTile(10,x,LEFT,Color.WHITE,game);
        }
        //Inventory
        for(int x=11; x<19;x++) {
            drawTile(x,0,BOTTOM,Color.DARK_GRAY,game);
            drawTile(x,4,TOP,Color.WHITE,game);
        }
        for(int i=1;i<4; i++) {
            drawTile(10,i,RIGHT,Color.DARK_GRAY,game);
            drawTile(19,i,LEFT,Color.WHITE,game);
        }
        //Status
        for(int x=11; x<19;x++) {
            drawTile(x,4,BOTTOM,Color.DARK_GRAY,game);
            drawTile(x,7,TOP,Color.WHITE,game);
        }
        for(int i=5;i<7; i++) {
            drawTile(10,i,RIGHT,Color.DARK_GRAY,game);
            drawTile(19,i,LEFT,Color.WHITE,game);
        }
        //Spells
        for(int x=11; x<19;x++) {
            drawTile(x,7,BOTTOM,Color.DARK_GRAY,game);
            drawTile(x,10,TOP,Color.WHITE,game);
        }
        for(int i=8;i<10; i++) {
            drawTile(10,i,RIGHT,Color.DARK_GRAY,game);
            drawTile(19,i,LEFT,Color.WHITE,game);
        }
    }
    */
}
