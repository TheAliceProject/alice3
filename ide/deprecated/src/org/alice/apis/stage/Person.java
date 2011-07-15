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
package org.lookingglassandalice.storytelling;

import org.alice.apis.moveandturn.AngleInRevolutions;
import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.ReferenceFrame;
import org.alice.apis.moveandturn.RollDirection;
import org.alice.apis.moveandturn.Style;
import org.alice.apis.moveandturn.TurnDirection;

import edu.cmu.cs.dennisc.alice.annotations.*;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Joint;

/**
 * @author Dennis Cosgrove
 */
public abstract class Person extends Model {
	private edu.cmu.cs.dennisc.nebulous.Person nebPerson;
	
	//todo: remove when bounding box works
	@Override
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
					java.awt.geom.Dimension2D textSize ) {
				double height = Person.this.getHeight();
				edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
				if( bubble instanceof edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble ) {
					offsetAsSeenBySubject.x = 0.0;
					offsetAsSeenBySubject.y = height*0.9;
					offsetAsSeenBySubject.z = -0.25;
				} else if( bubble instanceof edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble ) {
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
				
				java.awt.Point p = sgCamera.transformToAWT_New( offsetAsSeenByCamera, lookingGlass );
				//			float x = (float)( offsetAsSeenByViewport.x / offsetAsSeenByViewport.w );
				//			float y = (float)( offsetAsSeenByViewport.y / offsetAsSeenByViewport.w );
				
				out_originOfTail.setLocation( p );
				out_bodyConnectionLocationOfTail.setLocation( 0f, 0f );
				out_textBoundsOffset.setLocation( 0f, 0f );
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

	public final Gender getGender() {
		return gender;
	}
	public void setGender( Gender gender ) {
		if( this.gender != gender ) {
			this.gender = gender;
			if( this.gender != null ) {
				getNebPerson().setGender( this.gender );
			}
		}
	}

	private Outfit m_outfit = null;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Outfit getOutfit() {
		return m_outfit;
	}
	public void setOutfit( Outfit outfit ) {
		assert outfit != null;
		if( m_outfit != outfit ) {
			m_outfit = outfit;
			getNebPerson().setOutfit( outfit );
		}
	}
	
	private SkinTone m_skinTone = null;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public SkinTone getSkinTone() {
		return m_skinTone;
	}
	public void setSkinTone( SkinTone skinTone ) {
		assert skinTone != null;
		if( m_skinTone != skinTone ) {
			m_skinTone = skinTone;
			getNebPerson().setSkinTone( m_skinTone );
		}
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
	
	private void setFitnessLevelRightNow( double fitnessLevel ) {
		if( m_fitnessLevel != fitnessLevel ) {
			m_fitnessLevel = fitnessLevel;
			getNebPerson().setFitnessLevel( m_fitnessLevel );
		}
	}
	public void setFitnessLevel( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=org.alice.apis.moveandturn.Portion.class )
			final Number fitnessLevel,
			Number duration, 
			final org.alice.apis.moveandturn.Style style
		) {
		final double actualDuration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
			this.setFitnessLevelRightNow( fitnessLevel.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( actualDuration, style, getFitnessLevel(), fitnessLevel.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					Person.this.setFitnessLevelRightNow( d );
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
		if( m_eyeColor != eyeColor ) {
			m_eyeColor = eyeColor;
			getNebPerson().setEyeColor( m_eyeColor );
		}
	}
	
	private Hair m_hair = null;
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Hair getHair() {
		return m_hair;
	}
	public void setHair( Hair hair ) {
		assert hair != null;
		if( m_hair != hair ) {
			m_hair = hair;
			getNebPerson().setHair( hair );
		}
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

	protected abstract edu.cmu.cs.dennisc.math.AxisAlignedBox getLocalAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv );
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.ReferenceFrame asSeenBy, org.alice.apis.moveandturn.HowMuch howMuch, org.alice.apis.moveandturn.OriginInclusionPolicy originPolicy ) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox rv = new edu.cmu.cs.dennisc.math.AxisAlignedBox();
		this.getLocalAxisAlignedMinimumBoundingBox( rv );
		//todo: asSeenBy
		return rv;
	}
	
	private void applyRotationInRadiansToJoint( final org.lookingglassandalice.storytelling.resources.JointId joint, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, Number duration, Style style ) {
        assert axis != null;
        assert duration.doubleValue() >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
//        if (!this.getNebPerson().hasJoint(joint))
//        {
//            System.err.println("No joint named "+joint);
//            return;
//        }
        
        class RotateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
            private edu.cmu.cs.dennisc.math.Vector3 m_axis;
            private double m_angleInRadians;
            private double m_angleSumInRadians;

            public RotateAnimation( Number duration, Style style, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
                super( duration, style );
                m_axis = axis;
                m_angleInRadians = angleInRadians;
            }
            @Override
            protected void prologue() {
                m_angleSumInRadians = 0;
            }
            @Override
            protected void setPortion( double portion ) {
                double anglePortionInRadians = (m_angleInRadians * portion) - m_angleSumInRadians;
                applyRotationInRadians(joint, m_axis, anglePortionInRadians );
                m_angleSumInRadians += anglePortionInRadians;
            }
            @Override
            protected void epilogue() {
                applyRotationInRadians( joint, m_axis, m_angleInRadians - m_angleSumInRadians  );
            }
        }

        duration = adjustDurationIfNecessary( duration );
        if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
            applyRotationInRadians( joint, axis, angleInRadians );
        } else {
            perform( new RotateAnimation( duration, style, axis, angleInRadians ) );
        }
    }
	
	@Deprecated
	private void applyRotationInRadians( org.lookingglassandalice.storytelling.resources.JointId joint, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) 
    {
	    this.getNebPerson().applyRotationToJointAboutArbitraryAxisInRadians( joint, axis, angleInRadians);
    }
	
	private Vector3 getAxisForTurnDirection( TurnDirection direction )
    {
        switch (direction)
        {
            case LEFT     : return edu.cmu.cs.dennisc.math.Vector3.createPositiveYAxis();
            case RIGHT    : return edu.cmu.cs.dennisc.math.Vector3.createNegativeYAxis();
            case FORWARD  : return edu.cmu.cs.dennisc.math.Vector3.createNegativeXAxis();
            case BACKWARD : return edu.cmu.cs.dennisc.math.Vector3.createPositiveXAxis();
            default       : return edu.cmu.cs.dennisc.math.Vector3.createPositiveXAxis();
        }
    }
    
    private Vector3 getAxisForRollDirection( RollDirection direction )
    {
        switch (direction)
        {
            case LEFT     : return edu.cmu.cs.dennisc.math.Vector3.createPositiveZAxis();
            case RIGHT    : return edu.cmu.cs.dennisc.math.Vector3.createNegativeZAxis();
            default       : return edu.cmu.cs.dennisc.math.Vector3.createPositiveZAxis();
        }
    }
    
    @MethodTemplate( visibility=Visibility.PRIME_TIME )
    public void turnJoint( 
            org.lookingglassandalice.storytelling.resources.BipedResource.BipedJointId joint,
            TurnDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount, 
            Number duration,
            Style style 
        ) 
    {
        applyRotationInRadiansToJoint( joint, getAxisForTurnDirection(direction), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( amount.doubleValue() ), duration, style );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void turnJoint(
    		org.lookingglassandalice.storytelling.resources.BipedResource.BipedJointId joint,
            TurnDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount,
            Number duration
        ) {
        turnJoint( joint, direction, amount, duration, DEFAULT_STYLE );
    }
    
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void turnJoint( 
    		org.lookingglassandalice.storytelling.resources.BipedResource.BipedJointId joint,
            TurnDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount 
        ) {
        turnJoint( joint, direction, amount, DEFAULT_DURATION );
    }
    
    @MethodTemplate( visibility=Visibility.PRIME_TIME )
    public void rollJoint( 
    		org.lookingglassandalice.storytelling.resources.BipedResource.BipedJointId joint,
            RollDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount, 
            Number duration,
            Style style 
        ) {
        applyRotationInRadiansToJoint( joint, getAxisForRollDirection(direction), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( amount.doubleValue() ), duration, style );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void rollJoint( 
    		org.lookingglassandalice.storytelling.resources.BipedResource.BipedJointId joint,
            RollDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount, 
            Number duration
        ) {
        rollJoint( joint, direction, amount, duration, DEFAULT_STYLE );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void rollJoint( 
    		org.lookingglassandalice.storytelling.resources.BipedResource.BipedJointId joint,
            RollDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount 
        ) {
        rollJoint( joint, direction, amount, DEFAULT_DURATION );
    }
	
}
