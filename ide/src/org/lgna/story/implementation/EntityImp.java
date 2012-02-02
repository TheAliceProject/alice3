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

import org.lgna.story.AudioSource;

/**
 * @author Dennis Cosgrove
 */
public abstract class EntityImp implements ReferenceFrame {
	protected static final edu.cmu.cs.dennisc.scenegraph.Element.Key< EntityImp > ENTITY_IMP_KEY = edu.cmu.cs.dennisc.scenegraph.Element.Key.createInstance( "ENTITY_IMP_KEY" );
	public static EntityImp getInstance( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		return sgElement != null ? sgElement.getBonusDataFor( ENTITY_IMP_KEY ) : null;
	}
	protected void putInstance( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		sgElement.putBonusDataFor( ENTITY_IMP_KEY, this );
	}
	public static <T extends EntityImp> T getInstance( edu.cmu.cs.dennisc.scenegraph.Element sgElement, Class<T> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( getInstance( sgElement ), cls );
	}
	public static org.lgna.story.Entity getAbstractionFromSgElement( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		EntityImp imp = getInstance( sgElement );
		if( imp != null ) {
			return imp.getAbstraction();
		} else {
			return null;
		}
	}
	public static <T extends org.lgna.story.Entity> T getAbstractionFromSgElement( edu.cmu.cs.dennisc.scenegraph.Element sgElement, Class<T> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( getAbstractionFromSgElement( sgElement ), cls );
	}
	
	private String name;
	public String getName() {
		return this.name;
	}
	public void setName( String name ) {
		this.name = name;
		this.getSgComposite().setName( name + ".sgComposite" );
	}
	public Property<?> getPropertyForAbstractionGetter( java.lang.reflect.Method getterMthd ) {
		String propertyName = edu.cmu.cs.dennisc.property.PropertyUtilities.getPropertyNameForGetter( getterMthd );
		java.lang.reflect.Field fld = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getField( this.getClass(), propertyName );
		return (Property<?>)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fld, this );
	}

	//todo
//	protected abstract edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, ReferenceFrame asSeenBy );
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, org.lgna.story.implementation.ReferenceFrame asSeenBy ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "override", this );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = new edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound();
		this.updateCumulativeBound( cumulativeBound, asSeenBy );
		return cumulativeBound.getBoundingBox();
	}
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF );
	}
	
//	private edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound createCumulativeBound( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
//		java.util.List< Transformable > transformables = new java.util.LinkedList< Transformable >();
//		updateHowMuch( transformables, howMuch.isThisACandidate(), howMuch.isChildACandidate(), howMuch.isGrandchildAndBeyondACandidate() );
//		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv = new edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound();
//		ReferenceFrame actualAsSeenBy = asSeenBy.getActualReferenceFrame( this );
//
//		for( Transformable transformable : transformables ) {
//			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = transformable.getTransformation( actualAsSeenBy );
//			assert m.isNaN() == false;
//			transformable.updateCumulativeBound( rv, m, originPolicy.isOriginIncluded() );
//		}
//		return rv;
//	}
//	
//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
//		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = createCumulativeBound( asSeenBy, howMuch, originPolicy );
//		return cumulativeBound.getBoundingBox();
//	}
//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch ) {
//		return getAxisAlignedMinimumBoundingBox( asSeenBy, howMuch, DEFAULT_ORIGIN_INCLUSION_POLICY );
//	}
//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
//		return getAxisAlignedMinimumBoundingBox( asSeenBy, DEFAULT_HOW_MUCH );
//	}
//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
//		return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF );
//	}

	
	public abstract org.lgna.story.Entity getAbstraction();
	public abstract edu.cmu.cs.dennisc.scenegraph.Composite getSgComposite();
	public edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getSgReferenceFrame() {
		return this.getSgComposite();
	}
	public EntityImp getActualEntityImplementation( EntityImp ths ) {
		return this;
	}
	
	protected edu.cmu.cs.dennisc.scenegraph.Composite getSgVehicle() {
		return this.getSgComposite().getParent();
	}
	protected void setSgVehicle( edu.cmu.cs.dennisc.scenegraph.Composite sgVehicle ) {
		this.getSgComposite().setParent( sgVehicle );
	}
	
	//HACK
	private EntityImp getEntityImpForSgObject(edu.cmu.cs.dennisc.scenegraph.Composite sgObject) {
		EntityImp rv = getInstance( sgObject );
		if( rv != null ) {
			return rv;
		} else if (sgObject.getParent() != null){
			return getEntityImpForSgObject(sgObject.getParent());
		}
		return null;
	}
	
	public final EntityImp getVehicle() {
		edu.cmu.cs.dennisc.scenegraph.Composite sgVehicle = this.getSgVehicle();
		if( sgVehicle != null ) {
			EntityImp rv = getInstance( sgVehicle );
			if( rv != null ) {
				//pass
			} else {
				rv = getEntityImpForSgObject(sgVehicle);
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "No instance found for sgVehicle "+sgVehicle+". Searched parent and got "+rv );
				if (rv != null) {
					//pass
				}
				else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, sgVehicle );
				}
			}
			return rv;
		} else {
			return null;
		}
	}
	public void setVehicle( EntityImp vehicle ) {
		assert vehicle != this;
		this.setSgVehicle( vehicle != null ? vehicle.getSgComposite() : null );
	}

	public boolean isDescendantOf( EntityImp candidateAncestor ) {
		assert candidateAncestor != null : this;
		EntityImp vehicle = this.getVehicle();
		if( vehicle != null ) {
			if( vehicle == candidateAncestor ) {
				return true;
			} else {
				return vehicle.isDescendantOf( candidateAncestor );
			}
		} else {
			return false;
		}
	}

	public SceneImp getScene() {
		EntityImp vehicle = this.getVehicle();
		return vehicle != null ? vehicle.getScene() : null;
	}
	protected ProgramImp getProgram() {
		SceneImp scene = this.getScene();
		return scene != null ? scene.getProgram() : null;
	}
	
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation() {
		return this.getSgComposite().getAbsoluteTransformation();
	}
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( ReferenceFrame asSeenBy ) {
		return this.getSgComposite().getTransformation( asSeenBy.getSgReferenceFrame() );
	}

	public StandInImp createStandIn() {
		StandInImp rv = new StandInImp();
		rv.setVehicle( this );
		return rv;
	}
	public StandInImp createOffsetStandIn( double x, double y, double z ) {
		StandInImp rv = this.createStandIn();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
		m.translation.set( x, y, z );
		rv.setLocalTransformation( m );
		return rv;
	}
	
	protected static final double RIGHT_NOW = 0.0;
	protected static final double DEFAULT_DURATION = 1.0;
	protected static final edu.cmu.cs.dennisc.animation.Style DEFAULT_STYLE = edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY;
//	public static final Style DEFAULT_SPEED_STYLE = org.alice.apis.moveandturn.TraditionalStyle.BEGIN_AND_END_ABRUPTLY;
//	public static final HowMuch DEFAULT_HOW_MUCH = HowMuch.THIS_AND_DESCENDANT_PARTS;
	
	private double getSimulationSpeedFactor() {
		ProgramImp programImplementation = this.getProgram();
		if( programImplementation != null ) {
			return programImplementation.getSimulationSpeedFactor();
		} else {
			return Double.NaN;
		}
	}
	protected double adjustDurationIfNecessary( double duration ) {
		if( duration == RIGHT_NOW ) {
			//pass
		} else {
			double simulationSpeedFactor = this.getSimulationSpeedFactor();
			if( Double.isNaN( simulationSpeedFactor ) ) {
				duration = RIGHT_NOW;
			} else if( Double.isInfinite( simulationSpeedFactor ) ) {
				duration = RIGHT_NOW;
			} else {
				duration = duration / simulationSpeedFactor;
			}
		}
		return duration;
	}
	public void alreadyAdjustedDelay( double duration ) {
		if( duration == RIGHT_NOW ) {
			//pass;
		} else {
			perform( new edu.cmu.cs.dennisc.animation.DurationBasedAnimation( duration ) {
				@Override
				protected void prologue() {
				}
				@Override
				protected void setPortion( double portion ) {
				}
				@Override
				protected void epilogue() {
				}
			} );
		}
	}
	public void delay( double duration ) {
		this.alreadyAdjustedDelay( this.adjustDurationIfNecessary( duration ) );
	}
		
	public void playAudio( AudioSource audioSource ) {
		edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
		edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( audioSource.getAudioResource(), audioSource.getVolume(), audioSource.getStartTime(), audioSource.getStopTime() );
		this.perform( new edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation( player ) );		
	}

	protected void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		ProgramImp programImplementation = this.getProgram();
		if( programImplementation != null ) {
			programImplementation.perform( animation, animationObserver );
		} else {
			animation.complete( animationObserver );
		}
	}
	protected final void perform( edu.cmu.cs.dennisc.animation.Animation animation ) {
		this.perform( animation, null );
	}
	
	protected void appendRepr( StringBuilder sb ) {
	}
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		this.appendRepr( sb );
		sb.append( "]" );
		return sb.toString();
	}
}