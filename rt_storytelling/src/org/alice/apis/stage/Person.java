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
package org.alice.apis.stage;

import edu.cmu.cs.dennisc.alice.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class Person extends Model {
	private edu.cmu.cs.dennisc.nebulous.Person nebPerson;
	
	//todo: remove when bounding box works
	@Override
	protected org.alice.apis.moveandturn.graphic.Originator createOriginator() {
		return new org.alice.apis.moveandturn.graphic.Originator() {
			public java.awt.geom.Point2D calculateOriginOfTail( org.alice.apis.moveandturn.graphic.Bubble bubble, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass ) {
				double height = Person.this.getHeight();
				edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
				if( bubble instanceof org.alice.apis.moveandturn.graphic.SpeechBubble ) {
					offsetAsSeenBySubject.x = 0.0;
					offsetAsSeenBySubject.y = height*0.9;
					offsetAsSeenBySubject.z = -0.25;
				} else if( bubble instanceof org.alice.apis.moveandturn.graphic.ThoughtBubble ) {
					offsetAsSeenBySubject.x = 0.0;
					offsetAsSeenBySubject.y = height*1.1;
					offsetAsSeenBySubject.z = 0.0;
//					offsetAsSeenBySubject.x = 2.0;
//					offsetAsSeenBySubject.y = 0.25;
//					offsetAsSeenBySubject.z = 0.0;
				} else {
					offsetAsSeenBySubject.x = 0;
					offsetAsSeenBySubject.y = 0;
					offsetAsSeenBySubject.z = 0;
				}
				offsetAsSeenBySubject.w = 1.0;

				edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenByCamera = Person.this.getSGTransformable().transformTo_New( offsetAsSeenBySubject, sgCamera );
				//			edu.cmu.cs.dennisc.math.Vector4d offsetAsSeenByViewport = m_camera.transformToViewport( m_lookingGlass, offsetAsSeenByCamera );
				java.awt.Point p = sgCamera.transformToAWT_New( offsetAsSeenByCamera, lookingGlass );
				//			float x = (float)( offsetAsSeenByViewport.x / offsetAsSeenByViewport.w );
				//			float y = (float)( offsetAsSeenByViewport.y / offsetAsSeenByViewport.w );
				return new java.awt.geom.Point2D.Float( p.x, p.y );
			}
			public java.awt.geom.Point2D calculateBodyConnectionLocationOfTail( org.alice.apis.moveandturn.graphic.Bubble bubble, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass ) {
				return new java.awt.geom.Point2D.Float( 300f, 100f );
			}
			public java.awt.geom.Point2D calculateTextBoundsOffset( org.alice.apis.moveandturn.graphic.Bubble bubble, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass ) {
				return new java.awt.geom.Point2D.Float( 0f, 0f );
			}
		};
	}

//	class BodyPart extends edu.wustl.cse.ckelleher.apis.dissertation.PolygonalModel {
//		public BodyPart() {
//			int[] indices = { 0,1,2, 2,1,0 };
//			org.alice.apis.moveandturn.Polygons polygons = new org.alice.apis.moveandturn.Polygons();
//			polygons.setVertices( createBoneVertices( new edu.cmu.cs.dennisc.math.Vector3( 0, 0.1, 0 ) ) );
//			polygons.setPolygonData( indices );
//			setPolygons( polygons );
//			getSGVisual().isShowing.setValue( true );
//			getSGTransformable().localTransformation.addPropertyListener( new
//				edu.cmu.cs.dennisc.property.event.PropertyListener() {
//					public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//					}
//					public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//						getNebulousPerson().setLocalTransformationForBodyPartNamed( getName(), getLocalTransformation() );
//					}
//				}
//			);
//		}
//		private edu.cmu.cs.dennisc.scenegraph.Vertex[] createBoneVertices( edu.cmu.cs.dennisc.math.Vector3 boneAxis ) {
//			return new edu.cmu.cs.dennisc.scenegraph.Vertex[] {
//					edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZ( -0.1,0,0 ),
//					edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZ( +0.1,0,0 ),
//					edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZ( boneAxis.x, boneAxis.y, boneAxis.z ),
//			};
//		}
////		private String getChildName() {
////			org.alice.apis.moveandturn.Transformable[] components = getComponents();
////			switch( components.length ) {
////			case 0:
////				return null;
////			case 1:
////				return components[ 0 ].getName();
////			default:
////				throw new RuntimeException();
////			}
////		}
//		@Override
//		public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.ReferenceFrame asSeenBy, org.alice.apis.moveandturn.HowMuch howMuch, org.alice.apis.moveandturn.OriginInclusionPolicy originPolicy ) {
//			org.alice.apis.moveandturn.Polygons polygons = getPolygons();
//			polygons.setVertices( createBoneVertices( getNebulousPerson().getBoneAxis( getName() ) ) );
//			setPolygons( polygons );
//			//edu.cmu.cs.dennisc.print.PrintUtilities.println( polygons );
//			return super.getAxisAlignedMinimumBoundingBox( asSeenBy, howMuch, originPolicy );
//		}
//	}
//
//	private java.util.Map< String, BodyPart > m_mapNameToBodyPart = new java.util.HashMap< String, BodyPart >();

//	@Override
//	public edu.wustl.cse.ckelleher.apis.dissertation.PolygonalModel getBodyPartNamed( String name ) {
//		edu.cmu.cs.dennisc.nebulous.Person nebPerson = getNebulousPerson();
//		if( nebPerson != null ) {
//			BodyPart rv = m_mapNameToBodyPart.get( name );
//			if( rv != null ) {
//				//pass
//			} else {
//				rv = new BodyPart();
//				m_mapNameToBodyPart.put( name, rv );
//			}
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( name, rv );
//			return rv;
//		} else {
//			return null;
//		}
//	}
//	private void setUpChain( String... names ) {
//		edu.cmu.cs.dennisc.nebulous.Person nebPerson = getNebulousPerson();
//		if( nebPerson != null ) {
//			org.alice.apis.moveandturn.Composite parent = m_root;
//			for( String name : names ) {
//				edu.wustl.cse.ckelleher.apis.dissertation.PolygonalModel part = getBodyPartNamed( name );
//				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = nebPerson.getLocalTransformationForBodyPartNamed( name );
//				//edu.cmu.cs.dennisc.math.SpaceChangeUtilities.swapYAndZ( m );
//				part.setName( name );
//				part.setLocalTransformation( m );
//				parent.addComponent( part );
//				part.setVehicle( parent );
//				parent = part;
//			}
//		}
//	}
//	
//	private org.alice.apis.moveandturn.Transformable m_root = new org.alice.apis.moveandturn.Transformable(){};
	private LifeStage lifeStage;
	private Gender gender;
	public Person( LifeStage lifeStage, Gender gender ) {
		this.lifeStage = lifeStage;
		setGender( gender );
		//final String PELVIS_PART_NAME = "pelvis";
		
//		this.addComponent( m_root );
//		m_root.setVehicle( this );
//		m_root.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new edu.cmu.cs.dennisc.math.AngleInRevolutions( 0.25 ) );
//		//m_root.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new edu.cmu.cs.dennisc.math.AngleInRevolutions( 0.5 ) );
//		setUpChain( 
//				//PELVIS_PART_NAME,
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_UPPER_LEG_PART_NAME, 
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_LOWER_LEG_PART_NAME, 
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_FOOT_PART_NAME 
//		);
//		setUpChain( 
//				//PELVIS_PART_NAME,
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.RIGHT_UPPER_LEG_PART_NAME, 
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.RIGHT_LOWER_LEG_PART_NAME, 
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.RIGHT_FOOT_PART_NAME 
//		);
//		setUpChain( 
//				//PELVIS_PART_NAME,
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_UPPER_ARM_PART_NAME, 
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_LOWER_ARM_PART_NAME 
//		);
//		setUpChain( 
//				//PELVIS_PART_NAME,
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.RIGHT_UPPER_ARM_PART_NAME, 
//				edu.wustl.cse.ckelleher.apis.dissertation.Person.RIGHT_LOWER_ARM_PART_NAME 
//		);
//		
//		getBodyPartNamed( edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_UPPER_LEG_PART_NAME ).setColor( edu.cmu.cs.dennisc.color.Color4f.RED );
//		getBodyPartNamed( edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_LOWER_LEG_PART_NAME ).setColor( edu.cmu.cs.dennisc.color.Color4f.GREEN );
//		getBodyPartNamed( edu.wustl.cse.ckelleher.apis.dissertation.Person.LEFT_FOOT_PART_NAME ).setColor( edu.cmu.cs.dennisc.color.Color4f.BLUE );
//
//		for( BodyPart bodyPart : m_mapNameToBodyPart.values() ) {
//			bodyPart.getAxisAlignedMinimumBoundingBox();
//		}
	}
	public final LifeStage getLifeStage() {
		return lifeStage;
	}
	public final Gender getGender() {
		return gender;
	}
	public void setGender( Gender gender ) {
		this.gender = gender;
		if( this.gender != null ) {
			getNebPerson().setGender( this.gender );
		}
	}
	
	

	@Override
	protected void createSGGeometryIfNecessary() {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "createSGGeometryIfNecessary", m_lifeStage, m_gender );
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return getNebPerson();
	}
	@Override
	protected edu.cmu.cs.dennisc.nebulous.Model getNebulousModel() {
		return getNebPerson();
	}
	protected edu.cmu.cs.dennisc.nebulous.Person getNebulousPerson() {
		return getNebPerson();
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.nebulous.Person getNebPerson() {
		if( this.nebPerson != null ) {
			//pass
		} else {
			try {
				if( this.lifeStage != null ) {
					this.nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( this );
				}
			} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
				throw new RuntimeException( lre );
			}
		}
		return this.nebPerson;
	}
	
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public abstract Boolean isPregnant();

	private int m_atomicChangeCount = 0;
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void pushAtomicChange() {
		m_atomicChangeCount++;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void popAtomicChange() {
		m_atomicChangeCount--;
	}

//	@Override
//	/*package-private*/void handleAutomaticDisplayCompleted() {
//		super.handleAutomaticDisplayCompleted();
//		if( m_atomicChangeCount == 0 ) {
//			m_nebPerson.updateIfAppropriate();
//		}
//	}

	protected void handleIsPregnantChange() {
		//todo
	}
	
	
	
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	@Override
	public boolean isEasilyTransformed() {
		return true;
	}

	private Outfit m_outfit = null;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Outfit getOutfit() {
		return m_outfit;
	}
	public void setOutfit( Outfit outfit ) {
		assert outfit != null;
		m_outfit = outfit;
		getNebPerson().setOutfit( outfit );
	}
	
	private SkinTone m_skinTone = null;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public SkinTone getSkinTone() {
		return m_skinTone;
	}
	public void setSkinTone( SkinTone skinTone ) {
		assert skinTone != null;
		m_skinTone = skinTone;
		getNebPerson().setSkinTone( m_skinTone );
	}


	private double m_fitnessLevel = 0.5;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getFitnessLevel() {
		return m_fitnessLevel;
	}
//	public void setFitnessLevel( Number fitnessLevel ) {
//		assert fitnessLevel != null;
//		m_fitnessLevel = fitnessLevel.doubleValue();
//		getNebPerson().setFitnessLevel( m_fitnessLevel );
//	}
	
	public void setFitnessLevel( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=org.alice.apis.moveandturn.Portion.class )
			final Number fitnessLevel,
			Number duration, 
			final org.alice.apis.moveandturn.Style style
		) {
		final double actualDuration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
			m_fitnessLevel = fitnessLevel.doubleValue();
			getNebPerson().setFitnessLevel( m_fitnessLevel );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( actualDuration, style, getFitnessLevel(), fitnessLevel.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_fitnessLevel = d.doubleValue();
					getNebPerson().setFitnessLevel( m_fitnessLevel );
				}
			} );
		}
	}
	public void setFitnessLevel( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=org.alice.apis.moveandturn.Portion.class )
			Number fitnessLevel,
			Number duration 
		) {
		setFitnessLevel( fitnessLevel, duration, DEFAULT_STYLE );
	}
	public void setFitnessLevel( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=org.alice.apis.moveandturn.Portion.class )
			Number fitnessLevel
		) {
		setFitnessLevel( fitnessLevel, DEFAULT_DURATION );
	}
	
	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	@Deprecated
	public void setFitnessLevel( FitnessLevel fitnessLevel ) {
		double d;
		if( fitnessLevel == FitnessLevel.SOFT ) {
			d = 1.0;
		} else if( fitnessLevel == FitnessLevel.NORMAL ) {
			d = 0.5;
		} else {
			d = 0.0;
		}
		setFitnessLevel( d );
	}
	
	
	private EyeColor m_eyeColor = null;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public EyeColor getEyeColor() {
		return m_eyeColor;
	}
	public void setEyeColor( EyeColor eyeColor ) {
		assert eyeColor != null;
		m_eyeColor = eyeColor;
		getNebPerson().setEyeColor( m_eyeColor );
	}
	
	private Hair m_hair = null;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Hair getHair() {
		return m_hair;
	}
	public void setHair( Hair hair ) {
		assert hair != null;
		m_hair = hair;
		getNebPerson().setHair( hair );
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public final void setInitialPose() {
		FiniteStateMachine.State neutralState = getNeutralState();
		assert neutralState != null;
		performStateTransition( neutralState );
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setPose( CannedAnimation cannedAnimation, int frameIndex ) {
		cannedAnimation.update( 0.0, null );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setPose( CannedAnimation cannedAnimation ) {
		setPose( cannedAnimation, 0 );
	}
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setPose( String path, int frameIndex ) {
//		setPose( new CannedAnimation( path, this ), frameIndex );
//	}
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setPose( String path ) {
//		setPose( path, 0 );
//	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isFacing( Person other ) {
		return other.getPosition( this ).z < 0.0;
	}

	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.ReferenceFrame asSeenBy, org.alice.apis.moveandturn.HowMuch howMuch, org.alice.apis.moveandturn.OriginInclusionPolicy originPolicy ) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox rv = new edu.cmu.cs.dennisc.math.AxisAlignedBox();
		double x = 0.208;
		double y = 1.7;
		double z = 0.131;
		rv.setMinimum( -x, 0.0, -z );
		rv.setMaximum( +x, y, +z );
//		final double RADIUS = 0.4;
//		final double HEIGHT = 3.0;
//		rv.setMinimum( -RADIUS, 0.0, -RADIUS );
//		rv.setMaximum( +RADIUS, HEIGHT, +RADIUS );
		return rv;
	}
	
}
