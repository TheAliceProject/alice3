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
package edu.cmu.cs.dennisc.texture;

/**
 * @author Dennis Cosgrove
 */
public class TextureCoordinate2f implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public float u;
	public float v;
	
	public TextureCoordinate2f() {
	}
	public TextureCoordinate2f( TextureCoordinate2f other ) {
		set( other );
	}
	public TextureCoordinate2f( float u, float v ) {
		this.u = u;
		this.v = v;
	}
	
	//todo
	public static TextureCoordinate2f createNaN() {
		return new TextureCoordinate2f( Float.NaN, Float.NaN );
	}
	
	public void set( TextureCoordinate2f other ) {
		this.u = other.u;
		this.v = other.v;
	}
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		u = binaryDecoder.decodeFloat();
		v = binaryDecoder.decodeFloat();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( u );
		binaryEncoder.encode( v );
	}
	public void set( float u, float v ) {
		this.u = u;
		this.v = v;
	}
	public void setNaN() {
		u = v = Float.NaN;
	}
	public boolean isNaN() {
		return Float.isNaN( u ) || Float.isNaN( v );
	}
}
