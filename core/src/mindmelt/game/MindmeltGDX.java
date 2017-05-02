package mindmelt.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import mindmelt.game.screens.StartScreen;

public class MindmeltGDX extends Game {
	public AssetManager manager;
	public SpriteBatch batch;
	public Texture tileImages, startScreen;
    public TextureRegion[][] tile;
	public Skin skin;
	public BitmapFont font;
	
	@Override
	public void create () {
        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("theme.ogg", Music.class);
		manager.load("uiskin.json", Skin.class);
		manager.finishLoading();

		tileImages = new Texture("tiles.png");
		tile = TextureRegion.split(tileImages, 32, 32);

		startScreen = new Texture("start.bmp");
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
        System.exit(0);
    }

    public TextureRegion getTile(int tileId) {
	    int i = tileId/20;
	    int j = tileId%20;
	    return tile[i][j];
    }
}
