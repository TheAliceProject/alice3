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
	public final PaintProperty paint = new PaintProperty( ModelImp.this ) {
		@Override
		protected void internalSetValue(org.lgna.story.Paint value) {
			edu.cmu.cs.dennisc.color.Color4f color4f = org.lgna.story.ImplementationAccessor.getColor4f( value, edu.cmu.cs.dennisc.color.Color4f.WHITE );
			edu.cmu.cs.dennisc.texture.Texture texture = org.lgna.story.ImplementationAccessor.getTexture( value, null );
			for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : ModelImp.this.getSgPaintAppearances() ) {
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
			return ModelImp.this.getSgOpacityAppearances()[ 0 ].opacity.getValue();
		}
		@Override
		protected void handleSetValue( Float value ) {
			for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : ModelImp.this.getSgOpacityAppearances() ) {
				sgAppearance.opacity.setValue( value );
			}
		}
	};
	
	protected abstract edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgPaintAppearances();
	protected abstract edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgOpacityAppearances();
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
	
	private void applyScale( edu.cmu.cs.dennisc.math.Vector3 axis, boolean isScootDesired ) {
		if( isScootDesired ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getSgComposite().localTransformation.getValue();
			m.translation.multiply( axis );
			this.getSgComposite().localTransformation.setValue( m );
		}
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.getSgVisuals() ) {
			edu.cmu.cs.dennisc.math.Matrix3x3 scale = sgVisual.scale.getValue();
			edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( scale, axis );
			sgVisual.scale.setValue( scale );
		}
	}
	private void animateApplyScale( edu.cmu.cs.dennisc.math.Vector3 axis, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		class ScaleAnimation extends edu.cmu.cs.dennisc.math.animation.Vector3Animation {
			private final edu.cmu.cs.dennisc.math.Vector3 vPrev = new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 );
			private final edu.cmu.cs.dennisc.math.Vector3 vBuffer = new edu.cmu.cs.dennisc.math.Vector3();

			private final ModelImp subject;
			private final ModelImp[] scoots;
			public ScaleAnimation( double duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Vector3 axis, ModelImp subject, ModelImp[] scoots ) {
				super( duration, style, new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 ), axis );
				this.subject = subject;
				this.scoots = scoots;
			}
			@Override
			protected void updateValue( edu.cmu.cs.dennisc.math.Vector3 v ) {
				edu.cmu.cs.dennisc.math.Vector3.setReturnValueToDivision( this.vBuffer, v, this.vPrev );
				this.subject.applyScale( this.vBuffer, false );
				for( ModelImp model : this.scoots ) {
					model.applyScale( this.vBuffer, true );
				}
				this.vPrev.set( v );
			}
		}

		double actualDuration = adjustDurationIfNecessary( duration );
		ModelImp[] scoots = {};
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
			this.applyScale( axis, false );
			for( ModelImp model : scoots ) {
				model.applyScale( axis, true );
			}
		} else {
			this.perform( new ScaleAnimation( actualDuration, style, axis, this, scoots ) );
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
		if( duration > 0  ) {
			
		}
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
	public double getWidth() {
		return this.getSize().x;
	}
	public double getHeight() {
		return this.getSize().y;
	}
	public double getDepth() {
		return this.getSize().z;
	}

	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		this.animateSetSize( size, 0, null );
	}

	private static double getScale( double prevSize, double nextSize ) {
		if( prevSize == 0.0 ) {
			if( nextSize == 0.0 ) {
				return 1.0;
			} else {
				throw new RuntimeException( "unable to set the size of model that has zero(0) along a dimension" );
			}
		} else {
			return nextSize/prevSize;
		}
	}
	public void animateSetSize( edu.cmu.cs.dennisc.math.Dimension3 size, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		edu.cmu.cs.dennisc.math.Dimension3 prevSize = this.getSize();
		edu.cmu.cs.dennisc.math.Vector3 scale = new edu.cmu.cs.dennisc.math.Vector3(
				getScale( prevSize.x, size.x ),
				getScale( prevSize.y, size.y ),
				getScale( prevSize.z, size.z )
		);
		this.animateApplyScale( scale, duration, style );
	}

	public void animateSetWidth( double width, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert ( isVolumePreserved && isAspectRatioPreserved ) == false;
		double prevWidth = this.getWidth();
		assert Double.isNaN( prevWidth ) == false;
		assert prevWidth >= 0;
		if( prevWidth > 0.0 ) {
			double factor = width / prevWidth;
			if( isAspectRatioPreserved ) {
				this.animateResize( factor, duration, style );
			} else {
				this.animateResizeWidth( factor, isVolumePreserved, duration, style );
			}
		} else {
			if( width != 0.0 ) {
				throw new RuntimeException( "unable to set the width of model that has zero(0) width" );
			}
		}
	}
	public void animateSetHeight( double height, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert ( isVolumePreserved && isAspectRatioPreserved ) == false;
		double prevHeight = this.getHeight();
		assert Double.isNaN( prevHeight ) == false;
		assert prevHeight >= 0;
		if( prevHeight > 0.0 ) {
			double factor = height / prevHeight;
			if( isAspectRatioPreserved ) {
				this.animateResize( factor, duration, style );
			} else {
				this.animateResizeHeight( factor, isVolumePreserved, duration, style );
			}
		} else {
			if( height != 0.0 ) {
				throw new RuntimeException( "unable to set the height of model that has zero(0) height" );
			}
		}
	}
	public void animateSetDepth( double depth, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert ( isVolumePreserved && isAspectRatioPreserved ) == false;
		double prevDepth = this.getDepth();
		assert Double.isNaN( prevDepth ) == false;
		assert prevDepth >= 0;
		if( prevDepth > 0.0 ) {
			double factor = depth / prevDepth;
			if( isAspectRatioPreserved ) {
				this.animateResize( factor, duration, style );
			} else {
				this.animateResizeDepth( factor, isVolumePreserved, duration, style );
			}
		} else {
			if( depth != 0.0 ) {
				throw new RuntimeException( "unable to set the height of model that has zero(0) height" );
			}
		}
	}

	private static enum Dimension {
		LEFT_TO_RIGHT( true,  false, false ),
		TOP_TO_BOTTOM( false, true,  false ),
		FRONT_TO_BACK( false, false, true );
		
		private final boolean isXScaled;
		private final boolean isYScaled;
		private final boolean isZScaled;

		private Dimension( boolean isXScaled, boolean isYScaled, boolean isZScaled ) {
			this.isXScaled = isXScaled;
			this.isYScaled = isYScaled;
			this.isZScaled = isZScaled;
			assert this.isXScaled ^ this.isYScaled ^ this.isZScaled;
		}
		
		public edu.cmu.cs.dennisc.math.Vector3 getResizeAxis( edu.cmu.cs.dennisc.math.Vector3 rv, double amount, boolean isVolumePreserved ) {
			//todo: center around 0 as opposed to 1?
			assert amount > 0;
			
			double x;
			double y;
			double z;

			if( isVolumePreserved ) {
				double squash = 1.0/Math.sqrt( amount );
				if( this.isXScaled ) {
					x = amount;
					y = squash;
					z = squash;
				} else if( this.isYScaled ) {
					x = squash;
					y = amount;
					z = squash;
				} else if( this.isZScaled ) {
					x = squash;
					y = squash;
					z = amount;
				} else {
					throw new RuntimeException();
				}
			} else {
				x = 1;
				y = 1;
				z = 1;
				if( this.isXScaled ) {
					x = amount;
				}
				if( this.isYScaled ) {
					y = amount;
				}
				if( this.isZScaled ) {
					z = amount;
				}
			}

			rv.set( x, y, z );
			return rv;
		}
		public edu.cmu.cs.dennisc.math.Vector3 getResizeAxis( double amount, boolean isVolumePreserved ) {
			return getResizeAxis( edu.cmu.cs.dennisc.math.Vector3.createNaN(), amount, isVolumePreserved );
		}
	}

	public void animateResize( double factor, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateApplyScale( new edu.cmu.cs.dennisc.math.Vector3( factor, factor, factor ), duration, style );
	}
	
	public void animateResizeWidth( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateApplyScale( Dimension.LEFT_TO_RIGHT.getResizeAxis( factor, isVolumePreserved ), duration, style );
	}
	public void animateResizeHeight( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateApplyScale( Dimension.TOP_TO_BOTTOM.getResizeAxis( factor, isVolumePreserved ), duration, style );
	}
	public void animateResizeDepth( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateApplyScale( Dimension.FRONT_TO_BACK.getResizeAxis( factor, isVolumePreserved ), duration, style );
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
