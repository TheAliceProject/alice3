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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.scenegraph.*;

/**
 * @author Dennis Cosgrove
 */
public class ExtravagantAxes extends Transformable {
	private java.util.Map< Cylinder.BottomToTopAxis, SimpleAppearance > axisToSGAppearanceMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	private static final double CYLINDER_PORTION = 0.8;
	private static final double CONE_PORTION = 1.0-CYLINDER_PORTION;
	
	private Arrow createArrow( double unit, double lengthFactor, Cylinder.BottomToTopAxis bottomToTopAxis ) {
		double lengthCylinder = unit * lengthFactor * CYLINDER_PORTION;
		double radiusCylinder = unit * 0.025;
		double lengthCone = unit * lengthFactor * CONE_PORTION;
		double radiusCone = radiusCylinder * 2.0;
		return new Arrow( lengthCylinder, radiusCylinder, lengthCone, radiusCone, bottomToTopAxis, axisToSGAppearanceMap.get( bottomToTopAxis ), false );
	}
	public ExtravagantAxes( double unitLength, double forwardFactor ) {
		SimpleAppearance sgRedAppearance = new SimpleAppearance();
		SimpleAppearance sgGreenAppearance = new SimpleAppearance();
		SimpleAppearance sgBlueAppearance = new SimpleAppearance();
		SimpleAppearance sgWhiteAppearance = new SimpleAppearance();

		sgRedAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.RED );
		sgGreenAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.GREEN );
		sgBlueAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.BLUE );
		sgWhiteAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.WHITE );
		
		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_X, sgRedAppearance );
		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_Y, sgGreenAppearance );
		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_Z, sgBlueAppearance );
		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.NEGATIVE_Z, sgWhiteAppearance );
		
		Arrow sgXAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_X );
		Arrow sgYAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Y );
		Arrow sgZAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Z );
		Arrow sgFAxis = createArrow( unitLength, forwardFactor, Cylinder.BottomToTopAxis.NEGATIVE_Z );

		sgXAxis.setParent( this );
	    sgYAxis.setParent( this );
	    sgZAxis.setParent( this );
	    sgFAxis.setParent( this );
	}
	
	public ExtravagantAxes( double unitLength ) {
		this( unitLength, 2.0 );
	}
	
	public float getOpacity()
	{
		return axisToSGAppearanceMap.values().iterator().next().opacity.getValue();
	}
	
	public void setOpacity(float opacity)
	{
		for (SimpleAppearance appearance : axisToSGAppearanceMap.values())
		{
			appearance.opacity.setValue(opacity);
		}
	}
	
	public void setIsShowing(boolean isShowing)
	{
		for (Component child : this.getComponents())
		{
			if (child instanceof Arrow)
			{
				Arrow a = (Arrow)child;
				for (Visual v : a.getVisuals())
				{
					v.isShowing.setValue(isShowing);
				}
			}
		}
	}
}
