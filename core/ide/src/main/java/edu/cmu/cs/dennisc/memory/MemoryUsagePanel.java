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
package edu.cmu.cs.dennisc.memory;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Group;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.SwingComponentView;

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.UUID;

class MemoryUsageGraph extends SwingComponentView<JComponent> {
  private static final long K = 1024;
  //private static final long M = K*K;
  private List<MemoryUsage> samples = Lists.newLinkedList();

  public MemoryUsageGraph() {
    this.setBackgroundColor(Color.BLACK);
    this.setForegroundColor(Color.WHITE);
  }

  public void addSample(MemoryUsage heapUsage) {
    this.samples.add(heapUsage);
    this.repaint();
  }

  private void paintComponent(Graphics2D g2) {
    GeneralPath path = null;
    final int xDelta = 10;
    float x = 0.0f;
    int width = this.getWidth();
    int height = this.getHeight();
    int sampleCount = width / xDelta;

    final int N = this.samples.size();
    int i0 = Math.max(N - sampleCount, 0);
    for (int i = i0; i < N; i++) {
      MemoryUsage sample = this.samples.get(i);
      double portion = sample.getUsed() / K / (double) (sample.getMax() / K);
      float y = (float) ((1.0 - portion) * height);
      if (path != null) {
        path.lineTo(x, y);
      } else {
        path = new GeneralPath();
        path.moveTo(x, y);
      }
      x += xDelta;
    }
    if ((N - i0) > 1) {
      g2.draw(path);
    }
  }

  @Override
  protected JComponent createAwtComponent() {
    JComponent rv = new JComponent() {
      @Override
      protected void paintComponent(Graphics g) {
        //super.paintComponent( g );
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.getBackground());
        g2.fill(g.getClipBounds());
        g2.setColor(this.getForeground());
        MemoryUsageGraph.this.paintComponent(g2);
      }

      @Override
      public Dimension getPreferredSize() {
        return new Dimension(640, 480);
      }
    };
    rv.setOpaque(false);
    return rv;
  }
}

class GarbageCollectAction extends ActionOperation {
  private static final Group SYSTEM_GROUP = Group.getInstance(UUID.fromString("7261a372-2b8d-4862-9669-852ba5e217e6"), "SYSTEM_GROUP");

  public GarbageCollectAction() {
    super(SYSTEM_GROUP, UUID.fromString("04dd2f4c-31d8-400e-8467-22a810e089b4"));
    this.setName("garbage collect");
  }

  @Override
  protected void perform(UserActivity activity) {
    System.gc();
    activity.finish();
  }
}

public class MemoryUsagePanel extends BorderPanel {
  private GarbageCollectAction garbageCollectAction = new GarbageCollectAction();
  private MemoryUsageGraph memoryUsageGraph = new MemoryUsageGraph();
  private Timer timer = new Timer(500, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
      MemoryUsage heapUsage = memory.getHeapMemoryUsage();
      MemoryUsagePanel.this.memoryUsageGraph.addSample(heapUsage);
    }
  });

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    this.timer.start();
  }

  @Override
  protected void handleUndisplayable() {
    this.timer.stop();
    super.handleUndisplayable();
  }

  public MemoryUsagePanel() {
    this.addCenterComponent(this.memoryUsageGraph);
    this.addPageEndComponent(this.garbageCollectAction.createButton());
  }
}
