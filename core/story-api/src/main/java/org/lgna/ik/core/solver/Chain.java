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

package org.lgna.ik.core.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import org.lgna.ik.core.solver.Bone.Axis;

import edu.cmu.cs.dennisc.math.Vector3;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointId;

/**
 * @author Dennis Cosgrove
 */
public class Chain {

  // Chain includes all the joints that will turn

  public static Chain createInstance(JointedModelImp<?, ?> jointedModelImp, JointId anchorId, JointId endId) {
    List<Bone.Direction> directions = new ArrayList<Bone.Direction>();
    List<JointImp> jointImps = jointedModelImp.getInclusiveListOfJointsBetween(anchorId, endId, directions);
    return new Chain(jointImps, directions);
  }

  private final List<JointImp> jointImps;
  private final Bone[] bones;
  private final Point3 endEffectorLocalPosition;
  private final List<Bone.Direction> directions; //the order doesn't change but the directions do

  //  private final java.util.Map< org.lgna.ik.solver.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 > linearVelocityContributions;
  //  private final java.util.Map< org.lgna.ik.solver.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 > angularVelocityContributions;
  //
  private final Map<Bone, Map<Axis, Vector3>> linearVelocityContributions;
  private final Map<Bone, Map<Axis, Vector3>> angularVelocityContributions;

  private Chain(List<JointImp> jointImps, List<Bone.Direction> directions) {
    this.jointImps = jointImps;
    this.directions = directions;

    this.endEffectorLocalPosition = Point3.createZero();
    //    this.linearVelocityContributions = new java.util.HashMap< org.lgna.ik.solver.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 >();
    //    this.angularVelocityContributions = new java.util.HashMap< org.lgna.ik.solver.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3 >();
    this.linearVelocityContributions = new HashMap<Bone, Map<Axis, Vector3>>();
    this.angularVelocityContributions = new HashMap<Bone, Map<Axis, Vector3>>();

    final int N = this.jointImps.size();
    this.bones = new Bone[N];
    for (int i = 0; i < N; i++) {
      Bone newBone = new Bone(this, i);
      this.bones[i] = newBone;
      this.linearVelocityContributions.put(newBone, new HashMap<Bone.Axis, Vector3>());
      this.angularVelocityContributions.put(newBone, new HashMap<Bone.Axis, Vector3>());
    }
  }

  //if this is not called, in linear motions, you will be moving the anchor of the last joint
  //this helps you move the tip of the finger
  public void setEndEffectorPosition(Point3 eePosition) {
    JointImp eeJointImp = jointImps.get(jointImps.size() - 1);

    //get the world eePosition local to eeJointImp
    AffineMatrix4x4 eeJointInverse = AffineMatrix4x4.createInverse(eeJointImp.getTransformation(AsSeenBy.SCENE));

    eeJointInverse.setReturnValueToTransformed(endEffectorLocalPosition, eePosition);
  }

  public void setEndEffectorLocalPosition(Point3 endEffectorLocalPosition) {
    this.endEffectorLocalPosition.set(endEffectorLocalPosition);
  }

  public Bone[] getBones() {
    return this.bones;
  }

  public JointImp getJointImpAt(int index) {
    return this.jointImps.get(index);
  }

  public Bone.Direction getDirectionAt(int index) {
    return this.directions.get(index);
  }

  // these could be local or world. does it matter? it could only matter if you don't move the end effector target with the character.
  // so, these are world
  public Point3 getEndEffectorPosition() {
    JointImp eeJointImp = jointImps.get(jointImps.size() - 1);
    return eeJointImp.getTransformation(AsSeenBy.SCENE).setReturnValueToTransformed(new Point3(), endEffectorLocalPosition);
  }

  public OrthogonalMatrix3x3 getEndEffectorOrientation() {
    JointImp eeJointImp = jointImps.get(jointImps.size() - 1);
    return eeJointImp.getTransformation(AsSeenBy.SCENE).orientation;
  }

  public void updateStateFromJoints() {
    for (Bone bone : this.bones) {
      bone.updateStateFromJoint();
    }
  }

  public Map<Bone, Map<Axis, Vector3>> computeLinearVelocityContributions() {
    for (Bone bone : this.bones) {
      Vector3 jointEeVector = Vector3.createSubtraction(this.getEndEffectorPosition(), bone.getAnchorPosition());
      bone.updateLinearContributions(jointEeVector);

      //axis has inverse value. then contrib should also be inversed when merging here?
      // no. it's good like this.

      Map<Axis, Vector3> contributionsForBone = linearVelocityContributions.get(bone);

      for (Bone.Axis axis : bone.getAxes()) {
        contributionsForBone.put(axis, axis.getLinearContribution());
      }
    }

    return linearVelocityContributions;
  }

  public Map<Bone, Map<Axis, Vector3>> computeAngularVelocityContributions() {
    for (Bone bone : this.bones) {
      bone.updateAngularContributions();

      Map<Axis, Vector3> contributionsForBone = angularVelocityContributions.get(bone);

      for (Bone.Axis axis : bone.getAxes()) {
        contributionsForBone.put(axis, axis.getAngularContribution());
      }
    }

    return angularVelocityContributions;
  }

  //these are axis->vel
  //however axis reference is not the unique thing. it's the (joint,axis index) pair
  //TODO would this be an issue with multiple chains?
  public Map<Bone, Map<Axis, Vector3>> getLinearVelocityContributions() {
    return linearVelocityContributions;
  }

  public Map<Bone, Map<Axis, Vector3>> getAngularVelocityContributions() {
    return angularVelocityContributions;
  }

  public JointImp getLastJointImp() {
    return jointImps.get(jointImps.size() - 1);
  }

  public Point3 getAnchorPosition() {
    return bones[0].getAnchorPosition();
  }

  public boolean isEmpty() {
    return getBones().length == 0;
  }

}
