package actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.martinimoe.projektkarpador.GameContext;

public class Button extends com.badlogic.gdx.scenes.scene2d.ui.Button{

	TextureRegion tex = null;
	
	public Button(GameContext gameContext, String texture) {
		super();
		tex = gameContext.getAtlas().findRegion(texture);
		setWidth(tex.getRegionWidth()*2);
		setHeight(tex.getRegionHeight()*2);
	}
	

	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(tex, getX(), getY(), 0, 0, getWidth(), getHeight(), 1, 1, 0);
	}
	

	
}
