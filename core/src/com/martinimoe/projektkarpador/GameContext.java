package com.martinimoe.projektkarpador;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;

public class GameContext {
	private World world = null;
	private TextureAtlas atlas = null;

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
}
