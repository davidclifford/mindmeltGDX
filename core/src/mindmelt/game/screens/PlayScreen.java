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
    private World world;

    private Window window;
    private Window subWindow;
    private Button button;

    public PlayScreen(MindmeltGDX game) {
        this.game = game;
        this.batch = game.batch;
        this.font = game.font;
        exitGame = false;
        Gdx.input.setInputProcessor(this);

        window = (Window) new Window(0,0,640,480).setName("Main");
        subWindow = (Window) new Window(100,100,120,80).setName("Sub");
        window.addElement(subWindow);
        button = (Button) new Button(40,40, 32, 32).setName("Button");
        button.setUpIcon(140);
        subWindow.addElement(button);

        world = new World();
        world.loadMap("world");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rand = new Random(412294L);
        batch.begin();

        window.render(batch,game);
//        for (int i=0; i<10000; i++) {
//            int id = rand.nextInt(183)+1;
//            int x = rand.nextInt(20);
//            int y = rand.nextInt(16);
//            batch.draw(game.getTile(id), x * 32, Gdx.graphics.getHeight() - y * 32);
//        }
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, 40);

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
        window.click(screenX,screenY);
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
