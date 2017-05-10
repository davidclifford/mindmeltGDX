package mindmelt.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Button;
import mindmelt.game.gui.Window;
import mindmelt.game.maps.World;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;
import mindmelt.game.objects.ObjectStore;
import mindmelt.game.windows.MainWindow;
import mindmelt.game.windows.ViewWindow;

import java.util.Random;

/**
 * Created by David on 27/04/2017.
 */
public class PlayScreen  implements Screen, InputProcessor {
    private MindmeltGDX game;
    private Batch batch;
    private BitmapFont font;
    private boolean exitGame;
    private Random rand;

    private Window window;
    private Window spellWindow;
    private Window viewWindow;
    private Window backPackWindow;
    private Window stausWindow;
    private Window messageWindow;
    private Button button;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;


    public PlayScreen(MindmeltGDX game) {
        this.game = game;
        this.batch = game.batch;
        this.font = game.font;
        exitGame = false;
        Gdx.input.setInputProcessor(this);

        window = (MainWindow) new MainWindow(0,0,20,15).setName("Main");
        viewWindow = (ViewWindow) new ViewWindow(1,1,9,9).setName("View");
        backPackWindow = (Window) new Window(11,1, 8, 3).setName("Backpack");
        stausWindow = (Window) new Window(11,5, 8, 2).setName("Status");
        spellWindow = (Window) new Window(11,8,7,2).setName("Spells");
        messageWindow = (Window) new Window(0,11,20,4).setName("Messages");

        window.addElement(spellWindow);
        window.addElement(viewWindow);
        window.addElement(backPackWindow);
        window.addElement(stausWindow);
        window.addElement(messageWindow);

        for (int i=0;i<14;i++) {
            button = (Button) new Button(i%7, i/7, i+135).setName(String.format("Spell %d",i+1));
            if (i<3) button.setState(Button.UP);
            spellWindow.addElement(button);
            button = null;
        }

        game.objects = new ObjectStore();
        game.objects.loadObjects("initial");

        game.world = new World();
        game.world.loadMap("world");

        game.objects.initMap(game.world);
        game.player = (ObjPlayer) game.objects.getPlayer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        boolean ready = true;
        int px = game.player.getX();
        int py = game.player.getY();
        int pz = game.player.getZ();
        if (up||down||right||left) ready = game.player.isReady(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rand = new Random(412294L);

        if (left && ready) game.player.moveToMap(px-1,py,pz,game.world,game.objects);
        if (right && ready) game.player.moveToMap(px+1,py,pz,game.world,game.objects);
        if (up && ready) game.player.moveToMap(px,py-1,pz,game.world,game.objects);
        if (down && ready) game.player.moveToMap(px,py+1,pz,game.world,game.objects);

        batch.begin();
          window.render(game,delta);
          font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, 16);
          font.draw(batch, "wait: "+game.player.getWait(),0,32);
          font.draw(batch, "speed: "+game.player.getSpeed(),0,48);
        batch.end();

        if(exitGame) {
            game.setScreen(new StartScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                exitGame = true;
                break;
            case Input.Keys.LEFT:
                left = true;
                break;
            case Input.Keys.RIGHT:
                right = true;
                break;
            case Input.Keys.UP:
                up = true;
                break;
            case Input.Keys.DOWN:
                down = true;
                break;

        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        up = false;
        down = false;
        right = false;
        left = false;
        game.player.setWait(game.player.getSpeed());
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        window.click(screenX,screenY,game);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


}
