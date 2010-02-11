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
public class MultipleAppearance extends Appearance {
	public final edu.cmu.cs.dennisc.property.CopyableArrayProperty< SingleAppearance > singleAppearances = new edu.cmu.cs.dennisc.property.CopyableArrayProperty< SingleAppearance >( this ) {
		@Override
		protected SingleAppearance[] createArray( int length ) {
			return new SingleAppearance[ length ];
		}
		@Override
		protected SingleAppearance createCopy( SingleAppearance src ) {
			//todo?
			return src;
		}
	};

	@Override
	protected void actuallyRelease() {
		super.actuallyRelease();
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.release();
		}
	}


	@Override
	public void setAmbientColor( edu.cmu.cs.dennisc.color.Color4f ambientColor ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setAmbientColor( ambientColor );
		}
	}

	@Override
	public void setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f diffuseColor ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setDiffuseColor( diffuseColor );
		}
	}
	@Override
	public void setOpacity( float opacity ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setOpacity( opacity );
		}
	}
	@Override
	public void setSpecularHighlightExponent( float specularHighlightExponent ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setSpecularHighlightExponent( specularHighlightExponent );
		}
	}
	@Override
	public void setSpecularHighlightColor( edu.cmu.cs.dennisc.color.Color4f specularHighlightColor ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setSpecularHighlightColor( specularHighlightColor );
		}
	}
	@Override
	public void setEmissiveColor( edu.cmu.cs.dennisc.color.Color4f emissiveColor ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setEmissiveColor( emissiveColor );
		}
	}
	@Override
	public void setShadingStyle( ShadingStyle shadingStyle ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setShadingStyle( shadingStyle );
		}
	}

	@Override
	public void setFillingStyle( FillingStyle fillingStyle ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setFillingStyle( fillingStyle );
		}
	}
	@Override
	public void setDiffuseColorTextureAlphaBlended( boolean isDiffuseColorTextureAlphaBlended ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setDiffuseColorTextureAlphaBlended( isDiffuseColorTextureAlphaBlended );
		}
	}
	@Override
	public void setDiffuseColorTextureClamped( boolean isDiffuseColorTextureClamped ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setDiffuseColorTextureClamped( isDiffuseColorTextureClamped );
		}
	}

	@Override
	public void setDiffuseColorTexture( edu.cmu.cs.dennisc.texture.Texture diffuseColorTexture ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setDiffuseColorTexture( diffuseColorTexture );
		}
	}

	@Override
	public void setBumpTexture( edu.cmu.cs.dennisc.texture.Texture bumpTexture ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setBumpTexture( bumpTexture );
		}
	}

	@Override
	public void setEthereal( boolean isEthereal ) {
		for( SingleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setEthereal( isEthereal );
		}
	}
}
