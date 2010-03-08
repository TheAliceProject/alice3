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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class Transformable extends AbstractTransformable {

	private edu.cmu.cs.dennisc.scenegraph.Transformable m_sgTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();

	private edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator m_originator = createOriginator();
	
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
				edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
				edu.cmu.cs.dennisc.math.AxisAlignedBox bb = Transformable.this.getAxisAlignedMinimumBoundingBox();
				if( bubble instanceof edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble ) {
					offsetAsSeenBySubject.x = ( bb.getXMinimum() + bb.getXMaximum() ) * 0.5;
					offsetAsSeenBySubject.y = ( bb.getYMinimum() + bb.getYMaximum() ) * 0.75;
					offsetAsSeenBySubject.z = bb.getZMinimum();
				} else if( bubble instanceof edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble ) {
					offsetAsSeenBySubject.x = ( bb.getXMinimum() + bb.getXMaximum() ) * 0.5;
					offsetAsSeenBySubject.y = bb.getYMaximum();
					offsetAsSeenBySubject.z = ( bb.getZMinimum() + bb.getZMaximum() ) * 0.5;
				} else {
					offsetAsSeenBySubject.x = ( bb.getXMinimum() + bb.getXMaximum() ) * 0.5;
					offsetAsSeenBySubject.y = ( bb.getYMinimum() + bb.getYMaximum() ) * 0.5;
					offsetAsSeenBySubject.z = ( bb.getZMinimum() + bb.getZMaximum() ) * 0.5;
				}
				offsetAsSeenBySubject.w = 1.0;
	
				edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenByCamera = Transformable.this.getSGTransformable().transformTo_New( offsetAsSeenBySubject, sgCamera );
				//			edu.cmu.cs.dennisc.math.Vector4d offsetAsSeenByViewport = m_camera.transformToViewport( m_lookingGlass, offsetAsSeenByCamera );
				java.awt.Point p = sgCamera.transformToAWT_New( offsetAsSeenByCamera, lookingGlass );
				//			float x = (float)( offsetAsSeenByViewport.x / offsetAsSeenByViewport.w );
				//			float y = (float)( offsetAsSeenByViewport.y / offsetAsSeenByViewport.w );
				
				out_originOfTail.setLocation( p );
				out_bodyConnectionLocationOfTail.setLocation( 200f, 100f );
				out_textBoundsOffset.setLocation( 0f, 0f );
			}
		};
	}

	public Transformable() {
		putElement( m_sgTransformable );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSGAbstractTransformable() {
		return m_sgTransformable;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.scenegraph.Transformable getSGTransformable() {
		return m_sgTransformable;
	}
	
	private java.util.List< org.alice.apis.moveandturn.event.KeyListener > keyListeners = new java.util.LinkedList< org.alice.apis.moveandturn.event.KeyListener >();
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void addKeyListener( org.alice.apis.moveandturn.event.KeyListener keyListener ) {
		synchronized( this.keyListeners ) {
			this.keyListeners.add( keyListener );
		}
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void removeKeyListener( org.alice.apis.moveandturn.event.KeyListener keyListener ) {
		synchronized( this.keyListeners ) {
			this.keyListeners.remove( keyListener );
		}
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public java.util.List< org.alice.apis.moveandturn.event.KeyListener > getKeyListeners() {
		return this.keyListeners;
	}

	private boolean m_isRealized = false;
	protected void realize() {
		m_isRealized = true;
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void realizeIfNecessary() {
		if( m_isRealized ) {
			//pass
		} else {
			this.realize();
		}
	}

	@Override
	public void setName( String name ) {
		super.setName( name );
		m_sgTransformable.setName( name + ".m_sgTransformable" );
	}

	@Override
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Composite getVehicle() {
		edu.cmu.cs.dennisc.scenegraph.Composite sgVehicle = m_sgTransformable.getParent();
		return (Composite)getElement( sgVehicle );
	}
	public void setVehicle( Composite vehicle ) {
		// assert that vehicle is not this or a descendant of this
		Composite o = vehicle;
		while( true ) {
			assert o != this;
			if( o == null ) {
				break;
			}
			if( o instanceof Transformable ) {
				o = ((Transformable)o).getVehicle();
			} else {
				break;
			}
		}

		Composite previousVehicle = getVehicle();

		edu.cmu.cs.dennisc.math.AffineMatrix4x4 absolute = null;
		if( previousVehicle != null ) {
			absolute = m_sgTransformable.getTransformation( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
			previousVehicle.removeComponent( this );
		}
		
		if( vehicle != null ) {
			this.realizeIfNecessary();
			vehicle.addComponent( this );
			if( absolute != null ) {
				m_sgTransformable.setTransformation( absolute, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
			}
		}
	}
	protected void handleVehicleChange( Composite vehicle ) {
		if( vehicle != null ) {
			this.realizeIfNecessary();
	
//			if( previousVehicle != null ) {
//				absolute = m_sgTransformable.getTransformation( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//				//previousVehicle.removeComponent( this );
//			}
			m_sgTransformable.setParent( vehicle.getSGComposite() );
//			if( previousVehicle != null ) {
//				m_sgTransformable.setTransformation( absolute, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//			}
	
		} else {
			m_sgTransformable.setParent( null );
		}
	}
	public static final OriginInclusionPolicy DEFAULT_ORIGIN_INCLUSION_POLICY = OriginInclusionPolicy.EXCLUDE_ORIGINS;
	
	protected java.util.List< Transformable > updateHowMuch( java.util.List< Transformable > rv, boolean isThisIncluded, boolean isChildIncluded, boolean isGrandchildAndBeyondACandidate ) {
		if( isThisIncluded ) {
			rv.add( this );
		}
		if( isChildIncluded ) {
			for( Transformable child : getComponents() ) {
				child.updateHowMuch( rv, true, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate );
			}
		}
		return rv;
	}
	
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans, boolean isOriginIncluded ) {
		if( isOriginIncluded ) {
			rv.addOrigin( trans );
		}
		return rv;
	}
	
	private edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound createCumulativeBound( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
		java.util.List< Transformable > transformables = new java.util.LinkedList< Transformable >();
		updateHowMuch( transformables, howMuch.isThisACandidate(), howMuch.isChildACandidate(), howMuch.isGrandchildAndBeyondACandidate() );
		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv = new edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound();
		ReferenceFrame actualAsSeenBy = asSeenBy.getActualReferenceFrame( this );

		for( Transformable transformable : transformables ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = transformable.getTransformation( actualAsSeenBy );
			assert m.isNaN() == false;
			transformable.updateCumulativeBound( rv, m, originPolicy.isOriginIncluded() );
		}
		return rv;
	}
	
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = createCumulativeBound( asSeenBy, howMuch, originPolicy );
		return cumulativeBound.getBoundingBox();
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch ) {
		return getAxisAlignedMinimumBoundingBox( asSeenBy, howMuch, DEFAULT_ORIGIN_INCLUSION_POLICY );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
		return getAxisAlignedMinimumBoundingBox( asSeenBy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF );
	}

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = createCumulativeBound( asSeenBy, howMuch, originPolicy );
		return cumulativeBound.getBoundingSphere();
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( ReferenceFrame asSeenBy, HowMuch howMuch ) {
		return getBoundingSphere( asSeenBy, howMuch, DEFAULT_ORIGIN_INCLUSION_POLICY );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( ReferenceFrame asSeenBy ) {
		return getBoundingSphere( asSeenBy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere() {
		return getBoundingSphere( AsSeenBy.SELF );
	}
	
	private org.alice.apis.moveandturn.Font DEFAULT_FONT = new org.alice.apis.moveandturn.Font().deriveScaledFont( 2.0f );
	
	protected void displayBubble( edu.cmu.cs.dennisc.scenegraph.graphics.Bubble bubble, Number duration ) {
		SceneOwner owner = this.getOwner();
		if( owner != null ) {
			perform( new org.alice.apis.moveandturn.graphic.animation.BubbleAnimation( this, 0.2, duration.doubleValue(), 0.2, bubble ) );
		} else {
			//todo
			javax.swing.JOptionPane.showMessageDialog( null, "unable to display bubble" );
		}
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void say( String text, Number duration, org.alice.apis.moveandturn.Font font, Color textColor, Color bubbleFillColor, Color bubbleOutlineColor ) {
		edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble bubble = new edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble();
		bubble.text.setValue(text);
		bubble.font.setValue(font.getAsAWTFont());
		bubble.textColor.setValue(textColor.getInternal());
		bubble.fillColor.setValue(bubbleFillColor.getInternal());
		bubble.outlineColor.setValue(bubbleOutlineColor.getInternal());
		bubble.originator.setValue( m_originator );
		displayBubble( bubble, duration );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void say( String text, Number duration, org.alice.apis.moveandturn.Font font, Color textColor, Color bubbleFillColor ) {
		say( text, duration, font, textColor, bubbleFillColor, Color.BLACK );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void say( String text, Number duration, org.alice.apis.moveandturn.Font font, Color textColor ) {
		say( text, duration, font, textColor, Color.WHITE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void say( String text, Number duration, org.alice.apis.moveandturn.Font font ) {
		say( text, duration, font, Color.BLACK );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void say( String text, Number duration ) {
		say( text, duration, DEFAULT_FONT );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void say( String text ) {
		say( text, DEFAULT_DURATION );
	}

	//todo: default duration based on number of words?  syllables?

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void think( String text, Number duration, org.alice.apis.moveandturn.Font font, Color textColor, Color bubbleFillColor, Color bubbleOutlineColor ) {
		edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble bubble = new edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble();
		bubble.text.setValue(text);
		bubble.font.setValue(font.getAsAWTFont());
		bubble.textColor.setValue(textColor.getInternal());
		bubble.fillColor.setValue(bubbleFillColor.getInternal());
		bubble.outlineColor.setValue(bubbleOutlineColor.getInternal());
		bubble.originator.setValue( m_originator );
		displayBubble( bubble, duration );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void think( String text, Number duration, org.alice.apis.moveandturn.Font font, Color textColor, Color bubbleFillColor ) {
		think( text, duration, font, textColor, bubbleFillColor, Color.BLACK );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void think( String text, Number duration, org.alice.apis.moveandturn.Font font, Color textColor ) {
		think( text, duration, font, textColor, Color.WHITE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void think( String text, Number duration, org.alice.apis.moveandturn.Font font ) {
		think( text, duration, font, Color.BLACK );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void think( String text, Number duration ) {
		think( text, duration, DEFAULT_FONT );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void think( String text ) {
		think( text, DEFAULT_DURATION );
	}
	
	
	protected void applyScale( edu.cmu.cs.dennisc.math.Vector3 axis, boolean isScootDesired ) {
		if( isScootDesired ) {
			//todo?
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getSGTransformable().localTransformation.getValue();
			m.translation.multiply( axis );
			getSGTransformable().localTransformation.setValue( m );
		}
	}

	protected void applyScale( final edu.cmu.cs.dennisc.math.Vector3 axis, Number duration, HowMuch howMuch, final Style style ) {
		class ScaleAnimation extends edu.cmu.cs.dennisc.math.animation.Vector3Animation {
			private edu.cmu.cs.dennisc.math.Vector3 m_vPrev = new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 );
			private edu.cmu.cs.dennisc.math.Vector3 m_vBuffer = new edu.cmu.cs.dennisc.math.Vector3();

			private Transformable m_subject;
			private boolean m_isScootDesired;
			public ScaleAnimation( double duration, Style style, edu.cmu.cs.dennisc.math.Vector3 axis, Transformable subject, boolean isScootDesired ) {
				super( duration, style, new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 ), axis );
				m_subject = subject;
				m_isScootDesired = isScootDesired;
			}
			@Override

			protected void updateValue( edu.cmu.cs.dennisc.math.Vector3 v ) {
				edu.cmu.cs.dennisc.math.Vector3.setReturnValueToDivision( m_vBuffer, v, m_vPrev );
				m_subject.applyScale( m_vBuffer, m_isScootDesired );
				m_vPrev.set( v );
			}
		}

		final double actualDuration = adjustDurationIfNecessary( duration );
		java.util.List< Transformable > transformables = new java.util.LinkedList< Transformable >();
		updateHowMuch( transformables, howMuch.isThisACandidate(), howMuch.isChildACandidate(), howMuch.isGrandchildAndBeyondACandidate() );
		
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
			for( Transformable transformable : transformables ) {
				transformable.applyScale( axis, transformable != this );
			}
		} else {
			Runnable[] runnables = new Runnable[ transformables.size() ];
			int i = 0;
			for( final Transformable transformable : transformables ) {
				runnables[ i++ ] = new Runnable() { 
					public void run() {
						perform( new ScaleAnimation( actualDuration, style, axis, transformable, transformable!=Transformable.this ) );
					}
				};
			}
			org.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
		}
	}
	
	protected enum Dimension {
		LEFT_TO_RIGHT( true,  false, false ),
		TOP_TO_BOTTOM( false, true,  false ),
		FRONT_TO_BACK( false, false, true );
		
		private boolean m_isXScaled;
		private boolean m_isYScaled;
		private boolean m_isZScaled;

		private Dimension( boolean isXScaled, boolean isYScaled, boolean isZScaled ) {
			m_isXScaled = isXScaled;
			m_isYScaled = isYScaled;
			m_isZScaled = isZScaled;
			assert m_isXScaled ^ m_isYScaled ^ m_isZScaled;
		}
		
		public edu.cmu.cs.dennisc.math.Vector3 getResizeAxis( edu.cmu.cs.dennisc.math.Vector3 rv, double amount, ResizePolicy resizePolicy ) {
			boolean isVolumePreserved = resizePolicy.isVolumePreserved();
			//todo: center around 0 as opposed to 1?
			assert amount > 0;
			
			double x;
			double y;
			double z;

			if( isVolumePreserved ) {
				double squash = 1.0/Math.sqrt( amount );
				if( m_isXScaled ) {
					x = amount;
					y = squash;
					z = squash;
				} else if( m_isYScaled ) {
					x = squash;
					y = amount;
					z = squash;
				} else if( m_isZScaled ) {
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
				if( m_isXScaled ) {
					x = amount;
				}
				if( m_isYScaled ) {
					y = amount;
				}
				if( m_isZScaled ) {
					z = amount;
				}
			}

			rv.set( x, y, z );
			return rv;
		}
		public edu.cmu.cs.dennisc.math.Vector3 getResizeAxis( double amount, ResizePolicy resizePolicy ) {
			return getResizeAxis( edu.cmu.cs.dennisc.math.Vector3.createNaN(), amount, resizePolicy );
		}
	}
	

	private AxisAlignedBoundingBoxDecorator m_boundingBoxDecorator;

	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public Boolean isBoundingBoxShowing() {
		return m_boundingBoxDecorator != null && m_boundingBoxDecorator.isShowing();
	}
	public void setBoundingBoxShowing( Boolean isBoundingBoxShowing ) {
		if( isBoundingBoxShowing ) {
			if( m_boundingBoxDecorator != null ) {
				//pass
			} else {
				m_boundingBoxDecorator = new AxisAlignedBoundingBoxDecorator( this, this );
			}
			m_boundingBoxDecorator.setShowing( true );
		} else {
			if( m_boundingBoxDecorator != null ) {
				m_boundingBoxDecorator.setShowing( false );
			}
		}
	}

	private BoundingSphereDecorator m_boundingSphereDecorator;

	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public Boolean isBoundingSphereShowing() {
		return m_boundingSphereDecorator != null && m_boundingSphereDecorator.isShowing();
	}
	public void setBoundingSphereShowing( Boolean isBoundingSphereShowing ) {
		if( isBoundingSphereShowing ) {
			if( m_boundingSphereDecorator != null ) {
				//pass
			} else {
				m_boundingSphereDecorator = new BoundingSphereDecorator( this );
			}
			m_boundingSphereDecorator.setShowing( true );
		} else {
			if( m_boundingSphereDecorator != null ) {
				m_boundingSphereDecorator.setShowing( false );
			}
		}
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isWithinThresholdOf( Number threshold, Model other ) {
		return getDistanceTo( other ) < threshold.doubleValue();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isAtLeastThresholdAwayFrom( Number threshold, Model other ) {
		return getDistanceTo( other ) >= threshold.doubleValue();
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDistanceTo( Model other ) {
		assert other != null;
		return getTransformation( other ).translation.calculateMagnitude();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDistanceToTheLeftOf( Model other ) {
		assert other != null;
		return -getTransformation( other ).translation.x;
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDistanceToTheRightOf( Model other ) {
		assert other != null;
		return getTransformation( other ).translation.x;
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDistanceAbove( Model other ) {
		assert other != null;
		return getTransformation( other ).translation.y;
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDistanceBelow( Model other ) {
		assert other != null;
		return -getTransformation( other ).translation.y;
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDistanceInFrontOf( Model other ) {
		assert other != null;
		return -getTransformation( other ).translation.z;
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDistanceBehind( Model other ) {
		assert other != null;
		return getTransformation( other ).translation.z;
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getWidth() {
		return getAxisAlignedMinimumBoundingBox().getWidth();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getHeight() {
		return getAxisAlignedMinimumBoundingBox().getHeight();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDepth() {
		return getAxisAlignedMinimumBoundingBox().getDepth();
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isSmallerThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getVolume() < bbOther.getVolume();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isLargerThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getVolume() > bbOther.getVolume();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isNarrowerThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getWidth() < bbOther.getWidth();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isWiderThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getWidth() > bbOther.getWidth();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isShorterThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getHeight() < bbOther.getHeight();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isTallerThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getHeight() > bbOther.getHeight();
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isThinnerThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getDepth() < bbOther.getDepth();
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isThickerThan( Model other ) {
		assert other != null;
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbThis = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbOther = other.getAxisAlignedMinimumBoundingBox();
		return bbThis.getDepth() > bbOther.getDepth();
	}

	private boolean isToCertainSideOf( Model other, Model asSeenBy, MoveDirection direction ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 thisTransformation = getTransformation( asSeenBy );
		edu.cmu.cs.dennisc.math.AxisAlignedBox thisBB = getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.Hexahedron thisHexahedron = thisBB.getHexahedron();
		thisHexahedron.transform( thisTransformation );
		edu.cmu.cs.dennisc.math.AxisAlignedBox thisTransformedBB = thisHexahedron.getAxisAlignedMinimumBoundingBox(); 

		edu.cmu.cs.dennisc.math.AffineMatrix4x4 otherTransformation = other.getTransformation( asSeenBy );
		edu.cmu.cs.dennisc.math.AxisAlignedBox otherBB = other.getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.Hexahedron otherHexahedron = otherBB.getHexahedron();
		otherHexahedron.transform( otherTransformation );
		edu.cmu.cs.dennisc.math.AxisAlignedBox otherTransformedBB = otherHexahedron.getAxisAlignedMinimumBoundingBox(); 

		edu.cmu.cs.dennisc.math.Vector3 axis = direction.getAxis();
		if( axis.isPositiveXAxis() ) {
			return thisTransformedBB.getXMinimum() > otherTransformedBB.getXMaximum();
		} else if( axis.isNegativeXAxis() ) {
			return thisTransformedBB.getXMaximum() < otherTransformedBB.getXMinimum();
		} else if( axis.isPositiveYAxis() ) {
			return thisTransformedBB.getYMinimum() > otherTransformedBB.getYMaximum();
		} else if( axis.isNegativeYAxis() ) {
			return thisTransformedBB.getYMaximum() < otherTransformedBB.getYMinimum();
		} else if( axis.isPositiveZAxis() ) {
			return thisTransformedBB.getZMinimum() > otherTransformedBB.getZMaximum();
		} else if( axis.isNegativeZAxis() ) {
			return thisTransformedBB.getZMaximum() < otherTransformedBB.getZMinimum();
		} else {
			throw new RuntimeException();
		}
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isToTheLeftOf( Model other, Model asSeenBy ) {
		return isToCertainSideOf( other, asSeenBy, MoveDirection.LEFT );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public Boolean isToTheLeftOf( Model other ) {
		return isToTheLeftOf( other, other );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isToTheRightOf( Model other, Model asSeenBy ) {
		return isToCertainSideOf( other, asSeenBy, MoveDirection.RIGHT );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public Boolean isToTheRightOf( Model other ) {
		return isToTheRightOf( other, other );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isAbove( Model other, Model asSeenBy ) {
		return isToCertainSideOf( other, asSeenBy, MoveDirection.UP );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public Boolean isAbove( Model other ) {
		return isAbove( other, other );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isBelow( Model other, Model asSeenBy ) {
		return isToCertainSideOf( other, asSeenBy, MoveDirection.DOWN );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public Boolean isBelow( Model other ) {
		return isBelow( other, other );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isInFrontOf( Model other, Model asSeenBy ) {
		return isToCertainSideOf( other, asSeenBy, MoveDirection.FORWARD );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public Boolean isInFrontOf( Model other ) {
		return isInFrontOf( other, other );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isBehind( Model other, Model asSeenBy ) {
		return isToCertainSideOf( other, asSeenBy, MoveDirection.BACKWARD );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public Boolean isBehind( Model other ) {
		return isBehind( other, other );
	}


	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resize( Number amount, Number duration, HowMuch howMuch, Style style ) {
		applyScale( new edu.cmu.cs.dennisc.math.Vector3( amount.doubleValue(), amount.doubleValue(), amount.doubleValue() ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resize( Number amount, Number duration, HowMuch howMuch ) {
		resize( amount, duration, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resize( Number amount, Number duration ) {
		resize( amount, duration, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resize( Number amount ) {
		resize( amount, DEFAULT_DURATION );
	}
	
	public static final ResizePolicy DEFAULT_RESIZE_POLICY = ResizePolicy.PRESERVE_VOLUME;
	public static final SetSizePolicy DEFAULT_SET_SIZE_POLICY = SetSizePolicy.PRESERVE_ASPECT_RATIO;

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeWidth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch, Style style ) {
		applyScale( Dimension.LEFT_TO_RIGHT.getResizeAxis( amount.doubleValue(), resizePolicy ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch ) {
		resizeWidth( amount, duration, resizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount, Number duration, ResizePolicy resizePolicy ) {
		resizeWidth( amount, duration, resizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount, Number duration ) {
		resizeWidth( amount, duration, DEFAULT_RESIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount ) {
		resizeWidth( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeHeight( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch, Style style ) {
		applyScale(  Dimension.TOP_TO_BOTTOM.getResizeAxis( amount.doubleValue(), resizePolicy ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch ) {
		resizeHeight( amount, duration, resizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount, Number duration, ResizePolicy resizePolicy ) {
		resizeHeight( amount, duration, resizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount, Number duration ) {
		resizeHeight( amount, duration, DEFAULT_RESIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount ) {
		resizeHeight( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeDepth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch, Style style ) {
		applyScale( Dimension.FRONT_TO_BACK.getResizeAxis( amount.doubleValue(), resizePolicy ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch ) {
		resizeDepth( amount, duration, resizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount, Number duration, ResizePolicy resizePolicy ) {
		resizeDepth( amount, duration, resizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount, Number duration ) {
		resizeDepth( amount, duration, DEFAULT_RESIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount ) {
		resizeDepth( amount, DEFAULT_DURATION );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setWidth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch, Style style ) {
		double width = this.getWidth();
		if( width > 0.0 ) {
			double factor = amount.doubleValue() / width;
			assert Double.isNaN( factor ) == false;
			if( setSizePolicy.isAspectRatioPreserved() ) {
				resize( factor, duration, howMuch, style );
			} else {
				resizeWidth( factor, duration, setSizePolicy.getResizePolicy(), howMuch, style );
			}
		} else {
			throw new ArithmeticException( edu.cmu.cs.dennisc.alice.virtualmachine.ExceptionDetailUtilities.createExceptionDetail( "unable to set the width of object that has zero width to begin with.") );
		}
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch ) {
		setWidth( amount, duration, setSizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount, Number duration, SetSizePolicy setSizePolicy ) {
		setWidth( amount, duration, setSizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount, Number duration ) {
		setWidth( amount, duration, DEFAULT_SET_SIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount ) {
		setWidth( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setHeight( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch, Style style ) {
		double height = this.getHeight();
		if( height > 0.0 ) {
			double factor = amount.doubleValue() / height;
			assert Double.isNaN( factor ) == false;
			if( setSizePolicy.isAspectRatioPreserved() ) {
				resize( factor, duration, howMuch, style );
			} else {
				resizeHeight( factor, duration, setSizePolicy.getResizePolicy(), howMuch, style );
			}
		} else {
			throw new ArithmeticException( edu.cmu.cs.dennisc.alice.virtualmachine.ExceptionDetailUtilities.createExceptionDetail( "unable to set the height of object that has zero height to begin with.") );
		}
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch ) {
		setHeight( amount, duration, setSizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount, Number duration, SetSizePolicy setSizePolicy ) {
		setHeight( amount, duration, setSizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount, Number duration ) {
		setHeight( amount, duration, DEFAULT_SET_SIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount ) {
		setHeight( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setDepth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch, Style style ) {
		double depth = this.getDepth();
		if( depth > 0.0 ) {
			double factor = amount.doubleValue() / depth;
			assert Double.isNaN( factor ) == false;
			if( setSizePolicy.isAspectRatioPreserved() ) {
				resize( factor, duration, howMuch, style );
			} else {
				resizeDepth( factor, duration, setSizePolicy.getResizePolicy(), howMuch, style );
			}
		} else {
			throw new ArithmeticException( edu.cmu.cs.dennisc.alice.virtualmachine.ExceptionDetailUtilities.createExceptionDetail( "unable to set the depth of object that has zero depth to begin with.") );
		}
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch ) {
		setDepth( amount, duration, setSizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount, Number duration, SetSizePolicy setSizePolicy ) {
		setDepth( amount, duration, setSizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount, Number duration ) {
		setDepth( amount, duration, DEFAULT_SET_SIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount ) {
		setDepth( amount, DEFAULT_DURATION );
	}
}
