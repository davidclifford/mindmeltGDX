package mindmelt.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import mindmelt.game.screens.StartScreen;

public class MindmeltGDX extends Game {
	public AssetManager manager;
	public SpriteBatch batch;
	public Texture tileImages, startScreen, mainWindow;
    public TextureRegion[][] tile;
	public Skin skin;
	public BitmapFont font;
	
	@Override
	public void create () {
        font = new BitmapFont(Gdx.files.internal("skin/courier.fnt"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("sound/theme.ogg", Music.class);
		manager.load("sound/wilhelm.ogg", Sound.class);
		manager.finishLoading();

		tileImages = new Texture("image/tiles.png");
		tile = TextureRegion.split(tileImages, 32, 32);

		Pixmap px = new Pixmap(Gdx.files.internal("image/pointer.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(px,0,0));

		mainWindow = new Texture("image/mainWindow.png");
		startScreen = new Texture("image/start.bmp");
		setScreen(new StartScreen(this));
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		super.dispose();
//		batch.dispose();
		tileImages.dispose();
		manager.dispose();
	}

	public void exit() {
	    dispose();
	    Gdx.app.exit();
    }

    public TextureRegion getTile(int tileId) {
	    int i = tileId/20;
	    int j = tileId%20;
	    return tile[i][j];
    }
}
