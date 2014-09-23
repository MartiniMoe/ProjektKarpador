package com.martinimoe.projektkarpador;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;

public class GameContext {
	private World world = null;
	private TextureAtlas atlas = null;
	private float timeElapsed = 0;

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
	
	
	
	
}
