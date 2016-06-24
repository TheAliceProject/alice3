/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.scenegraph;

public class SimpleAppearance extends Appearance {

	public final edu.cmu.cs.dennisc.color.property.Color4fProperty ambientColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, new edu.cmu.cs.dennisc.color.Color4f( Float.NaN, Float.NaN, Float.NaN, Float.NaN ), true ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.color.Color4f value ) {
			if( value == null ) {
				value = edu.cmu.cs.dennisc.color.Color4f.createNaN();
			}
			super.setValue( value );
		}
	};

	public final edu.cmu.cs.dennisc.color.property.Color4fProperty diffuseColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.WHITE );
	public final edu.cmu.cs.dennisc.property.FloatProperty opacity = new edu.cmu.cs.dennisc.property.FloatProperty( this, 1.0f );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<FillingStyle> fillingStyle = new edu.cmu.cs.dennisc.property.InstanceProperty<FillingStyle>( this, FillingStyle.SOLID ) {
		@Override
		public void setValue( FillingStyle value ) {
			assert value != null : this;
			super.setValue( value );
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty<ShadingStyle> shadingStyle = new edu.cmu.cs.dennisc.property.InstanceProperty<ShadingStyle>( this, ShadingStyle.SMOOTH ) {
		@Override
		public void setValue( ShadingStyle value ) {
			assert value != null : this;
			super.setValue( value );
		}
	};
	public final edu.cmu.cs.dennisc.color.property.Color4fProperty specularHighlightColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.BLACK );
	public final edu.cmu.cs.dennisc.color.property.Color4fProperty emissiveColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.BLACK );
	public final edu.cmu.cs.dennisc.property.FloatProperty specularHighlightExponent = new edu.cmu.cs.dennisc.property.FloatProperty( this, 0.0f );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isEthereal = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );

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
	public void setEthereal( boolean isEthereal ) {
		this.isEthereal.setValue( isEthereal );
	}
}
