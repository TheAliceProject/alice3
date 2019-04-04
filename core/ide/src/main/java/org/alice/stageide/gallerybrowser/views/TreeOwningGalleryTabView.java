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
package org.alice.stageide.gallerybrowser.views;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.stageide.gallerybrowser.TreeOwningGalleryTab;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.ResourceNodeTreeState;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TreeDirectoryViewController;
import org.lgna.croquet.views.TreePathViewController;

import javax.swing.*;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class TreeOwningGalleryTabView extends GalleryTabView {
  private class ModelResourceDirectoryView extends TreeDirectoryViewController<ResourceNode> {
    public ModelResourceDirectoryView(SingleSelectTreeState<ResourceNode> model) {
      super(model);
    }

    @Override
    protected SwingComponentView<?> getComponentFor(ResourceNode value) {
      return TreeOwningGalleryTabView.this.getGalleryDragComponent(value, getModel());
    }
  }

  private final ValueListener<ResourceNode> treeListener = new ValueListener<ResourceNode>() {
    @Override
    public void valueChanged(ValueEvent<ResourceNode> e) {
      handleChanged(e.getPreviousValue(), e.getNextValue());
    }
  };

  private final Map<ResourceNode, Integer> mapNodeToHorizontalScrollPosition = Maps.newHashMap();
  private final ScrollPane scrollPane;
  private final ModelResourceDirectoryView view;

  public TreeOwningGalleryTabView(TreeOwningGalleryTab composite) {
    super(composite);

    ResourceNodeTreeState state = composite.getResourceNodeTreeSelectionState();
    view = new ModelResourceDirectoryView(state);

    this.scrollPane = createGalleryScrollPane(view);

    BorderPanel panel = new BorderPanel.Builder().vgap(PAD).pageStart(new TreePathViewController(state, null)).center(scrollPane).build();

    this.addCenterComponent(panel);

    //todo
    view.setBackgroundColor(GalleryView.BACKGROUND_COLOR);
    panel.setBackgroundColor(GalleryView.BACKGROUND_COLOR);
    this.setBackgroundColor(GalleryView.BACKGROUND_COLOR);
  }

  @Override
  protected void handleDisplayable() {
    TreeOwningGalleryTab composite = (TreeOwningGalleryTab) this.getComposite();
    ResourceNodeTreeState state = composite.getResourceNodeTreeSelectionState();
    state.addNewSchoolValueListener(this.treeListener);
    super.handleDisplayable();
  }

  @Override
  protected void handleUndisplayable() {
    super.handleUndisplayable();
    TreeOwningGalleryTab composite = (TreeOwningGalleryTab) this.getComposite();
    ResourceNodeTreeState state = composite.getResourceNodeTreeSelectionState();
    state.removeNewSchoolValueListener(this.treeListener);
  }

  private void handleChanged(ResourceNode prevValue, ResourceNode nextValue) {
    final JScrollBar jHorizontalScrollBar = this.scrollPane.getAwtComponent().getHorizontalScrollBar();
    this.mapNodeToHorizontalScrollPosition.put(prevValue, jHorizontalScrollBar.getValue());
    Integer i = this.mapNodeToHorizontalScrollPosition.get(nextValue);
    final int nextScrollPosition;
    if (i != null) {
      nextScrollPosition = i;
    } else {
      nextScrollPosition = 0;
    }
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        jHorizontalScrollBar.setValue(nextScrollPosition);
      }
    });
  }

  @Override
  public void modelUpdated() {
    view.modelUpdated();
  }
}
