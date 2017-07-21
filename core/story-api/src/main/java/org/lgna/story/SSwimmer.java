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
package org.lgna.story;

import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;

public class SSwimmer extends SJointedModel {
	private final org.lgna.story.implementation.SwimmerImp implementation;

	@Override
	/* package-private */org.lgna.story.implementation.SwimmerImp getImplementation() {
		return this.implementation;
	}

	public SSwimmer( org.lgna.story.resources.SwimmerResource resource ) {
		this.implementation = resource.createImplementation( this );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void swimTo( SThing entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: swimTo" );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public SJoint getRoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.ROOT );
	}

	public SJoint getNeck() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.NECK );
	}

	public SJoint getHead() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.HEAD );
	}

	public SJoint getMouth() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.MOUTH );
	}

	//	public SJoint getLowerLip() {
	//		 return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.LOWER_LIP);
	//	}
	public SJoint getLeftEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.LEFT_EYE );
	}

	public SJoint getRightEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.RIGHT_EYE );
	}

	public SJoint getLeftEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.LEFT_EYELID );
	}

	public SJoint getRightEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.RIGHT_EYELID );
	}

	public SJoint getFrontLeftFin() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_LEFT_FIN );
	}

	//	public SJoint getFrontLeftFinTip() {
	//		 return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_LEFT_FIN_TIP);
	//	}
	public SJoint getFrontRightFin() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_RIGHT_FIN );
	}

	//	public SJoint getFrontRightFinTip() {
	//		 return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_RIGHT_FIN_TIP);
	//	}
	public SJoint getSpineBase() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_BASE );
	}

	public SJoint getSpineMiddle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_MIDDLE );
	}

	public SJoint getTail() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.TAIL );
	}
}
