package org.alice.stageide.gallerybrowser;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import org.alice.stageide.StageIDE;
import org.alice.stageide.gallerybrowser.views.MyGalleryTabView;
import org.alice.stageide.gallerybrowser.views.TreeOwningGalleryTabView;
import org.alice.stageide.modelresource.TreeUtilities;
import org.lgna.croquet.Operation;
import org.lgna.croquet.edits.Edit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class MyGalleryTab extends TreeOwningGalleryTab {
  MyGalleryTab() {
    super(UUID.fromString("569f0623-bef4-4d37-b96a-65cd25048a6e"), TreeUtilities.getUserTreeState(), "CustomGalleryTab");
  }

  public Operation getInvokeExternalScriptOperation() {
    return createActionOperation("invokeExternalScriptOperation", (userActivity, source) -> invokeScript());
  }

  private Edit invokeScript() {
    File galleryDir = StageIDE.getActiveInstance().getGalleryDirectory();
    Path script = galleryDir.toPath().resolve("addToGallery." + SystemUtilities.getScriptExtension());
    ProcessBuilder pb = new ProcessBuilder(script.toString());
    pb.directory(galleryDir);
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
