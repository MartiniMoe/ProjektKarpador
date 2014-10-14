package com.martinimoe.projektkarpador;

import gamestates.Game;
import gamestates.GameState;
import gamestates.MenuMain;
import actors.Fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameContext {
	private World world = null;
	private TextureAtlas atlas = null;
	private float timeElapsed = 0;
	private Stage stage = null;
	private Fish fish = null;
	private boolean mute = false;
	
	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

	private Game game;
	private MenuMain menuMain;
	
	//0 = Menu, 1 = Spiel
	private GameState gameState = null;

	public ApplicationAdapter getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		Gdx.input.setInputProcessor(gameState.getStage());
		System.out.println(gameState.getClass());
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

	public GameContext(World world, TextureAtlas atlas) {
		setWorld(world);
		setAtlas(atlas);
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
	
	
	
	
}
