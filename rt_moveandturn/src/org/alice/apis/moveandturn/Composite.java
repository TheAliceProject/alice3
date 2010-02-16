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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*;

/**
 * @author Dennis Cosgrove
 */
@ClassTemplate(isFollowToSuperClassDesired = true, isConsumptionBySubClassDesired = true)
public abstract class Composite extends Element implements ReferenceFrame {
	public static final Double DEFAULT_DURATION = 1.0;
	public static final Double RIGHT_NOW = 0.0;
	public static final Style DEFAULT_STYLE = org.alice.apis.moveandturn.TraditionalStyle.BEGIN_AND_END_GENTLY;
	public static final Style DEFAULT_SPEED_STYLE = org.alice.apis.moveandturn.TraditionalStyle.BEGIN_AND_END_ABRUPTLY;
	public static final HowMuch DEFAULT_HOW_MUCH = HowMuch.THIS_AND_DESCENDANT_PARTS;

	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Transformable[] > COMPONENTS_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Transformable[] >( Composite.class, "Components" );

	private java.util.List< Transformable > m_components = new java.util.LinkedList< Transformable >();

	protected void alreadyAdjustedDelay( Number duration ) {
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
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public PointOfView getPointOfView( ReferenceFrame asSeenBy ) {
		return new PointOfView( getTransformation( asSeenBy ) );
	}

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void delay( Number duration ) {
		alreadyAdjustedDelay( adjustDurationIfNecessary( duration ) );
	}
	//todo 
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public void print( @edu.cmu.cs.dennisc.lang.ParameterAnnotation(isVariable = true) Object... values ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( values );
	}

	private static edu.cmu.cs.dennisc.pattern.DefaultPool< StandIn > s_standInPool = new edu.cmu.cs.dennisc.pattern.DefaultPool< StandIn >( StandIn.class );

	protected static StandIn acquireStandIn( Composite composite ) {
		StandIn rv = s_standInPool.acquire();
		rv.setVehicle( composite );
		rv.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
		return rv;
	}
	protected static void releaseStandIn( StandIn standIn ) {
		s_standInPool.release( standIn );
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public org.alice.apis.moveandturn.Composite getActualReferenceFrame( org.alice.apis.moveandturn.Composite ths ) {
		return this;
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public StandIn createOffsetStandIn( double x, double y, double z ) {
		assert Double.isNaN( x ) == false;
		assert Double.isNaN( y ) == false;
		assert Double.isNaN( z ) == false;
		return createOffsetStandIn( MoveDirection.RIGHT, x, MoveDirection.UP, y, MoveDirection.BACKWARD, z );
	}

	@Deprecated
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public Composite createOffsetStandInIfNecessary( edu.cmu.cs.dennisc.math.Tuple3 offset ) {
		if( offset != null ) {
			return createOffsetStandIn( offset.x, offset.y, offset.z );
		} else {
			return this;
		}
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public StandIn createOffsetStandIn( edu.cmu.cs.dennisc.math.Tuple3 offset ) {
		assert offset != null;
		return createOffsetStandIn( offset.x, offset.y, offset.z );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public StandIn createOffsetStandIn( edu.cmu.cs.dennisc.math.Orientation offset ) {
		StandIn standIn = new StandIn();
		standIn.setVehicle( this );
		standIn.getSGAbstractTransformable().setAxesOnly( offset, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT );
		return standIn;
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public StandIn createOffsetStandIn( edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset ) {
		StandIn standIn = new StandIn();
		standIn.setVehicle( this );
		standIn.getSGAbstractTransformable().setLocalTransformation( offset );
		return standIn;
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public StandIn createOffsetStandIn( MoveDirection direction, Number amount ) {
		StandIn standIn = new StandIn();
		standIn.setVehicle( this );
		standIn.move( direction, amount, RIGHT_NOW );
		return standIn;
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public StandIn createOffsetStandIn( MoveDirection directionA, Number amountA, MoveDirection directionB, Number amountB ) {
		StandIn standIn = new StandIn();
		standIn.setVehicle( this );
		standIn.move( directionA, amountA, RIGHT_NOW );
		standIn.move( directionB, amountB, RIGHT_NOW );
		return standIn;
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public StandIn createOffsetStandIn( MoveDirection directionA, Number amountA, MoveDirection directionB, Number amountB, MoveDirection directionC, Number amountC ) {
		StandIn standIn = new StandIn();
		standIn.setVehicle( this );
		standIn.move( directionA, amountA, RIGHT_NOW );
		standIn.move( directionB, amountB, RIGHT_NOW );
		standIn.move( directionC, amountC, RIGHT_NOW );
		return standIn;
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public void addComponent( Transformable component ) {
		synchronized( m_components ) {
			if( component.getVehicle() != this ) {
				assert component.getVehicle() == null;
				m_components.add( component );
				component.handleVehicleChange( this );
			}
		}
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public void removeComponent( Transformable component ) {
		synchronized( m_components ) {
			assert component.getVehicle() == this;
			assert m_components.contains( component );
			m_components.remove( component );
			component.handleVehicleChange( null );
		}
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public Iterable< Transformable > getComponentIterable() {
		return m_components;
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void clearComponents() {
		synchronized( m_components ) {
			java.util.ListIterator< Transformable > listIterator = m_components.listIterator();
			while( listIterator.hasNext() ) {
				//removeComponent( listIterator.next() );
				Transformable component = listIterator.next();
				assert component.getVehicle() == this;
				assert m_components.contains( component );
				component.handleVehicleChange( null );
			}
			m_components.clear();
		}
	}
	private Transformable[] getComponents( Transformable[] rv ) {
		synchronized( m_components ) {
			return m_components.toArray( rv );
		}
	}
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public Transformable[] getComponents() {
		return getComponents( new Transformable[ m_components.size() ] );
	}
	public void setComponents( Transformable... components ) {
		synchronized( m_components ) {
			clearComponents();
			for( Transformable component : components ) {
				addComponent( component );
			}
		}
	}

	protected void handleOwnerChange( SceneOwner owner ) {
		for( Transformable component : getComponents() ) {
			component.handleOwnerChange( owner );
		}
	}

	//todo: reduce visibility to protected?
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public final edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getSGReferenceFrame() {
		return getSGComposite();
	}
	//todo: reduce visibility to protected?
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public abstract edu.cmu.cs.dennisc.scenegraph.Composite getSGComposite();

	private String m_name = "<unnamed>";

	@Override
	@PropertyGetterTemplate(visibility = Visibility.PRIME_TIME)
	public String getName() {
		return m_name;
	}
	@Override
	public void setName( String name ) {
		m_name = name;
	}
	public abstract Scene getScene();
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public final AbstractCamera findFirstCamera() {
		Scene scene = this.getScene();
		if( scene != null ) {
			return scene.findFirstMatch( AbstractCamera.class );
		} else {
			return null;
		}
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public abstract SceneOwner getOwner();
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public edu.cmu.cs.dennisc.lookingglass.LookingGlass getLookingGlass() {
		SceneOwner owner = getOwner();
		if( owner != null ) {
			return owner.getOnscreenLookingGlass();
		} else {
			return null;
		}
	}

	protected double adjustDurationIfNecessary( Number duration ) {
		if( duration == RIGHT_NOW ) {
			//pass
		} else {
			double simulationSpeedFactor = getGlobalSimulationSpeedFactor();
			if( Double.isNaN( simulationSpeedFactor ) ) {
				//todo: reconsider this
				duration = RIGHT_NOW;
			} else {
				duration = duration.doubleValue() / simulationSpeedFactor;
			}
		}
		return duration.doubleValue();
	}

	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public Double getGlobalSimulationSpeedFactor() {
		SceneOwner owner = getOwner();
		if( owner != null ) {
			return owner.getSimulationSpeedFactor();
		} else {
			return Double.NaN;
		}
	}
	public void setGlobalSimulationSpeedFactor( Number simulationSpeedFactor ) {
		SceneOwner owner = getOwner();
		if( owner != null ) {
			owner.setSimulationSpeedFactor( simulationSpeedFactor.doubleValue() );
		} else {
			throw new RuntimeException();
		}
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	protected void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		SceneOwner owner = getOwner();
		if( owner != null ) {
			owner.perform( animation, animationObserver );
		} else {
			animation.complete( animationObserver );
		}
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	protected final void perform( edu.cmu.cs.dennisc.animation.Animation animation ) {
		perform( animation, null );
	}

	private <E extends Transformable> E getFirstChildNamed( Class< E > cls, String name ) {
		for( Transformable child : getComponents() ) {
			if( cls.isAssignableFrom( child.getClass() ) ) {
				if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( child.getName(), name ) ) {
					return (E)child;
				}
			}
		}
		return null;
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public <E extends Transformable> E getDescendant( Class< E > cls, String... pathOfNames ) {
		Composite rv = this;
		for( String name : pathOfNames ) {
			rv = rv.getFirstChildNamed( cls, name );
			assert rv != null;
		}
		if( cls.isAssignableFrom( rv.getClass() ) ) {
			return (E)rv;
		} else {
			return null;
		}
	}

	protected java.util.List< String[] > update( java.util.List< String[] > rv, java.util.Stack< String > path, Class< ? extends Transformable > cls ) {
		for( Transformable child : getComponents() ) {
			String name = child.getName();
			//assert name != null;
			//todo: __Unnamed%d__ ?
			path.push( name );
			child.update( rv, path, cls );
			if( cls.isAssignableFrom( child.getClass() ) ) {
				String[] array = path.toArray( new String[ path.size() ] );
				for( String s : array ) {
					assert s != null;
				}
				rv.add( array );
			}
			path.pop();
		}
		return rv;
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public java.util.List< String[] > findPathsToAllDescendants( Class< ? extends Transformable > cls ) {
		java.util.List< String[] > rv = new java.util.LinkedList< String[] >();
		java.util.Stack< String > path = new java.util.Stack< String >();
		return update( rv, path, cls );
	}
	//	public String[] getPathOfNamesToDescendant( Element descendant ) {
	//		Element e = descendant;
	//		while( e != this ) {
	//			assert e != null;
	//			e = e.getParent();
	//		}
	//		return null;
	//	}

	private static <E extends Composite> E getFirstToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, Composite component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		assert component != null;
		E rv = null;
		boolean isAcceptedByAll;
		if( isComponentACandidate ) {
			if( cls == null || cls.isAssignableFrom( component.getClass() ) ) {
				isAcceptedByAll = true;
				if( criterions == null ) {
					//pass
				} else {
					for( edu.cmu.cs.dennisc.pattern.Criterion criterion : criterions ) {
						if( criterion.accept( component ) ) {
							//pass
						} else {
							isAcceptedByAll = false;
							break;
						}
					}
				}
			} else {
				isAcceptedByAll = false;
			}
		} else {
			isAcceptedByAll = false;
		}
		if( isAcceptedByAll ) {
			rv = (E)component;
		} else {
			if( isChildACandidate ) {
				for( Transformable componentI : component.getComponentIterable() ) {
					rv = getFirstToAccept( isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, componentI, cls, criterions );
					if( rv != null ) {
						break;
					}
				}
			}
		}
		return rv;
	}

	private static <E extends Composite> void updateAllToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, java.util.List< E > list, Composite component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		assert component != null;

		if( isComponentACandidate ) {
			if( cls == null || cls.isAssignableFrom( component.getClass() ) ) {
				boolean isAcceptedByAll = true;
				if( criterions == null ) {
					//pass
				} else {
					for( edu.cmu.cs.dennisc.pattern.Criterion criterion : criterions ) {
						if( criterion.accept( component ) ) {
							//pass
						} else {
							isAcceptedByAll = false;
							break;
						}
					}
				}
				if( isAcceptedByAll ) {
					list.add( (E)component );
				}
			}
		}

		if( isChildACandidate ) {
			for( Transformable componentI : component.getComponentIterable() ) {
				updateAllToAccept( isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, list, componentI, cls, criterions );
			}
		}
	}
	private static <E extends Composite> E getFirstToAccept( HowMuch candidateMask, Composite component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return getFirstToAccept( candidateMask.isThisACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), component, cls, criterions );
	}

	private static <E extends Composite> void updateAllToAccept( HowMuch candidateMask, java.util.List< E > list, Composite component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		updateAllToAccept( candidateMask.isThisACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), list, component, cls, criterions );
	}

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public <E extends Composite> E findFirstMatch( HowMuch howMuch, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return Composite.getFirstToAccept( howMuch, this, cls, criterions );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public <E extends Composite> E findFirstMatch( Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findFirstMatch( DEFAULT_HOW_MUCH, cls, criterions );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public <E extends Composite> E findFirstMatch( Class< E > cls ) {
		return findFirstMatch( cls, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public Element findFirstMatch( edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findFirstMatch( null, criterions );
	}

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public <E extends Transformable> E findFirstDescendantNamed( Class< E > cls, String name ) {
		return findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, cls, new edu.cmu.cs.dennisc.pattern.NameEqualsCriterion( name, false ) );
	}

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public <E extends Composite> java.util.List< E > findAllMatches( HowMuch howMuch, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		java.util.List< E > list = new java.util.LinkedList< E >();
		Composite.updateAllToAccept( howMuch, list, this, cls, criterions );
		return list;
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public <E extends Composite> java.util.List< E > findAllMatches( Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findAllMatches( DEFAULT_HOW_MUCH, cls, criterions );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public <E extends Composite> java.util.List< E > findAllMatches( Class< E > cls ) {
		return findAllMatches( cls, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public java.util.List< Composite > findAllMatches( edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findAllMatches( null, criterions );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public java.util.List< Composite > findAllMatches() {
		return findAllMatches( null, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public java.awt.Point transformToAWT( java.awt.Point rv, edu.cmu.cs.dennisc.math.Point3 p, AbstractCamera camera ) {
		//todo
		edu.cmu.cs.dennisc.math.Vector4 xyzw = new edu.cmu.cs.dennisc.math.Vector4( p.x, p.y, p.z, 1.0 );
		getSGComposite().transformToAWT( rv, xyzw, camera.getLookingGlass(), camera.getSGCamera() );
		return rv;
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public java.awt.Point transformToAWT( edu.cmu.cs.dennisc.math.Point3 p, AbstractCamera camera ) {
		return transformToAWT( new java.awt.Point(), p, camera );
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.Point3 transformFromAWT( edu.cmu.cs.dennisc.math.Point3 rv, java.awt.Point p, double z, AbstractCamera camera ) {
		//todo
		edu.cmu.cs.dennisc.math.Vector4 xyzw = new edu.cmu.cs.dennisc.math.Vector4( p.x, p.y, z, 1.0 );
		getSGComposite().transformFromAWT( xyzw, p, z, camera.getLookingGlass(), camera.getSGCamera() );
		rv.set( xyzw.x / xyzw.w, xyzw.y / xyzw.w, xyzw.z / xyzw.w );
		return rv;
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.Point3 transformFromAWT( java.awt.Point p, double z, AbstractCamera camera ) {
		return transformFromAWT( new edu.cmu.cs.dennisc.math.Point3(), p, z, camera );
	}

	private java.awt.Component getOwnerComponent() {
		SceneOwner owner = this.getOwner();
		if( owner != null ) {
			edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = owner.getOnscreenLookingGlass();
			if( lg != null ) {
				return lg.getAWTComponent();
			}
		}
		return null;
	}
	private void closeProgramInCaseOfNull( Object o ) {
		if( o != null ) {
			//pass
		} else {
			SceneOwner owner = this.getOwner();
			if( owner instanceof Program ) {
				edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = owner.getOnscreenLookingGlass();
				if( lg != null ) {
					java.awt.Component component = lg.getAWTComponent();
					java.awt.Component root = javax.swing.SwingUtilities.getRoot( component );
					root.setVisible( false );
				}
			}
			throw new edu.cmu.cs.dennisc.alice.ProgramClosedException();
		}
	}

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public Boolean getBooleanFromUser( String message ) {
		org.alice.apis.moveandturn.inputpanes.BooleanInputPane inputPane = new org.alice.apis.moveandturn.inputpanes.BooleanInputPane( message );
		Boolean rv = inputPane.showInJDialog( this.getOwnerComponent() );
		this.closeProgramInCaseOfNull( rv );
		return rv;
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public String getStringFromUser( String message ) {
		org.alice.apis.moveandturn.inputpanes.StringInputPane inputPane = new org.alice.apis.moveandturn.inputpanes.StringInputPane( message );
		String rv = inputPane.showInJDialog( this.getOwnerComponent() );
		this.closeProgramInCaseOfNull( rv );
		return rv;
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public Integer getIntegerFromUser( String message ) {
		org.alice.apis.moveandturn.inputpanes.IntegerInputPane inputPane = new org.alice.apis.moveandturn.inputpanes.IntegerInputPane( message );
		Integer rv = inputPane.showInJDialog( this.getOwnerComponent() );
		this.closeProgramInCaseOfNull( rv );
		return rv;
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public Double getDoubleFromUser( String message ) {
		org.alice.apis.moveandturn.inputpanes.DoubleInputPane inputPane = new org.alice.apis.moveandturn.inputpanes.DoubleInputPane( message );
		Double rv = inputPane.showInJDialog( this.getOwnerComponent() );
		this.closeProgramInCaseOfNull( rv );
		return rv;
	}
	
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void playAudio( AudioSource audioSource ) {
		edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
		edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( audioSource.getAudioResource(), audioSource.getVolume(), audioSource.getStartTime(), audioSource.getStopTime() );
		this.perform( new edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation( player ) );		
	}
}
