package com.martinimoe.projektkarpador;

import actors.Cat;
import actors.Decoration;
import actors.EvilCrab;

import com.badlogic.gdx.graphics.Color;
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
	private PolygonRegion pRegion, pRegionGrass;
	private float terrainWidth = 0;
	private int difficulty = 0;
	private int maxLevelHeight = 700;
	Body body = null;
	public static final Color colorEarth = new Color(217f/255f,164f/255f,72f/255f,1f); 
	//public static final Color colorGrass = new Color(20f/255f,220f/255f,29f/255f,1f);
	public static final Color colorGrass = new Color(248f/255f,204f/255f,75f/255f,1f);
	private int numClouds = 20;
	public Terrain(float width, GameContext gameContext, int diff) {
		this.terrainWidth = width;
		this.difficulty = diff;
		
		// Landscape generieren. Hochkompliziert!
		float[] vertices = new float[(int) (terrainWidth/100)];
		float[] metricVertices = new float[vertices.length];
		// Der erste Punkt muss auf Y=0 liegen!
		vertices[0] = -600;
		vertices[1] = 0;
		metricVertices[0] =  vertices[0]/Config.PIXELSPERMETER;
		metricVertices[1] =  vertices[1]/Config.PIXELSPERMETER;
		
		
		// Eine Plattform auf der das Haus steht:
		vertices[2] = -600;
		vertices[3] = 400;
		metricVertices[2] =  vertices[2]/Config.PIXELSPERMETER;
		metricVertices[3] =  vertices[3]/Config.PIXELSPERMETER;
		
		vertices[4] = 300;
		vertices[5] = 400;
		metricVertices[4] =  vertices[4]/Config.PIXELSPERMETER;
		metricVertices[5] =  vertices[5]/Config.PIXELSPERMETER;
		
		
		for (int i = 6; i < vertices.length-2; i++) {
			// Jede Zweite Koordinate ist x-Koordinate
			// Diese sollen gleichmäßig verteilt sein.
			if(i%2 == 0){
				vertices[i] = i * ((terrainWidth / vertices.length) * Config.SCREENSPERLEVEL);
			}
			// Sonst Höhe zufällig berechnen
			else{
				int height = MathUtils.random(200, 200 * difficulty);
				if (height > maxLevelHeight) {height = maxLevelHeight;};
				vertices[i] = height;
			}
			metricVertices[i] = vertices[i]/Config.PIXELSPERMETER;
		}
		// Der letzte ist punkt ist an ( Levelende | 0 )
		vertices[vertices.length-2] = terrainWidth * Config.SCREENSPERLEVEL;
		vertices[vertices.length-1] = -120;
		metricVertices[metricVertices.length-2] = vertices[vertices.length-2]/Config.PIXELSPERMETER;
		metricVertices[metricVertices.length-1] = vertices[vertices.length-1]/Config.PIXELSPERMETER;
		
		// Der ECT berechnet aus den koordinaten die polygone, danach wird konvertiert
		EarClippingTriangulator ect = new EarClippingTriangulator();
		
		//DelaunayTriangulator dt = new DelaunayTriangulator();
		ShortArray sa = ect.computeTriangles(vertices);
		// sa = dt.computeTriangles(vertices, true);
		
		Texture texture = new Texture("terrain_erde.png");
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		Texture textureGrass = new Texture("terrain_gras.png");
		textureGrass.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		// Die PolygonRegion enthält den Levelboden
		pRegion = new PolygonRegion(new TextureRegion(texture), vertices,  sa.toArray());
		pRegionGrass = new PolygonRegion(new TextureRegion(textureGrass), vertices,  sa.toArray());
		
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
		
		// Gegner einfügen
		int enemyNum = difficulty * 3;
		for (int i=0;i<enemyNum;i++) 
			gameContext.getStage().addActor(new EvilCrab(gameContext, ((i + 1) * (terrainWidth * Config.SCREENSPERLEVEL / (enemyNum + 1))) * Config.SCREENSPERLEVEL, maxLevelHeight + 100, new Color(255f/255f,0f/255f,0f/255f,1f), 4f * difficulty));

		
		//Add Clouds
		for (int i=0;i<numClouds;i++)
			gameContext.getSky().addActor(new Decoration(gameContext, i*(terrainWidth * Config.SCREENSPERLEVEL / (numClouds + 1))+MathUtils.random(100, 200),MathUtils.random(800, 1200),"Terrain/wolke", 450, 200));
		
		
		
		//KATZE!!!
		gameContext.setCat(new Cat(gameContext, 200, 1100, Color.BLACK, 12f * difficulty));
		
		
	}
	
	public void draw(PolygonSpriteBatch pBatch) {
		
		pBatch.setColor(colorGrass);
		pBatch.draw(pRegionGrass, -25, 55);
		
		pBatch.setColor(colorEarth);
		pBatch.draw(pRegion, 25, -55);
		
		pBatch.setColor(1,1, 1, 1);
	}
}
