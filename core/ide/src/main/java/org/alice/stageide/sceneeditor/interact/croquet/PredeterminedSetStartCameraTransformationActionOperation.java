package org.alice.stageide.sceneeditor.interact.croquet;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.Group;
import org.lgna.project.ast.UserField;

import java.util.UUID;

public class PredeterminedSetStartCameraTransformationActionOperation extends AbstractPredeterminedSetLocalTransformationActionOperation {
  private final StorytellingSceneEditor sceneEditor;

  public PredeterminedSetStartCameraTransformationActionOperation(Group group, boolean isDoRequired, Animator animator, UserField field, AffineMatrix4x4 prevLT, AffineMatrix4x4 nextLT, StorytellingSceneEditor sceneEditor, String editPresentationKey) {
    super(group, UUID.fromString("6c925ae4-ad06-4929-8da9-3e13dd17035c"), isDoRequired, animator, field, prevLT, nextLT, editPresentationKey);
    this.sceneEditor = sceneEditor;
  }

  protected void setLocalTransformation(AffineMatrix4x4 lt) {
    if (sceneEditor.isStartingCameraView()) {
      super.setLocalTransformation(lt);
    } else {
      sceneEditor.setStartingCameraMarkerTransformation(lt);
    }
  }
}
