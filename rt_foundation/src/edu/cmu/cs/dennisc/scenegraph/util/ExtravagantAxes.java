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
	
	private Arrow createArrow( double unit, double lengthFactor, Cylinder.BottomToTopAxis bottomToTopAxis ) {
		return new Arrow( unit, lengthFactor, bottomToTopAxis, s_axisToSGAppearanceMap.get( bottomToTopAxis ), false );
	}
	public ExtravagantAxes( double unitLength, double forwardFactor ) {
		Arrow sgXAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_X );
		Arrow sgYAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Y );
		Arrow sgZAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Z );
		Arrow sgFAxis = createArrow( unitLength, 2.0, Cylinder.BottomToTopAxis.NEGATIVE_Z );

		sgXAxis.setParent( this );
	    sgYAxis.setParent( this );
	    sgZAxis.setParent( this );
	    sgFAxis.setParent( this );
	}
	public ExtravagantAxes( double unitLength ) {
		this( unitLength, 2.0 );
	}
}
