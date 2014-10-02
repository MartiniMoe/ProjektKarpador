package com.martinimoe.projektkarpador.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
	static String inputDir = "/Users/Moritz/ownCloud/ProjektKarpador/Sprites";
	static String outputDir = "/Users/Moritz/ownCloud/ProjektKarpador/Atlas";
	static String packFileName = "Karpador.pack";
	
    public static void main (String[] args) throws Exception {
        TexturePacker.process(inputDir, outputDir, packFileName);
    }
}