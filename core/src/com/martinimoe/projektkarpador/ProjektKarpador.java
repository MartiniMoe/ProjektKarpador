package com.martinimoe.projektkarpador;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class ProjektKarpador extends ApplicationAdapter {
	SpriteBatch batch;
	AtlasRegion grassLeft;
	AtlasRegion grassMiddle;
	AtlasRegion grassRight;
	AtlasRegion playerStanding;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("KarpadorAtlas.pack"));
		grassLeft = atlas.findRegion("Tiles/grassLeft");
		grassMiddle = atlas.findRegion("Tiles/grassMid");
		grassRight = atlas.findRegion("Tiles/grassRight");
		playerStanding = atlas.findRegion("Player/p1_stand");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(grassLeft, 0, 0);
		batch.draw(grassMiddle, 70, 0);
		batch.draw(grassRight, 140, 0);
		batch.draw(playerStanding, 20, 70);
		batch.end();
		
	}
}