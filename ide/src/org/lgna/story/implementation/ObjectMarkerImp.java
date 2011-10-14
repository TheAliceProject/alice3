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

import java.util.LinkedList;


import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.util.Arrow;

/**
 * @author dculyba
 * 
 */
public class ObjectMarkerImp extends MarkerImp{

	private java.util.Map< Cylinder.BottomToTopAxis, TexturedAppearance > axisToSGAppearanceMap;
	private double scale = 1.0;

	private edu.cmu.cs.dennisc.scenegraph.Visual[] sgVisuals;
	private edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgAppearance = new edu.cmu.cs.dennisc.scenegraph.TexturedAppearance();
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] sgAppearances = { sgAppearance };
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgRedAppearance = new TexturedAppearance();
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgGreenAppearance = new TexturedAppearance();
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgBlueAppearance = new TexturedAppearance();
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgWhiteAppearance = new TexturedAppearance();

	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] opacityAppearances = {sgAppearance, sgRedAppearance, sgGreenAppearance, sgBlueAppearance, sgWhiteAppearance };
	
	public ObjectMarkerImp(org.lgna.story.ObjectMarker abstraction){
		super(abstraction);
	}
	
	private Arrow createArrow( double unit, double lengthFactor, Cylinder.BottomToTopAxis bottomToTopAxis ) {
		double lengthCylinder = unit * lengthFactor * 0.8;
		double radiusCylinder = unit * 0.05;
		double lengthCone = unit * lengthFactor * 0.3;
		double radiusCone = radiusCylinder * 3.0;
		return new Arrow( lengthCylinder, radiusCylinder, lengthCone, radiusCone, bottomToTopAxis, this.getSgPaintAppearances()[0], axisToSGAppearanceMap.get( bottomToTopAxis ), false );
	}
	
	@Override
	protected void createVisuals()
	{
		this.scale = .3;
		createAxes(2.0*scale, scale);
	}
	
	private void createAxes( double unitLength, double forwardFactor ) {
		axisToSGAppearanceMap = new java.util.HashMap< Cylinder.BottomToTopAxis, TexturedAppearance >();
		
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
		Arrow sgFAxis = createArrow( unitLength, 2.0, Cylinder.BottomToTopAxis.NEGATIVE_Z );

		sgXAxis.setParent( this.getSgComposite() );
	    sgYAxis.setParent( this.getSgComposite() );
	    sgZAxis.setParent( this.getSgComposite() );
	    sgFAxis.setParent( this.getSgComposite() );
	    
	    LinkedList<Visual> axisVisuals = new LinkedList<Visual>();
	    
	    for (Visual v : sgXAxis.getVisuals()) { axisVisuals.add(v); }
	    for (Visual v : sgYAxis.getVisuals()) { axisVisuals.add(v); }
	    for (Visual v : sgZAxis.getVisuals()) { axisVisuals.add(v); }
	    for (Visual v : sgFAxis.getVisuals()) { axisVisuals.add(v); }

		this.sgVisuals = axisVisuals.toArray(new Visual[axisVisuals.size()]);
		
	}
	
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] getSgPaintAppearances() {
		return this.sgAppearances;
	}
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] getSgOpacityAppearances() {
		return this.opacityAppearances;
	}

	@Override
	protected Visual[] getSgVisuals() {
		return this.sgVisuals;
	}
	
	@Override
	public float getDefaultMarkerOpacity()
	{
		return .75f;
	}
	
}
