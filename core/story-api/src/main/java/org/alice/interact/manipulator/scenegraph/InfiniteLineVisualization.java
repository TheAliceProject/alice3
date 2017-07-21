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
import edu.cmu.cs.dennisc.scenegraph.LineArray;
import edu.cmu.cs.dennisc.scenegraph.ShadingStyle;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author David Culyba
 */
public class InfiniteLineVisualization extends Visual {

	private static final double LINE_DISTANCE = 1000.0d;

	public InfiniteLineVisualization( Vector3 line ) {
		Vertex[] vertices = new Vertex[ 2 ];

		Point3 lineEnd1 = Point3.createMultiplication( line, LINE_DISTANCE );
		Point3 lineEnd2 = Point3.createMultiplication( line, -LINE_DISTANCE );
		vertices[ 0 ] = Vertex.createXYZRGB( lineEnd1.x, lineEnd1.y, lineEnd1.z, 0, 1, 0 );
		vertices[ 1 ] = Vertex.createXYZRGB( lineEnd2.x, lineEnd2.y, lineEnd2.z, 0, 1, 0 );

		LineArray sgLineArray = new LineArray();
		sgLineArray.vertices.setValue( vertices );
		geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgLineArray } );
		this.sgFrontFacingAppearance.setShadingStyle( ShadingStyle.NONE );
		frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
	}

	public void setLine( Vector3 line ) {
		LineArray lines = (LineArray)( geometries.getValue()[ 0 ] );
		Vertex[] vertices = lines.vertices.getValue();
		Point3 lineEnd1 = Point3.createMultiplication( line, LINE_DISTANCE );
		Point3 lineEnd2 = Point3.createMultiplication( line, -LINE_DISTANCE );
		vertices[ 0 ].position.x = lineEnd1.x;
		vertices[ 0 ].position.y = lineEnd1.y;
		vertices[ 0 ].position.z = lineEnd1.z;
		vertices[ 1 ].position.x = lineEnd2.x;
		vertices[ 1 ].position.y = lineEnd2.y;
		vertices[ 1 ].position.z = lineEnd2.z;
	}

	public void setColor( Color4f color ) {
		this.sgFrontFacingAppearance.setDiffuseColor( color );
	}

	private final SimpleAppearance sgFrontFacingAppearance = new SimpleAppearance();
}
