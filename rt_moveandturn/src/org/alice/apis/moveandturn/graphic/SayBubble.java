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
package org.alice.apis.moveandturn.graphic;

/**
 * @author Dennis Cosgrove
 */
public class SayBubble extends SpeechBubble {
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 2 );

	public SayBubble( String text, java.awt.Font font, java.awt.Color foregroundColor, java.awt.Color backgroundColor, java.awt.Color outlineColor, Originator originator ) {
		super( text, font, foregroundColor, backgroundColor, outlineColor, originator );
	}	

	@Override
	protected java.awt.Stroke getStroke() {
		return STROKE;
	}
}
