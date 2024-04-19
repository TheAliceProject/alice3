package org.lgna.croquet;

import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;

import java.util.UUID;

/*
 * General purpose operation to apply an Edit connected to a specific Group.
 */
public class EditOperation extends ActionOperation {
  private final Edit edit;

  // Default to Project Group so the Edit lands in the undo stack
  public EditOperation(Edit edit) {
    this(Application.PROJECT_GROUP, edit);
  }

  public EditOperation(Group group, Edit edit) {
    super(group, UUID.randomUUID());
    this.edit = edit;
  }

  @Override
  protected void perform(UserActivity activity) {
    activity.commitAndInvokeDo(edit);
  }
}
