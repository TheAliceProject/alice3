/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser.pose;

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.stageide.ast.ExpressionCreator;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaConstructor;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SJoint;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Matt May
 */
public class JointQPair {

	private final JointId joint;
	private final UnitQuaternion unitQuaternion;
	private final Point3 translation;
	private final JointQPair child;
	private static final ExpressionCreator blah = new ExpressionCreator();

	public JointQPair( JointId joint, UnitQuaternion unitQuaternion ) {
		this( joint, unitQuaternion, null );
	}

	public JointQPair( SJoint sJoint, JointQPair child ) {
		this.joint = ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getJointId();
		this.unitQuaternion = new UnitQuaternion( ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getLocalTransformation().orientation );
		this.translation = new Point3( ( (JointImp)ImplementationAccessor.getImplementation( sJoint ) ).getLocalTransformation().translation );
		this.child = child;
	}

	public JointQPair( SJoint sJoint ) {
		this( sJoint, null );
	}

	public JointQPair( JointId joint, UnitQuaternion unitQuaternion, JointQPair child ) {
		this.joint = joint;
		this.unitQuaternion = unitQuaternion;
		this.translation = Point3.createZero();
		this.child = child;
	}

	public JointId getJointId() {
		return this.joint;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getUnitQuaternion() {
		return this.unitQuaternion;
	}

	public JointQPair getChild() {
		return this.child;
	}

	@Override
	public String toString() {
		return "[ " + joint + " " + unitQuaternion + " ]"; //affineMatrix does not have a nice toString() method.
		//		// the below is enough for now to differentiate between poses
		//		String thePoint = String.format( "(%.03f, %.03f, %.03f)", affineMatrix.translation.x, affineMatrix.translation.y, affineMatrix.translation.z );
		//		return "[ " + joint + " " + thePoint + " ]";
	}

	public boolean equals( JointQPair other ) {
		if( getChild() == null ) {
			return ( ( other.getChild() == null ) && getUnitQuaternion().equals( other.getUnitQuaternion() ) );
		} else {
			return ( getUnitQuaternion().equals( other.getUnitQuaternion() ) && getChild().equals( other.getChild() ) );
		}
	}

	public static Expression createInstance( JointQPair jqPair ) {
		Expression child = null;
		if( jqPair.child != null ) {
			child = createInstance( jqPair.child );
		}
		Expression jointExpression = null;
		Expression matrixExpression = null;
		try {
			matrixExpression = blah.createExpression( jqPair.unitQuaternion );
		} catch( CannotCreateExpressionException e ) {
			System.out.println( "blablah" );
			e.printStackTrace();
			assert false;
		}
		try {
			jointExpression = blah.createExpression( jqPair.joint );
		} catch( CannotCreateExpressionException e ) {
			System.out.println( "hello?" );
			e.printStackTrace();
		}
		//		AstUtilities.createInstanceCreation( Java, argumentExpressions )
		InstanceCreation rv = AstUtilities.createInstanceCreation( JavaConstructor.getInstance( JointQPair.class, JointId.class, AffineMatrix4x4.class, JointQPair.class ), jointExpression, matrixExpression, child );
		return rv;
	}
}
