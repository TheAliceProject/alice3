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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.scenegraph.*;

/**
 * @author Dennis Cosgrove
 */
public class ExtravagantAxes extends Transformable {
	private static java.util.Map< Cylinder.BottomToTopAxis, SingleAppearance > s_axisToSGAppearanceMap = new java.util.HashMap< Cylinder.BottomToTopAxis, SingleAppearance >();

	static {
		SingleAppearance sgRedAppearance = new SingleAppearance();
		SingleAppearance sgGreenAppearance = new SingleAppearance();
		SingleAppearance sgBlueAppearance = new SingleAppearance();
		SingleAppearance sgWhiteAppearance = new SingleAppearance();

		sgRedAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.RED );
		sgGreenAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.GREEN );
		sgBlueAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.BLUE );
		sgWhiteAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.WHITE );
		
		s_axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_X, sgRedAppearance );
		s_axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_Y, sgGreenAppearance );
		s_axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_Z, sgBlueAppearance );
		s_axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.NEGATIVE_Z, sgWhiteAppearance );
	}
	
	private class Axis extends Transformable {
		public Axis( double unit, double lengthFactor, Cylinder.BottomToTopAxis bottomToTopAxis ) {
			SingleAppearance frontFacingAppearance = s_axisToSGAppearanceMap.get( bottomToTopAxis );
			
			double lengthCylinder = unit * lengthFactor * 0.8;
			double radiusCylinder = unit * 0.05;
			double lengthCone = unit * lengthFactor * 0.2;
			double radiusCone = radiusCylinder * 1.2;

			Visual sgVisualCylinder = new Visual();
			sgVisualCylinder.frontFacingAppearance.setValue( frontFacingAppearance );
			
			Cylinder sgCylinder = new Cylinder();
			sgCylinder.topRadius.setValue( radiusCylinder );
			sgCylinder.bottomRadius.setValue( radiusCylinder );
			sgCylinder.length.setValue( lengthCylinder );
			sgCylinder.bottomToTopAxis.setValue( bottomToTopAxis );
			sgCylinder.hasTopCap.setValue( false );
			//todo?
			sgCylinder.hasBottomCap.setValue( false );
			
			
			edu.cmu.cs.dennisc.math.Vector3 translation = edu.cmu.cs.dennisc.math.Vector3.createMultiplication(  
				new edu.cmu.cs.dennisc.math.Vector3( lengthCylinder, lengthCylinder, lengthCylinder ), 
				bottomToTopAxis.accessVector() 
			);
			
			Transformable sgTransformableCone = new Transformable();
			sgTransformableCone.applyTranslation( translation );

			Visual sgVisualCone = new Visual();
			sgVisualCone.frontFacingAppearance.setValue( frontFacingAppearance );

			Cylinder sgCone = new Cylinder();
		    sgCone.topRadius.setValue( 0.0 );
		    sgCone.bottomRadius.setValue( radiusCone );
		    sgCone.length.setValue( lengthCone );
		    sgCone.bottomToTopAxis.setValue( bottomToTopAxis );
		    sgCone.hasTopCap.setValue( false ); //redundant
		    sgCone.hasBottomCap.setValue( true );
		    
		    sgVisualCylinder.geometries.setValue( new Geometry[] { sgCylinder } );
		    sgVisualCone.geometries.setValue( new Geometry[] { sgCone } );
		    
		    sgVisualCylinder.setParent( this );
		    sgTransformableCone.setParent( this );
		    sgVisualCone.setParent( sgTransformableCone );
		}
	}
	
	public ExtravagantAxes( double unitLength, double forwardFactor ) {
		Axis sgXAxis = new Axis( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_X );
		Axis sgYAxis = new Axis( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Y );
		Axis sgZAxis = new Axis( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Z );
		Axis sgFAxis = new Axis( unitLength, 2.0, Cylinder.BottomToTopAxis.NEGATIVE_Z );

		sgXAxis.setParent( this );
	    sgYAxis.setParent( this );
	    sgZAxis.setParent( this );
	    sgFAxis.setParent( this );
	}
	public ExtravagantAxes( double unitLength ) {
		this( unitLength, 2.0 );
	}
}
