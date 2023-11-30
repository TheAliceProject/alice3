package org.alice.stageide.gallerybrowser;

import org.alice.stageide.StageIDE;
import org.alice.stageide.gallerybrowser.views.MyGalleryTabView;
import org.alice.stageide.gallerybrowser.views.TreeOwningGalleryTabView;
import org.alice.stageide.modelresource.TreeUtilities;
import org.lgna.croquet.Operation;
import org.lgna.croquet.edits.Edit;

import java.io.IOException;
import java.util.UUID;

public class MyGalleryTab extends TreeOwningGalleryTab {
  MyGalleryTab() {
    super(UUID.fromString("569f0623-bef4-4d37-b96a-65cd25048a6e"), TreeUtilities.getUserTreeState(), "CustomGalleryTab");
  }

  public Operation getInvokeExternalScriptOperation() {
    return createActionOperation("invokeExternalScriptOperation", (userActivity, source) -> invokeScript());
  }

  private Edit invokeScript() {
    ProcessBuilder pb = new ProcessBuilder("./addToGallery");
    pb.directory(StageIDE.getActiveInstance().getGalleryDirectory());
    try {
      pb.start();
    } catch (IOException e) {
      System.out.println("Invoking addToGallery threw " + e);
    }
    return null;
  }

  @Override
  protected TreeOwningGalleryTabView createView() {
    return new MyGalleryTabView(this);
  }
}
