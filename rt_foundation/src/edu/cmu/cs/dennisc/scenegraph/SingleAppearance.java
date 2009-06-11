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
public class SingleAppearance extends Appearance {
	public final edu.cmu.cs.dennisc.color.property.Color4fProperty ambientColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, new edu.cmu.cs.dennisc.color.Color4f( Float.NaN, Float.NaN, Float.NaN, Float.NaN ), true ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.PropertyOwner owner, edu.cmu.cs.dennisc.color.Color4f value ) {
		    if( value == null ) {
		    	value = new edu.cmu.cs.dennisc.color.Color4f( Float.NaN, Float.NaN, Float.NaN, Float.NaN );
			}
			super.setValue( owner, value );
		}
	};

	public final edu.cmu.cs.dennisc.color.property.Color4fProperty diffuseColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.WHITE );
	public final edu.cmu.cs.dennisc.property.FloatProperty opacity = new edu.cmu.cs.dennisc.property.FloatProperty( this, 1.0f );
	public final edu.cmu.cs.dennisc.property.InstanceProperty< FillingStyle > fillingStyle = new edu.cmu.cs.dennisc.property.InstanceProperty< FillingStyle >( this, FillingStyle.SOLID ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.PropertyOwner owner, FillingStyle value ) {
			assert value != null;
			super.setValue( owner, value );
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty< ShadingStyle > shadingStyle = new edu.cmu.cs.dennisc.property.InstanceProperty< ShadingStyle >( this, ShadingStyle.SMOOTH ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.PropertyOwner owner, ShadingStyle value ) {
			assert value != null;
			super.setValue( owner, value );
		}
	};
	public final edu.cmu.cs.dennisc.color.property.Color4fProperty specularHighlightColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.BLACK );
	public final edu.cmu.cs.dennisc.color.property.Color4fProperty emissiveColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.BLACK );
	public final edu.cmu.cs.dennisc.property.FloatProperty specularHighlightExponent = new edu.cmu.cs.dennisc.property.FloatProperty( this, 0.0f );
	public final edu.cmu.cs.dennisc.property.InstanceProperty< edu.cmu.cs.dennisc.texture.Texture > diffuseColorTexture = new edu.cmu.cs.dennisc.property.InstanceProperty< edu.cmu.cs.dennisc.texture.Texture >( this, null );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isDiffuseColorTextureAlphaBlended = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
	public final edu.cmu.cs.dennisc.property.InstanceProperty< edu.cmu.cs.dennisc.texture.Texture > bumpTexture = new edu.cmu.cs.dennisc.property.InstanceProperty< edu.cmu.cs.dennisc.texture.Texture >( this, null );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isEthereal = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );

	@Override
	protected void actuallyRelease() {
		super.actuallyRelease();
		//todo: remove? referenceCount?
		if( diffuseColorTexture.getValue() != null ) {
			diffuseColorTexture.getValue().release();
		}
		//todo: remove? referenceCount?
		if( bumpTexture.getValue() != null ) {
			bumpTexture.getValue().release();
		}
	}

	@Override
	public void setAmbientColor( edu.cmu.cs.dennisc.color.Color4f ambientColor ) {
		this.ambientColor.setValue( ambientColor );
	}

	@Override
	public void setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f diffuseColor ) {
		this.diffuseColor.setValue( diffuseColor );
	}
	@Override
	public void setOpacity( float opacity ) {
		this.opacity.setValue( opacity );
	}
	@Override
	public void setSpecularHighlightExponent( float specularHighlightExponent ) {
		this.specularHighlightExponent.setValue( specularHighlightExponent );
	}
	@Override
	public void setSpecularHighlightColor( edu.cmu.cs.dennisc.color.Color4f specularHighlightColor ) {
		this.specularHighlightColor.setValue( specularHighlightColor );
	}
	@Override
	public void setEmissiveColor( edu.cmu.cs.dennisc.color.Color4f emissiveColor ) {
		this.emissiveColor.setValue( emissiveColor );
	}
	@Override
	public void setShadingStyle( ShadingStyle shadingStyle ) {
		this.shadingStyle.setValue( shadingStyle );
	}

	@Override
	public void setFillingStyle( FillingStyle fillingStyle ) {
		this.fillingStyle.setValue( fillingStyle );
	}
	@Override
	public void setDiffuseColorTextureAlphaBlended( boolean isDiffuseColorTextureAlphaBlended ) {
		this.isDiffuseColorTextureAlphaBlended.setValue( isDiffuseColorTextureAlphaBlended );
	}

	@Override
	public void setDiffuseColorTexture( edu.cmu.cs.dennisc.texture.Texture diffuseColorTexture ) {
		this.diffuseColorTexture.setValue( diffuseColorTexture );
	}

	@Override
	public void setBumpTexture( edu.cmu.cs.dennisc.texture.Texture bumpTexture ) {
		this.bumpTexture.setValue( bumpTexture );
	}

	@Override
	public void setEthereal( boolean isEthereal ) {
		this.isEthereal.setValue( isEthereal );
	}
	
}
