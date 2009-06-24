/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.lookingglass;

/**
 * @author Dennis Cosgrove
 */
public abstract class Graphics2D extends java.awt.Graphics2D {
	public abstract boolean isValid();

	//todo?
	//public abstract void transform( edu.cmu.cs.dennisc.math.Matrix3x3 transform );
	//public abstract void setTransform( edu.cmu.cs.dennisc.math.Matrix3x3 transform );
	
	public abstract boolean isRemembered( java.awt.Font font );
	public abstract void remember( java.awt.Font font );
	public abstract void forget( java.awt.Font font );
	public abstract void disposeForgottenFonts();
	
	public abstract java.awt.geom.Rectangle2D getBounds( String text, java.awt.Font font );

	public abstract boolean isRemembered( java.awt.Image image );
	public abstract void remember( java.awt.Image image ); 
	public abstract void forget( java.awt.Image image ); 
	public abstract void disposeForgottenImages();
	
	public abstract boolean isRemembered( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator );
	public abstract void remember( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ); 
	public abstract void paint( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator, float x, float y, float alpha ); 
	public abstract void forget( edu.cmu.cs.dennisc.image.ImageGenerator imageGenerator ); 
	public abstract void disposeForgottenImageGenerators();
}
