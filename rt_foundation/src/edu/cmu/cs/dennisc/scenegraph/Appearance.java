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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class Appearance extends Element {
	public abstract void setAmbientColor( edu.cmu.cs.dennisc.color.Color4f ambientColor );
	public abstract void setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f diffuseColor );
	public abstract void setOpacity( float opacity );
	public abstract void setSpecularHighlightExponent( float specularHighlightExponent );
	public abstract void setSpecularHighlightColor( edu.cmu.cs.dennisc.color.Color4f specularHighlightColor );
	public abstract void setEmissiveColor( edu.cmu.cs.dennisc.color.Color4f emissiveColor );
	public abstract void setFillingStyle( FillingStyle fillingStyle );
	public abstract void setShadingStyle( ShadingStyle shadingStyle );
	public abstract void setDiffuseColorTexture( edu.cmu.cs.dennisc.texture.Texture diffuseColorTexture );
	public abstract void setDiffuseColorTextureAlphaBlended( boolean isDiffuseColorTextureAlphaBlended  );
	public abstract void setBumpTexture( edu.cmu.cs.dennisc.texture.Texture bumpTexture );
	public abstract void setEthereal( boolean isEthereal  );
}
