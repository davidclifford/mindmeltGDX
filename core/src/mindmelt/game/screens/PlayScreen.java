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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rand = new Random(412294L);
        batch.begin();

        window.render(game,delta);
//        for (int i=0; i<10000; i++) {
//            int id = rand.nextInt(183)+1;
//            int x = rand.nextInt(20);
//            int y = rand.nextInt(16);
//            batch.draw(game.getTile(id), x * 32, Gdx.graphics.getHeight() - y * 32);
//        }
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, 16);

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
        if(keycode==Input.Keys.ESCAPE)
            exitGame = true;
        if(keycode==Input.Keys.LEFT) game.player.setX(game.player.getX()-1);
        if(keycode==Input.Keys.RIGHT) game.player.setX(game.player.getX()+1);
        if(keycode==Input.Keys.UP) game.player.setY(game.player.getY()-1);
        if(keycode==Input.Keys.DOWN) game.player.setY(game.player.getY()+1);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
