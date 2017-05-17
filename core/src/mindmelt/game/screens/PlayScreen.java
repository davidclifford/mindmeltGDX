package mindmelt.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.gui.Button;
import mindmelt.game.gui.Window;
import mindmelt.game.maps.EntryExit;
import mindmelt.game.maps.TileType;
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
    private Sound wilhelm;

    private Window window;
    private Window spellWindow;
    private Window viewWindow;
    private Window adjacentWindow;
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
        adjacentWindow = (Window) new Window(3,3,3,3).setName("Adjacent");

        window.addElement(spellWindow);
        window.addElement(viewWindow);
        window.addElement(backPackWindow);
        window.addElement(stausWindow);
        window.addElement(messageWindow);
        viewWindow.addElement(adjacentWindow);

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

        wilhelm = game.manager.get("sound/wilhelm.ogg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updatePlayer(delta);


        batch.begin();
        window.render(game,delta);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, 16);
        //batch.draw(game.getTile(103),Gdx.input.getX(),448-Gdx.input.getY());
        batch.end();

        if(exitGame) {
            game.setScreen(new StartScreen(game));
            dispose();
        }
    }

    private void updatePlayer(float delta) {
        int px = game.player.getX();
        int py = game.player.getY();
        int pz = game.player.getZ();

        if ((up||down||right||left) && game.player.isReady(delta)) {
            if (up) py--;
            if (down) py++;
            if (right) px++;
            if (left) px--;
            if (game.world.canEnter(game.player,px,py,pz)) {
                game.player.moveToMap(px, py, pz, game.world, game.objects);
            } else {
                activateTile(px,py,pz);
            }
        }
    }

    private void activateTile(int x, int y, int z) {
        int tile = game.world.getTile(x, y, z).getId();

        if (tile == TileType.door.getId()) {
            game.world.changeTile(x, y, z, TileType.opendoor);
        } else if (tile == TileType.opendoor.getId()) {
            game.world.changeTile(x, y, z, TileType.door);
        } else if (tile == TileType.lockeddoor.getId()) {
            game.world.changeTile(x, y, z, TileType.openlockeddoor);
        } else if (tile == TileType.openlockeddoor.getId()) {
            game.world.changeTile(x, y, z, TileType.lockeddoor);
        } else if (tile == TileType.gate.getId()) {
            game.world.changeTile(x, y, z, TileType.openlockedgate);
        } else if (tile == TileType.openlockedgate.getId()) {
            game.world.changeTile(x, y, z, TileType.gate);
        } else if (tile == TileType.uplever.getId()) {
            game.world.changeTile(x, y, z, TileType.downlever);
        } else if (tile == TileType.downlever.getId()) {
            game.world.changeTile(x, y, z, TileType.uplever);
        }
    }

    private void enterExit(int x, int y, int z) {
        if(game.world.isEntryExit(x, y, z)) {
            if(game.player.isAt(x,y,z)) {
                doEntryExit(x, y, z);
            } else {
                EntryExit entry = game.world.getEntryExit(x, y, z);
                game.player.setMessage(entry.getDescription());
            }
        }
    }

    public void doEntryExit(int x, int y, int z) {
        EntryExit entry = game.world.getEntryExit(x, y, z);
        if (entry!=null) {
            if(!entry.getToMap().equals(game.world.getFilename())) {
                game.world = new World();
                game.world.loadMap(entry.getToMap());
                game.objects.initMap(game.world);
            }
            game.objects.getPlayer().moveToMap(entry.getToX(),entry.getToY(),entry.getToZ(), game.world, game.objects);
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
            case Input.Keys.SPACE:
                wilhelm.play();
                break;
            case Input.Keys.E:
                enterExit(game.player.getX(),game.player.getY(),game.player.getZ());
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
