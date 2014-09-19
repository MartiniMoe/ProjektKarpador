package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public class Fish extends Actor{
	private Animation flounder = null;
	private float elapsed = 0;
	private Body body = null;
	
	public Fish(GameContext gameContext, float x, float y){
		setX(x);
		setY(y);
		
		Array<AtlasRegion> frames = gameContext.getAtlas().findRegions("Fisch/Fisch");
		flounder = new Animation(0.05f,frames);
		setWidth(frames.get(0).packedWidth);
		setHeight(frames.get(0).packedHeight);
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(((getX()+getWidth())/2)/Config.PIXELSPERMETER, (getY()+getHeight()/2)/Config.PIXELSPERMETER);
		body = gameContext.getWorld().createBody(bd);
		CircleShape circle = new CircleShape();
		circle.setRadius((getHeight()/2)/Config.PIXELSPERMETER);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = circle;
		
		body.createFixture(fDef);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(flounder.getKeyFrame(elapsed, true), body.getPosition().x*Config.PIXELSPERMETER, body.getPosition().y*Config.PIXELSPERMETER);
		elapsed += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
}
