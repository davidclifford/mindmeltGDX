package mindmelt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mindmelt.game.screens.PlayScreen;
import mindmelt.game.screens.StartScreen;

public class MindmeltGDX extends Game {
	public AssetManager manager;
	public SpriteBatch batch;
	public Texture img, startScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("theme.ogg", Music.class);
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
		batch.dispose();
		img.dispose();
		manager.dispose();
	}
}
