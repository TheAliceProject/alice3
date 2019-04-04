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
package org.alice.stageide.gallerybrowser.search.croquet.views;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import org.alice.stageide.gallerybrowser.search.croquet.SearchTab;
import org.alice.stageide.gallerybrowser.views.GalleryTabView;
import org.alice.stageide.gallerybrowser.views.GalleryView;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.TreeUtilities;
import org.lgna.croquet.views.AbstractLabel;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.TextField;

import javax.swing.Icon;
import java.awt.Color;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class SearchTabView extends GalleryTabView {
  public static final Icon SEARCH_ICON = IconUtilities.createImageIcon(SearchTabView.class.getResource("images/system-search.png"));
  private final AbstractLabel noMatchesLabel;
  private final AbstractLabel noEntryLabel;

  private final LineAxisPanel filteredResourcesView = new LineAxisPanel();
  private final TextField filterTextField;

  public SearchTabView(SearchTab composite) {
    super(composite);

    this.noMatchesLabel = composite.getNoMatchesLabel().createLabel(1.4f, TextPosture.OBLIQUE);
    this.noEntryLabel = composite.getNoEntryLabel().createLabel(1.4f, TextPosture.OBLIQUE);
    this.noMatchesLabel.setForegroundColor(Color.DARK_GRAY);
    this.noEntryLabel.setForegroundColor(Color.DARK_GRAY);

    this.filterTextField = composite.getFilterState().createTextField();
    this.filterTextField.setMinimumPreferredWidth(320);
    this.filterTextField.setMaximumSizeClampedToPreferredSize(true);
    this.filterTextField.scaleFont(1.2f);
    this.filterTextField.enableSelectAllWhenFocusGained();

    ScrollPane scrollPane = createGalleryScrollPane(this.filteredResourcesView);
    this.filteredResourcesView.setBackgroundColor(GalleryView.BACKGROUND_COLOR);

    this.addPageStartComponent(new LineAxisPanel(composite.getFilterState().getSidekickLabel().createLabel(), this.filterTextField));
    this.addCenterComponent(scrollPane);
  }

  @Override
  public void handleCompositePreActivation() {
    super.handleCompositePreActivation();
    this.filterTextField.requestFocusLater();
  }

  //@Override
  //public void handleCompositePostDeactivation() {
  //  super.handleCompositePostDeactivation();
  //}

  public void removeAllGalleryDragComponents() {
    synchronized (this.getTreeLock()) {
      this.filteredResourcesView.removeAllComponents();
    }
    this.filteredResourcesView.revalidateAndRepaint();
  }

  public void addGalleryDragComponents(List<ResourceNode> resourceNodes) {
    synchronized (this.getTreeLock()) {
      for (ResourceNode resourceNode : resourceNodes) {
        this.filteredResourcesView.addComponent(this.getGalleryDragComponent(resourceNode, TreeUtilities.getClassTreeState()));
      }
    }
    this.filteredResourcesView.revalidateAndRepaint();
  }

  public void setComponentsToGalleryDragComponents(String filter, List<ResourceNode> resourceNodes) {
    synchronized (this.getTreeLock()) {
      this.filteredResourcesView.removeAllComponents();
      if (filter.length() > 0) {
        if (resourceNodes.size() > 0) {
          for (ResourceNode resourceNode : resourceNodes) {
            this.filteredResourcesView.addComponent(this.getGalleryDragComponent(resourceNode, TreeUtilities.getClassTreeState()));
          }
        } else {
          this.filteredResourcesView.addComponent(this.noMatchesLabel);
        }
      } else {
        this.filteredResourcesView.addComponent(this.noEntryLabel);
      }
    }
    this.filteredResourcesView.revalidateAndRepaint();
  }
}
