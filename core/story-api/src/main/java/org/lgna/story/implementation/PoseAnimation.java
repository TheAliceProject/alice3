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
package org.lgna.story.implementation;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.Pose;

import edu.cmu.cs.dennisc.animation.DurationBasedAnimation;
import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Matt May
 */
public class PoseAnimation extends DurationBasedAnimation {
	private final org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel;
	private final Pose<?> pose;
	private transient java.util.List<JointInfo> jointInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	private static class JointInfo {
		private final org.lgna.story.implementation.JointImp jointImp;

		private edu.cmu.cs.dennisc.math.UnitQuaternion m_q0;
		private edu.cmu.cs.dennisc.math.UnitQuaternion m_q1;
		private edu.cmu.cs.dennisc.math.UnitQuaternion m_qBuffer;
		private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_m0;
		private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_m1;
		private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_mBuffer;

		public JointInfo( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel, JointIdTransformationPair jtPair ) {
			this.jointImp = jointedModel.getJointImplementation( jtPair.getJointId() );

			m_q0 = this.jointImp.getLocalOrientation().createUnitQuaternion();
			m_q1 = jtPair.getTransformation().orientation.createUnitQuaternion();
			m_qBuffer = edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN();

			m_m0 = this.jointImp.getLocalTransformation();
			//If the pose affects the translation of the joint, use the supplied translation as the target
			// Otherwise, use the current translation as the target
			if( jtPair.affectsTranslation() ) {
				m_m1 = new edu.cmu.cs.dennisc.math.AffineMatrix4x4( jtPair.getTransformation() );
			} else {
				m_m1 = this.jointImp.getLocalTransformation();
			}
			m_mBuffer = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN();
		}

		public void setPortion( double portion ) {
			//Note that the scale of the jointedModel is applied to the translation. Since poses encode both orientation and position, they inherently encode the scale they were created at. This multiplication accounts for that.
			m_mBuffer.translation.setToInterpolation( m_m0.translation, Point3.createMultiplication( m_m1.translation, this.jointImp.getJointedModelImplementation().getScale() ), portion );
			m_qBuffer.setToInterpolation( m_q0, m_q1, portion );
			m_mBuffer.orientation.setValue( m_qBuffer );

			jointImp.setLocalTransformation( m_mBuffer );
		}

		public void epilogue() {
			//Note that the scale of the jointedModel is applied to the translation. Since poses encode both orientation and position, they inherently encode the scale they were created at. This multiplication accounts for that.
			jointImp.setLocalTransformation( new AffineMatrix4x4( m_q1, Point3.createMultiplication( m_m1.translation, this.jointImp.getJointedModelImplementation().getScale() ) ) );
		}
	}

	public PoseAnimation( double duration, Style style, org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel, Pose pose ) {
		super( duration, style );
		this.jointedModel = jointedModel;
		this.pose = pose;
	}

	@Override
	protected void prologue() {
		this.jointInfos.clear();
		for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( this.pose ) ) {
			appendJointInfos( this.jointInfos, this.jointedModel, jtPair );
		}
	}

	private static void appendJointInfos( java.util.List<JointInfo> jointInfos, org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel, JointIdTransformationPair jtPair ) {
		jointInfos.add( new JointInfo( jointedModel, jtPair ) );
	}

	@Override
	protected void setPortion( double portion ) {
		for( JointInfo jointInfo : this.jointInfos ) {
			jointInfo.setPortion( portion );
		}
	}

	@Override
	protected void epilogue() {
		for( JointInfo jointInfo : this.jointInfos ) {
			jointInfo.epilogue();
		}
	}

}
