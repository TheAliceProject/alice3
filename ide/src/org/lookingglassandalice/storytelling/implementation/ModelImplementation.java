/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.lookingglassandalice.storytelling.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class ModelImplementation extends TransformableImplementation {
	protected abstract edu.cmu.cs.dennisc.scenegraph.SingleAppearance[] getSgAppearances();
	protected abstract edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals();
	public final edu.cmu.cs.dennisc.color.Color4f getColor() {
		return this.getSgAppearances()[ 0 ].diffuseColor.getValue();
	}
	public final void setColor( edu.cmu.cs.dennisc.color.Color4f color ) {
		for( edu.cmu.cs.dennisc.scenegraph.SingleAppearance sgAppearance : this.getSgAppearances() ) {
			sgAppearance.diffuseColor.setValue( color );
		}
	}
	public final void animateColor( edu.cmu.cs.dennisc.color.Color4f color, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setColor( color );
		} else {
			perform( new edu.cmu.cs.dennisc.color.animation.Color4fAnimation( duration, style, this.getColor(), color ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.color.Color4f c ) {
					ModelImplementation.this.setColor( c );
				}
			} );
		}
	}
	
	public final float getOpacity() {
		return this.getSgAppearances()[ 0 ].opacity.getValue();
	}
	public final void setOpacity( float opacity ) {
		for( edu.cmu.cs.dennisc.scenegraph.SingleAppearance sgAppearance : this.getSgAppearances() ) {
			sgAppearance.opacity.setValue( opacity );
		}
	}
	public final void animateOpacity( float opacity, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setOpacity( opacity );
		} else {
			this.perform( new edu.cmu.cs.dennisc.animation.interpolation.FloatAnimation( duration, style, this.getOpacity(), opacity ) {
				@Override
				protected void updateValue( Float globalBrightness ) {
					ModelImplementation.this.setOpacity( globalBrightness );
				}
			} );
		}
	}
	
	
	public final void setDiffuseColorTexture( edu.cmu.cs.dennisc.texture.Texture diffuseColorTexture ) {
		for( edu.cmu.cs.dennisc.scenegraph.SingleAppearance sgAppearance : this.getSgAppearances() ) {
			sgAppearance.diffuseColorTexture.setValue( diffuseColorTexture );
		}
	}
	
	public edu.cmu.cs.dennisc.math.Dimension3 getScale() {
		return null;
	}
	public void setScale( edu.cmu.cs.dennisc.math.Dimension3 scale ) {
	}
	public void animateSetScale( edu.cmu.cs.dennisc.math.Dimension3 scale, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}

	public edu.cmu.cs.dennisc.math.Dimension3 getSize() {
		return null;
	}
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
	}

	public void animateSetSize( edu.cmu.cs.dennisc.math.Dimension3 size, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}

	public void animateSetWidth( double width, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert isVolumePreserved && isAspectRatioPreserved == false;
	}
	public void animateSetHeight( double height, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert isVolumePreserved && isAspectRatioPreserved == false;
	}
	public void animateSetDepth( double depth, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert isVolumePreserved && isAspectRatioPreserved == false;
	}

	public void animateResize( double factor, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}
	public void animateResizeWidth( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}
	public void animateResizeHeight( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}
	public void animateResizeDepth( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}
	
//	@Override
//	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans, boolean isOriginIncluded ) {
//		super.updateCumulativeBound( rv, trans, isOriginIncluded );
//		rv.add( this.sgFrontFace, trans );
//		rv.add( this.sgBackFace, trans );
//		return rv;
//	}
//	
//	@Override
//	protected void applyScale( edu.cmu.cs.dennisc.math.Vector3 axis, boolean isScootDesired ) {
//		super.applyScale( axis, isScootDesired );
//		edu.cmu.cs.dennisc.math.Matrix3x3 scale = sgFrontFace.scale.getValue();
//		edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( scale, axis );
//		sgFrontFace.scale.setValue( scale );
//		sgBackFace.scale.setValue( scale );
//	}
}
