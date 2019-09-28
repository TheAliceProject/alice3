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

import java.lang.reflect.Field;
import java.util.*;

import edu.cmu.cs.dennisc.animation.DurationBasedAnimation;
import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.Vector4;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.*;
import org.lgna.ik.core.solver.Bone;
import org.lgna.ik.core.solver.Bone.Direction;
import org.lgna.story.Paint;
import org.lgna.story.Pose;
import org.lgna.story.SJoint;
import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.visualization.JointedModelVisualization;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resources.JointArrayId;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointedModelImp<A extends SJointedModel, R extends JointedModelResource> extends ModelImp {
  public static interface VisualData<R extends JointedModelResource> {
    public Visual[] getSgVisuals();

    public SkeletonVisual getSgVisualForExporting(R resource);

    public SimpleAppearance[] getSgAppearances();

    public double getBoundingSphereRadius();

    public void setSGParent(Composite parent);

    public Composite getSGParent();
  }

  public static interface JointImplementationAndVisualDataFactory<R extends JointedModelResource> {
    public R getResource();

    public JointImp createJointImplementation(JointedModelImp<?, R> jointedModelImplementation, JointId jointId);

    public boolean hasJointImplementation(JointedModelImp<?, R> jointedModelImplementation, JointId jointId);

    public JointId[] getJointArrayIds(JointedModelImp jointedModelImplementation, JointArrayId jointArrayId);

    public VisualData createVisualData();

    public UnitQuaternion getOriginalJointOrientation(JointId jointId);

    public AffineMatrix4x4 getOriginalJointTransformation(JointId jointId);

    boolean isSims();
  }

  private static class JointImpWrapper extends JointImp {
    public JointImpWrapper(JointedModelImp<?, ?> jointedModelImp, JointImp joint) {
      super(jointedModelImp);
      this.internalJointImp = joint;

    }

    @Override
    public final void setAbstraction(SJoint abstraction) {
      super.setAbstraction(abstraction);
      if (this.internalJointImp != null) {
        this.internalJointImp.setAbstraction(abstraction);
      }
    }

    @Override
    public JointImp getJointParent() {
      return jointParentWrapper;
    }

    @Override
    public List<JointImp> getJointChildren() {
      return jointChildrenWrapper;
    }

    @Override
    void setJointParent(JointImp jointParent) {
      if (this.jointParentWrapper != null) {
        this.jointParentWrapper.getJointChildren().remove(this);
      }
      this.jointParentWrapper = jointParent;
      if (this.jointParentWrapper != null) {
        jointParent.getJointChildren().add(this);
      }
    }

    @Override
    public SceneImp getScene() {
      return this.internalJointImp.getScene();
    }

    @Override
    public JointId getJointId() {
      return internalJointImp.getJointId();
    }

    @Override
    public boolean isFreeInX() {
      return internalJointImp.isFreeInX();
    }

    @Override
    public boolean isFreeInY() {
      return internalJointImp.isFreeInY();
    }

    @Override
    public boolean isFreeInZ() {
      return internalJointImp.isFreeInZ();
    }

    void replaceWithJoint(JointImp newJoint, UnitQuaternion originalRotation) {
      OrthogonalMatrix3x3 currentRotation = this.getLocalOrientation();
      OrthogonalMatrix3x3 invertedOriginal = new OrthogonalMatrix3x3();
      invertedOriginal.setValue(originalRotation);
      invertedOriginal.invert();
      OrthogonalMatrix3x3 dif = new OrthogonalMatrix3x3();
      dif.setToMultiplication(invertedOriginal, currentRotation);
      if (!dif.isWithinReasonableEpsilonOfIdentity()) {
        OrthogonalMatrix3x3 newRotation = new OrthogonalMatrix3x3();
        newRotation.setToMultiplication(newJoint.getLocalOrientation(), dif);
        newJoint.setLocalOrientation(newRotation);
      }
      if (this.getAbstraction() != null) {
        newJoint.setAbstraction(this.getAbstraction());
      }
      for (Component child : this.internalJointImp.getSgComposite().getComponents()) {
        if (!(child instanceof ModelJoint)) {
          child.setParent(newJoint.getSgComposite());

        }
      }
      this.internalJointImp = newJoint;
    }

    @Override
    public AxisAlignedBox getAxisAlignedMinimumBoundingBox(ReferenceFrame asSeenBy) {
      return internalJointImp.getAxisAlignedMinimumBoundingBox(asSeenBy);
    }

    @Override
    public AbstractTransformable getSgComposite() {
      return internalJointImp.getSgComposite();
    }

    @Override
    protected CumulativeBound updateCumulativeBound(CumulativeBound rv, AffineMatrix4x4 trans) {
      return internalJointImp.updateCumulativeBound(rv, trans);
    }

    @Override
    public UnitQuaternion getOriginalOrientation() {
      return internalJointImp.getOriginalOrientation();
    }

    @Override
    public AffineMatrix4x4 getOriginalTransformation() {
      return internalJointImp.getOriginalTransformation();
    }

    @Override
    public AffineMatrix4x4 getLocalTransformation() {
      return internalJointImp.getLocalTransformation();
    }

    @Override
    public void setLocalTransformation(AffineMatrix4x4 transformation) {
      internalJointImp.setLocalTransformation(transformation);
    }

    @Override
    protected void postCheckSetVehicle(EntityImp vehicle) {
      //todo?
      internalJointImp.postCheckSetVehicle(vehicle);
    }

    @Override
    public boolean isFacing(EntityImp other) {
      return this.internalJointImp.isFacing(other);
    }

    @Override
    public void applyTranslation(double x, double y, double z, ReferenceFrame asSeenBy) {
      this.internalJointImp.applyTranslation(x, y, z, asSeenBy);
    }

    @Override
    public void applyRotationInRadians(Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy) {
      this.internalJointImp.applyRotationInRadians(axis, angleInRadians, asSeenBy);
    }

    @Override
    public boolean isPivotVisible() {
      return internalJointImp.isPivotVisible();
    }

    @Override
    public void setPivotVisible(boolean isPivotVisible) {
      internalJointImp.setPivotVisible(isPivotVisible);
    }

    private JointImp internalJointImp;
    private JointImp jointParentWrapper;
    private List<JointImp> jointChildrenWrapper = new ArrayList<>();
  }

  public JointedModelImp(A abstraction, JointImplementationAndVisualDataFactory<R> factory) {
    this.abstraction = abstraction;
    this.factory = factory;
    this.visualData = this.factory.createVisualData();
    this.sgScalable = null;

    List<JointId> allJointIds = getAllJoints();

    Map<JointId, JointImp> jointMap = createJointImps(allJointIds);
    //Make all the joint wrappers and put them in the map
    for (Map.Entry<JointId, JointImp> entry : jointMap.entrySet()) {
      JointImpWrapper wrapper = new JointImpWrapper(this, entry.getValue());
      mapIdToJoint.put(entry.getKey(), wrapper);
    }
    //Go through all the wrappers and link up the parent/child relationship
    for (Map.Entry<JointId, JointImpWrapper> entry : mapIdToJoint.entrySet()) {
      entry.getValue().setJointParent(mapIdToJoint.get(entry.getKey().getParent()));
    }

    this.visualData.setSGParent(getSgComposite());

    for (JointImpWrapper entry : mapIdToJoint.values()) {
      final AbstractTransformable sgJoint = entry.getSgComposite();
      if (sgJoint.getParent() == null) {
        sgJoint.setParent(getSgComposite());
      }
    }

    for (Visual sgVisual : this.visualData.getSgVisuals()) {
      putInstance(sgVisual);
    }
    for (SimpleAppearance sgAppearance : this.visualData.getSgAppearances()) {
      putInstance(sgAppearance);
    }
  }

  private Map<JointId, JointImp> createJointImps(List<JointId> allJointIds) {
    Map<JointId, JointImp> jointMap = new HashMap<>();
    for (JointId jointId : allJointIds) {
      jointMap.put(jointId, this.createJointImplementation(jointId));
    }
    for (JointId jointId : allJointIds) {
      JointImp jointImp = jointMap.get(jointId);
      JointImp parentImp = jointMap.get(jointId.getParent());
      jointImp.setJointParent(parentImp);
      if (jointImp.getSgVehicle() == null) {
        jointImp.setVehicle(parentImp);
      }
    }
    return jointMap;
  }

  private List<JointId> getAllJoints() {
    List<JointId> allJoints = new ArrayList<>();
    JointedModelResource resource = getResource();
    for (Field jointField : ReflectionUtilities.getPublicStaticFinalFields(resource.getClass(), JointId.class)) {
      try {
        JointId joint = (JointId) jointField.get(null);
        allJoints.add(joint);
      } catch (IllegalAccessException iae) {
        Logger.throwable(iae, jointField);
      }
    }
    if (resource instanceof DynamicResource) {
      allJoints.addAll(Arrays.asList(((DynamicResource) resource).getModelSpecificJoints()));
    }
    //Handle joint arrays
    for (JointArrayId arrayId : this.getJointArrayIds()) {
      JointId[] jointArrayIds = this.factory.getJointArrayIds(this, arrayId);
      for (JointId jointId : jointArrayIds) {
        allJoints.add(jointId);
      }
    }

    return allJoints;
  }

  public JointId[] getJointIdArray(JointArrayId jointArrayId) {
    return this.mapArrayIdToJointIdArray.get(jointArrayId);
  }

  //TODO: Do we need this? Why do we need this.getJointArrayIds()?
  public JointArrayId[] getJointArrayIds() {
    if (this.jointArrayIds == null) {
      List<JointArrayId> jointArrayIdsList = ReflectionUtilities.getPublicStaticFinalInstances(this.getResource().getClass(), JointArrayId.class);
      this.jointArrayIds = jointArrayIdsList.toArray(new JointArrayId[jointArrayIdsList.size()]);
    }
    return this.jointArrayIds;
  }

  //  private List<JointId> getMissingJoints() {
  //    List<JointId> missingJoints = new ArrayList<JointId>();
  //    List<JointId> jointsToCheck = new ArrayList<JointId>();
  //    JointId[] rootIds = this.getRootJointImps();
  //    Collections.addAll( jointsToCheck, rootIds );
  //    while( !jointsToCheck.isEmpty() ) {
  //      JointId joint = jointsToCheck.remove( 0 );
  //      if( !this.hasJointImplementation( joint ) ) {
  //        missingJoints.add( joint );
  //      }
  //      for( JointId child : joint.getChildren( this.getResource() ) ) {
  //        jointsToCheck.add( child );
  //      }
  //    }
  //    return missingJoints;
  //  }

  public void setNewResource(JointedModelResource resource) {
    if (resource != this.getResource()) {
      Map<JointId, UnitQuaternion> mapIdToOriginalRotation = Maps.newHashMap();
      for (Map.Entry<JointId, JointImpWrapper> jointEntry : this.mapIdToJoint.entrySet()) {
        mapIdToOriginalRotation.put(jointEntry.getKey(), jointEntry.getValue().getOriginalOrientation());
      }
      Composite originalParent = this.visualData.getSGParent();
      VisualData<?> oldVisualData = this.visualData;
      Dimension3 oldScale = this.getScale();
      InstanceProperty[] oldScaleProperties = this.getScaleProperties();
      this.factory = (JointImplementationAndVisualDataFactory<R>) resource.getImplementationAndVisualFactory();
      float originalOpacity = this.opacity.getValue();
      Paint originalPaint = this.paint.getValue();
      this.visualData = this.factory.createVisualData();

      List<JointId> allJointIds = getAllJoints();
      Map<JointId, JointImp> newJoints = createJointImps(allJointIds);

      matchNewDataToExistingJoints(mapIdToOriginalRotation, newJoints);

      this.visualData.setSGParent(originalParent);
      oldVisualData.setSGParent(null);
      this.opacity.setValue(originalOpacity);
      this.paint.setValue(originalPaint);

      InstanceProperty<?>[] newScaleProperties = this.getScaleProperties();
      for (int i = 0; i < oldScaleProperties.length; i++) {
        InstanceProperty<?> oldProp = oldScaleProperties[i];
        assert oldProp != null : i;
        for (PropertyListener propListener : oldProp.getPropertyListeners()) {
          newScaleProperties[i].addPropertyListener(propListener);
        }
      }
      this.setScale(oldScale);
    }
  }

  public JointedModelResource getVisualResource() {
    return this.factory.getResource();
  }

  public Iterable<JointImp> getJoints() {
    final List<JointImp> rv = Lists.newLinkedList();
    this.treeWalk(new TreeWalkObserver() {
      @Override
      public void pushJoint(JointImp joint) {
        rv.add(joint);
      }

      @Override
      public void handleBone(JointImp parent, JointImp child) {
      }

      @Override
      public void popJoint(JointImp joint) {
      }
    });
    return rv;
  }

  private void matchNewDataToExistingJoints(Map<JointId, UnitQuaternion> mapIdToOriginalRotation, Map<JointId, JointImp> newJoints) {
    List<JointId> toRemove = Lists.newLinkedList();
    for (Map.Entry<JointId, JointImpWrapper> jointEntry : this.mapIdToJoint.entrySet()) {
      if (newJoints.containsKey(jointEntry.getKey())) {
        JointImp newJoint = newJoints.get(jointEntry.getKey());
        jointEntry.getValue().replaceWithJoint(newJoint, mapIdToOriginalRotation.get(jointEntry.getKey()));
      } else {
        toRemove.add(jointEntry.getKey());
      }
    }
    for (JointId id : toRemove) {
      JointImpWrapper impToRemove = this.mapIdToJoint.remove(id);
      //Reparent children
      for (JointImp childImp : impToRemove.getJointChildren()) {
        childImp.setJointParent(impToRemove.getJointParent());
      }
      impToRemove.setJointParent(null);
      AbstractTransformable sgJoint = impToRemove.getSgComposite();
      for (Component c : sgJoint.getComponents()) {
        c.setParent(impToRemove.getJointParent().getSgComposite());
      }
      sgJoint.setParent(null);
    }
  }

  public void setAllJointPivotsVisibile(boolean isPivotVisible) {
    for (Map.Entry<JointId, JointImpWrapper> jointEntry : this.mapIdToJoint.entrySet()) {
      jointEntry.getValue().setPivotVisible(isPivotVisible);
    }
  }

  @Override
  public A getAbstraction() {
    return this.abstraction;
  }

  public R getResource() {
    return this.factory.getResource();
  }

  public VisualData getVisualData() {
    return this.visualData;
  }

  @Override
  public final Visual[] getSgVisuals() {
    return this.visualData.getSgVisuals();
  }

  @Override
  protected final SimpleAppearance[] getSgPaintAppearances() {
    return this.visualData.getSgAppearances();
  }

  @Override
  protected final SimpleAppearance[] getSgOpacityAppearances() {
    return this.getSgPaintAppearances();
  }

  public JointImp getJointImplementation(JointId jointId) {
    JointImpWrapper wrapper = this.mapIdToJoint.get(jointId);
    if (wrapper != null) {
      return wrapper;
    }
    return null;
  }

  //String based lookup for DynamicJointIds
  public JointImp getJointImplementation(String jointName) {
    JointImpWrapper wrapper = null;
    //TODO: Think about maintaining a map of names to joints. It would be double accounting and might be a pain though
    for (Map.Entry<JointId, JointImpWrapper> entry : this.mapIdToJoint.entrySet()) {
      if (entry.getKey().toString().equals(jointName)) {
        wrapper = entry.getValue();
        break;
      }
    }
    if (wrapper != null) {
      return wrapper;
    }
    return null;
  }

  protected Vector4 getFrontOffsetForJoint(JointImp jointImp) {
    Vector4 offsetAsSeenBySubject = new Vector4();
    AxisAlignedBox bbox = jointImp.getAxisAlignedMinimumBoundingBox(this);
    Point3 point = bbox.getCenterOfFrontFace();
    offsetAsSeenBySubject.x = point.x;
    offsetAsSeenBySubject.y = point.y;
    offsetAsSeenBySubject.z = point.z;
    offsetAsSeenBySubject.w = 1;
    return offsetAsSeenBySubject;
  }

  protected Vector4 getTopOffsetForJoint(JointImp jointImp) {
    Vector4 offsetAsSeenBySubject = new Vector4();
    AxisAlignedBox bbox = jointImp.getAxisAlignedMinimumBoundingBox(this);
    Point3 point = bbox.getCenterOfTopFace();
    offsetAsSeenBySubject.x = point.x;
    offsetAsSeenBySubject.y = point.y;
    offsetAsSeenBySubject.z = point.z;
    offsetAsSeenBySubject.w = 1;
    return offsetAsSeenBySubject;
  }

  public UnitQuaternion getOriginalJointOrientation(JointId jointId) {
    return this.factory.getOriginalJointOrientation(jointId);
  }

  public AffineMatrix4x4 getOriginalJointTransformation(JointId jointId) {
    return this.factory.getOriginalJointTransformation(jointId);
  }

  public SkeletonVisual getSgSkeletonVisual() {
    if (this.getSgVisuals()[0] instanceof SkeletonVisual) {
      return (SkeletonVisual) this.getSgVisuals()[0];
    }
    return null;
  }

  @Override
  protected InstanceProperty[] getScaleProperties() {
    if (this.sgScalable != null) {
      return new InstanceProperty[] {this.sgScalable.scale};
    } else {
      return new InstanceProperty[] {this.visualData.getSgVisuals()[0].scale};
    }
  }

  @Override
  public Dimension3 getScale() {
    if (this.sgScalable != null) {
      return this.sgScalable.scale.getValue();
    } else {
      Matrix3x3 scale = this.visualData.getSgVisuals()[0].scale.getValue();
      return new Dimension3(scale.right.x, scale.up.y, scale.backward.z);
    }
  }

  @Override
  public void setScale(Dimension3 scale) {
    if (this.sgScalable != null) {
      this.sgScalable.scale.setValue(new Dimension3(scale));
    } else {
      Matrix3x3 m = Matrix3x3.createZero();
      m.right.x = scale.x;
      m.up.y = scale.y;
      m.backward.z = scale.z;

      for (Visual sgVisual : this.visualData.getSgVisuals()) {
        sgVisual.scale.setValue(m);
      }
      for (JointImp jointImp : this.mapIdToJoint.values()) {
        AffineMatrix4x4 lt = jointImp.getLocalTransformation();
        lt.translation.setToMultiplication(jointImp.getOriginalTransformation().translation, scale);
        jointImp.setLocalTransformation(lt);
      }
    }
  }

  public AxisAlignedBox getAxisAlignedMinimumBoundingBox(ReferenceFrame asSeenBy, boolean ignoreJointOrientations) {
    AffineMatrix4x4 trans = this.getTransformation(asSeenBy);
    CumulativeBound cumulativeBound = new CumulativeBound();
    this.updateCumulativeBound(cumulativeBound, trans, ignoreJointOrientations);
    return cumulativeBound.getBoundingBox();
  }

  public AxisAlignedBox getAxisAlignedMinimumBoundingBox(boolean ignoreJointOrientations) {
    return getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF, ignoreJointOrientations);
  }

  @Override
  public AxisAlignedBox getAxisAlignedMinimumBoundingBox(ReferenceFrame asSeenBy) {
    return getAxisAlignedMinimumBoundingBox(asSeenBy, true);
  }

  @Override
  public AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
    return getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF, true);
  }

  @Override
  public AxisAlignedBox getDynamicAxisAlignedMinimumBoundingBox(ReferenceFrame asSeenBy) {
    return getAxisAlignedMinimumBoundingBox(asSeenBy, false);
  }

  @Override
  public AxisAlignedBox getDynamicAxisAlignedMinimumBoundingBox() {
    return getDynamicAxisAlignedMinimumBoundingBox(AsSeenBy.SELF);
  }

  @Override
  public Dimension3 getSize() {
    return getAxisAlignedMinimumBoundingBox().getSize();
  }

  public Dimension3 getSize(boolean ignoreJointOrientations) {
    return getAxisAlignedMinimumBoundingBox(ignoreJointOrientations).getSize();
  }

  @Override
  public void setSize(Dimension3 size) {
    setScale(getScaleForSize(size));
  }

  protected CumulativeBound updateCumulativeBound(CumulativeBound rv, AffineMatrix4x4 trans, boolean ignoreJointOrientations) {
    for (Visual sgVisual : this.getSgVisuals()) {
      if (sgVisual instanceof SkeletonVisual) {
        rv.addSkeletonVisual((SkeletonVisual) sgVisual, trans, ignoreJointOrientations);
      } else {
        rv.add(sgVisual, trans);
      }
    }
    return rv;
  }

  @Override
  protected CumulativeBound updateCumulativeBound(CumulativeBound rv, AffineMatrix4x4 trans) {
    return updateCumulativeBound(rv, trans, true);
  }

  protected final JointImp createJointImplementation(JointId jointId) {
    return this.factory.createJointImplementation(this, jointId);
  }

  protected final boolean hasJointImplementation(JointId jointId) {
    return this.factory.hasJointImplementation(this, jointId);
  }

  private JointedModelVisualization visualization;

  @Override
  protected Leaf getVisualization() {
    if (this.visualization != null) {
      //pass
    } else {
      this.visualization = new JointedModelVisualization(this);
    }
    return this.visualization;
  }

  @Override
  public void showVisualization() {
    this.getVisualization().setParent(this.getSgComposite());
  }

  @Override
  public void hideVisualization() {
    if (this.visualization != null) {
      this.visualization.setParent(null);
    }
  }

  private List<JointImp> getRootJointImps() {
    List<JointImp> rootJoints = new ArrayList<>();
    for (Map.Entry<JointId, JointImpWrapper> entry : mapIdToJoint.entrySet()) {
      if (entry.getKey().getParent() == null) {
        rootJoints.add(entry.getValue());
      }
    }
    return rootJoints;
  }

  public static interface TreeWalkObserver {
    public void pushJoint(JointImp joint);

    public void handleBone(JointImp parent, JointImp child);

    public void popJoint(JointImp joint);
  }

  private void treeWalk(JointImp parentImp, TreeWalkObserver observer) {
    if (parentImp != null) {
      observer.pushJoint(parentImp);
      R resource = this.getResource();
      for (JointImp childImp : parentImp.getJointChildren()) {
        if (childImp != null) {
          observer.handleBone(parentImp, childImp);
        }
      }
      observer.popJoint(parentImp);
      for (JointImp childImp : parentImp.getJointChildren()) {
        treeWalk(childImp, observer);
      }
    }
  }

  public void treeWalk(TreeWalkObserver observer) {
    for (JointImp root : this.getRootJointImps()) {
      this.treeWalk(root, observer);
    }
  }

  private static enum AddOp {
    PREPEND {
      @Override
      public List<JointImp> add(List<JointImp> rv, JointImp joint, List<Bone.Direction> directions, Bone.Direction direction) {
        rv.add(0, joint);
        if (directions != null) {
          directions.add(0, direction);
        }
        return rv;
      }
    }, APPEND {
      @Override
      public List<JointImp> add(List<JointImp> rv, JointImp joint, List<Bone.Direction> directions, Bone.Direction direction) {
        rv.add(joint);
        if (directions != null) {
          directions.add(direction);
        }
        return rv;
      }
    };

    public abstract List<JointImp> add(List<JointImp> rv, JointImp joint, List<Bone.Direction> directions, Bone.Direction direction);
  }

  private List<JointImp> updateJointsBetween(List<JointImp> rv, List<Bone.Direction> directions, JointImp joint, EntityImp ancestorToReach, AddOp addOp) {
    if (joint == ancestorToReach) {
      //pass
    } else {
      JointId parentId = joint.getJointId().getParent();
      if (parentId != null) {
        JointImp parent = this.getJointImplementation(parentId);
        this.updateJointsBetween(rv, directions, parent, ancestorToReach, addOp);
      }
    }
    Bone.Direction direction;
    if (addOp == AddOp.APPEND) {
      direction = Bone.Direction.DOWNSTREAM;
    } else {
      direction = Bone.Direction.UPSTREAM;
    }
    addOp.add(rv, joint, directions, direction);
    return rv;
  }

  public List<JointImp> getInclusiveListOfJointsBetween(JointImp jointA, JointImp jointB, List<Bone.Direction> directions) {
    assert jointA != null : this;
    assert jointB != null : this;
    List<JointImp> rv = Lists.newLinkedList();
    if (jointA == jointB) {
      //?
      rv.add(jointA);
      directions.add(Bone.Direction.DOWNSTREAM);
      //      throw new RuntimeException( "To Gazi: Please ensure that direction is correct in this case." );
    } else {
      if (jointA.isDescendantOf(jointB)) {
        this.updateJointsBetween(rv, directions, jointA, jointB, AddOp.PREPEND);
      } else if (jointB.isDescendantOf(jointA)) {
        this.updateJointsBetween(rv, directions, jointB, jointA, AddOp.APPEND);
      } else {
        //It shouldn't even use the joint on which direction is changed (the common ancestor)
        //that's what the below call does
        this.updateJointsUpToAndExcludingCommonAncestor(rv, directions, jointA, jointB);
      }
    }
    return rv;
  }

  private void updateJointsUpToAndExcludingCommonAncestor(List<JointImp> rvPath, List<Direction> rvDirections, JointImp jointA, JointImp jointB) {
    List<JointImp> pathA = Lists.newLinkedList();
    List<JointImp> pathB = Lists.newLinkedList();

    List<Direction> directionsA = new ArrayList<Direction>();
    List<Direction> directionsB = new ArrayList<Direction>();

    this.updateJointsBetween(pathA, directionsA, jointA, this, AddOp.PREPEND);
    this.updateJointsBetween(pathB, directionsB, jointB, this, AddOp.APPEND);

    JointImp commonAncestor = null;

    for (JointImp jointInA : pathA) {
      if (pathB.contains(jointInA)) {
        commonAncestor = jointInA;
        break;
      }
    }

    if (commonAncestor == null) {
      throw new RuntimeException("Probably not connected with a chain.");
    }

    ListIterator<JointImp> pathAIterator = pathA.listIterator(pathA.size());
    ListIterator<Direction> directionsAIterator = directionsA.listIterator(directionsA.size());
    for (; pathAIterator.hasPrevious(); ) {
      JointImp jointImp = pathAIterator.previous();
      directionsAIterator.previous();

      pathAIterator.remove();
      directionsAIterator.remove();

      if (jointImp == commonAncestor) {
        break;
      }
    }

    ListIterator<JointImp> pathBIterator = pathB.listIterator();
    ListIterator<Direction> directionsBIterator = directionsB.listIterator();
    for (; pathBIterator.hasNext(); ) {
      JointImp jointImp = (JointImp) pathBIterator.next();
      directionsBIterator.next();

      pathBIterator.remove();
      directionsBIterator.remove();

      if (jointImp == commonAncestor) {
        break;
      }
    }

    rvPath.addAll(pathA);
    rvPath.addAll(pathB);
    rvDirections.addAll(directionsA);
    rvDirections.addAll(directionsB);
  }

  public List<JointImp> getInclusiveListOfJointsBetween(JointId idA, JointId idB, List<Bone.Direction> directions) {
    return this.getInclusiveListOfJointsBetween(this.getJointImplementation(idA), this.getJointImplementation(idB), directions);
  }

  private static class JointData {
    private final JointImp jointImp;
    private final UnitQuaternion q0;
    private final UnitQuaternion q1;

    public JointData(JointImp jointImp) {
      this.jointImp = jointImp;
      this.q0 = this.jointImp.getLocalOrientation().createUnitQuaternion();
      UnitQuaternion q = this.jointImp.getOriginalOrientation();
      if (q != null) {
        if (this.q0.isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon(q)) {
          this.q1 = null;
        } else {
          this.q1 = q;
        }
      } else {
        this.q1 = null;
      }
    }

    //    public JointImp getJointImp() {
    //      return this.jointImp;
    //    }
    //    public edu.cmu.cs.dennisc.math.UnitQuaternion getQ0() {
    //      return this.q0;
    //    }
    //    public edu.cmu.cs.dennisc.math.UnitQuaternion getQ1() {
    //      return this.q1;
    //    }
    public void setPortion(double portion) {
      if (this.q1 != null) {
        this.jointImp.setLocalOrientationOnly(UnitQuaternion.createInterpolation(this.q0, this.q1, portion).createOrthogonalMatrix3x3());
      } else {
        //System.err.println( "skipping: " + this.jointImp );
      }
    }

    public void epilogue() {
      if (this.q1 != null) {
        this.jointImp.setLocalOrientationOnly(this.q1.createOrthogonalMatrix3x3());
      } else {
        //System.err.println( "skipping: " + this.jointImp );
      }
    }
  }

  private static class StraightenTreeWalkObserver implements TreeWalkObserver {
    private List<JointData> list = Lists.newLinkedList();

    @Override
    public void pushJoint(JointImp jointImp) {
      list.add(new JointData(jointImp));
    }

    @Override
    public void handleBone(JointImp parent, JointImp child) {
    }

    @Override
    public void popJoint(JointImp joint) {
    }
  }

  public void straightenOutJoints() {
    StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
    this.treeWalk(treeWalkObserver);
    for (JointData jointData : treeWalkObserver.list) {
      jointData.epilogue();
    }
  }

  public void animateStraightenOutJoints(double duration, Style style) {
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      this.straightenOutJoints();
    } else {
      final StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
      this.treeWalk(treeWalkObserver);
      class StraightenOutJointsAnimation extends DurationBasedAnimation {
        public StraightenOutJointsAnimation(double duration, Style style) {
          super(duration, style);
        }

        @Override
        protected void prologue() {
        }

        @Override
        protected void setPortion(double portion) {
          for (JointData jointData : treeWalkObserver.list) {
            jointData.setPortion(portion);
          }
        }

        @Override
        protected void epilogue() {
          for (JointData jointData : treeWalkObserver.list) {
            jointData.epilogue();
          }
        }
      }
      perform(new StraightenOutJointsAnimation(duration, style));
    }
  }

  public void animateStraightenOutJoints(double duration) {
    this.animateStraightenOutJoints(duration, DEFAULT_STYLE);
  }

  public void animateStraightenOutJoints() {
    this.animateStraightenOutJoints(DEFAULT_DURATION);
  }

  public void strikePose(Pose<? extends SJointedModel> pose, double duration, Style style) {
    this.getProgram().perform(new PoseAnimation(duration, style, this, pose), null);
  }

  private final A abstraction;
  private final Scalable sgScalable;
  private final Map<JointId, JointImpWrapper> mapIdToJoint = Maps.newHashMap();
  private final Map<JointArrayId, JointId[]> mapArrayIdToJointIdArray = Maps.newHashMap();
  private JointArrayId[] jointArrayIds = null;
  private JointImplementationAndVisualDataFactory<R> factory;
  private VisualData visualData;
}
