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

import edu.cmu.cs.dennisc.math.Vector4;
import org.lgna.ik.core.IKCore;
import org.lgna.ik.core.IKCore.Limb;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SBiped;
import org.lgna.story.SThing;
import org.lgna.story.resources.BipedResource;

/**
 * @author Dennis Cosgrove
 */
public final class BipedImp extends JointedModelImp<SBiped, BipedResource> {

	public BipedImp( SBiped abstraction, JointImplementationAndVisualDataFactory<BipedResource> factory ) {
		super( abstraction, factory );
	}

	@Override
	protected Vector4 getThoughtBubbleOffset() {
		return this.getTopOffsetForJoint( this.getJointImplementation( BipedResource.HEAD ) );
	}

	@Override
	protected Vector4 getSpeechBubbleOffset() {
		return this.getFrontOffsetForJoint( this.getJointImplementation( BipedResource.MOUTH ) );
	}

	public void reachFor( SThing entity, Limb reachingLimb ) {
		JointImp anchor;
		JointImp end;
		switch( reachingLimb ) {
		case RIGHT_ARM:
			anchor = EmployeesOnly.getImplementation( this.getAbstraction().getRightClavicle() );
			end = EmployeesOnly.getImplementation( this.getAbstraction().getRightWrist() );
			break;
		case LEFT_ARM:
			anchor = EmployeesOnly.getImplementation( this.getAbstraction().getLeftClavicle() );
			end = EmployeesOnly.getImplementation( this.getAbstraction().getLeftWrist() );
			break;
		case RIGHT_LEG:
			anchor = EmployeesOnly.getImplementation( this.getAbstraction().getRightHip() );
			end = EmployeesOnly.getImplementation( this.getAbstraction().getRightFoot() );
			break;
		case LEFT_LEG:
			anchor = EmployeesOnly.getImplementation( this.getAbstraction().getLeftHip() );
			end = EmployeesOnly.getImplementation( this.getAbstraction().getLeftFoot() );
			break;
		default:
			System.out.println( "Unhandled LIMB: " + reachingLimb );
			return;
		}
		IKCore.moveChainToPointInSceneSpace( anchor, end, EmployeesOnly.getImplementation( entity ).getTransformation( AsSeenBy.SCENE ).translation );
	}
}
