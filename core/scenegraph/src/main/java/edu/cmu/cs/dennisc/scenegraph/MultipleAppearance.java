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

/**
 * @author Dennis Cosgrove
 */
public class MultipleAppearance extends Appearance {
	public final edu.cmu.cs.dennisc.property.CopyableArrayProperty<SimpleAppearance> singleAppearances = new edu.cmu.cs.dennisc.property.CopyableArrayProperty<SimpleAppearance>( this ) {
		@Override
		protected SimpleAppearance[] createArray( int length ) {
			return new SimpleAppearance[ length ];
		}

		@Override
		protected SimpleAppearance createCopy( SimpleAppearance src ) {
			//todo?
			return src;
		}
	};

	@Override
	protected void actuallyRelease() {
		super.actuallyRelease();
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.release();
		}
	}

	@Override
	public void setAmbientColor( edu.cmu.cs.dennisc.color.Color4f ambientColor ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setAmbientColor( ambientColor );
		}
	}

	@Override
	public void setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f diffuseColor ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setDiffuseColor( diffuseColor );
		}
	}

	@Override
	public void setOpacity( float opacity ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setOpacity( opacity );
		}
	}

	@Override
	public void setSpecularHighlightExponent( float specularHighlightExponent ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setSpecularHighlightExponent( specularHighlightExponent );
		}
	}

	@Override
	public void setSpecularHighlightColor( edu.cmu.cs.dennisc.color.Color4f specularHighlightColor ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setSpecularHighlightColor( specularHighlightColor );
		}
	}

	@Override
	public void setEmissiveColor( edu.cmu.cs.dennisc.color.Color4f emissiveColor ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setEmissiveColor( emissiveColor );
		}
	}

	@Override
	public void setShadingStyle( ShadingStyle shadingStyle ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setShadingStyle( shadingStyle );
		}
	}

	@Override
	public void setFillingStyle( FillingStyle fillingStyle ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setFillingStyle( fillingStyle );
		}
	}

	@Override
	public void setEthereal( boolean isEthereal ) {
		for( SimpleAppearance singleAppearance : singleAppearances.getValue() ) {
			singleAppearance.setEthereal( isEthereal );
		}
	}
}
