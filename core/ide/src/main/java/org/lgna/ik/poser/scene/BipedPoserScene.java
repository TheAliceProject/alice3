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
package org.lgna.ik.poser.scene;

import java.util.ArrayList;

import org.lgna.ik.core.IKCore;
import org.lgna.ik.core.IKCore.Limb;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SJoint;
import org.lgna.story.implementation.JointImp;

import edu.cmu.cs.dennisc.java.util.Lists;

/**
 * @author Matt May
 */
public class BipedPoserScene extends AbstractPoserScene<SBiped> {

	public BipedPoserScene( SCamera camera, SBiped biped ) {
		super( camera, biped );
	}

	@Override
	protected void initializeJointSelectionSpheresAndLimbs() {
		JointSelectionSphere a = createJSS( model.getRightHip(), null );
		JointSelectionSphere b = createJSS( model.getRightKnee(), a );
		JointSelectionSphere c = createJSS( model.getRightAnkle(), b );
		limbToJointMap.put( Limb.RIGHT_LEG, Lists.newArrayList( a, b, c ) );
		JointSelectionSphere d = createJSS( model.getRightClavicle(), null );
		JointSelectionSphere e = createJSS( model.getRightShoulder(), d );
		JointSelectionSphere f = createJSS( model.getRightElbow(), e );
		JointSelectionSphere g = createJSS( model.getRightWrist(), f );
		limbToJointMap.put( Limb.RIGHT_ARM, Lists.newArrayList( d, e, f, g ) );
		JointSelectionSphere h = createJSS( model.getLeftHip(), null );
		JointSelectionSphere i = createJSS( model.getLeftKnee(), h );
		JointSelectionSphere j = createJSS( model.getLeftAnkle(), i );
		limbToJointMap.put( Limb.LEFT_LEG, Lists.newArrayList( h, i, j ) );
		JointSelectionSphere k = createJSS( model.getLeftClavicle(), null );
		JointSelectionSphere l = createJSS( model.getLeftShoulder(), k );
		JointSelectionSphere m = createJSS( model.getLeftElbow(), l );
		JointSelectionSphere n = createJSS( model.getLeftWrist(), m );
		limbToJointMap.put( Limb.LEFT_ARM, Lists.newArrayList( k, l, m, n ) );

		for( IKCore.Limb limb : limbToJointMap.keySet() ) {
			for( JointSelectionSphere sphere : limbToJointMap.get( limb ) ) {
				jointToLimbMap.put( sphere.getJoint(), limb );
				sphere.setOpacity( 0 );
			}
		}
		this.jssArr = Lists.newArrayList( a, b, c, d, e, f, g, h, i, j, k, l, m, n );
	}

	@Override
	protected void initializeLimbAnchors() {
		ArrayList<SJoint> sJointList = Lists.newArrayList( model.getRightClavicle(), model.getLeftClavicle(), model.getRightHip(), model.getLeftHip() );
		for( SJoint joint : sJointList ) {
			anchorPoints.add( ( (JointImp)EmployeesOnly.getImplementation( joint ) ).getJointId() );
		}
	}

}
