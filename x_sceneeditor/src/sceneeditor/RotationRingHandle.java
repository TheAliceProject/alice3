/**
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
package sceneeditor;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.Torus;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;
import edu.cmu.cs.dennisc.scenegraph.util.Jack;
import edu.cmu.cs.dennisc.scenegraph.util.ModestAxes;

/**
 * @author David Culyba
 */
public class RotationRingHandle extends ManipulationHandle{

	protected static final double MINOR_RADIUS = .05d;
	
	protected Torus sgTorus = new Torus();
	protected Sphere sgSphere = new Sphere();
	protected Vector3 rotationAxis;
	
	public RotationRingHandle( )
	{
		this( Vector3.accessPositiveYAxis());
	}
	
	public RotationRingHandle( Vector3 rotationAxis )
	{
		super();
		this.sgTorus.minorRadius.setValue( MINOR_RADIUS );
		this.rotationAxis = rotationAxis;
		this.rotationAxis.normalize();
		
		this.localTransformation.setValue( this.getTransformationForAxis( this.rotationAxis ) );
		
		this.sgVisual.geometries.setValue( new Geometry[] { sgTorus } );
	}

	public AffineMatrix4x4 getTransformationForAxis( Vector3 rotationAxis )
	{
		double upDot = Vector3.calculateDotProduct( rotationAxis, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 transform = new AffineMatrix4x4();
		if ( upDot != 1.0d)
		{
			
			Vector3 rightAxis = Vector3.createCrossProduct( rotationAxis, Vector3.accessPositiveYAxis() );
			Vector3 upAxis = rotationAxis;
			Vector3 backwardAxis = Vector3.createCrossProduct( rightAxis, upAxis );
			transform.orientation.set( rightAxis, upAxis, backwardAxis );
		}
		return transform;
	}
	
	@Override
	public void resizeRelativeToObject( Composite object )
	{
		if (object != null)
		{
			Object bbox = object.getBonusDataFor( SceneEditor.BOUNDING_BOX_KEY );
			if (bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
			{
				edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = (edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox;
				Plane planeOfRotation = new Plane(Point3.createZero(), this.rotationAxis);
				Point3 minPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMinimum() );
				Point3 maxPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMaximum() );
				Point3 diagonal = Point3.createSubtraction( maxPlanePoint, minPlanePoint );
				double radius = diagonal.calculateMagnitude() / 2.0d;
				sgTorus.majorRadius.setValue( radius + sgTorus.minorRadius.getValue() );
			}
		}
	}
	
	public Vector3 getRotationAxis()
	{
		return this.rotationAxis;
	}

	@Override
	public void positionRelativeToObject( Composite object ) {
		//These handles just use their local transform as their position
	}

}
