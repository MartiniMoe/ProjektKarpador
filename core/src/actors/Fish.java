package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.martinimoe.projektkarpador.Config;
import com.martinimoe.projektkarpador.GameContext;

public class Fish extends Actor{
	private Animation flounder = null;
	private float elapsed = 0;
	private Body body = null;
	private Action bouncing;
	
	public Fish(GameContext gameContext, float x, float y){
		setX(x);
		setY(y);
		
		// Textur laden
		Array<AtlasRegion> frames = gameContext.getAtlas().findRegions("Fisch/Fisch");
		flounder = new Animation(0.05f,frames);
		// Fischgröße aus Textur generieren
		setWidth(frames.get(0).getRegionWidth());
		setHeight(frames.get(0).getRegionHeight());
		
		// Box2d driss
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(((getX()-getWidth())/2)/Config.PIXELSPERMETER, (getY()+getHeight()/2)/Config.PIXELSPERMETER);
		body = gameContext.getWorld().createBody(bd);
		CircleShape circle = new CircleShape();
		circle.setRadius((getHeight()/2)/Config.PIXELSPERMETER);
		
		FixtureDef fDef = new FixtureDef();
		fDef.friction =.25f;
		fDef.restitution = .5f;
		fDef.density = 1;
		fDef.shape = circle;
		
		body.createFixture(fDef);
		
		bouncing = new Action() {
			private float timeElapsed;
			@Override
			public boolean act(float delta) {
				timeElapsed += delta;
				if (timeElapsed >= 1f)
				{
					body.applyForceToCenter(0, 200, true);
					timeElapsed = 0 ;
				}
				
				
				return false;
			}
		};
		
		addAction(bouncing);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		setX(body.getPosition().x*Config.PIXELSPERMETER-getWidth()/2);
		setY(body.getPosition().y*Config.PIXELSPERMETER-getHeight()/1.2f);
		batch.draw(flounder.getKeyFrame(elapsed, true),
					getX(),
					getY(),
					getWidth()/2,
					getHeight()/2,
					getWidth(),
					getHeight(),
					1f,
					1f,
					MathUtils.radDeg*body.getAngle()
					);
		elapsed += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

	public void move(int i) {
		body.applyForceToCenter(i*100, 0, true);
	}
	

}
