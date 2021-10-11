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
package org.alice.ide.members.components;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.map.MapToMap;
import org.alice.ide.IDE;
import org.alice.ide.ast.delete.DeleteStatementOperation;
import org.alice.ide.ast.draganddrop.statement.StatementDragModel;
import org.alice.ide.common.TypeComponent;
import org.alice.ide.croquet.components.InstanceFactoryPopupButton;
import org.alice.ide.members.MembersComposite;
import org.alice.ide.recyclebin.RecycleBin;
import org.alice.ide.recyclebin.icons.ClosedTrashCanSymbolicStyleIcon;
import org.alice.ide.recyclebin.icons.OpenTrashCanSymbolicStyleIcon;
import org.lgna.croquet.AbstractDropReceptor;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.FolderTabbedPane;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Statement;

import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public class MembersView extends BorderPanel {
  private static MapToMap<Class<?>, AbstractType<?, ?, ?>, TypeComponent> mapToMap = MapToMap.newInstance();
  public static final byte PROTOTYPE = 0;

  public static TypeComponent getComponentFor(Class<?> cls, AbstractType<?, ?, ?> type) {
    return mapToMap.getInitializingIfAbsent(cls, type, new MapToMap.Initializer<Class<?>, AbstractType<?, ?, ?>, TypeComponent>() {
      @Override
      public TypeComponent initialize(Class<?> cls, AbstractType<?, ?, ?> type) {
        return TypeComponent.createInstance(type);
      }
    });
  }

  private static final int SIZE = 32;

  private static enum DragReceptorState {
    IDLE(null, null, null, 0), STARTED(Color.YELLOW, new Color(191, 191, 191, 0), null, SIZE), ENTERED(Color.YELLOW, new Color(127, 127, 127, 191), new ClosedTrashCanSymbolicStyleIcon(128, 128, Color.LIGHT_GRAY), SIZE), ENTERED_FAR_ENOUGH(new Color(0xCCFF99), new Color(127, 127, 127, 191), new OpenTrashCanSymbolicStyleIcon(128, 128, Color.WHITE), SIZE);
    private final Color colorA;
    private final Color colorB;
    private final Icon icon;
    private final int gradientAmount;

    private DragReceptorState(Color colorA, Color colorB, Icon icon, int gradientAmount) {
      this.colorA = colorA;
      this.colorB = colorB;
      this.icon = icon;
      this.gradientAmount = gradientAmount;
    }

    public Color getColorA() {
      return this.colorA;
    }

    public Color getColorB() {
      return this.colorB;
    }

    public int getGradientAmount() {
      return this.gradientAmount;
    }

    public Icon getIcon() {
      return this.icon;
    }
  }

  private class RecycleBinDropReceptor extends AbstractDropReceptor {
    private DragReceptorState dragReceptorState = DragReceptorState.IDLE;

    @Override
    public boolean isPotentiallyAcceptingOf(DragModel dragModel) {
      return dragModel instanceof StatementDragModel;
    }

    private void setDragReceptorState(DragReceptorState dragReceptorState) {
      this.dragReceptorState = dragReceptorState;
      MembersView.this.repaint();
    }

    @Override
    public void dragStarted(DragStep step) {
      this.setDragReceptorState(DragReceptorState.STARTED);
    }

    @Override
    public void dragEntered(DragStep step) {
      this.setDragReceptorState(DragReceptorState.ENTERED);
    }

    @Override
    public DropSite dragUpdated(DragStep step) {
      MouseEvent e = step.getLatestMouseEvent();
      Point p = ComponentUtilities.convertPoint(e.getComponent(), e.getPoint(), MembersView.this.getAwtComponent());

      final int FAR_ENOUGH = 64;
      if (p.x < (MembersView.this.getWidth() - FAR_ENOUGH)) {
        this.setDragReceptorState(DragReceptorState.ENTERED_FAR_ENOUGH);
        return MembersView.this.dropSite;
      } else {
        this.setDragReceptorState(DragReceptorState.ENTERED);
        return null;
      }
    }

    @Override
    protected Triggerable dragDroppedPostRejectorCheck(DragStep step) {
      DropSite dropSite = step.getCurrentPotentialDropSite();
      if (dropSite != null) {
        DragModel dragModel = step.getModel();
        if (dragModel instanceof StatementDragModel) {
          StatementDragModel statementDragModel = (StatementDragModel) dragModel;
          Statement statement = statementDragModel.getStatement();
          return new DeleteStatementOperation(statement);
        } else {
          return null;
        }
      } else {
        return null;
      }
    }

    @Override
    public void dragExited(DragStep step, boolean isDropRecipient) {
      //      step.getDragSource().showDragProxy();
      this.setDragReceptorState(DragReceptorState.STARTED);
    }

    @Override
    public void dragStopped(DragStep step) {
      this.setDragReceptorState(DragReceptorState.IDLE);
    }

    @Override
    public TrackableShape getTrackableShape(DropSite potentialDropSite) {
      return MembersView.this;
    }

    @Override
    public SwingComponentView<?> getViewController() {
      return MembersView.this;
    }

  }

  //todo
  private static class RecycleBinDropSite implements DropSite {
    public RecycleBinDropSite() {
    }

    public RecycleBinDropSite(BinaryDecoder binaryDecoder) {
      //todo
    }

    @Override
    public void encode(BinaryEncoder binaryEncoder) {
      //todo
    }

    @Override
    public int hashCode() {
      return 0;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof RecycleBinDropSite;
    }

    @Override
    public DropReceptor getOwningDropReceptor() {
      return RecycleBin.SINGLETON.getDropReceptor();
    }
  }

  private final RecycleBinDropSite dropSite = new RecycleBinDropSite();

  private final RecycleBinDropReceptor recycleBinDropReceptor = new RecycleBinDropReceptor();

  public MembersView(MembersComposite composite) {
    super(composite);
    InstanceFactoryPopupButton instanceFactoryPopupButton = new InstanceFactoryPopupButton(IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState());
    //    org.lgna.croquet.components.LineAxisPanel instancePanel = new org.lgna.croquet.components.LineAxisPanel();
    //    instancePanel.addComponent( new org.alice.ide.croquet.components.InstanceFactoryPopupButton( org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance() ) );
    //    instancePanel.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
    //    instancePanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 0, 4 ) );
    //
    //    this.addPageStartComponent( instancePanel );
    this.setBackgroundColor(FolderTabbedPane.DEFAULT_BACKGROUND_COLOR);
    this.addPageStartComponent(instanceFactoryPopupButton);
    this.addCenterComponent(composite.getTabState().createFolderTabbedPane());
  }

  @Override
  protected JPanel createJPanel() {
    JPanel rv = new DefaultJPanel() {
      @Override
      public void paint(Graphics g) {
        super.paint(g);
        if (recycleBinDropReceptor.dragReceptorState == DragReceptorState.IDLE) {
          //pass
        } else {
          Graphics2D g2 = (Graphics2D) g;
          int width = this.getWidth();
          int height = this.getHeight();
          Color colorA = recycleBinDropReceptor.dragReceptorState.getColorA();
          Color colorB = recycleBinDropReceptor.dragReceptorState.getColorB();
          int gradientAmount = recycleBinDropReceptor.dragReceptorState.getGradientAmount();
          Paint paint = new GradientPaint(width, 0, colorA, width - gradientAmount, gradientAmount, colorB);
          g2.setPaint(paint);
          g2.fillRect(0, 0, width, height);
          Icon icon = recycleBinDropReceptor.dragReceptorState.getIcon();
          if (icon != null) {
            g2.setPaint(new Color(221, 221, 221));
            int x = (width - icon.getIconWidth()) / 2;
            int y = (height - icon.getIconHeight()) / 2;
            y = Math.min(y, 100);
            icon.paintIcon(this, g2, x, y);
          }
        }
      }
    };
    return rv;
  }

  public RecycleBinDropReceptor getRecycleBinDropReceptor() {
    return this.recycleBinDropReceptor;
  }
}
