package com.martinimoe.projektkarpador;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.ShortArray;

public class Terrain {
	private PolygonRegion pRegion;
	private float terrainWidth = 0;
	Body body = null;
	
	public Terrain(float width, GameContext gameContext) {
		this.terrainWidth = width;
		
		// Landscape generieren. Hochkompliziert!
		float[] vertices = new float[(int) (terrainWidth/100)];
		float[] metricVertices = new float[vertices.length];
		// Der erste Punkt ist am Koordinatenursprung
		vertices[0] = 0;
		vertices[1] = 0;
		metricVertices[0] = 0;
		metricVertices[1] = 0;
		
		for (int i = 2; i < vertices.length-2; i++) {
			// Jede Zweite Koordinate ist x-Koordinate
			// Diese sollen gleichmäßig verteilt sein.
			if(i%2 == 0){
				vertices[i] = i * ((terrainWidth / vertices.length) * Config.SCREENSPERLEVEL);
			}
			// Sonst Höhe zufällig berechnen
			else{
				vertices[i] = MathUtils.random(100, 600);
			}
			metricVertices[i] = vertices[i]/Config.PIXELSPERMETER;
		}
		// Der letzte ist punkt ist an ( Levelende | 0 )
		vertices[vertices.length-2] = terrainWidth * Config.SCREENSPERLEVEL;
		vertices[vertices.length-1] = 0;
		metricVertices[metricVertices.length-2] = vertices[vertices.length-2]/Config.PIXELSPERMETER;
		metricVertices[metricVertices.length-1] = vertices[vertices.length-1]/Config.PIXELSPERMETER;
		
		// Der ECT berechnet aus den koordinaten die polygone, danach wird konvertiert
		EarClippingTriangulator ect = new EarClippingTriangulator();
		
		//DelaunayTriangulator dt = new DelaunayTriangulator();
		ShortArray sa = ect.computeTriangles(vertices);
		// sa = dt.computeTriangles(vertices, true);
		
		Texture texture = new Texture("terrain.png");
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		// Die PolygonRegion enthält den Levelboden
		pRegion = new PolygonRegion(new TextureRegion(texture), vertices,  sa.toArray());
		
		//Box2d Physik-Dingsi
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(0, 0);
		body = gameContext.getWorld().createBody(bd);
//		PolygonShape pShape = new PolygonShape();
		ChainShape cShape = new ChainShape();
//		pShape.set(metricVertices);
		cShape.createChain(metricVertices);
		FixtureDef fDef = new FixtureDef();
		fDef.shape = cShape;
		Fixture f = body.createFixture(fDef);
		f.setUserData(this);
	}
	
	public void draw(PolygonSpriteBatch pBatch) {
		pBatch.draw(pRegion, 0, 0);
	}
}
