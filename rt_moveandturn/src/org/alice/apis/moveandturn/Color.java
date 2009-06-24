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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class Color implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public static final Color BLACK = new Color( edu.cmu.cs.dennisc.color.Color4f.BLACK );
	public static final Color BLUE = new Color( edu.cmu.cs.dennisc.color.Color4f.BLUE );
	public static final Color CYAN = new Color( edu.cmu.cs.dennisc.color.Color4f.CYAN );
	public static final Color DARK_GRAY = new Color( edu.cmu.cs.dennisc.color.Color4f.DARK_GRAY );
	public static final Color GRAY = new Color( edu.cmu.cs.dennisc.color.Color4f.GRAY );
	public static final Color GREEN = new Color( edu.cmu.cs.dennisc.color.Color4f.GREEN );
	public static final Color LIGHT_GRAY = new Color( edu.cmu.cs.dennisc.color.Color4f.LIGHT_GRAY );
	public static final Color MAGENTA = new Color( edu.cmu.cs.dennisc.color.Color4f.MAGENTA );
	public static final Color ORANGE = new Color( edu.cmu.cs.dennisc.color.Color4f.ORANGE );
	public static final Color PINK = new Color( edu.cmu.cs.dennisc.color.Color4f.PINK );
	public static final Color RED = new Color( edu.cmu.cs.dennisc.color.Color4f.RED );
	public static final Color WHITE = new Color( edu.cmu.cs.dennisc.color.Color4f.WHITE );
	public static final Color YELLOW = new Color( edu.cmu.cs.dennisc.color.Color4f.YELLOW );
	
	public static final Color PURPLE = new Color( edu.cmu.cs.dennisc.color.Color4f.PURPLE );
	public static final Color BROWN = new Color( edu.cmu.cs.dennisc.color.Color4f.BROWN );

	private edu.cmu.cs.dennisc.color.Color4f internal = new edu.cmu.cs.dennisc.color.Color4f();
	public Color() {
		this.internal.set( 1.0f, 1.0f, 1.0f, 1.0f );
	}
	public Color( Number red, Number green, Number blue ) {
		this.internal.set( red.floatValue(), green.floatValue(), blue.floatValue(), 1.0f );
	}
	@Deprecated
	public Color( Number red, Number green, Number blue, Number alpha ) {
		this.internal.set( red.floatValue(), green.floatValue(), blue.floatValue(), alpha.floatValue() );
	}
	public Color( edu.cmu.cs.dennisc.color.Color4f internal ) {
		this.internal.set( internal );
	}
	
	public edu.cmu.cs.dennisc.color.Color4f getInternal() {
		return this.internal;
	}
	
	public Double getRed() {
		return (double)this.internal.red;
	}
	public Double getGreen() {
		return (double)this.internal.green;
	}
	public Double getBlue() {
		return (double)this.internal.blue;
	}
	
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		this.internal.decode( binaryDecoder );
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		this.internal.encode( binaryEncoder );
	}
	
}
