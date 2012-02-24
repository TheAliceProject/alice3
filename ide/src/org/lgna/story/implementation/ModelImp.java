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

import java.util.LinkedList;

/**
 * @author Dennis Cosgrove
 */
public abstract class ModelImp extends TransformableImp implements org.alice.interact.manipulator.Scalable {
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
	
	public ModelImp() {
		this.getSgComposite().putBonusDataFor( org.alice.interact.manipulator.Scalable.KEY, this );
	}
	protected abstract edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgPaintAppearances();
	protected abstract edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgOpacityAppearances();
	protected abstract edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals();

	@Override
	public void setName( java.lang.String name ) {
		super.setName( name );
		int i = 0;
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.getSgVisuals() ) {
			sgVisual.setName( name + ".sgVisual" + i );
			i += 1;
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, org.lgna.story.implementation.ReferenceFrame asSeenBy ) {
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.getSgVisuals() ) {
			rv.add( sgVisual, this.getTransformation( asSeenBy ) );
		}
		return rv;
	}

	//	public final void setDiffuseColorTexture( edu.cmu.cs.dennisc.texture.Texture diffuseColorTexture ) {
//		for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : this.getSgAppearances() ) {
//			if (sgAppearance instanceof edu.cmu.cs.dennisc.scenegraph.TexturedAppearance)
//			{
//				((edu.cmu.cs.dennisc.scenegraph.TexturedAppearance)sgAppearance).diffuseColorTexture.setValue( diffuseColorTexture );
//			}
//		}
//	}

	public abstract void addScaleListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener);
	public abstract void removeScaleListener(edu.cmu.cs.dennisc.property.event.PropertyListener listener);
	public abstract edu.cmu.cs.dennisc.math.Dimension3 getScale();
	public abstract void setScale( edu.cmu.cs.dennisc.math.Dimension3 scale );
	public void animateSetScale( edu.cmu.cs.dennisc.math.Dimension3 scale, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		double actualDuration = this.adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, 0.0 ) ) {
			this.setScale( scale );
		} else {
			class ScaleAnimation extends edu.cmu.cs.dennisc.math.animation.Dimension3Animation {
				public ScaleAnimation( double duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Dimension3 scale0, edu.cmu.cs.dennisc.math.Dimension3 scale1 ) {
					super( duration, style, scale0, scale1 );
				}
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.Dimension3 v ) {
					ModelImp.this.setScale( v );
				}
			}
			this.perform( new ScaleAnimation( duration, style, ModelImp.this.getScale(), scale ) );
		}
	}

//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox()
//	{
//		edu.cmu.cs.dennisc.scenegraph.Visual[] sgVisuals = getSgVisuals();
//		edu.cmu.cs.dennisc.math.AxisAlignedBox bBox = edu.cmu.cs.dennisc.math.AxisAlignedBox.createNaN();
//		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : sgVisuals ) {
//			edu.cmu.cs.dennisc.math.AxisAlignedBox bb = sgVisual.getAxisAlignedMinimumBoundingBox();
//			bBox.union( bb );
//		}
//		assert bBox.isNaN() == false;
//		return bBox;
//	}
//	
	public edu.cmu.cs.dennisc.math.Dimension3 getSize() {
		return getAxisAlignedMinimumBoundingBox().getSize();
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
		edu.cmu.cs.dennisc.math.Dimension3 prevScale = this.getScale();
		
		edu.cmu.cs.dennisc.math.Dimension3 scale = new edu.cmu.cs.dennisc.math.Dimension3(
				size.x / (prevSize.x / prevScale.x),
				size.y / (prevSize.y / prevScale.y),
				size.z / (prevSize.z / prevScale.z)
		);
		this.animateSetScale( scale, duration, style );
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
		
		public edu.cmu.cs.dennisc.math.Dimension3 getResizeAxis( edu.cmu.cs.dennisc.math.Dimension3 rv, double amount, boolean isVolumePreserved ) {
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
		public edu.cmu.cs.dennisc.math.Dimension3 getResizeAxis( double amount, boolean isVolumePreserved ) {
			return getResizeAxis( edu.cmu.cs.dennisc.math.Dimension3.createNaN(), amount, isVolumePreserved );
		}
	}

	public void animateResize( double factor, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateSetScale( new edu.cmu.cs.dennisc.math.Dimension3( factor, factor, factor ), duration, style );
	}
	
	public void animateResizeWidth( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateSetScale( Dimension.LEFT_TO_RIGHT.getResizeAxis( factor, isVolumePreserved ), duration, style );
	}
	public void animateResizeHeight( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateSetScale( Dimension.TOP_TO_BOTTOM.getResizeAxis( factor, isVolumePreserved ), duration, style );
	}
	public void animateResizeDepth( double factor, boolean isVolumePreserved, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateSetScale( Dimension.FRONT_TO_BACK.getResizeAxis( factor, isVolumePreserved ), duration, style );
	}
	
	public void displayBubble(edu.cmu.cs.dennisc.scenegraph.graphics.Bubble bubble, Number duration) {
		if( this.getScene() != null ) {
			perform( new edu.cmu.cs.dennisc.animation.BubbleAnimation( this, 0.2, duration.doubleValue(), 0.2, bubble ) );
		} else {
			//todo
			javax.swing.JOptionPane.showMessageDialog( null, "unable to display bubble" );
		}
	}
	
	public void sayText(String textToSay, org.alice.flite.VoiceType voice, edu.cmu.cs.dennisc.scenegraph.graphics.Bubble bubble) {		
		final boolean showSpeechBubble = bubble != null;
		final org.lgna.common.resources.TextToSpeechResource tts = org.lgna.common.resources.TextToSpeechResource.valueOf(textToSay, voice.getVoiceString());
		final edu.cmu.cs.dennisc.animation.AudioLimitedBubbleAnimation bubbleAnimation = (showSpeechBubble)? new edu.cmu.cs.dennisc.animation.AudioLimitedBubbleAnimation(this, .3, .3, bubble) : null;
		
		Runnable textToSpeech =  new Runnable() { 
			public void run() {
				if (!tts.isLoaded())
				{
					tts.loadResource();
				}
				edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
				edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( tts, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_VOLUME, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_START_TIME, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_STOP_TIME  );
				if (showSpeechBubble)
				{
					bubbleAnimation.setDuration(tts.getDuration()+.2);
				}
				perform( new edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation( player ) );	
			}
		};
		if (showSpeechBubble)
		{
			Runnable[] runnables = new Runnable[ 2 ];
			runnables[ 0 ] = new Runnable() { 
				public void run() {
					perform(bubbleAnimation);
				}
			};
			runnables[ 1 ] = textToSpeech;
			org.lgna.common.DoTogether.invokeAndWait( runnables );
		}
		else
		{
			textToSpeech.run();
		}
		
		
	}
	
	public edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator getSpeechBubbleOriginator() {
		return this.m_originator;
	}
	
	private edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator m_originator = createOriginator();
	
	protected edu.cmu.cs.dennisc.math.Vector4 getThoughtBubbleOffset() {
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bb = ModelImp.this.getAxisAlignedMinimumBoundingBox();
		offsetAsSeenBySubject.x = ( bb.getXMinimum() + bb.getXMaximum() ) * 0.5;
		offsetAsSeenBySubject.y = bb.getYMaximum();
		offsetAsSeenBySubject.z = ( bb.getZMinimum() + bb.getZMaximum() ) * 0.5;
		offsetAsSeenBySubject.w = 1.0;
		return offsetAsSeenBySubject;
	}
	
	protected edu.cmu.cs.dennisc.math.Vector4 getSpeechBubbleOffset() {
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bb = ModelImp.this.getAxisAlignedMinimumBoundingBox();
		offsetAsSeenBySubject.x = ( bb.getXMinimum() + bb.getXMaximum() ) * 0.5;
		offsetAsSeenBySubject.y = ( bb.getYMinimum() + bb.getYMaximum() ) * 0.75;
		offsetAsSeenBySubject.z = bb.getZMinimum();
		offsetAsSeenBySubject.w = 1.0;
		return offsetAsSeenBySubject;
	}
	
	protected edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator createOriginator() {
		return new edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator() {
			public void calculate( 
					java.awt.geom.Point2D.Float out_originOfTail, 
					java.awt.geom.Point2D.Float out_bodyConnectionLocationOfTail, 
					java.awt.geom.Point2D.Float out_textBoundsOffset, 
					edu.cmu.cs.dennisc.scenegraph.graphics.Bubble bubble,
					edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
					java.awt.Rectangle actualViewport, 
					edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, 
					java.awt.geom.Dimension2D textSize 
				) {
				edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject;
				if( bubble instanceof edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble ) {
					offsetAsSeenBySubject = getSpeechBubbleOffset();
				} else if( bubble instanceof edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble ) {
					offsetAsSeenBySubject = getThoughtBubbleOffset();
				} else {
					offsetAsSeenBySubject = getThoughtBubbleOffset();
				}
				edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenByCamera = ModelImp.this.getSgComposite().transformTo_New( offsetAsSeenBySubject, sgCamera );
				//			edu.cmu.cs.dennisc.math.Vector4d offsetAsSeenByViewport = m_camera.transformToViewport( m_lookingGlass, offsetAsSeenByCamera );
				java.awt.Point p = sgCamera.transformToAWT_New( offsetAsSeenByCamera, lookingGlass );
				//			float x = (float)( offsetAsSeenByViewport.x / offsetAsSeenByViewport.w );
				//			float y = (float)( offsetAsSeenByViewport.y / offsetAsSeenByViewport.w );
				
				out_originOfTail.setLocation( p );
				out_bodyConnectionLocationOfTail.setLocation( actualViewport.getWidth()*0.05, textSize.getHeight()+actualViewport.getHeight()*0.05 );
				out_textBoundsOffset.setLocation( 0f, 0f );
			}
		};
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
