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
package org.alice.stageide.sceneeditor.side.views;

import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.croquet.components.InstanceFactoryPopupButton;
import org.alice.ide.preferences.IsToolBarShowing;
import org.alice.interact.handle.HandleStyle;
import org.alice.stageide.oneshot.DynamicOneShotMenuModel;
import org.alice.stageide.sceneeditor.side.SideComposite;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ToolPaletteCoreComposite;
import org.lgna.croquet.views.*;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * this is the side panel of the scene editor view
 * @author Dennis Cosgrove
 */
public class SideView extends BorderPanel {
  private static Border createSeparatorBorder(int top, int bottom) {
    return BorderFactory.createMatteBorder(top, 0, bottom, 0, UIManager.getColor("Separator.foreground"));
  }

  public SideView(SideComposite composite) {
    super(composite);
    if (!IsToolBarShowing.getValue()) {
      ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
      FlowPanel undoRedoPanel = new FlowPanel(FlowPanel.Alignment.CENTER, projectDocumentFrame.getUndoOperation().createButton(), projectDocumentFrame.getRedoOperation().createButton());

      undoRedoPanel.setBorder(createSeparatorBorder(0, 1));
      this.addPageStartComponent(undoRedoPanel);
    }

    MigPanel migPanel = new MigPanel(null, "fill, insets 0, aligny top", "", "");
    migPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

    ItemSelectablePanel<HandleStyle> radioButtons = new DefaultRadioButtons<>(composite.getHandleStyleState(), false) {
      @Override
      protected BooleanStateButton<?> createButtonForItemSelectedState(HandleStyle item, BooleanState itemSelectedState) {
        ToggleButton button = itemSelectedState.createToggleButton();
        button.setVerticalTextPosition(VerticalTextPosition.BOTTOM);
        button.setHorizontalTextPosition(HorizontalTextPosition.CENTER);
        return button;
      }
    };
    migPanel.addComponent(new LineAxisPanel(composite.getHandleStyleState().getSidekickLabel().createLabel(1.2f), radioButtons), "wrap");

    ToolPaletteView toolPaletteView = composite.getSnapDetailsToolPaletteCoreComposite().getOuterComposite().getView();
    ToolPaletteTitle title = toolPaletteView.getTitle();
    title.setSeparatorShowing(true);

    migPanel.addComponent(new FlowPanel(composite.getIsSnapEnabledState().createCheckBox(), title), "wrap, gapleft 4");
    migPanel.addComponent(toolPaletteView, "wrap");

    migPanel.addComponent(new InstanceFactoryPopupButton(IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState()), "wrap");

    //todo
    //migPanel.addComponent( composite.getAreJointsShowingState().createCheckBox(), "wrap" );

    migPanel.addComponent(DynamicOneShotMenuModel.getInstance().getPopupPrepModel().createPopupButton(), "wrap");

    ToolPaletteCoreComposite<?>[] toolPaletteCoreComposites = {composite.getObjectPropertiesTab(), composite.getObjectMarkersTab(), composite.getCameraMarkersTab()};

    for (ToolPaletteCoreComposite<?> toolPaletteCoreComposite : toolPaletteCoreComposites) {
      ToolPaletteTitle toolPaletteTitle = toolPaletteCoreComposite.getOuterComposite().getView().getTitle();
      toolPaletteTitle.scaleFont(1.4f);
      toolPaletteTitle.changeFont(TextWeight.BOLD);
      toolPaletteTitle.setSeparatorShowing(true);
      toolPaletteTitle.setBackgroundColor(UIManager.getColor("Alice.background"));
      migPanel.addComponent(toolPaletteCoreComposite.getOuterComposite().getView(), "wrap, growx");
    }
    migPanel.addComponent(new Label(), "wrap, grow, push");

    ScrollPane scrollPane = new ScrollPane(migPanel);
    this.addCenterComponent(scrollPane);

    this.setMaximumPreferredWidth(400);
  }
}
