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
public abstract class Texture extends edu.cmu.cs.dennisc.pattern.AbstractElement implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable, edu.cmu.cs.dennisc.image.ImageGenerator {
	private java.util.Vector< edu.cmu.cs.dennisc.texture.event.TextureListener > m_textureListeners = new java.util.Vector< edu.cmu.cs.dennisc.texture.event.TextureListener >();

	public boolean isValid() {
		return getWidth() > 0 && getHeight() > 0;
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract boolean isPotentiallyAlphaBlended();
	public abstract boolean isMipMappingDesired();

	public void addTextureListener( edu.cmu.cs.dennisc.texture.event.TextureListener textureListener ) {
		m_textureListeners.addElement( textureListener );
	}
	public void removeTextureListener( edu.cmu.cs.dennisc.texture.event.TextureListener textureListener ) {
		m_textureListeners.removeElement( textureListener );
	}
	public Iterable< edu.cmu.cs.dennisc.texture.event.TextureListener > accessTextureListeners() {
		return m_textureListeners;
	}
	public void fireTextureChanged( edu.cmu.cs.dennisc.texture.event.TextureEvent textureEvent ) {
		for( edu.cmu.cs.dennisc.texture.event.TextureListener hl : m_textureListeners ) {
			hl.textureChanged( textureEvent );
		}
	}

	public void fireTextureChanged() {
		fireTextureChanged( new edu.cmu.cs.dennisc.texture.event.TextureEvent( this ) );
	}
}
