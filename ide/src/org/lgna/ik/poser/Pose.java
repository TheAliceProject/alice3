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
package org.lgna.ik.poser;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SBiped;
import org.lgna.story.SJoint;
import org.lgna.story.implementation.JointImp;

/**
 * @author Matt May
 */
public class Pose {

	private final JointQPair base = new JointQPair( null, null, null );
	private final JointQPair rightArmBase;
	private final JointQPair leftArmBase;
	private final JointQPair rightLegBase;
	private final JointQPair leftLegBase;

	public Pose( SBiped biped ) {
		rightArmBase = createJQP( biped.getRightClavicle(), base );
		JointQPair rShoulder = createJQP( biped.getRightShoulder(), rightArmBase );
		JointQPair rElbow = createJQP( biped.getRightShoulder(), rShoulder );
		createJQP( biped.getRightShoulder(), rElbow );
		leftArmBase = createJQP( biped.getRightClavicle(), base );
		JointQPair lShoulder = createJQP( biped.getRightShoulder(), leftArmBase );
		JointQPair lElbow = createJQP( biped.getRightShoulder(), lShoulder );
		createJQP( biped.getRightShoulder(), lElbow );
		rightLegBase = createJQP( biped.getPelvis(), base );
		JointQPair rHip = createJQP( biped.getLeftHip(), rightLegBase );
		JointQPair rKnee = createJQP( biped.getLeftKnee(), rHip );
		createJQP( biped.getLeftAnkle(), rKnee );
		leftLegBase = createJQP( biped.getPelvis(), base );
		JointQPair lHip = createJQP( biped.getLeftHip(), leftLegBase );
		JointQPair lKnee = createJQP( biped.getLeftKnee(), lHip );
		createJQP( biped.getLeftAnkle(), lKnee );
	}

	private JointQPair createJQP( SJoint joint, JointQPair parent ) {
		JointImp jointImp = ImplementationAccessor.getImplementation( joint );
		JointQPair rv = new JointQPair( parent, jointImp.getJointId(), jointImp.getAbsoluteTransformation() );
		if( parent != base ) {
			parent.setChild( rv );
		}
		return rv;
	}

	@Override
	public String toString() {
		String rv = "";
		rv += "\n";
		rv += "Right Arm: ";
		rv += stringForChain( rightArmBase );
		rv += "\n";
		rv += "Left Arm: ";
		rv += stringForChain( leftArmBase );
		rv += "\n";
		rv += "Right Leg: ";
		rv += stringForChain( rightLegBase );
		rv += "\n";
		rv += "Left Leg: ";
		rv += stringForChain( leftLegBase );
		return rv;
	}

	private String stringForChain( JointQPair base ) {
		JointQPair jqpPointer = base;
		String rv = "";
		boolean comma = false;
		while( jqpPointer != null ) {
			if( comma ) {
				rv += ", ";
			}
			comma = true;
			rv += jqpPointer.toString();
			jqpPointer = jqpPointer.getChild();
		}
		return rv;
	}
}
