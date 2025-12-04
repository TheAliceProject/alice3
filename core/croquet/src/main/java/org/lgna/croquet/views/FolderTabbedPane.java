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

package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.javax.swing.SpringUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JCloseButton;
import org.lgna.croquet.*;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.Trigger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.UUID;

/**
 * The folder tabbed pane ecosystem (there are so many classes in just this one file!) appears to be some slightly
 * modified version of the swing JTabbedPane.  It would be worth looking into if there is a simpler way to get whatever
 * non-standard behavior we need (or at least document in a comment WHY we needed to reinvent this)
 *
 * @author Dennis Cosgrove
 */
public class FolderTabbedPane<E extends TabComposite<?>> extends CardBasedTabbedPane<E> {
  private static final int TRAILING_TAB_PAD = 32;
  private static final int OUTLINE_THICKNESS = 1;

  private static class FolderTabTitleUI extends BasicToggleButtonUI {
    @Override
    public Dimension getPreferredSize(JComponent c) {
      javax.swing.AbstractButton button = (javax.swing.AbstractButton) c;
      Font font = button.getFont();
      FontMetrics fm = button.getFontMetrics(font);
      String text = button.getText();
      Icon icon = button.getIcon();
      Dimension size;
      if (icon != null) {
        int verticalAlignment = button.getVerticalAlignment();
        int horizontalAlignment = button.getHorizontalAlignment();
        int verticalTextPosition = button.getVerticalTextPosition();
        int horizontalTextPosition = button.getHorizontalTextPosition();
        Rectangle viewR = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
        Rectangle iconR = new Rectangle();
        Rectangle textR = new Rectangle();
        int textIconGap = button.getIconTextGap();
        SwingUtilities.layoutCompoundLabel(c, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);

        size = iconR.union(textR).getSize();
      } else {
        size = fm.getStringBounds(text, button.getGraphics()).getBounds().getSize();
      }

      Insets insets = button.getInsets();
      size.width += insets.left + insets.right;
      size.height += insets.top + insets.bottom;

      if (button.getComponentCount() > 0) {
        for (Component component : button.getComponents()) {
          size.width += 4;
          size.width += component.getPreferredSize().width;
        }
      }

      return size;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
      javax.swing.AbstractButton button = (javax.swing.AbstractButton) c;
      Icon icon = button.getIcon();
      if (icon != null) {
        super.paint(g, c);
      } else {
        String text = button.getText();
        Insets insets = button.getInsets();
        int x = insets.left;
        if (!button.getComponentOrientation().isLeftToRight()) {
          for (Component component : button.getComponents()) {
            x += component.getPreferredSize().width;
            x += 4;
          }
        }
        Color outlineColor = ColorUtilities.scaleHSB(button.getBackground(), 1, 4.8, .74);
        g.setColor(button.isSelected() ? UIManager.getColor("TabbedPane.foreground") :  button.getModel().isRollover() ? UIManager.getColor("TabbedPane.disabledForeground") : outlineColor);
        g.drawString(text, x, button.getBaseline(c.getWidth(), c.getHeight()));
      }
    }
  }

  private static class JFolderTabTitle extends JToggleButton {
    private final ItemListener itemListener = e -> JFolderTabTitle.this.revalidate();

    public JFolderTabTitle() {
      this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 8));
      this.setAlignmentX(Component.LEFT_ALIGNMENT);
      this.setAlignmentY(Component.BOTTOM_ALIGNMENT);
      this.setHorizontalTextPosition(SwingConstants.LEADING);
      this.setHorizontalAlignment(SwingConstants.LEADING);
      this.setLayout(new SpringLayout());
    }

    @Override
    public boolean isOpaque() {
      return false;
    }

    @Override
    public void updateUI() {
      this.setUI(new FolderTabTitleUI());
    }

    @Override
    public void addNotify() {
      super.addNotify();
      this.getModel().addItemListener(this.itemListener);
    }

    @Override
    public void removeNotify() {
      this.getModel().removeItemListener(this.itemListener);
      super.removeNotify();
    }

    @Override
    public void repaint() {
      Container parent = this.getParent();
      if (parent != null) {
        parent.repaint(this.getX(), this.getY(), this.getWidth() + TRAILING_TAB_PAD, this.getHeight());
      } else {
        super.repaint();
      }
    }
  }

  private class FolderTabTitle extends BooleanStateButton<javax.swing.AbstractButton> {
    private final JButton closeButton;

    public FolderTabTitle(final E item, BooleanState booleanState) {
      super(booleanState);

      if (item.isPotentiallyCloseable()) {
        ActionListener closeButtonActionListener = e -> FolderTabbedPane.this.getModel().removeItemAndSelectAppropriateReplacement(item);
        this.closeButton = new JCloseButton();
        this.closeButton.addActionListener(closeButtonActionListener);
      } else {
        this.closeButton = null;
      }
    }

    @Override
    public <F extends TabComposite<?>> void updateFor(F item) {
      setCloseable(item.isCloseable());
    }

    public void setCloseable(boolean isCloseable) {
      if (this.closeButton != null) {
        if (isCloseable == (this.closeButton.getParent() == null)) {
          javax.swing.AbstractButton awtButton = this.getAwtComponent();
          if (isCloseable) {
            SpringUtilities.Horizontal hOrientation = this.getComponentOrientation().isLeftToRight() ?  SpringUtilities.Horizontal.EAST : SpringUtilities.Horizontal.WEST;
            SpringUtilities.add(awtButton, this.closeButton, hOrientation, -1, SpringUtilities.Vertical.NORTH, 5);
          } else {
            awtButton.remove(this.closeButton);
          }
          awtButton.revalidate();
          awtButton.repaint();
        }
      }
    }

    @Override
    protected javax.swing.AbstractButton createAwtComponent() {
      return new JFolderTabTitle();
    }
  }

  protected static class TitlesPanel extends LineAxisPanel {
    private static final int NORTH_AREA_PAD = 1;

    protected static class JTitlesPanel extends JPanel {
      @Override
      public Dimension getPreferredSize() {
        Dimension rv = super.getPreferredSize();
        rv.width += TRAILING_TAB_PAD;
        return rv;
      }

      private GeneralPath addToPath(GeneralPath rv, float x, float y, float width, float height) {
        float a = height * 0.25f;

        float xStart;
        float xEnd;
        float xA;
        float tabPad;
        if (this.getComponentOrientation().isLeftToRight()) {
          xStart = x;
          xEnd = (x + width) - 1;
          tabPad = TRAILING_TAB_PAD;
          xA = xStart + a;
        } else {
          xStart = (x + width) - 1;
          xEnd = x;
          tabPad = -TRAILING_TAB_PAD;
          xA = xStart - a;
        }

        float xCurve0 = xEnd - (tabPad / 2);
        float xCurve1 = xEnd + tabPad;
        float cx0 = xCurve0 + (tabPad * 0.75f);
        float cx1 = xCurve0;

        float y0 = y + NORTH_AREA_PAD;
        float y1 = y + height + 1; // + this.contentBorderInsets.top;
        float cy0 = y0;
        float cy1 = y1;

        float yA = y + a;

        rv.moveTo(xCurve1, y1);

        rv.lineTo(xCurve1, y1 - 1);
        rv.curveTo(cx1, cy1, cx0, cy0, xCurve0, y0);
        rv.lineTo(xA, y0);
        rv.quadTo(xStart, y0, xStart, yA);
        rv.lineTo(xStart, y1);

        return rv;
      }

      private void paintTab(Graphics2D g2, javax.swing.AbstractButton button) {
        Color prevColor = g2.getColor();
        Shape prevClip = g2.getClip();

        try {
          int x = button.getX();
          int y = button.getY();
          int width = button.getWidth();
          int height = button.getHeight();

          Color color = button.getBackground();
          Color outlineColor = ColorUtilities.scaleHSB(color, 1, 4.8, .74);

          if (button.isSelected()) {
            // draw one more pixel down to connect with the panel outline
            Rectangle bounds = prevClip.getBounds();
            bounds.height += 1;
            g2.setClip(bounds);
          } else {
            color = button.getModel().isRollover() ? color : ColorUtilities.scaleHSB(color, 1, .5, 1);
          }
          g2.setColor(color);

          GeneralPath path = addToPath(new GeneralPath(), x, y, width, height);

          // draw the background before the outline
          g2.fill(path);
          g2.setColor(outlineColor);
          g2.draw(path);
        } finally {
          g2.setColor(prevColor);
        }
      }

      @Override
      protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Object prevAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        javax.swing.AbstractButton selectedButton = null;
        Component[] components = this.getComponents();
        final int N = components.length;
        for (int i = 0; i < N; i++) {
          Component component = components[N - 1 - i];
          if (component instanceof javax.swing.AbstractButton button) {
            if (button.isSelected()) {
              selectedButton = button;
            } else {
              this.paintTab(g2, button);
            }
          }
        }
        // paint selected button last so that it shows up on top
        if (selectedButton != null) {
          this.paintTab(g2, selectedButton);
        }
        super.paintChildren(g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, prevAntialiasing == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : prevAntialiasing);
      }
    }

    @Override
    protected JPanel createJPanel() {
      return new JTitlesPanel();
    }
  }

  private final TitlesPanel titlesPanel = this.createTitlesPanel();
  private final ScrollPane titlesScrollPane = new ScrollPane(this.titlesPanel);
  private final BorderPanel innerHeaderPanel = new BorderPanel();
  private final BorderPanel outerHeaderPanel = new BorderPanel();

  protected TitlesPanel createTitlesPanel() {
    return new TitlesPanel();
  }

  //private java.util.Map<E, javax.swing.Action> mapItemToAction = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
  private Action getActionFor(E item) {
    Operation operation = this.getModel().getItemSelectionOperation(item);
    operation.initializeIfNecessary();
    return operation.getImp().getSwingModel().getAction();
  }

  //todo: PopupOperation
  private class PopupOperation extends ActionOperation {
    public PopupOperation() {
      super(Application.DOCUMENT_UI_GROUP, UUID.fromString("7923b4c8-6a9f-4c8b-99b5-909ae6c0889a"));
    }

    @Override
    protected void localize() {
      super.localize();
      this.setName(">>");
    }

    @Override
    protected void perform(UserActivity activity) {
      Trigger trigger = activity.getTrigger();
      JPopupMenu popupMenu = new JPopupMenu();
      ButtonGroup buttonGroup = new ButtonGroup();
      for (E item : FolderTabbedPane.this.getModel()) {
        if (item != null) {
          JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem(getActionFor(item));
          checkBox.setSelected(FolderTabbedPane.this.getModel().getValue() == item);
          popupMenu.add(checkBox);
          buttonGroup.add(checkBox);
        } else {
          popupMenu.addSeparator();
        }
      }
      ViewController<?, ?> viewController = trigger.getViewController();
      popupMenu.show(viewController.getAwtComponent(), 0, viewController.getHeight());
    }
  }

  private class PopupButton extends OperationButton<JButton, Operation> {
    public PopupButton(Operation operation) {
      super(operation);
    }

    @Override
    protected final JButton createAwtComponent() {
      JButton rv = new JButton() {
        @Override
        public String getText() {
          if (isTextClobbered()) {
            return getClobberText();
          } else {
            return super.getText();
          }
        }

        private boolean isNecessary() {
          // have we opened so many tabs that we need this button to show up for selecting tabs that are out of view?
          Container parent = this.getParent();
          if (parent != null) {
            int width = parent.getWidth();
            int preferredWidth = parent.getPreferredSize().width;
            return width < (preferredWidth - TRAILING_TAB_PAD);
          } else {
            return false;
          }
        }

        @Override
        public void paint(Graphics g) {
          if (isNecessary()) {
            super.paint(g);
          } else {
            g.setColor(FolderTabbedPane.this.getBackgroundColor());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
          }
        }

        @Override
        public boolean contains(int x, int y) {
          if (isNecessary()) {
            return super.contains(x, y);
          } else {
            return false;
          }
        }
      };
      rv.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
      return rv;
    }
  }

  private class ScrollListener implements MouseListener, MouseMotionListener {
    private Integer pressedLocationX;
    private Integer pressedViewPositionX;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
      this.pressedLocationX = e.getPoint().x;
      this.pressedViewPositionX = titlesScrollPane.getAwtComponent().getViewport().getViewPosition().x;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      this.pressedLocationX = null;
      this.pressedViewPositionX = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (this.pressedLocationX != null) {
        Dimension viewSize = titlesScrollPane.getAwtComponent().getViewport().getView().getSize();
        Rectangle viewportRect = titlesScrollPane.getAwtComponent().getViewport().getViewRect();

        int xDelta = this.pressedLocationX - e.getX();
        int value = this.pressedViewPositionX + xDelta;
        value = Math.max(value, 0);
        value = Math.min(value, viewSize.width - viewportRect.width);

        titlesScrollPane.getAwtComponent().getViewport().setViewPosition(new Point(value, 0));
      }
    }
  }

  public FolderTabbedPane(TabState<E, ?> model) {
    super(model);
    CardOwnerComposite cardOwner = this.getCardOwner();
    this.titlesScrollPane.setHorizontalScrollbarPolicy(ScrollPane.HorizontalScrollbarPolicy.NEVER);
    this.titlesScrollPane.setVerticalScrollbarPolicy(ScrollPane.VerticalScrollbarPolicy.NEVER);

    ScrollListener scrollListener = new ScrollListener();
    this.titlesScrollPane.addMouseListener(scrollListener);
    this.titlesScrollPane.addMouseMotionListener(scrollListener);

    this.titlesScrollPane.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
    cardOwner.getView().setBorder(new Border() {
      @Override
      public Insets getBorderInsets(Component c) {
        return new Insets(OUTLINE_THICKNESS, OUTLINE_THICKNESS, OUTLINE_THICKNESS, OUTLINE_THICKNESS);
      }

      @Override
      public boolean isBorderOpaque() {
        return true;
      }

      // this is for the border around the area under the tabs
      @Override
      public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (titlesPanel.getComponentCount() <= 0) {
          return;
        }
        for (AwtComponentView<?> component : titlesPanel.getComponents()) {
          if (component instanceof AbstractButton<?, ?> button && button.getAwtComponent().getModel().isSelected()) {
            Rectangle bounds = button.getBounds(AwtContainerView.lookup(c));
            Color background = button.getBackgroundColor();
            g.setColor(ColorUtilities.scaleHSB(background, 1, 4.8, .74));
            // top
            g.fillRect(x, y, width, OUTLINE_THICKNESS);
            // bottom
            g.fillRect(x, y + height - 1, width, OUTLINE_THICKNESS);
            // right
            g.fillRect(x + width - 1, y, OUTLINE_THICKNESS, height);
            // left
            g.fillRect(x, y, OUTLINE_THICKNESS, height);

            // re-draw over the top of the border where the tab will be so it looks seamless
            g.setColor(background);
            int x0 = c.getComponentOrientation().isLeftToRight() ? bounds.x : bounds.x - TRAILING_TAB_PAD;
            g.fillRect(x0, y, (bounds.width - 1) + TRAILING_TAB_PAD, OUTLINE_THICKNESS);
            break;
          }
        }
      }
    });
    PopupOperation popupOperation = new PopupOperation();
    this.setInnerHeaderTrailingComponent(new PopupButton(popupOperation));

  }

  public void setHeaderLeadingComponent(SwingComponentView<?> component) {
    if (component != null) {
      component.setAlignmentY(Component.BOTTOM_ALIGNMENT);
      this.innerHeaderPanel.addLineStartComponent(component);
    } else {
      AwtComponentView<?> prevComponent = this.innerHeaderPanel.getLineStartComponent();
      if (prevComponent != null) {
        this.innerHeaderPanel.removeComponent(prevComponent);
      }
    }
    this.innerHeaderPanel.revalidateAndRepaint();
  }

  private void setInnerHeaderTrailingComponent(SwingComponentView<?> component) {
    if (component != null) {
      if (!component.isOpaque()) {
        component.setBackgroundColor(this.getBackgroundColor());
      }
      component.setAlignmentY(Component.BOTTOM_ALIGNMENT);
      this.innerHeaderPanel.addLineEndComponent(component);
    } else {
      AwtComponentView<?> prevComponent = this.innerHeaderPanel.getLineEndComponent();
      if (prevComponent != null) {
        this.innerHeaderPanel.removeComponent(prevComponent);
      }
    }
    this.innerHeaderPanel.revalidateAndRepaint();
  }

  public void setHeaderTrailingComponent(SwingComponentView<?> component) {
    if (component != null) {
      if (!component.isOpaque()) {
        component.setBackgroundColor(this.getBackgroundColor());
      }
      component.setAlignmentY(Component.BOTTOM_ALIGNMENT);
      this.outerHeaderPanel.addLineEndComponent(component);
    } else {
      AwtComponentView<?> prevComponent = this.outerHeaderPanel.getLineEndComponent();
      if (prevComponent != null) {
        this.outerHeaderPanel.removeComponent(prevComponent);
      }
    }
    this.outerHeaderPanel.revalidateAndRepaint();
  }

  @Override
  protected JPanel createAwtComponent() {
    JPanel rv = super.createAwtComponent();
    this.innerHeaderPanel.addCenterComponent(this.titlesScrollPane);
    this.outerHeaderPanel.addCenterComponent(this.innerHeaderPanel);
    rv.add(this.outerHeaderPanel.getAwtComponent(), BorderLayout.PAGE_START);
    rv.add(this.getCardOwner().getView().getAwtComponent(), BorderLayout.CENTER);
    return rv;
  }

  @Override
  protected LayoutManager createLayoutManager(JPanel jPanel) {
    return new BorderLayout();
  }

  @Override
  protected BooleanStateButton<? extends javax.swing.AbstractButton> createTitleButton(E item, BooleanState itemSelectedState) {
    return new FolderTabTitle(item, itemSelectedState);
  }

  @Override
  protected void removeAllDetails() {
    super.removeAllDetails();
    this.titlesPanel.removeAllComponents();
  }

  @Override
  protected void addItem(E item, BooleanStateButton<?> button) {
    super.addItem(item, button);
    button.updateFor(item);
    this.titlesPanel.addComponent(button);
  }

  @Override
  protected void addSeparator() {
    super.addSeparator();
    this.titlesPanel.addComponent(BoxUtilities.createHorizontalSliver(16));
  }

  @Override
  public void setBackgroundColor(Color color) {
    super.setBackgroundColor(color);
    this.titlesPanel.setBackgroundColor(color);
    this.titlesScrollPane.setBackgroundColor(color);
  }

  @Override
  public void setForegroundColor(Color color) {
    super.setForegroundColor(color);
    this.titlesPanel.setForegroundColor(color);
    this.titlesScrollPane.setForegroundColor(color);
  }
}
