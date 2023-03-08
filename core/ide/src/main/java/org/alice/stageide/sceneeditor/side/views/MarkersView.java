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

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import net.miginfocom.swing.MigLayout;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.declarationseditor.type.FieldMenuModel;
import org.alice.stageide.sceneeditor.side.MarkersToolPalette;
import org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.RefreshableDataSingleSelectListState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BooleanStateButton;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.FlowPanel;
import org.lgna.croquet.views.HorizontalAlignment;
import org.lgna.croquet.views.ItemSelectablePanel;
import org.lgna.croquet.views.PopupButton;
import org.lgna.project.ast.UserField;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class MarkersView extends BorderPanel {

  public static final Color BACKGROUND_COLOR = new Color(173, 167, 208);
  public static final Color SELECTED_COLOR = ColorUtilities.scaleHSB(Color.YELLOW, 1.0, 0.3, 1.0);
  public static final Color UNSELECTED_COLOR = ColorUtilities.scaleHSB(BACKGROUND_COLOR, 1.0, 0.9, 0.8);

  private static class MarkerView extends BooleanStateButton<AbstractButton> {
    public MarkerView(BooleanState model) {
      super(model);
    }

    @Override
    protected AbstractButton createAwtComponent() {
      JToggleButton rv = new JToggleButton() {
        @Override
        public Color getBackground() {
          if (this.isSelected()) {
            return SELECTED_COLOR;
          } else {
            return UNSELECTED_COLOR;
          }
        }
      };

      //rv.setLayout( new java.awt.BorderLayout() );
      //rv.add( new javax.swing.JLabel( "hello" ), java.awt.BorderLayout.LINE_END );
      return rv;
    }
  }

  private class MarkerListView extends ItemSelectablePanel<UserField> {
    private class MarkerPopupButton extends PopupButton {
      private final UserField field;

      public MarkerPopupButton(UserField field) {
        super(FieldMenuModel.getInstance(field).getPopupPrepModel());
        this.field = field;
      }

      private boolean isFieldSelected() {
        return MarkerListView.this.getModel().getValue() == field;
      }

      @Override
      protected AbstractButton createSwingButton() {
        return new JPopupButton() {
          @Override
          public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            boolean isAlphaDesired = isFieldSelected();
            Composite prevComposite = g2.getComposite();
            if (isAlphaDesired) {
              //pass
            } else {
              g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            }
            try {
              super.paint(g);
            } finally {
              if (isAlphaDesired) {
                //pass
              } else {
                g2.setComposite(prevComposite);
              }
            }

          }

          //          @Override
          //          public boolean contains( int x, int y ) {
          //            if( isFieldSelected() ) {
          //              return super.contains( x, y );
          //            } else {
          //              return false;
          //            }
          //          }
        };
      }
    }

    private final Map<UserField, MarkerPopupButton> mapFieldToPopupButton = Maps.newHashMap();

    private final ValueListener<UserField> selectionListener = new ValueListener<UserField>() {
      @Override
      public void valueChanged(ValueEvent<UserField> e) {
        MarkersView.this.repaint();
      }
    };

    public MarkerListView(RefreshableDataSingleSelectListState<UserField> model) {
      super(model);
    }

    @Override
    protected void handleAddedTo(AwtComponentView<?> parent) {
      super.handleAddedTo(parent);
      this.getModel().addNewSchoolValueListener(this.selectionListener);
    }

    @Override
    protected void handleRemovedFrom(AwtComponentView<?> parent) {
      this.getModel().removeNewSchoolValueListener(this.selectionListener);
      super.handleRemovedFrom(parent);
    }

    @Override
    protected LayoutManager createLayoutManager(JPanel jPanel) {
      return new MigLayout();
    }

    @Override
    protected void removeAllDetails() {
      this.internalRemoveAllComponents();
    }

    @Override
    protected void addPrologue(int count) {
    }

    @Override
    protected void addItem(UserField item, BooleanStateButton<?> button) {
      this.internalAddComponent(this.mapFieldToPopupButton.get(item));
      this.internalAddComponent(button, "wrap, grow");
    }

    @Override
    protected void addEpilogue() {
    }

    @Override
    protected BooleanStateButton<?> createButtonForItemSelectedState(final UserField item, final BooleanState itemSelectedState) {
      itemSelectedState.setIconForBothTrueAndFalse(MarkerUtilities.getIconForMarkerField(item));

      item.name.addPropertyListener(e -> itemSelectedState.setTextForBothTrueAndFalse(item.name.getValue()));

      MarkerView rv = new MarkerView(itemSelectedState);
      rv.setHorizontalAlignment(HorizontalAlignment.LEADING);
      this.mapFieldToPopupButton.put(item, new MarkerPopupButton(item));
      return rv;
    }
  }

  private final MouseListener mouseListener = new MouseListener() {
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
      unselectMarker();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
  };

  public MarkersView(MarkersToolPalette composite) {
    super(composite);
    this.addPageStartComponent(new FlowPanel(FlowPanel.Alignment.LEADING, composite.getMoveToMarkerOperation().createButton(), composite.getMoveMarkerToOperation().createButton()));
    this.addCenterComponent(new MarkerListView(composite.getMarkerListState()));
    this.addPageEndComponent(new FlowPanel(FlowPanel.Alignment.LEADING, composite.getAddOperation().createButton()));
    this.setBackgroundColor(ThemeUtilities.getActiveTheme().getPrimaryBackgroundColor());
  }

  private void unselectMarker() {
    Logger.outln("unselectMarker");
    MarkersToolPalette composite = (MarkersToolPalette) getComposite();
    composite.getMarkerListState().clearSelection();
  }

  @Override
  protected void handleAddedTo(AwtComponentView<?> parent) {
    super.handleAddedTo(parent);
    this.addMouseListener(this.mouseListener);
  }

  @Override
  protected void handleRemovedFrom(AwtComponentView<?> parent) {
    this.removeMouseListener(this.mouseListener);
    super.handleRemovedFrom(parent);
  }
}
