/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package rose;

class IsLayerVisibleState extends edu.cmu.cs.dennisc.croquet.BooleanState {
  private static java.util.Map<Layer, IsLayerVisibleState> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

  public static IsLayerVisibleState getInstance(Layer layer) {
    IsLayerVisibleState rv = map.get(layer);
    if (rv != null) {
      //pass
    } else {
      rv = new IsLayerVisibleState(layer);
      map.put(layer, rv);
    }
    return rv;
  }

  private Layer layer;

  private IsLayerVisibleState(Layer layer) {
    super(Rose.ROSE_GROUP, java.util.UUID.fromString("ec4d7690-dfbd-49b1-98c3-0efad175525d"), true);
    this.layer = layer;
  }

  @Override
  protected void localize() {
    super.localize();
    this.setTextForBothTrueAndFalse("is visible");
  }
}

class Layer {
  private java.util.UUID id = java.util.UUID.randomUUID();
  private final javax.media.jai.PlanarImage planarImage;

  public static Layer createFromPath(String path) {
    return new Layer(javax.media.jai.JAI.create("fileload", path));
  }

  public static Layer createFromColor(java.awt.Color color) {
    int alpha = color.getAlpha();
    final Byte[] bandValues;
    if (alpha < 255) {
      bandValues = new Byte[] {(byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha()};
    } else {
      bandValues = new Byte[] {(byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue()};
    }
    java.awt.image.renderable.ParameterBlock parameterBlock = new java.awt.image.renderable.ParameterBlock();
    parameterBlock.add(new Float(302));
    parameterBlock.add(new Float(414));
    parameterBlock.add(bandValues);
    return new Layer(javax.media.jai.JAI.create("constant", parameterBlock));
  }

  private Layer(javax.media.jai.PlanarImage planarImage) {
    this.planarImage = planarImage;
  }

  public java.util.UUID getId() {
    return this.id;
  }

  public javax.media.jai.PlanarImage getPlanarImage() {
    return this.planarImage;
  }

  public int getWidth() {
    return this.planarImage.getWidth();
  }

  public int getHeight() {
    return this.planarImage.getHeight();
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    } else {
      if (o instanceof Layer) {
        Layer other = (Layer) o;
        return this.id.equals(other.id);
      } else {
        return false;
      }
    }
  }

  @Override
  public final int hashCode() {
    return this.id.hashCode();
  }
}

class SaturationBrightness {

}

class LayerListModel extends javax.swing.AbstractListModel {
  private java.util.List<Layer> layers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

  public Layer getElementAt(int index) {
    return this.layers.get(index);
  }

  public int getSize() {
    return this.layers.size();
  }

  public void addLayer(Layer layer) {
    this.layers.add(layer);
  }

  public void removeLayer(Layer layer) {
    this.layers.remove(layer);
  }
}

class LayerListSelectionState {
  public static LayerListModel layerListModel = new LayerListModel();

  static {
    layerListModel.addLayer(Layer.createFromColor(new java.awt.Color(191, 191, 255, 255)));
    layerListModel.addLayer(Layer.createFromPath(System.getProperty("user.home") + "/Pictures/paintingTheRoseBushes.png"));
  }
}

class Canvas extends edu.cmu.cs.dennisc.croquet.JComponent<javax.swing.JComponent> {
  @Override
  protected javax.swing.JComponent createAwtComponent() {
    return new javax.swing.JComponent() {
      @Override
      protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        final int N = LayerListSelectionState.layerListModel.getSize();
        if (N > 0) {
          //          java.awt.image.renderable.ParameterBlock parameterBlock = new java.awt.image.renderable.ParameterBlock();
          //          for( int i=0; i<N; i++ ) {
          //            Layer layer = LayerListSelectionState.layerListModel.getElementAt( i );
          //            parameterBlock.addSource( layer.getPlanarImage() );
          //          }
          //=
          //          javax.media.jai.PlanarImage planarImage = javax.media.jai.JAI.create( "overlay", parameterBlock );
          //
          int x = (this.getWidth() - LayerListSelectionState.layerListModel.getElementAt(0).getWidth()) / 2;
          int y = (this.getHeight() - LayerListSelectionState.layerListModel.getElementAt(0).getHeight()) / 2;
          //          g2.drawImage( planarImage.getAsBufferedImage(), x, y, this );

          for (int i = 0; i < N; i++) {
            Layer layer = LayerListSelectionState.layerListModel.getElementAt(i);
            g2.drawImage(layer.getPlanarImage().getAsBufferedImage(), x, y, this);
          }
        }
      }
    };
  }
}

class LayerDetailsPanel extends edu.cmu.cs.dennisc.croquet.PageAxisPanel {
  private static java.util.Map<Layer, LayerDetailsPanel> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

  public static LayerDetailsPanel getLayerDetailsPanel(Layer layer) {
    LayerDetailsPanel rv = map.get(layer);
    if (rv != null) {
      //pass
    } else {
      rv = new LayerDetailsPanel(layer);
    }
    return rv;
  }

  private LayerDetailsPanel(Layer layer) {
    this.addComponent(IsLayerVisibleState.getInstance(layer).createCheckBox());
  }
}

class LayerDetailsCardPanel extends edu.cmu.cs.dennisc.croquet.CardPanel {
  public void addLayer(Layer layer) {
    LayerDetailsPanel layerDetailsPanel = LayerDetailsPanel.getLayerDetailsPanel(layer);
    Key key = this.createKey(layerDetailsPanel, layer.getId());
    this.addComponent(key);
    this.show(key);
  }
}

class LayerPanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
  public LayerPanel() {
    this.addComponent(new edu.cmu.cs.dennisc.croquet.Label("layers"), Constraint.PAGE_START);
    edu.cmu.cs.dennisc.croquet.VerticalSplitPane splitPane = new edu.cmu.cs.dennisc.croquet.VerticalSplitPane();
    splitPane.setTopComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(new javax.swing.JList(LayerListSelectionState.layerListModel)));

    LayerDetailsCardPanel layerDetailsCardPanel = new LayerDetailsCardPanel();

    //todo
    layerDetailsCardPanel.addLayer(LayerListSelectionState.layerListModel.getElementAt(0));

    splitPane.setBottomComponent(layerDetailsCardPanel);
    this.addComponent(splitPane, Constraint.CENTER);
  }
}

/**
 * @author Dennis Cosgrove
 */
public class Rose extends edu.cmu.cs.dennisc.croquet.Application {
  public static final edu.cmu.cs.dennisc.croquet.Group ROSE_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance(java.util.UUID.fromString("0275aa15-0e97-4d84-9541-909f488e5fcd"), "ROSE_GROUP");

  @Override
  protected edu.cmu.cs.dennisc.croquet.Component<?> createContentPane() {
    edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
    rv.addComponent(new edu.cmu.cs.dennisc.croquet.Label("toolbox"), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_START);
    rv.addComponent(new Canvas(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER);
    rv.addComponent(new LayerPanel(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_END);
    return rv;
  }

  @Override
  protected void handleWindowOpened(java.awt.event.WindowEvent e) {
  }

  @Override
  protected void handlePreferences(java.util.EventObject e) {
  }

  @Override
  protected void handleAbout(java.util.EventObject e) {
  }

  @Override
  protected void handleQuit(java.util.EventObject e) {
    System.exit(0);
  }

  @Override
  public edu.cmu.cs.dennisc.croquet.DropReceptor getDropReceptor(edu.cmu.cs.dennisc.croquet.DropSite dropSite) {
    return null;
  }

  public static void main(String[] args) {
    Rose rose = new Rose();
    rose.initialize(args);
    rose.getFrame().setSize(640, 480);
    rose.getFrame().setVisible(true);
  }
}
