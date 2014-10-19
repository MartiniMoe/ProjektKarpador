package actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.martinimoe.projektkarpador.GameContext;

public class Decoration extends Actor{

	private GameContext gameContext;
	private TextureRegion texture;
	
	public Decoration(GameContext gameContext, float x, float y, String texture, float w, float h) {
		super();
		
		this.gameContext = gameContext;
		this.texture = gameContext.getAtlas().findRegion(texture);
		
		
		setX(x);
		setY(y);
		setWidth(this.texture.getRegionWidth());
		setHeight(this.texture.getRegionHeight());
		if (w > 0 || h >0)
		{
			setWidth(w);
			setHeight(h);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		batch.draw(texture, getX(), getY(), texture.getRegionWidth()/2, texture.getRegionHeight()/2, getWidth(), getHeight(), getScaleX(), getScaleY(), 0);
	}
	
	
}
