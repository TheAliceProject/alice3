/**
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
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

import org.lgna.story.EmployeesOnly;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author dculyba
 */
public class JointIdTransformationPair {
	private final JointId id;
	private final AffineMatrix4x4 transformation;
	private final boolean affectsTranslation;

	public JointIdTransformationPair( JointId id, AffineMatrix4x4 transformation, boolean affectsTranslation ) {
		this.id = id;
		this.transformation = transformation;
		this.affectsTranslation = affectsTranslation;
	}

	public JointIdTransformationPair( JointId id, AffineMatrix4x4 transformation ) {
		this( id, transformation, true );
	}

	public JointIdTransformationPair( JointId id, UnitQuaternion quaternion, Point3 translation ) {
		this( id, new AffineMatrix4x4( quaternion, translation ) );
	}

	public JointIdTransformationPair( JointId id, UnitQuaternion quaternion, Point3 translation, boolean affectsTranslation ) {
		this( id, new AffineMatrix4x4( quaternion, translation ), affectsTranslation );
	}

	public JointIdTransformationPair( JointId id, UnitQuaternion quaternion ) {
		this( id, quaternion, Point3.createZero(), false );
	}

	public JointIdTransformationPair( JointId id, org.lgna.story.Orientation orientation ) {
		this( id, EmployeesOnly.getOrthogonalMatrix3x3( orientation ).createUnitQuaternion() );
	}

	public JointIdTransformationPair( JointId id, Orientation orientation, Point3 point3 ) {
		this( id, EmployeesOnly.getOrthogonalMatrix3x3( orientation ).createUnitQuaternion(), point3 );
	}

	public JointIdTransformationPair( JointId id, Orientation orientation, Position position ) {
		this( id, EmployeesOnly.getOrthogonalMatrix3x3( orientation ).createUnitQuaternion(), new Point3( position.getRight(), position.getUp(), position.getBackward() ) );
	}

	public boolean affectsTranslation() {
		return this.affectsTranslation;
	}

	public boolean orientationOnly() {
		return !this.affectsTranslation;
	}

	public JointId getJointId() {
		return id;
	}

	public AffineMatrix4x4 getTransformation() {
		return this.transformation;
	}
}
