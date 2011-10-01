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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class ModelImp extends TransformableImp {
	@Deprecated
	public final ColorProperty color = new ColorProperty( ModelImp.this ) {
		@Override
		public edu.cmu.cs.dennisc.color.Color4f getValue() {
			return ModelImp.this.getSgAppearances()[ 0 ].diffuseColor.getValue();
		}
		@Override
		protected void handleSetValue(edu.cmu.cs.dennisc.color.Color4f value) {
			for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : ModelImp.this.getSgAppearances() ) {
				sgAppearance.diffuseColor.setValue( value );
			}
		}
	};
	@Deprecated
	public void addColorListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener)
	{
		this.getSgAppearances()[ 0 ].diffuseColor.addPropertyListener(listener);
	}
	
	@Deprecated
	public void removeColorListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener)
	{
		this.getSgAppearances()[ 0 ].diffuseColor.removePropertyListener(listener);
	}
	
	@Deprecated
	public void addOpacityListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener)
	{
		this.getSgAppearances()[ 0 ].opacity.addPropertyListener(listener);
	}
	
	@Deprecated
	public void removeOpacityListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener)
	{
		this.getSgAppearances()[ 0 ].opacity.removePropertyListener(listener);
	}

	public final PaintProperty paint = new PaintProperty( ModelImp.this ) {
		@Override
		protected void internalSetValue(org.lgna.story.Paint value) {
			edu.cmu.cs.dennisc.color.Color4f color4f = org.lgna.story.ImplementationAccessor.getColor4f( value, edu.cmu.cs.dennisc.color.Color4f.WHITE );
			edu.cmu.cs.dennisc.texture.Texture texture = org.lgna.story.ImplementationAccessor.getTexture( value, null );
			for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : ModelImp.this.getSgAppearances() ) {
				if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( color4f, sgAppearance.diffuseColor.getValue() ) ) {
					//pass
				} else {
					sgAppearance.diffuseColor.setValue( color4f );
				}
				if( sgAppearance instanceof edu.cmu.cs.dennisc.scenegraph.TexturedAppearance ) {
					edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgTexturedAppearance = (edu.cmu.cs.dennisc.scenegraph.TexturedAppearance)sgAppearance;
					if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( texture, sgTexturedAppearance.diffuseColorTexture.getValue() ) ) {
						//pass
					} else {
						sgTexturedAppearance.diffuseColorTexture.setValue( texture );
					}
				}
			}
		}
	};
	public final FloatProperty opacity = new FloatProperty( ModelImp.this ) {
		@Override
		public Float getValue() {
			return ModelImp.this.getSgAppearances()[ 0 ].opacity.getValue();
		}
		@Override
		protected void handleSetValue( Float value ) {
			for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : ModelImp.this.getSgAppearances() ) {
				sgAppearance.opacity.setValue( value );
			}
		}
	};
	
	protected abstract edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgAppearances();
	protected abstract edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals();
		
	
//	public final void setDiffuseColorTexture( edu.cmu.cs.dennisc.texture.Texture diffuseColorTexture ) {
//		for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : this.getSgAppearances() ) {
//			if (sgAppearance instanceof edu.cmu.cs.dennisc.scenegraph.TexturedAppearance)
//			{
//				((edu.cmu.cs.dennisc.scenegraph.TexturedAppearance)sgAppearance).diffuseColorTexture.setValue( diffuseColorTexture );
//			}
//		}
//	}
	
	public void addScaleListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener){
		this.getSgVisuals()[0].scale.addPropertyListener(listener);
	}
	
	public void removeScaleListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener){
		this.getSgVisuals()[0].scale.removePropertyListener(listener);
	}
	
	private edu.cmu.cs.dennisc.math.Matrix3x3 getSgVisualsScale() {
		return this.getSgVisuals()[0].scale.getValue();
	}
	private void setSgVisualsScale( edu.cmu.cs.dennisc.math.Matrix3x3 m ) {
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.getSgVisuals() ) {
			sgVisual.scale.setValue( m );
		}
	}
	public edu.cmu.cs.dennisc.math.Dimension3 getScale() {
		edu.cmu.cs.dennisc.math.Matrix3x3 scale = this.getSgVisualsScale();
		return new edu.cmu.cs.dennisc.math.Dimension3( scale.right.x, scale.up.y, scale.backward.z );
	}
	public void setScale( edu.cmu.cs.dennisc.math.Dimension3 scale ) {
		edu.cmu.cs.dennisc.math.Matrix3x3 m = edu.cmu.cs.dennisc.math.Matrix3x3.createIdentity();
		m.right.x = scale.x;
		m.up.y = scale.y;
		m.backward.z = scale.z;
		this.setSgVisualsScale( m );
	}
	public void animateSetScale( edu.cmu.cs.dennisc.math.Dimension3 scale, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}

	public edu.cmu.cs.dennisc.math.Dimension3 getSize() {
		edu.cmu.cs.dennisc.scenegraph.Visual[] sgVisuals = getSgVisuals();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bBox = edu.cmu.cs.dennisc.math.AxisAlignedBox.createNaN();
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : sgVisuals ) {
			edu.cmu.cs.dennisc.math.AxisAlignedBox bb = sgVisual.getAxisAlignedMinimumBoundingBox();
			bBox.union( bb );
		}
		assert bBox.isNaN() == false;
		return bBox.getSize();
	}
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
	}

	public void animateSetSize( edu.cmu.cs.dennisc.math.Dimension3 size, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	}

	public void animateSetWidth( double width, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert ( isVolumePreserved && isAspectRatioPreserved ) == false;
	}
	public void animateSetHeight( double height, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert ( isVolumePreserved && isAspectRatioPreserved ) == false;
	}
	public void animateSetDepth( double depth, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert ( isVolumePreserved && isAspectRatioPreserved ) == false;
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
