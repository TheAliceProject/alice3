package org.alice.stageide.sceneeditor.viewmanager.edits;

import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.TransformableImp;

/*
 * Edit operation with fixed object
 */
public class MoveTransformableEdit extends AbstractEdit<ActionOperation> {
  private final AbstractTransformableImp toMove;
  private final AffineMatrix4x4 start;
  private final AffineMatrix4x4 end;

  public MoveTransformableEdit(UserActivity userActivity, TransformableImp toMove, AffineMatrix4x4 end) {
    super(userActivity);
    this.toMove = toMove;
    this.start = toMove.getAbsoluteTransformation();
    this.end = end;
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    super.encode(binaryEncoder);
    assert false : "Not implemented yet";
  }

  @Override
  protected void doOrRedoInternal(boolean isDo) {
    if (toMove != null && end != null) {
      toMove.animateTransformation(AsSeenBy.SCENE, end);
    }
  }

  @Override
  protected void undoInternal() {
    if (start != null) {
      toMove.animateTransformation(AsSeenBy.SCENE, start);
    }
  }

  @Override
  protected void appendDescription(StringBuilder rv, DescriptionStyle descriptionStyle) {
    rv.append("move something in the scene");
  }
}
