package mindmelt.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import mindmelt.game.screens.StartScreen;

public class MindmeltGDX extends Game {
	public AssetManager manager;
	public SpriteBatch batch;
	public Texture img, startScreen;
	public Stage stage;
	public Skin skin;
	
	@Override
	public void create () {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("theme.ogg", Music.class);
		manager.load("uiskin.json", Skin.class);
		manager.finishLoading();

		img = new Texture("badlogic.jpg");
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
		img.dispose();
		manager.dispose();
	}

	public void exit() {
	    dispose();
        System.exit(0);
    }
}
