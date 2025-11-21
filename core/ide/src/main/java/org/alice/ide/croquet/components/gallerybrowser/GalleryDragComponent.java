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

package org.alice.ide.croquet.components.gallerybrowser;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.Theme;
import org.alice.ide.croquet.components.KnurlDragComponent;
import org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
import org.alice.ide.icons.IconFactoryManager;
import org.alice.ide.icons.PlusIconFactory;
import org.alice.stageide.gallerybrowser.shapes.ShapeDragModel;
import org.alice.stageide.gallerybrowser.uri.UriGalleryDragModel;
import org.alice.stageide.modelresource.InstanceCreatorKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.triggers.MouseEventTrigger;
import org.lgna.croquet.views.*;
import org.lgna.croquet.views.Label;
import org.lgna.story.resources.ModelResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Popup that appears when adding a model via a drag in from the gallery
 * @author Dennis Cosgrove
 */
public class GalleryDragComponent extends KnurlDragComponent<GalleryDragModel> {
  private Color baseColor = UIManager.getColor("Label.background");

  private final SingleSelectTreeState<ResourceNode> controller;

  private static final class SuperclassIconLabel extends SwingComponentView<JLabel> {
    private final Class<?> modelResourceInterface;

    SuperclassIconLabel(Class<?> modelResourceInterface) {
      this.modelResourceInterface = modelResourceInterface;
    }

    @Override
    protected JLabel createAwtComponent() {
      JLabel rv = new JLabel() {
        private final JToolTip toolTipForTipLocation = new JToolTip();

        @Override
        public Point getToolTipLocation(MouseEvent event) {
          toolTipForTipLocation.setTipText(this.getToolTipText());
          int offset = toolTipForTipLocation.getPreferredSize().height;
          offset += 4;
          return new Point(0, -offset);
        }
      };
      StringBuilder sb = new StringBuilder();
      sb.append("superclass: ");
      String simpleName = modelResourceInterface.getSimpleName();
      if (simpleName.endsWith("Resource")) {
        simpleName = simpleName.substring(0, simpleName.length() - "Resource".length());
      }
      sb.append(simpleName);
      rv.setToolTipText(sb.toString());
      return rv;
    }
  }

  public GalleryDragComponent(ResourceNode model, SingleSelectTreeState<ResourceNode> controller) {
    super(model, false);
    this.controller = controller;
    if (model.isUserDefinedModel()) {
      this.baseColor = ColorUtilities.scaleHSB(this.baseColor, 1.0, 2.0, 1.0);
    } else if (!model.isInstanceCreator() || model.getNodeChildren().size() > 1) {
      // these are groups of items that we apply a folder color to
      this.baseColor = UIManager.getColor("Alice.folderColor");
    }

    if (!model.isBreadcrumbButtonIconDesired()) {
      ResourceKey resourceKey = model.getResourceKey();
      if (resourceKey instanceof InstanceCreatorKey instanceCreatorKey) {
        addSuperclassIcon(instanceCreatorKey.getModelResourceCls());
      }
    }
    setupDisplay(model);
  }

  public GalleryDragComponent(UriGalleryDragModel model) {
    super(model, false);
    controller = null;

    Label label = new Label(PlusIconFactory.getInstance().getIconToFit(Theme.SMALL_SQUARE_ICON_SIZE));
    label.setToolTipText(model.getTypeSummaryToolTipText());
    label.setVerticalAlignment(VerticalAlignment.BOTTOM);
    this.internalAddComponent(label, GalleryDragLayoutManager.TOP_RIGHT_CONSTRAINT);

    InstanceCreatorKey resourceKey = model.getResourceKey();
    if (resourceKey != null) {
      addSuperclassIcon(resourceKey.getModelResourceCls());
    }

    setupDisplay(model);
  }

  public GalleryDragComponent(ShapeDragModel model) {
    super(model, false);
    controller = null;

    setupDisplay(model);
  }

  private void addSuperclassIcon(Class<?> modelResourceCls) {
    if (modelResourceCls != null && modelResourceCls.isEnum()) {
      Class<?>[] modelResourceInterfaces = modelResourceCls.getInterfaces();
      if (modelResourceInterfaces.length > 0) {
        Class<?> modelResourceInterface = modelResourceInterfaces[0];
        if (ModelResource.class.isAssignableFrom(modelResourceInterface)) {
          IconFactory iconFactory = IconFactoryManager.getIconFactoryForResourceCls((Class<ModelResource>) modelResourceInterface);
          if (iconFactory != null) {
            Icon icon = iconFactory.getIconToFit(Theme.SMALL_RECT_ICON_SIZE);
            SuperclassIconLabel superclsLabel = new SuperclassIconLabel(modelResourceInterface);
            superclsLabel.getAwtComponent().setIcon(icon);
            this.internalAddComponent(superclsLabel, GalleryDragLayoutManager.TOP_LEFT_CONSTRAINT);
          }
        }
      }
    }
  }

  private void setupDisplay(GalleryDragModel model) {
    Label label = new Label();
    label.setText(model.getText());
    IconFactory iconFactory = model.getIconFactory();
    label.setIcon(iconFactory != null ? iconFactory.getIconToFit(model.getIconSize()) : null);
    label.setVerticalTextPosition(VerticalTextPosition.BOTTOM);
    label.setHorizontalTextPosition(HorizontalTextPosition.CENTER);

    this.internalAddComponent(label, GalleryDragLayoutManager.BASE_CONSTRAINT);
    this.setBackgroundColor(this.baseColor);
    this.setMaximumSizeClampedToPreferredSize(true);
    this.setAlignmentY(Component.TOP_ALIGNMENT);
  }

  @Override
  protected boolean isClickAndClackAppropriate() {
    GalleryDragModel model = this.getModel();
    return model.isClickAndClackAppropriate();
  }

  private static class GalleryDragLayoutManager implements LayoutManager {
    private static final String BASE_CONSTRAINT = "TOP_LEFT_CONSTRAINT_BASE";
    private static final String TOP_LEFT_CONSTRAINT = "TOP_LEFT_CONSTRAINT";
    private static final String TOP_RIGHT_CONSTRAINT = "TOP_RIGHT_CONSTRAINT";

    private final List<Component> topLeftComponents = Lists.newCopyOnWriteArrayList();
    private final List<Component> topRightComponents = Lists.newCopyOnWriteArrayList();
    private Component baseComponent;

    @Override
    public void addLayoutComponent(String name, Component comp) {
      if (name.contentEquals(BASE_CONSTRAINT)) {
        this.baseComponent = comp;
      }
      if (name.startsWith(TOP_LEFT_CONSTRAINT)) {
        this.topLeftComponents.add(comp);
      } else {
        this.topRightComponents.add(comp);
      }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
      return new Dimension();
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
      //note: ridiculous
      Dimension rv = this.baseComponent.getPreferredSize();
      Insets insets = parent.getInsets();
      rv.width += insets.left + insets.right;
      rv.height += insets.top + insets.bottom;
      return rv;
    }

    @Override
    public void layoutContainer(Container parent) {
      //note: ridiculous
      Insets insets = parent.getInsets();
      Dimension parentSize = parent.getSize();
      final int N = parent.getComponentCount();
      for (int i = 0; i < N; i++) {
        Component awtComponent = parent.getComponent(N - i - 1);
        awtComponent.setSize(awtComponent.getPreferredSize());
        awtComponent.setLocation(insets.left, insets.top);
      }
      for (Component awtComponent : this.topLeftComponents) {
        awtComponent.setLocation(insets.left, insets.top);
      }
      for (Component awtComponent : this.topRightComponents) {
        awtComponent.setLocation(parentSize.width - awtComponent.getWidth() - insets.right, insets.top);
      }
    }
  }

  @Override
  protected LayoutManager createLayoutManager(JPanel jComponent) {
    return new GalleryDragLayoutManager();
  }

  @Override
  protected void handleLeftMouseButtonQuoteClickedUnquote(MouseEvent e) {
    super.handleLeftMouseButtonQuoteClickedUnquote(e);
    if (e.getClickCount() == 1) {
      Triggerable leftButtonClickModel = this.getModel().getLeftButtonClickOperation(controller);
      if (leftButtonClickModel != null) {
        leftButtonClickModel.fire(MouseEventTrigger.createUserActivity(this, e));
      }
    }
  }

  @Override
  protected void handleBackButtonClicked(MouseEvent e) {
    super.handleBackButtonClicked(e);
    Logger.outln("todo: back");
  }

  @Override
  protected void handleForwardButtonClicked(MouseEvent e) {
    super.handleForwardButtonClicked(e);
    Logger.outln("todo: forward");
  }

  @Override
  protected int getInsetTop() {
    return 4;
  }

  @Override
  protected int getInsetRight() {
    return 4;
  }

  @Override
  protected int getInsetBottom() {
    return 4;
  }

  @Override
  protected int getDockInsetLeft() {
    return 1;
  }

  @Override
  protected int getInternalInsetLeft() {
    return 4;
  }

  @Override
  protected RoundRectangle2D.Float createShape(int x, int y, int width, int height) {
    return new RoundRectangle2D.Float(x, y, width - 1, height - 1, 8, 8);
  }

  @Override
  protected void fillBounds(Graphics2D g2, int x, int y, int width, int height) {
    g2.fill(this.createShape(x, y, width, height));
  }

  @Override
  protected void paintPrologue(Graphics2D g2, int x, int y, int width, int height) {
    RoundRectangle2D.Float shape = this.createShape(x, y, width, height);
    Paint prevPaint = g2.getPaint();
    Shape prevClip = g2.getClip();

    try {
      g2.setPaint(this.baseColor);
      g2.fill(shape);
    } finally {
      g2.setClip(prevClip);
      g2.setPaint(prevPaint);
    }
  }

  private static Shape createShapeAround(Rectangle2D bounds) {
    float x0 = (float) (bounds.getX() - 2);
    float y0 = (float) (bounds.getY() - 4);
    float x1 = (float) (x0 + bounds.getWidth() + 4);
    float y1 = (float) (y0 + bounds.getHeight() + 5);

    final int TAB_LENGTH = 6;

    GeneralPath rv = new GeneralPath();
    rv.moveTo(x0, y1);
    rv.lineTo(x0, y0);
    rv.lineTo(x0 + TAB_LENGTH, y0);
    rv.lineTo(x0 + TAB_LENGTH + 3, y0 + 3);
    rv.lineTo(x1, y0 + 3);
    rv.lineTo(x1, y1);
    rv.closePath();
    return rv;
  }

  @Override
  protected void paintEpilogue(Graphics2D g2, int x, int y, int width, int height) {
    super.paintEpilogue(g2, x, y, width, height);

    GalleryDragModel model = this.getModel();
    if (model instanceof ResourceNode resourceNode) {

      // this is how we paint the little folder icons with the counts inside
      List<ResourceNode> nodeChildren = resourceNode.getNodeChildren();
      if (nodeChildren.size() > 1) {
        String s = Integer.toString(nodeChildren.size());
        FontMetrics fm = g2.getFontMetrics();

        Rectangle2D actualTextBounds = fm.getStringBounds(s, g2);
        Rectangle2D minimumTextBounds = fm.getStringBounds("00", g2);

        Rectangle2D textBounds;
        if (actualTextBounds.getWidth() > minimumTextBounds.getWidth()) {
          textBounds = actualTextBounds;
        } else {
          textBounds = minimumTextBounds;
        }

        Shape shape = createShapeAround(textBounds);
        Rectangle2D shapeBounds = shape.getBounds();

        double xTranslate = (x + width) - shapeBounds.getWidth() - 4;
        double yTranslate = y + shapeBounds.getHeight();

        g2.translate(xTranslate, yTranslate);

        try {
          g2.setPaint(Color.DARK_GRAY);
          g2.draw(shape);
          g2.setPaint(UIManager.getColor("Label.foreground"));
          GraphicsUtilities.drawCenteredText(g2, s, textBounds);
        } finally {
          g2.translate(-xTranslate, -yTranslate);
        }
      }
    }
  }
}
