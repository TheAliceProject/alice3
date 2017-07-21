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
package org.alice.interact.manipulator.scenegraph;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.LineArray;
import edu.cmu.cs.dennisc.scenegraph.ShadingStyle;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author David Culyba
 */
public class SnapSphere extends Transformable {
	private static final double DEFAULT_RADIUS = .06d;

	public SnapSphere() {
		this( DEFAULT_RADIUS );
	}

	public SnapSphere( double radius ) {
		this.sgSphere.radius.setValue( radius );
		this.sgFrontFacingAppearance.shadingStyle.setValue( ShadingStyle.NONE );
		this.sgFrontFacingAppearance.diffuseColor.setValue( Color4f.GREEN );
		this.sgFrontFacingAppearance.opacity.setValue( new Float( 1f ) );
		this.sgSphereVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		this.sgSphereVisual.geometries.setValue( new Geometry[] { this.sgSphere, this.sgLineArray } );
		this.sgSphereVisual.setParent( this );
	}

	public void setColor( Color4f color ) {
		sgFrontFacingAppearance.setDiffuseColor( color );
	}

	//Since this visual is rooted at the location of the sphere (and is therefore centered on the rotation ring), we need to make the line extend back to the center of the ring
	public void setLineDirection( Point3 rootOrigin, Point3 sphereEndPoint ) {
		Vertex[] vertices = new Vertex[ 2 ];
		Vector3 lineOffset = Vector3.createSubtraction( rootOrigin, sphereEndPoint );
		vertices[ 0 ] = Vertex.createXYZ( 0, 0, 0 );
		vertices[ 1 ] = Vertex.createXYZ( lineOffset.x, lineOffset.y, lineOffset.z );

		this.sgLineArray.vertices.setValue( vertices );
	}

	private final Sphere sgSphere = new Sphere();
	private final Visual sgSphereVisual = new Visual();
	private final LineArray sgLineArray = new LineArray();
	private final SimpleAppearance sgFrontFacingAppearance = new SimpleAppearance();
}
