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

package org.alice.stageide.sceneeditor.views;

import edu.cmu.cs.dennisc.java.awt.event.AltTriggerMouseAdapter;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.property.event.ListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter;
import org.alice.ide.ApiConfigurationManager;
import org.alice.ide.IDE;
import org.alice.ide.croquet.models.ast.CenterCameraOnOperation;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessFactory;
import org.alice.ide.instancefactory.ThisInstanceFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryState;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.alice.stageide.StageIDE;
import org.alice.stageide.oneshot.DynamicOneShotMenuModel;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.triggers.MouseEventTrigger;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.PanelViewController;
import org.lgna.croquet.views.PopupButton;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;
import org.lgna.story.SGround;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SizeRequirements;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactorySelectionPanel extends PanelViewController<InstanceFactoryState> {
  private static final class InstanceFactoryLayout implements LayoutManager2 {
    private static final int INDENT = 16;

    @Override
    public void addLayoutComponent(String name, Component comp) {
      this.invalidateLayout(comp.getParent());
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
      this.invalidateLayout(comp.getParent());
    }

    @Override
    public void removeLayoutComponent(Component comp) {
      this.invalidateLayout(comp.getParent());
    }

    @Override
    public void invalidateLayout(Container target) {
      synchronized (this) {
        this.xChildren = null;
        this.yChildren = null;
        this.xTotal = null;
        this.yTotal = null;
      }
    }

    private void ensureSizeRequirementsUpToDate(Container target) {
      synchronized (this) {
        if ((xChildren == null) || (yChildren == null)) {
          int nChildren = target.getComponentCount();
          xChildren = new SizeRequirements[nChildren];
          yChildren = new SizeRequirements[nChildren];
          for (int i = 0; i < nChildren; i++) {
            Component c = target.getComponent(i);
            if (c.isVisible()) {
              Dimension min = c.getMinimumSize();
              Dimension typ = c.getPreferredSize();
              Dimension max = c.getMaximumSize();
              xChildren[i] = new SizeRequirements(min.width, typ.width, max.width, c.getAlignmentX());
              yChildren[i] = new SizeRequirements(min.height, typ.height, max.height, c.getAlignmentY());
            } else {
              xChildren[i] = new SizeRequirements(0, 0, 0, c.getAlignmentX());
              yChildren[i] = new SizeRequirements(0, 0, 0, c.getAlignmentY());
            }
          }
          xTotal = SizeRequirements.getAlignedSizeRequirements(xChildren);
          yTotal = SizeRequirements.getTiledSizeRequirements(yChildren);
        }
      }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
      this.ensureSizeRequirementsUpToDate(parent);
      Insets insets = parent.getInsets();

      return new Dimension(this.xTotal.minimum + insets.left + insets.right + INDENT + xChildren[xChildren.length - 1].minimum, this.yTotal.minimum + insets.top + insets.bottom);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
      this.ensureSizeRequirementsUpToDate(parent);
      Insets insets = parent.getInsets();
      return new Dimension(this.xTotal.preferred + insets.left + insets.right + INDENT + xChildren[xChildren.length - 1].preferred, this.yTotal.preferred + insets.top + insets.bottom);
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
      this.ensureSizeRequirementsUpToDate(parent);
      Insets insets = parent.getInsets();
      return new Dimension(this.xTotal.maximum + insets.left + insets.right + INDENT + xChildren[xChildren.length - 1].maximum, this.yTotal.maximum + insets.top + insets.bottom);
    }

    @Override
    public void layoutContainer(Container parent) {
      int nChildren = parent.getComponentCount();
      int[] xOffsets = new int[nChildren];
      int[] xSpans = new int[nChildren];
      int[] yOffsets = new int[nChildren];
      int[] ySpans = new int[nChildren];

      Dimension size = parent.getSize();
      Insets insets = parent.getInsets();

      Dimension availableSpace = new Dimension(size);
      availableSpace.width -= insets.left + insets.right;
      availableSpace.height -= insets.top + insets.bottom;

      synchronized (this) {
        this.ensureSizeRequirementsUpToDate(parent);
        SizeRequirements.calculateAlignedPositions(availableSpace.width, xTotal, xChildren, xOffsets, xSpans, true);
        SizeRequirements.calculateTiledPositions(availableSpace.height, yTotal, yChildren, yOffsets, ySpans);
      }

      for (int i = 0; i < nChildren; i++) {
        Component c = parent.getComponent(i);
        int x = insets.left + xOffsets[i];
        int y = insets.top + yOffsets[i];
        if (i > 0) {
          x += INDENT;
        }
        c.setBounds(x, y, xSpans[i], ySpans[i]);
      }

      Rectangle boundsI = new Rectangle();
      int indexOfFirstComponentThatFails = -1;
      int indexOfSelectedComponent = -1;
      for (int i = 0; i < (nChildren - 1); i++) {
        Component c = parent.getComponent(i);
        if (indexOfFirstComponentThatFails == -1) {
          c.getBounds(boundsI);
          if ((boundsI.y + boundsI.height) >= (size.height - insets.bottom)) {
            indexOfFirstComponentThatFails = i;
          }
        }
        if (c instanceof AbstractButton) {
          AbstractButton button = (AbstractButton) c;
          if (button.isSelected()) {
            indexOfSelectedComponent = i;
          }
        }
      }

      Component lastComponent = parent.getComponent(nChildren - 1);
      if (indexOfFirstComponentThatFails != -1) {
        if (indexOfFirstComponentThatFails > 0) {
          for (int i = indexOfFirstComponentThatFails - 1; i < (nChildren - 1); i++) {
            if (i != indexOfSelectedComponent) {
              parent.getComponent(i).setSize(0, 0);
            }
          }
          int i = indexOfFirstComponentThatFails - 1;
          Point p = parent.getComponent(i).getLocation();
          if (indexOfSelectedComponent >= i) {
            Component c = parent.getComponent(indexOfSelectedComponent);
            c.setLocation(p);
            p.x += c.getWidth();
          }
          lastComponent.setLocation(p);
        }
      } else {
        lastComponent.setSize(0, 0);
      }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
      this.ensureSizeRequirementsUpToDate(target);
      return this.xTotal.alignment;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
      this.ensureSizeRequirementsUpToDate(target);
      return this.yTotal.alignment;
    }

    private SizeRequirements[] xChildren;
    private SizeRequirements[] yChildren;
    private SizeRequirements xTotal;
    private SizeRequirements yTotal;
  }

  private static final class InternalButton extends SwingComponentView<AbstractButton> {
    private final InstanceFactory instanceFactory;
    private final Action action = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().setValueTransactionlessly(InternalButton.this.instanceFactory);
      }

    };

    private final AltTriggerMouseAdapter altTriggerMouseAdapter = new AltTriggerMouseAdapter() {
      @Override
      protected void altTriggered(MouseEvent e) {
        IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().setValueTransactionlessly(InternalButton.this.instanceFactory);
        InternalButton.this.handleAltTriggered(e);
      }
    };

    public InternalButton(InstanceFactory instanceFactory) {
      this.instanceFactory = instanceFactory;
    }

    @Override
    protected AbstractButton createAwtComponent() {
      JRadioButton rv = new JRadioButton(this.action) {
        @Override
        protected void paintComponent(Graphics g) {
          //note: do not invoke super
          //super.paintComponent( g );
        }

        @Override
        protected void paintChildren(Graphics g) {
          //todo: better indication of selection/rollover
          Graphics2D g2 = (Graphics2D) g;

          Composite prevComposite = g2.getComposite();

          Composite nextComposite;
          float alpha;
          if (model.isSelected()) {
            if (model.isRollover()) {
              alpha = 1.0f;
            } else {
              alpha = 0.9f;
            }
          } else {
            if (model.isRollover()) {
              alpha = 0.5f;
            } else {
              alpha = 0.25f;
            }
          }
          nextComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

          g2.setComposite(nextComposite);
          try {
            super.paintChildren(g);
          } finally {
            g2.setComposite(prevComposite);
          }
        }
      };
      rv.setOpaque(false);
      rv.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() > 1) {
            if (!StorytellingSceneEditor.getInstance().isStartingCameraView()) {
              UserField field = StorytellingSceneEditor.getInstance().getSelectedField();
              if (!field.getValueType().isAssignableTo(SGround.class) && field != StorytellingSceneEditor.getInstance().getActiveSceneField()) {
                CenterCameraOnOperation.getInstance(field).fire(MouseEventTrigger.createUserActivity(e));
              }
            }
          }
        }
      });

      rv.setLayout(new BoxLayout(rv, BoxLayout.LINE_AXIS));
      Expression expression = this.instanceFactory.createTransientExpression();
      rv.add(PreviewAstI18nFactory.getInstance().createExpressionPane(expression).getAwtComponent());
      return rv;
    }

    protected void handleAltTriggered(MouseEvent e) {
      DynamicOneShotMenuModel.getInstance().getPopupPrepModel().fire(MouseEventTrigger.createUserActivity(e));
    }

    @Override
    protected void handleDisplayable() {
      super.handleDisplayable();
      this.addMouseListener(this.altTriggerMouseAdapter);
    }

    @Override
    protected void handleUndisplayable() {
      this.removeMouseListener(this.altTriggerMouseAdapter);
      super.handleUndisplayable();
    }
  }

  private static final class InternalPanel extends Panel {
    public InternalPanel() {
      this.setBackgroundColor(null);
      this.dropDown = IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().getCascadeRoot().getPopupPrepModel().createPopupButton();
    }

    private InternalButton getButtonFor(InstanceFactory instanceFactory) {
      InternalButton rv = map.get(instanceFactory);
      if (rv == null) {
        rv = new InternalButton(instanceFactory);
        map.put(instanceFactory, rv);
      }
      return rv;
    }

    @Override
    protected LayoutManager createLayoutManager(JPanel jPanel) {
      return new InstanceFactoryLayout();
    }

    @Override
    protected void internalRefresh() {
      super.internalRefresh();
      this.removeAllComponents();
      List<InternalButton> buttons = Lists.newLinkedList();
      buttons.add(getButtonFor(ThisInstanceFactory.getInstance()));
      StageIDE ide = StageIDE.getActiveInstance();
      ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
      NamedUserType sceneType = ide.getSceneType();
      if (sceneType != null) {
        for (UserField field : sceneType.fields) {
          if (field.managementLevel.getValue() == ManagementLevel.MANAGED) {
            if (apiConfigurationManager.isInstanceFactoryDesiredForType(field.getValueType())) {
              buttons.add(getButtonFor(ThisFieldAccessFactory.getInstance(field)));
            }
          }
        }
      }
      for (InternalButton button : buttons) {
        this.internalAddComponent(button);
        this.buttonGroup.add(button.getAwtComponent());
      }

      this.internalAddComponent(this.dropDown);
      this.setSelected(IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().getValue());
    }

    private void setSelected(InstanceFactory instanceFactory) {
      InternalButton button;
      if (instanceFactory != null) {
        button = this.map.get(instanceFactory);
      } else {
        button = null;
      }
      if (button != null) {
        this.buttonGroup.setSelected(button.getAwtComponent().getModel(), true);
      } else {
        ButtonModel buttonModel = this.buttonGroup.getSelection();
        if (buttonModel != null) {
          this.buttonGroup.setSelected(buttonModel, false);
        }
      }
    }

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<InstanceFactory, InternalButton> map = Maps.newHashMap();
    private final PopupButton dropDown;
  }

  public InstanceFactorySelectionPanel() {
    super(IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState(), new InternalPanel());
    this.setBackgroundColor(null);
    this.getAwtComponent().setOpaque(false);
  }

  public UserType<?> getType() {
    return this.type;
  }

  public void setType(UserType<?> type) {
    if (this.type != type) {
      if (this.type != null) {
        this.type.fields.removeListPropertyListener(this.fieldsListener);
      }
      this.type = type;
      if (this.type != null) {
        this.type.fields.addListPropertyListener(this.fieldsListener);
      }
      this.getInternalPanel().refreshLater();
    }
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    this.getModel().addNewSchoolValueListener(this.instanceFactoryListener);
  }

  @Override
  protected void handleUndisplayable() {
    this.getModel().removeNewSchoolValueListener(this.instanceFactoryListener);
    super.handleUndisplayable();
  }

  private final ValueListener<InstanceFactory> instanceFactoryListener =
      e -> InstanceFactorySelectionPanel.this.getInternalPanel().refreshLater();

  private UserType<?> type;
  private final ListPropertyListener<UserField> fieldsListener = new SimplifiedListPropertyAdapter<>() {
    @Override
    protected void changed(ListPropertyEvent<UserField> e) {
      InstanceFactorySelectionPanel.this.getInternalPanel().refreshLater();
    }
  };
}
