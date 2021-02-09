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
package org.lgna.croquet.views.imp;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import javax.swing.JPopupMenu;
import javax.swing.SizeRequirements;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
class ScrollingPopupMenuLayout implements LayoutManager2 {
  private final List<Component> mainItems = Lists.newCopyOnWriteArrayList();
  private final Map<Component, Integer> mapSideItemToIndex = Maps.newHashMap();

  private Component pageStartComponent;
  private Component pageEndComponent;

  private SizeRequirements[] childWidthRequirements;
  private SizeRequirements[] childHeightRequirements;
  private SizeRequirements widthRequirement;
  private SizeRequirements heightRequirement;

  private int index0;

  public enum ScrollConstraint {
    PAGE_START, PAGE_END
  }

  public enum ColumnConstraint {
    MAIN, SIDE
  }

  private final Container target;

  public ScrollingPopupMenuLayout(Container target) {
    this.target = target;
  }

  private void constrainIndex() {
    final int N = mainItems.size();
    index0 = Math.max(index0, 0);
    index0 = Math.min(index0, N - 1);
  }

  public void adjustIndex(int delta) {
    index0 += delta;
    constrainIndex();
    if (mainItems.get(index0) instanceof JPopupMenu.Separator) {
      index0 += delta;
      constrainIndex();
    }
    layoutContainer(target);
  }

  @Override
  public synchronized float getLayoutAlignmentX(Container target) {
    updateRequirementsIfNecessary();
    return widthRequirement.alignment;
  }

  @Override
  public synchronized float getLayoutAlignmentY(Container target) {
    updateRequirementsIfNecessary();
    return heightRequirement.alignment;
  }

  @Override
  public void addLayoutComponent(String name, Component comp) {
    Logger.severe(this, name, comp);
    invalidateLayout(comp.getParent());
  }

  @Override
  public void addLayoutComponent(Component comp, Object constraints) {
    if (constraints == null || constraints == ColumnConstraint.MAIN) {
      mainItems.add(comp);
    } else if (constraints == ColumnConstraint.SIDE) {
      mapSideItemToIndex.put(comp, mainItems.size() - 1);
    } else if (constraints == ScrollConstraint.PAGE_START) {
      pageStartComponent = comp;
    } else if (constraints == ScrollConstraint.PAGE_END) {
      pageEndComponent = comp;
    } else {
      Logger.severe(this, comp, constraints);
    }
    invalidateLayout(comp.getParent());
  }

  @Override
  public void removeLayoutComponent(Component comp) {
    invalidateLayout(comp.getParent());
    if (mainItems.contains(comp)) {
      mainItems.remove(comp);
      constrainIndex();
    } else {
      if (mapSideItemToIndex.containsKey(comp)) {
        mapSideItemToIndex.remove(comp);
      } else {
        if ((comp != pageStartComponent) && (comp != pageEndComponent)) {
          Logger.severe("Failed to remove unexpected child component in ScrollingPopupMenuLayout", comp);
        }
        // TODO Handle else state - the removal of start or end component
      }
    }
  }

  @Override
  public synchronized void invalidateLayout(Container target) {
    index0 = 0;
    // Drop height info, but leave widths in place to preserve when scrolling to avoid expanding over sub menu triggers
    childHeightRequirements = null;
    heightRequirement = null;
  }

  private void updateRequirementsIfNecessary() {
    if (childHeightRequirements != null) {
      return;
    }
    final int childWidthCount = mainItems.size() + (pageStartComponent == null ? 0 : 1) + (pageEndComponent == null ? 0 : 1);
    final boolean recomputeWidths = childWidthRequirements == null || childWidthCount > childWidthRequirements.length;
    if (recomputeWidths) {
      childWidthRequirements = new SizeRequirements[childWidthCount];
    }
    childHeightRequirements = new SizeRequirements[mainItems.size()];
    int i = 0;
    for (Component componentI : mainItems) {
      addChildHeightRequirement(componentI, i);
      if (recomputeWidths) {
        addChildWidthRequirement(componentI, i);
      }
      i++;
    }
    if (pageStartComponent != null) {
      addChildWidthRequirement(pageStartComponent, i);
      i++;
    }
    if (pageEndComponent != null) {
      addChildWidthRequirement(pageEndComponent, i);
    }
    if (recomputeWidths) {
      widthRequirement = SizeRequirements.getAlignedSizeRequirements(childWidthRequirements);
    }
    heightRequirement = SizeRequirements.getTiledSizeRequirements(childHeightRequirements);
  }

  private void addChildHeightRequirement(Component child, int i) {
    if (child.isVisible()) {
      Dimension minimum = child.getMinimumSize();
      Dimension preferred = child.getPreferredSize();
      Dimension maximum = child.getMaximumSize();
      childHeightRequirements[i] = new SizeRequirements(minimum.height, preferred.height, maximum.height, child.getAlignmentY());
    } else {
      childHeightRequirements[i] = new SizeRequirements(0, 0, 0, child.getAlignmentY());
    }
  }

  private void addChildWidthRequirement(Component child, int i) {
    if (child.isVisible()) {
      Dimension minimum = child.getMinimumSize();
      Dimension preferred = child.getPreferredSize();
      Dimension maximum = child.getMaximumSize();
      childWidthRequirements[i] = new SizeRequirements(minimum.width, preferred.width, maximum.width, child.getAlignmentX());
    } else {
      childWidthRequirements[i] = new SizeRequirements(0, 0, 0, child.getAlignmentX());
    }
  }

  private static final int SIDE_WIDTH = 20;

  private int getSideWidth() {
    return mapSideItemToIndex.isEmpty() ? 0 : SIDE_WIDTH;
  }

  private Dimension getInsetSize(int width, int height, Container target) {
    Insets insets = target.getInsets();
    int sideWidth = getSideWidth();
    return new Dimension(width + insets.left + insets.right + sideWidth, height + insets.top + insets.bottom);
  }

  @Override
  public synchronized Dimension minimumLayoutSize(Container target) {
    updateRequirementsIfNecessary();
    return getInsetSize(widthRequirement.minimum, heightRequirement.minimum, target);
  }

  private Dimension actualPreferredLayoutSize(Container target) {
    return getInsetSize(widthRequirement.preferred, heightRequirement.preferred, target);
  }

  @Override
  public synchronized Dimension preferredLayoutSize(Container target) {
    invalidateLayout(target);
    updateRequirementsIfNecessary();
    Dimension rv = actualPreferredLayoutSize(target);
    GraphicsConfiguration graphicsConfiguration = target.getGraphicsConfiguration();
    if (graphicsConfiguration == null) {
      if (target instanceof JPopupMenu) {
        JPopupMenu jPopupMenu = (JPopupMenu) target;
        Component invoker = jPopupMenu.getInvoker();
        if (invoker != null) {
          graphicsConfiguration = invoker.getGraphicsConfiguration();
        }
      }
      if (graphicsConfiguration == null) {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsConfiguration = graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
        Logger.outln("todo: determine correct graphicsConfiguration.  using default:", graphicsConfiguration);
      }
    }
    Rectangle bounds = graphicsConfiguration.getBounds();
    rv.height = Math.min(rv.height, bounds.height - 64);
    return rv;
  }

  @Override
  public synchronized Dimension maximumLayoutSize(Container target) {
    updateRequirementsIfNecessary();
    return getInsetSize(widthRequirement.maximum, heightRequirement.maximum, target);
  }

  private static int calculateLastIndex(int firstIndex, int[] heights, int availableHeight) {
    int usedHeight = 0;
    int lastIndex = firstIndex;
    for (int i = firstIndex; i < heights.length; i++) {
      usedHeight += heights[i];
      if (usedHeight <= availableHeight) {
        lastIndex = i;
      } else {
        break;
      }
    }
    return lastIndex;
  }

  @Override
  public synchronized void layoutContainer(Container target) {
    updateRequirementsIfNecessary();
    Dimension layoutSize = actualPreferredLayoutSize(target);
    Insets insets = target.getInsets();
    int layoutWidth = layoutSize.width - insets.left - insets.right;
    int layoutHeight = layoutSize.height - insets.top - insets.bottom;

    final int N = mainItems.size();
    int[] ys = new int[N];
    int[] heights = new int[N];
    SizeRequirements.calculateTiledPositions(layoutHeight, heightRequirement, childHeightRequirements, ys, heights);

    Dimension size = target.getSize();
    int firstIndex;
    int lastIndex;
    int pageStartHeight;
    if (size.height < layoutSize.height) {
      firstIndex = index0;

      if (pageStartComponent != null) {
        if (firstIndex > 0) {
          pageStartHeight = pageStartComponent.getPreferredSize().height;
        } else {
          pageStartHeight = 0;
        }
        pageStartComponent.setBounds(0, 0, size.width, pageStartHeight);
      } else {
        pageStartHeight = 0;
      }

      lastIndex = calculateLastIndex(firstIndex, heights, size.height - pageStartHeight);

      int pageEndHeight;
      if (pageEndComponent != null) {
        if (lastIndex < (N - 1)) {
          pageEndHeight = pageEndComponent.getPreferredSize().height;
        } else {
          pageEndHeight = 0;
        }
        pageEndComponent.setBounds(0, size.height - pageEndHeight, size.width, pageEndHeight);
      } else {
        pageEndHeight = 0;
      }

      if (pageEndHeight > 0) {
        lastIndex = calculateLastIndex(firstIndex, heights, size.height - pageStartHeight - pageEndHeight);
      }

      if (lastIndex == (N - 1)) {
        int availableHeight = size.height - pageStartHeight;
        int usedHeight = 0;
        int i = lastIndex;
        while (i >= 0) {
          usedHeight += heights[i];
          if (usedHeight <= availableHeight) {
            firstIndex = i;
            i--;
          } else {
            break;
          }
        }
        index0 = firstIndex;
      }

      if (pageStartComponent instanceof JScrollMenuItem) {
        JScrollMenuItem pageStartScrollMenuItem = (JScrollMenuItem) pageStartComponent;
        pageStartScrollMenuItem.setCount(firstIndex);
        addChildWidthRequirement(pageStartComponent, lastIndex);
      }
      if (pageEndComponent instanceof JScrollMenuItem) {
        JScrollMenuItem pageEndScrollMenuItem = (JScrollMenuItem) pageEndComponent;
        pageEndScrollMenuItem.setCount(N - 1 - lastIndex);
        addChildWidthRequirement(pageStartComponent, lastIndex + 1);
      }
    } else {
      pageStartHeight = 0;
      firstIndex = 0;
      lastIndex = N - 1;
      if (pageStartComponent != null) {
        pageStartComponent.setBounds(0, 0, 0, 0);
        pageStartComponent.setEnabled(false);
      }
      if (pageEndComponent != null) {
        pageEndComponent.setBounds(0, 0, 0, 0);
        pageEndComponent.setEnabled(false);
      }
    }
    final int widthCount = childWidthRequirements.length;
    int[] xs = new int[widthCount];
    int[] widths = new int[widthCount];
    SizeRequirements.calculateAlignedPositions(layoutWidth, widthRequirement, childWidthRequirements, xs, widths);

    int sideWidth = getSideWidth();
    int i = 0;
    for (Component component : mainItems) {
      if ((firstIndex <= i) && (i <= lastIndex)) {
        component.setBounds(insets.left + xs[i], ((insets.top + ys[i]) + pageStartHeight) - ys[firstIndex], widths[i] - sideWidth, heights[i]);
      } else {
        component.setBounds(0, 0, 0, 0);
      }
      i++;
    }

    for (Component component : mapSideItemToIndex.keySet()) {
      i = mapSideItemToIndex.get(component);
      if ((firstIndex <= i) && (i <= lastIndex)) {
        component.setBounds(size.width - sideWidth, ((insets.top + ys[i]) + pageStartHeight) - ys[firstIndex], sideWidth, heights[i]);
      } else {
        component.setBounds(0, 0, 0, 0);
      }
    }

  }
}
