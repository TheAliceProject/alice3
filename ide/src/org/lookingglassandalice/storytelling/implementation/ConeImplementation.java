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

package org.lookingglassandalice.storytelling.implementation;

/**
 * @author Dennis Cosgrove
 */
public class ConeImplementation extends ShapeImplementation {
	private final edu.cmu.cs.dennisc.scenegraph.Cylinder sgCylinder = new edu.cmu.cs.dennisc.scenegraph.Cylinder();
	private final org.lookingglassandalice.storytelling.Cone abstraction;

	public ConeImplementation( org.lookingglassandalice.storytelling.Cone abstraction ) {
		this.abstraction = abstraction;
		this.sgCylinder.topRadius.setValue( 0.0 );
		this.sgCylinder.hasTopCap.setValue( false );
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgCylinder } );
	}
	@Override
	public org.lookingglassandalice.storytelling.Cone getAbstraction() {
		return this.abstraction;
	}
	public double getLength() {
		return this.sgCylinder.length.getValue();
	}
	public void setLength( double radius ) {
		this.sgCylinder.length.setValue( radius );
	}
	public double getBaseRadius() {
		return this.sgCylinder.bottomRadius.getValue();
	}
	public void setBaseRadius( double radius ) {
		this.sgCylinder.bottomRadius.setValue( radius );
	}

	//todo: produce more tight bound by shifting center of bounding sphere
	@Override
	protected double getBoundingSphereRadius() {
		return Math.max( this.getLength(), this.getBaseRadius());
	}

}
