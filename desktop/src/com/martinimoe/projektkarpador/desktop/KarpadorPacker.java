package com.martinimoe.projektkarpador.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class KarpadorPacker {
	public static void main (String[] args) throws Exception {
    	String inputDir = "/home/mo/ProjektKarpador/core/res";
    	String outputDir = "/home/mo/ProjektKarpador/core/res";
    	String packFileName = "karpador.pack";
        TexturePacker.process(inputDir, outputDir, packFileName);
    }
}
