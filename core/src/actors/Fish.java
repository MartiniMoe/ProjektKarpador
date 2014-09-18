package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Fish extends Actor{
	private Animation flounder;
	private float elapsed = 0;
	
	public Fish(TextureAtlas atlas){
		Array<AtlasRegion> frames = atlas.findRegions("Fisch/Fisch");
		flounder = new Animation(0.05f,frames);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(flounder.getKeyFrame(elapsed, true), 100, 100);
		elapsed += Gdx.graphics.getDeltaTime();
	}
}
