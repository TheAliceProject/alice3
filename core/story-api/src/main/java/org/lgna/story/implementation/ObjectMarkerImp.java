/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.util.Arrow;
import org.lgna.story.SThingMarker;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author dculyba
 * 
 */
public class ObjectMarkerImp extends MarkerImp {
	public ObjectMarkerImp( SThingMarker abstraction ) {
		super( abstraction );
	}

	private Arrow createArrow( double unit, double lengthFactor, Cylinder.BottomToTopAxis bottomToTopAxis ) {
		double lengthCylinder = unit * lengthFactor * 0.8;
		double radiusCylinder = unit * 0.05;
		double lengthCone = unit * lengthFactor * 0.3;
		double radiusCone = radiusCylinder * 3.0;
		return new Arrow( lengthCylinder, radiusCylinder, lengthCone, radiusCone, bottomToTopAxis, this.getSgPaintAppearances()[ 0 ], axisToSGAppearanceMap.get( bottomToTopAxis ), false );
	}

	@Override
	protected void initializeData() {
		this.axisToSGAppearanceMap = Maps.newHashMap();
	}

	@Override
	protected void createVisuals() {
		final double scale = 0.3;

		this.axisToSGAppearanceMap.clear();
		createAxes( 2.0 * scale, scale );
	}

	private void createAxes( double unitLength, double forwardFactor ) {
		this.sgAppearance = new SimpleAppearance();
		this.sgAppearances = new SimpleAppearance[] { sgAppearance };
		this.sgRedAppearance = new SimpleAppearance();
		this.sgGreenAppearance = new SimpleAppearance();
		this.sgBlueAppearance = new SimpleAppearance();
		this.sgWhiteAppearance = new SimpleAppearance();
		this.opacityAppearances = new SimpleAppearance[] { sgAppearance, sgRedAppearance, sgGreenAppearance, sgBlueAppearance, sgWhiteAppearance };

		sgRedAppearance.setDiffuseColor( Color4f.RED );
		sgGreenAppearance.setDiffuseColor( Color4f.GREEN );
		sgBlueAppearance.setDiffuseColor( Color4f.BLUE );
		sgWhiteAppearance.setDiffuseColor( Color4f.WHITE );

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

		List<Visual> axisVisuals = Lists.newLinkedList();

		Collections.addAll( axisVisuals, sgXAxis.getVisuals() );
		Collections.addAll( axisVisuals, sgYAxis.getVisuals() );
		Collections.addAll( axisVisuals, sgZAxis.getVisuals() );
		Collections.addAll( axisVisuals, sgFAxis.getVisuals() );

		this.sgVisuals = axisVisuals.toArray( new Visual[ axisVisuals.size() ] );

	}

	@Override
	protected final SimpleAppearance[] getSgPaintAppearances() {
		return this.sgAppearances;
	}

	@Override
	protected final SimpleAppearance[] getSgOpacityAppearances() {
		return this.opacityAppearances;
	}

	@Override
	public Visual[] getSgVisuals() {
		return this.sgVisuals;
	}

	@Override
	public float getDefaultMarkerOpacity() {
		return 0.75f;
	}

	private Map<Cylinder.BottomToTopAxis, SimpleAppearance> axisToSGAppearanceMap;

	private Visual[] sgVisuals;
	private SimpleAppearance sgAppearance;
	private SimpleAppearance[] sgAppearances;
	private SimpleAppearance sgRedAppearance;
	private SimpleAppearance sgGreenAppearance;
	private SimpleAppearance sgBlueAppearance;
	private SimpleAppearance sgWhiteAppearance;

	private SimpleAppearance[] opacityAppearances;
}
