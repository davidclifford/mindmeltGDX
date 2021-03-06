package mindmelt.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import mindmelt.game.actions.*;
import mindmelt.game.MindmeltGDX;
import mindmelt.game.buttons.*;
import mindmelt.game.engine.Engine;
import mindmelt.game.gui.Button;
import mindmelt.game.gui.GuiElem;
import mindmelt.game.gui.Window;
import mindmelt.game.maps.TileType;
import mindmelt.game.maps.World;
import mindmelt.game.objects.Obj;
import mindmelt.game.objects.ObjPlayer;
import mindmelt.game.objects.ObjectStore;
import mindmelt.game.spells.*;
import mindmelt.game.windows.*;

import java.util.List;
import java.util.Random;

/**
 * Created by David on 27/04/2017.
 */
public class PlayScreen implements Screen, InputProcessor {
    private MindmeltGDX game;
    private Engine engine;
    private Batch batch;
    private BitmapFont font;
    private boolean exitGame;
    private boolean wonGame;
    private Random rand;
    private Sound wilhelm;
    private long mouseWait;

    private Window window;
    private Window spellWindow;
    private Window viewWindow;
    private Window adjacentWindow;
    private Window backPackWindow;
    private Window statusWindow;
    private Window journalWindow;
    private Window throwWindowT;
    private Window throwWindowR;
    private Window throwWindowB;
    private Window throwWindowL;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    private TextureData tex;
    private Pixmap pix;
    private Pixmap newMouse;
    private int lastMouse;

    public PlayScreen(MindmeltGDX game, String status) {

        tex = game.tileImages.getTextureData();
        tex.prepare();
        pix = tex.consumePixmap();

        this.game = game;
        this.batch = game.batch;
        this.font = game.font;
        exitGame = false;
        wonGame = false;
        Gdx.input.setInputProcessor(this);

        window = (MainWindow) new MainWindow(0,0,20,15).setName("Main window");
        viewWindow = (ViewWindow) new ViewWindow(1,1,9,9).setName("View area");
        backPackWindow = (BackpackWindow) new BackpackWindow(11,1, 8, 3).setName("Backpack window");
        statusWindow = (Window) new StatusWindow(11,5, 8, 2).setName("Status window");
        spellWindow = (SpellWindow) new SpellWindow(11,8,7,2).setName("Spell window");
        journalWindow = (JournalWindow) new JournalWindow(0,11,20,4).setName("Journal window");
        adjacentWindow = (AdjacentWindow) new AdjacentWindow(3,3,3,3).setName("Adjacent area");
        throwWindowT = (ThrowWindow) new ThrowWindow(2,0,5,2, 0, -1).setName("Throw top area");
        throwWindowR = (ThrowWindow) new ThrowWindow(7,2,2,5, 1, 0).setName("Throw right area");
        throwWindowB = (ThrowWindow) new ThrowWindow(2,7,5,2, 0, 1).setName("Throw bottom area");
        throwWindowL = (ThrowWindow) new ThrowWindow(0,2,2,5, -1, 0).setName("Throw left area");

        window.addElement(spellWindow);
        window.addElement(viewWindow);
        window.addElement(backPackWindow);
        window.addElement(statusWindow);
        window.addElement(journalWindow);
        viewWindow.addElement(adjacentWindow);
        viewWindow.addElement(throwWindowT);
        viewWindow.addElement(throwWindowR);
        viewWindow.addElement(throwWindowL);
        viewWindow.addElement(throwWindowB);

        setupJournalButtons();

        //System.out.println(String.format("Game = %s",status));

        if (game.objects!=null && status.equals("save")) {
            game.objects.saveObjects("saved");
            game.player.saveStatus(game.engine, "saved");
            game.engine.saveChangeTiles("saved");
            game.engine.getJournal().saveJournal("saved");
        }

        if (status.equals("load") || status.equals("save")) {
            game.objects = new ObjectStore();
            game.objects.loadObjects("saved");

            game.player = (ObjPlayer) game.objects.getPlayer();
            setSpellButtons(game.player, false);

            game.world = new World();
            engine = new Engine(game);
            game.engine = engine;

            String mapName = game.player.loadStatus(game.engine,"saved");
            game.world.loadMap(mapName);
            game.engine.loadChangeTiles("saved");
            game.engine.overlay();
            game.objects.initMap(game.world);
            game.player.initInventory();
            engine.getJournal().loadJournal( "saved");
        } else { //new game
            game.objects = new ObjectStore();
            //game.objects.convertObjects("OBJ.DAT","initial.obj");
            game.objects.loadObjects("initial");

            game.world = new World();
            game.world.loadMap("world");

            game.objects.initMap(game.world);
            game.player = (ObjPlayer) game.objects.getPlayer();

            setSpellButtons(game.player, true); //default until game loaded
            engine = new Engine(game);
            game.engine = engine;

            engine.getJournal().loadJournal("initial");
        }

        setMouse(182);
        engine.setPlayerWait();
        //mouseWait = game.engine.getSystemTime()+5000000;
    }

    private void setupJournalButtons(){
        journalWindow.addElement(new JournalButton(19,0,131,"Go To Top ", new JournalTopAction()));
        journalWindow.addElement(new JournalButton(19,1,130,"Scroll Up", new JournalUpAction()));
        journalWindow.addElement(new JournalButton(19,2,133,"Scroll Down", new JournalDownAction()));
        journalWindow.addElement(new JournalButton(19,3,132,"Go To Bottom", new JournalBottomAction()));
    }

    private void setSpellButtons(ObjPlayer player, boolean isNew) {
        //Set up spell buttons
        SpellButton button = null;
        Spell spell = null;
        for (int b=0;b<14;b++) {
            if (b==0) button = new SinglePressSpellButton(spell=new MindMeltSpell(),b%7, b/7, b+135, "Mindmelt spell");
            else if (b==1) button = new SinglePressSpellButton(spell=new CircleSpell(),b%7, b/7, b+135, "Circle teleport spell");
            else if (b==2) button = new SinglePressSpellButton(spell=new DirectionSpell(),b%7, b/7, b+135, "Direction spell");
            else if (b==3) button = new SinglePressSpellButton(spell=new CoordsSpell(),b%7, b/7, b+135, "Coords spell");
            else if (b==4) button = new TimedSpellButton(spell=new LightSpell(),b%7, b/7, b+135, "Light spell");
            else if (b==5) button = new TimedSpellButton(spell=new XraySpell(),b%7, b/7, b+135, "Xray spell");
            else if (b==6) button = new TimedSpellButton(spell=new WaterSpell(),b%7, b/7, b+135, "Water walk spell");
            else if (b==7) button = new TimedSpellButton(spell=new StunSpell(),b%7, b/7, b+135, "Stun spell");
            else if (b==8) button = new SinglePressSpellButton(spell=new JumpSpell(),b%7, b/7, b+135, "Jump teleport spell");
            else if (b==9) button = new TimedSpellButton(spell=new ZapSpell(),b%7, b/7, b+135, "Zap spell");
            else if (b==10) button = new SinglePressSpellButton(spell=new HealthSpell(),b%7, b/7, b+135, "Health spell");
            else if (b==11) button = new TimedSpellButton(spell=new ForceFieldSpell(),b%7, b/7, b+135, "Forcefield walk spell");
            else if (b==12) button = new ToggleSpellButton(spell=new BackSpell(),b%7, b/7, b+135, "Back teleport spell");
            else if (b==13) button = new ToggleSpellButton(spell=new MapSpell(),b%7, b/7, b+135,"Map spell");
            spellWindow.addElement(button);
            if (isNew) {
                spell.setLearned(b<2);
            }
            player.addSpell(spell);
        }
    }

    private void setMouse(int sprite) {
        int px = (sprite%20)*GuiElem.SZ;
        int py = (sprite/20)*GuiElem.SZ;

        newMouse = new Pixmap(GuiElem.SZ,GuiElem.SZ, Pixmap.Format.RGBA8888);
        for (int y=0; y<GuiElem.SZ; y++) {
            for (int x=0; x<GuiElem.SZ; x++) {
                newMouse.drawPixel(x,y,pix.getPixel(px+x,py+y));
            }
        }
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(newMouse,GuiElem.SZ/2,GuiElem.SZ/2));
        newMouse.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updatePlayer(delta);
        updateObjects(delta);
        engine.runTriggers();
        engine.expireMessages();
        updateSpells();

        batch.begin();
          updateMouse();
          window.render(game,delta);
        batch.end();

        if(exitGame) {
            game.setScreen(new StartScreen(game));
            dispose();
        } else if(wonGame) {
            game.setScreen(new EndScreen(game));
        }
    }

    private void updateSpells() {
        engine.getPlayer().getSpells().forEach(sp -> sp.update(engine));
    }

    private void updateMouse() {
        Obj handObject = engine.getPlayerHandObject();
        if(handObject!=null && handObject.getIcon()!=lastMouse) {
            setMouse(handObject.getIcon());
            lastMouse = handObject.getIcon();
        } else if (handObject==null && lastMouse!=0) {
            lastMouse = 0;
            setMouse(182);
        }
    }

    private void updatePlayer(float delta) {
        int px = engine.getPlayerX();
        int py = engine.getPlayerY();
        int pz = engine.getPlayerZ();
        int dir = engine.getPlayerDirection();
        int ox = px;
        int oy = py;
        int oz = pz;
        int a[] = {1,0,-1,0};
        int b[] = {0,-1,0,1};

        if ((up||down||right||left) && engine.isPlayerReady(engine)) {
            if (up && !(right||left)) {
                px-=b[dir];
                py-=a[dir];
            }
            if (down && !(right||left)) {
                px+=b[dir];
                py+=a[dir];
            }
            if (right) {
                px+=a[dir];
                py-=b[dir];
            }
            if (left) {
                px-=a[dir];
                py+=b[dir];
            }
            Obj topOb=engine.getTopObject(px,py,pz);
            TileType tile = engine.getTile(px,py,pz);
            if (engine.isCheat() || (engine.canEnter(game.player,px,py,pz) && (topOb==null || !topOb.isPlayerBlocked()))) {
                engine.moveObjToMap(game.player,px, py, pz);
                if (topOb!=null && (topOb.isPerson() || topOb.isAnimal())) {
                    engine.moveObjToMap(topOb,ox,oy,oz);
                    topOb.resetWait(engine);
                }
                game.player.resetWait(engine);
            } else if (tile==TileType.door){
                engine.activateTile(px,py,pz);
            }
        }
        wonGame = game.player.wonGame();
    }

    private void updateObjects(float delta) {
        List<Obj> objects =  game.objects.getActiveObjects();
        objects.forEach(obj -> obj.update(game.engine,delta));
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
        pix.dispose();
        tex.disposePixmap();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (engine.getTalking().isTalking()) {
            return talkInput(keycode);
        } else {
            return moveInput(keycode);
        }
    }

    private boolean talkInput(int keycode) {
        ObjPlayer player = engine.getPlayer();
        switch (keycode) {
            case Input.Keys.ESCAPE:
                player.stopTalking(engine);
                break;
            default:
                player.talkInput(engine, keycode);
                break;
        }
        return true;
    }

    private boolean moveInput(int keycode) {
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
            case Input.Keys.PAGE_DOWN:
                game.player.rotate(1);
                break;
            case Input.Keys.PAGE_UP:
                game.player.rotate(-1);
                break;
            case Input.Keys.SPACE:
                wilhelm.play();
                break;
            case Input.Keys.E:
                engine.doEntryExit(engine.getPlayerX(),engine.getPlayerY(),engine.getPlayerZ());
                break;
            case Input.Keys.P:
                Gdx.app.log("Coords: ",String.format("%d,%d,%d",engine.getPlayerX(),engine.getPlayerY(),engine.getPlayerZ()));
                break;
            case Input.Keys.F1:
                engine.setCheat(!engine.isCheat());
                break;
            case Input.Keys.F2:
                engine.setSeeall(!engine.isSeeall());
                break;
            case Input.Keys.F3:
                engine.setXray(!engine.isXray());
                break;
            case Input.Keys.F4:
                engine.getPlayer().levelUp(engine);
                break;
            case Input.Keys.F5:
                engine.setDebugView(!engine.isDebugView());
                break;
            case Input.Keys.T:
                engine.talkAdjacent();
                break;
            case Input.Keys.COMMA:
                engine.getJournal().scrollUp();
                break;
            case Input.Keys.PERIOD:
                engine.getJournal().scrollDown();
                break;
            default:
                Gdx.app.log("Key = ",""+keycode);
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
        window.click(screenX, screenY, engine);
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
        window.toolTip(screenX, screenY, engine);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


}
