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

package org.alice.apis.stage;

import java.io.FileInputStream;

import org.alice.apis.moveandturn.AngleInRevolutions;
import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.MoveDirection;
import org.alice.apis.moveandturn.ReferenceFrame;
import org.alice.apis.moveandturn.RollDirection;
import org.alice.apis.moveandturn.Style;
import org.alice.apis.moveandturn.TurnDirection;
import org.alice.apis.moveandturn.gallery.GalleryModel;
import org.alice.apis.moveandturn.gallery.GalleryRootUtilities;
import org.lookingglassandalice.storytelling.resources.monsters.Ogre;

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;
import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.Visual;

public abstract class AliceModel extends org.alice.apis.moveandturn.Model {

	private SkeletonVisual skeletonModelResource;
	
	@Override
	protected Visual createSGVisual()
	{
	    if( skeletonModelResource != null ) {
	        //pass
	    } else {
	        try {
	            
	            java.io.InputStream is = Ogre.class.getResourceAsStream( this.getResourceString()+".alice" );
	            edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
	            this.skeletonModelResource = decoder.decodeReferenceableBinaryEncodableAndDecodable( new java.util.HashMap< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable >() );
	            
	        } catch( Exception  lre ) {
	            throw new RuntimeException( lre );
	        }
	    }
        return this.skeletonModelResource;
	}
	
	@Override
	protected void createSGGeometryIfNecessary() {
	}
	
	public AliceModel()
	{
	    putElement( skeletonModelResource );
        putElement( m_sgAppearance );

        skeletonModelResource.frontFacingAppearance.setValue( m_sgAppearance );
        skeletonModelResource.setParent( getSGTransformable() );
	}
	
	@Override
	protected Geometry getSGGeometry()
	{
	    //The skeleton resource controls our geometry
	    return null;
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
    @Override
    public edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual() {
        return skeletonModelResource;
    }
	
   @Override
    protected void realize() {
        createSGGeometryIfNecessary();
        super.realize();
        for (edu.cmu.cs.dennisc.scenegraph.Geometry g : skeletonModelResource.geometries.getValue())
        {
            putElement( g );
        }
        m_sgVisual.geometries.setValue( skeletonModelResource.geometries.getValue());
    }
   
   @Override
   public void setName( String name ) {
       super.setName( name );
       skeletonModelResource.setName(name + ".sgSkeletonModel");
   }
  
	protected String getResourceString()
	{
		return null;
	}
	
	private void applyRotationInRadians( Joint jointReference, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) 
	{
	    //skeletonModelResource.applyRotationToJointAboutArbitraryAxisInRadians( jointReference, axis, angleInRadians, sgAsSeenBy);
		jointReference.applyRotationAboutArbitraryAxisInRadians(  axis, angleInRadians, sgAsSeenBy );
    }
	
	private void applyTranslationToJointReference( Joint jointReference, double x, double y, double z, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) 
    {
//	    System.out.println("Moving <"+x+", "+y+", "+z+">");
		jointReference.applyTranslation( x, y, z, sgAsSeenBy );
        //skeletonModelResource.applyTranslationToJoint(jointReference, x, y, z, sgAsSeenBy);
    }
	
	protected void applyRotationInRadiansToJoint( String jointName, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, Number duration, ReferenceFrame asSeenBy, Style style ) {
        assert axis != null;
        assert duration.doubleValue() >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
        Joint sgSkeletonRoot = this.skeletonModelResource.skeleton.getValue();
        final Joint joint = sgSkeletonRoot.getJoint(jointName);

        if ( joint != null ) {
        	//pass
        } else {
            System.err.println("No joint named "+jointName);
            return;
        }
        
        class RotateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
            private edu.cmu.cs.dennisc.scenegraph.ReferenceFrame m_sgAsSeenBy;
            private edu.cmu.cs.dennisc.math.Vector3 m_axis;
            private double m_angleInRadians;
            private double m_angleSumInRadians;

            public RotateAnimation( Number duration, Style style, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy) {
                super( duration, style );
                m_axis = axis;
                m_angleInRadians = angleInRadians;
                m_sgAsSeenBy = sgAsSeenBy;
            }
            @Override
            protected void prologue() {
                m_angleSumInRadians = 0;
            }
            @Override
            protected void setPortion( double portion ) {
                double anglePortionInRadians = (m_angleInRadians * portion) - m_angleSumInRadians;
                applyRotationInRadians(joint,  m_axis, anglePortionInRadians, m_sgAsSeenBy );
                m_angleSumInRadians += anglePortionInRadians;
            }
            @Override
            protected void epilogue() {
                applyRotationInRadians( joint, m_axis, m_angleInRadians - m_angleSumInRadians, m_sgAsSeenBy  );
            }
        }

        duration = adjustDurationIfNecessary( duration );
        if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
            applyRotationInRadians( joint, axis, angleInRadians, asSeenBy.getSGReferenceFrame());
        } else {
            perform( new RotateAnimation( duration, style, axis, angleInRadians, asSeenBy.getSGReferenceFrame()) );
        }
    }
	
	protected void applyTranslationToJoint(String jointName, double x, double y, double z, Number duration, ReferenceFrame asSeenBy, Style style ) {
        assert Double.isNaN( x ) == false;
        assert Double.isNaN( y ) == false;
        assert Double.isNaN( z ) == false;
        assert duration.doubleValue() >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
        assert style != null;
        assert asSeenBy != null;
        Joint sgSkeletonRoot = this.skeletonModelResource.skeleton.getValue();
        final Joint joint = sgSkeletonRoot.getJoint(jointName);

        if ( joint != null ) {
        	//pass
        } else {
            System.err.println("No joint named "+jointName);
            return;
        }

        class TranslateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
            private edu.cmu.cs.dennisc.scenegraph.ReferenceFrame m_sgAsSeenBy;
            private double m_x;
            private double m_y;
            private double m_z;
            private double m_xSum;
            private double m_ySum;
            private double m_zSum;

            public TranslateAnimation( Number duration, Style style, double x, double y, double z, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
                super( duration, style );
                m_x = x;
                m_y = y;
                m_z = z;
                m_sgAsSeenBy = sgAsSeenBy;
            }
            
            @Override
            protected void prologue() {
                m_xSum = 0;
                m_ySum = 0;
                m_zSum = 0;
            }
            
            @Override
            protected void setPortion( double portion ) {
                double xPortion = (m_x * portion) - m_xSum;
                double yPortion = (m_y * portion) - m_ySum;
                double zPortion = (m_z * portion) - m_zSum;

                
                AliceModel.this.applyTranslationToJointReference( joint, xPortion, yPortion, zPortion, m_sgAsSeenBy );

                m_xSum += xPortion;
                m_ySum += yPortion;
                m_zSum += zPortion;
            }
            
            @Override
            protected void epilogue() {
                AliceModel.this.applyTranslationToJointReference( joint, m_x - m_xSum, m_y - m_ySum, m_z - m_zSum, m_sgAsSeenBy );
            }
        }

        duration = adjustDurationIfNecessary( duration );
        if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
            applyTranslationToJointReference( joint, x, y, z, asSeenBy.getSGReferenceFrame() );
        } else {
            perform( new TranslateAnimation( duration, style, x, y, z, asSeenBy.getSGReferenceFrame() ) );
        }
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
            String jointName,
            TurnDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount, 
            Number duration,
            ReferenceFrame asSeenBy,
            Style style 
        ) 
	{
	    applyRotationInRadiansToJoint( jointName, getAxisForTurnDirection(direction), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( amount.doubleValue() ), duration, asSeenBy, style );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void turnJoint(
            String jointName,
            TurnDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount,
            Number duration,
            ReferenceFrame asSeenBy
        ) {
        turnJoint( jointName, direction, amount, duration, asSeenBy, DEFAULT_STYLE );
    }
    
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void turnJoint( 
            String jointName,
            TurnDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount,
            Number duration
        ) {
        turnJoint( jointName, direction, amount, duration, AsSeenBy.SELF );
    }
    
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void turnJoint( 
            String jointName,
            TurnDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount 
        ) {
        turnJoint( jointName, direction, amount, DEFAULT_DURATION );
    }
    
    @MethodTemplate( visibility=Visibility.PRIME_TIME )
    public void rollJoint( 
            String jointName,
            RollDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount, 
            Number duration,
            ReferenceFrame asSeenBy,
            Style style 
        ) {
        applyRotationInRadiansToJoint( jointName, getAxisForRollDirection(direction), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( amount.doubleValue() ), duration, asSeenBy, style );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void rollJoint( 
            String jointName,
            RollDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount, 
            Number duration,
            ReferenceFrame asSeenBy
        ) {
        rollJoint( jointName, direction, amount, duration, asSeenBy, DEFAULT_STYLE );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void rollJoint( 
            String jointName,
            RollDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount,
            Number duration
        ) {
        rollJoint( jointName, direction, amount, duration, AsSeenBy.SELF );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void rollJoint( 
            String jointName,
            RollDirection direction, 
            @edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
            Number amount 
        ) {
        rollJoint( jointName, direction, amount, DEFAULT_DURATION );
    }
    
    @MethodTemplate( visibility=Visibility.PRIME_TIME )
    public void moveJoint( String jointName, MoveDirection direction, Number amount, Number duration, ReferenceFrame asSeenBy, Style style ) {
        edu.cmu.cs.dennisc.math.Vector3 axis = direction.getAxis();
        applyTranslationToJoint( jointName, axis.x * amount.doubleValue(), axis.y * amount.doubleValue(), axis.z * amount.doubleValue(), duration, asSeenBy, style );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void moveJoint( String jointName, MoveDirection direction, Number amount, Number duration, ReferenceFrame asSeenBy ) {
        moveJoint( jointName, direction, amount, duration, asSeenBy, DEFAULT_STYLE );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void moveJoint( String jointName, MoveDirection direction, Number amount, Number duration ) {
        moveJoint( jointName, direction, amount, duration, AsSeenBy.SELF );
    }
    @MethodTemplate( visibility=Visibility.CHAINED )
    public void moveJoint( String jointName, MoveDirection direction, Number amount ) {
        moveJoint( jointName, direction, amount, DEFAULT_DURATION );
    }
	
//    @MethodTemplate( visibility=Visibility.PRIME_TIME )
//    public Integer getJointCount()
//    {
//        return this.skeletonModelResource.getJointCount();
//    }
//    
//    @MethodTemplate( visibility=Visibility.PRIME_TIME )
//    public String getJointNameForIndex(Integer index)
//    {
//        Joint joint = this.skeletonModelResource.getJointForIndex(index);
//        if (joint != null)
//        {
//            return joint.jointID.getValue();
//        }
//        else
//        {
//            return null;
//        }
//    }

}
