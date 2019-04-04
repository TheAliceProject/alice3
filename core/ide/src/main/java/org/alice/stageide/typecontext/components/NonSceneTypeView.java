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

package org.alice.stageide.typecontext.components;

import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import edu.cmu.cs.dennisc.javax.swing.icons.AlphaIcon;
import org.alice.ide.IDE;
import org.alice.ide.Theme;
import org.alice.ide.common.TypeIcon;
import org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState;
import org.alice.stageide.StageIDE;
import org.alice.stageide.icons.IconFactoryManager;
import org.alice.stageide.icons.SceneIconFactory;
import org.alice.stageide.run.RunComposite;
import org.alice.stageide.typecontext.NonSceneTypeComposite;
import org.lgna.croquet.Operation;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.CornerSpringPanel;
import org.lgna.croquet.views.HorizontalAlignment;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.project.ast.NamedUserType;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.HierarchyEvent;

class SelectedTypeView extends BorderPanel {
  private final Label typeLabel = new Label();
  private final Label snapshotLabel = new Label();
  private final ValueListener<NamedUserType> typeListener = new ValueListener<NamedUserType>() {
    @Override
    public void valueChanged(ValueEvent<NamedUserType> e) {
      SelectedTypeView.this.handleTypeStateChanged(e.getNextValue());
    }
  };

  public SelectedTypeView() {
    //this.typeLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
    this.snapshotLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
    Label label = new Label(ResourceBundleUtilities.getStringForKey("selectedType", getClass()));
    this.addPageStartComponent(new LineAxisPanel(label, this.typeLabel));
    this.addCenterComponent(this.snapshotLabel);
  }

  private void handleTypeStateChanged(NamedUserType nextValue) {
    this.typeLabel.setIcon(TypeIcon.getInstance(nextValue));
    String snapshotText = null;
    Icon snapshotIcon = null;
    if (nextValue != null) {
      IconFactory iconFactory = IconFactoryManager.getIconFactoryForType(nextValue);
      if (iconFactory != null) {
        snapshotIcon = iconFactory.getIcon(iconFactory.getDefaultSize(Theme.DEFAULT_LARGE_ICON_SIZE));
      }
    }
    this.snapshotLabel.setText(snapshotText);
    this.snapshotLabel.setIcon(snapshotIcon);
    this.revalidateAndRepaint();
  }

  @Override
  protected void handleAddedTo(AwtComponentView<?> parent) {
    super.handleAddedTo(parent);
    IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().addAndInvokeValueListener(this.typeListener);
  }

  @Override
  protected void handleRemovedFrom(AwtComponentView<?> parent) {
    IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().removeValueListener(this.typeListener);
    super.handleRemovedFrom(parent);
  }
}

class ReturnToSceneTypeButton extends Button {
  private static Icon BACK_ICON = IconUtilities.createImageIcon(NonSceneTypeView.class.getResource("images/24/back.png"));
  private final Label thumbnailLabel = new Label(new AlphaIcon(SceneIconFactory.getInstance().getIcon(Theme.DEFAULT_SMALL_ICON_SIZE), 0.5f));
  private final Label typeIconLabel = new Label();

  public ReturnToSceneTypeButton(Operation operation) {
    super(operation);
    JButton jButton = this.getAwtComponent();
    jButton.setLayout(new BoxLayout(jButton, BoxLayout.LINE_AXIS));
    this.internalAddComponent(new Label(BACK_ICON));
    String backToText = ResourceBundleUtilities.getStringForKey("backTo", getClass());
    this.internalAddComponent(new Label(backToText));
    this.internalAddComponent(BoxUtilities.createHorizontalSliver(8));
    this.internalAddComponent(this.thumbnailLabel);
    this.internalAddComponent(this.typeIconLabel);
  }

  @Override
  protected void handleHierarchyChanged(HierarchyEvent e) {
    super.handleHierarchyChanged(e);
    SceneIconFactory.getInstance().markAllIconsDirty();

    //todo:
    NamedUserType sceneType = StageIDE.getActiveInstance().getSceneType();
    this.typeIconLabel.setIcon(TypeIcon.getInstance(sceneType));
    this.revalidateAndRepaint();
  }
}

/**
 * @author Dennis Cosgrove
 */
public class NonSceneTypeView extends CornerSpringPanel {
  private final ReturnToSceneTypeButton returnToSceneTypeButton;

  public NonSceneTypeView(NonSceneTypeComposite composite) {
    super(composite);
    this.setNorthWestComponent(new SelectedTypeView());
    this.setNorthEastComponent(RunComposite.getInstance().getLaunchOperation().createButton());

    Operation returnToSceneTypeOperation = composite.getSelectSceneTypeOperation();
    returnToSceneTypeOperation.initializeIfNecessary();
    returnToSceneTypeOperation.setName("");
    this.returnToSceneTypeButton = new ReturnToSceneTypeButton(returnToSceneTypeOperation);
    this.setSouthWestComponent(IsEmphasizingClassesState.getInstance().getValue() ? returnToSceneTypeButton : null);
  }

  @Override
  protected JPanel createJPanel() {
    return new DefaultJPanel() {
      @Override
      public Dimension getPreferredSize() {
        return new Dimension(320, 240);
      }
    };
  }
}
