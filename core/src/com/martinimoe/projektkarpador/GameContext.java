package com.martinimoe.projektkarpador;

import gamestates.Game;
import gamestates.GameState;
import gamestates.MenuMain;
import actors.Cat;
import actors.Fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameContext {
	private World world = null;
	private TextureAtlas atlas = null;
	private float timeElapsed = 0;
	private Stage stage = null;
	private Fish fish = null;
	private Music music, intro = null;
	private Game game;
	private MenuMain menuMain;
	private GameState gameState = null;
	private boolean muted = false;
	private Cat cat = null;

	public void playIntro() {
		intro.play();
	}
	
	public Music getIntro() {
		return intro;
	}
	
	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public boolean toggleMusic() {
		if (music.isPlaying()) music.stop();
		else music.play();
		
		return music.isPlaying();
	}
	
	public void startMusic() {
		if (!music.isPlaying()) music.play();
	}
	
	public void stopMusic() {
		if (music.isPlaying()) music.stop();
	}
	
	public ApplicationAdapter getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		Gdx.input.setInputProcessor(gameState.getStage());
		this.gameState.update();
	}

	public MenuMain getMenuMain() {
		return menuMain;
	}

	public void setMenuMain(MenuMain menuMain) {
		this.menuMain = menuMain;
	}

	public Fish getFish() {
		return fish;
	}

	public void setFish(Fish fish) {
		this.fish = fish;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public GameContext(World world, TextureAtlas atlas, Music music, Music intro) {
		setWorld(world);
		setAtlas(atlas);
		this.music = music;
		this.intro = intro;
	}
	
	public TextureAtlas getAtlas() {
		return atlas;
	}

	public void setAtlas(TextureAtlas atlas) {
		this.atlas = atlas;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void addDelta(float delta)
	{
		this.timeElapsed+=delta;
	}

	public float getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(float timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Cat getCat() {
		return cat;
	}

	public void setCat(Cat cat) {
		this.cat = cat;
		stage.addActor(cat);
	}
	
	
	
	
}
