package org.alice.stageide.gallerybrowser.views;

import org.alice.stageide.gallerybrowser.MyGalleryTab;
import org.alice.stageide.gallerybrowser.TreeOwningGalleryTab;
import org.alice.stageide.modelresource.ResourceNodeTreeState;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.MigPanel;

import javax.swing.*;

public class MyGalleryTabView extends TreeOwningGalleryTabView {

  public MyGalleryTabView(TreeOwningGalleryTab composite) {
    super(composite);
  }

  @Override
  protected AwtComponentView<JPanel> getPageStart(ResourceNodeTreeState state) {
    MigPanel holder = new MigPanel(null, "insets 0");
    holder.addComponent(super.getPageStart(state));
    holder.addComponent(((MyGalleryTab) getComposite()).getInvokeExternalScriptOperation().createButton());
    return holder;
  }
}
