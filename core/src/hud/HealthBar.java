package hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.martinimoe.projektkarpador.GameContext;

public class HealthBar extends Actor {

	private TextureRegion txHealthbar = null;
	private GameContext gameContext;
	public static final Color COLOR_BACKGROUND = new Color(.5f,.5f,.5f,1f);
	public static final Color COLOR_FOREGROUND = new Color(.1f,.9f,.1f,1f);
	
	
	public HealthBar(GameContext gameContext, float x, float y) {
		this.gameContext = gameContext;
		txHealthbar = gameContext.getAtlas().findRegion("Terrain/terrain_gras");
		setWidth(gameContext.getFish().getHealth());
		setPosition(x-getWidth()*2, y);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.setColor(COLOR_BACKGROUND);
		batch.draw(txHealthbar, getX(), getY(), 0, 0, gameContext.getFish().getWidth()*2, 64, 1, 1, 0);
		batch.setColor(COLOR_FOREGROUND);
		batch.draw(txHealthbar, getX(), getY(), 0, 0, gameContext.getFish().getHealth()*2, 64, 1, 1, 0);
	}
	
	
	

}
