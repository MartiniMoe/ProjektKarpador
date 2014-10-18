package actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public class House extends Decoration {

	private Body body;
	private boolean active;
	
	public House(GameContext gameContext, float x , float y) {
		super(gameContext, x, y, "Terrain/haus");
		setScale(4);
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.StaticBody;
		bDef.position.set(x/Config.PIXELSPERMETER, y/Config.PIXELSPERMETER);
		body = gameContext.getWorld().createBody(bDef);
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(((getWidth()*getScaleX())/2)/Config.PIXELSPERMETER, ((getHeight()*getScaleY())/2)/Config.PIXELSPERMETER);
		FixtureDef fDef = new FixtureDef();
		fDef.shape = pShape;
		Fixture f = body.createFixture(fDef);
		f.setUserData(this);
		body.setActive(false);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		body.setActive(active);
		this.active = active;
	}
	
	
	

}
