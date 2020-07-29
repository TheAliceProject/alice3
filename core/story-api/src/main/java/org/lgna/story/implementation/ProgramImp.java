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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.Animation;
import edu.cmu.cs.dennisc.animation.AnimationObserver;
import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderFactory;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;
import org.lgna.common.ProgramClosedException;
import org.lgna.story.SProgram;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProgramImp {
  // Hack for Netbeans plugin to operate without IDE localization
  private static final String DEFAULT_SPEED_FORMAT = "speed: %dx";
  private static Object ACCEPTABLE_HACK_FOR_NOW_classForNextInstanceLock = new Object();
  private static Class<? extends ProgramImp> ACCEPTABLE_HACK_FOR_NOW_classForNextInstance;
  private static Class<?>[] ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes;
  private static Object[] ACCEPTABLE_HACK_FOR_NOW_bonusArguments;

  public static void ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance(Class<? extends ProgramImp> classForNextInstance, Class<?>[] bonusParameterTypes, Object[] bonusArguments) {
    synchronized (ACCEPTABLE_HACK_FOR_NOW_classForNextInstanceLock) {
      assert ACCEPTABLE_HACK_FOR_NOW_classForNextInstance == null : ACCEPTABLE_HACK_FOR_NOW_classForNextInstance;
      assert ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes == null : ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes;
      assert ACCEPTABLE_HACK_FOR_NOW_bonusArguments == null : ACCEPTABLE_HACK_FOR_NOW_bonusArguments;
      ACCEPTABLE_HACK_FOR_NOW_classForNextInstance = classForNextInstance;
      ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes = bonusParameterTypes;
      ACCEPTABLE_HACK_FOR_NOW_bonusArguments = bonusArguments;
    }
  }

  public static void ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance(Class<? extends ProgramImp> classForNextInstance) {
    ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance(classForNextInstance, new Class<?>[] {}, new Object[] {});
  }

  public static ProgramImp createInstance(SProgram abstraction) {
    ProgramImp rv;
    synchronized (ACCEPTABLE_HACK_FOR_NOW_classForNextInstanceLock) {
      if (ACCEPTABLE_HACK_FOR_NOW_classForNextInstance != null) {

        Class<?>[] parameterTypes = new Class<?>[ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes.length + 1];
        parameterTypes[0] = SProgram.class;
        System.arraycopy(ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes, 0, parameterTypes, 1, ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes.length);

        Object[] arguments = new Object[ACCEPTABLE_HACK_FOR_NOW_bonusArguments.length + 1];
        arguments[0] = abstraction;
        System.arraycopy(ACCEPTABLE_HACK_FOR_NOW_bonusArguments, 0, arguments, 1, ACCEPTABLE_HACK_FOR_NOW_bonusArguments.length);

        Constructor<? extends ProgramImp> cnstrctr = ReflectionUtilities.getConstructor(ACCEPTABLE_HACK_FOR_NOW_classForNextInstance, parameterTypes);
        assert cnstrctr != null : ACCEPTABLE_HACK_FOR_NOW_classForNextInstance;
        rv = ReflectionUtilities.newInstance(cnstrctr, arguments);
        ACCEPTABLE_HACK_FOR_NOW_classForNextInstance = null;
        ACCEPTABLE_HACK_FOR_NOW_bonusParameterTypes = null;
        ACCEPTABLE_HACK_FOR_NOW_bonusArguments = null;
      } else {
        rv = new DefaultProgramImp(abstraction);
      }
    }
    return rv;
  }

  private static class ToggleFullScreenAction extends AbstractAction {
    private Rectangle prevNormalBounds;

    @Override
    public void actionPerformed(ActionEvent e) {
      AbstractButton button = (AbstractButton) e.getSource();
      ButtonModel buttonModel = button.getModel();
      Component root = SwingUtilities.getRoot(button);
      if (root != null) {
        Rectangle bounds;
        if (buttonModel.isSelected()) {
          this.prevNormalBounds = root.getBounds();
          bounds = root.getGraphicsConfiguration().getBounds();
        } else {
          bounds = this.prevNormalBounds;
          this.prevNormalBounds = null;
        }
        if (bounds != null) {
          root.setBounds(bounds);
        }
      }
    }
  }

    private static final class FullScreenIcon implements Icon {
    @Override
    public int getIconWidth() {
      return 24;
    }

    @Override
    public int getIconHeight() {
      return 16;
    }

    private static void paintRect(Graphics2D g2, Paint fillPaint, Paint drawPaint, int x, int y, int width, int height) {
      g2.setPaint(fillPaint);
      g2.fillRect(x, y, width, height);
      g2.setPaint(drawPaint);
      g2.drawRect(x, y, width, height);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      AbstractButton b = (AbstractButton) c;
      Graphics2D g2 = (Graphics2D) g;

      ButtonModel buttonModel = b.getModel();
      Paint fillPaint;
      if (buttonModel.isRollover()) {
        fillPaint = Color.WHITE;
      } else {
        fillPaint = Color.LIGHT_GRAY;
      }
      Paint drawPaint = Color.DARK_GRAY;

      int W = this.getIconWidth();
      int H = this.getIconHeight();

      int w = 8;
      int h = 6;

      g2.translate(x, y);
      paintRect(g2, fillPaint, drawPaint, 0, 0, w, h);
      paintRect(g2, fillPaint, drawPaint, 0, H - h, w, h);
      paintRect(g2, fillPaint, drawPaint, W - w, H - h, w, h);
      paintRect(g2, fillPaint, drawPaint, W - w, 0, w, h);

      paintRect(g2, Color.GRAY, drawPaint, 4, 3, W - 8, H - 6);
      g2.translate(-x, -y);
    }
  }

  protected ProgramImp(SProgram abstraction, OnscreenRenderTarget<?> onscreenRenderTarget) {
    this.abstraction = abstraction;
    this.onscreenRenderTarget = onscreenRenderTarget;
    this.toggleFullScreenAction.putValue(Action.SMALL_ICON, new FullScreenIcon());
  }

  protected void handleSpeedChange(double speedFactor) {
    this.getAnimator().setSpeedFactor(speedFactor);
  }

  public String getSpeedFormat() {
    return (speedFormat == null) ? DEFAULT_SPEED_FORMAT : speedFormat;
  }

  public void setSpeedFormat(String format) {
    speedFormat = format;
  }

  public Action getRestartAction() {
    return this.restartAction;
  }

  public void setRestartAction(Action restartAction) {
    this.restartAction = restartAction;
  }

  public Action getToggleFullScreenAction() {
    return this.toggleFullScreenAction;
  }

  public Rectangle getNormalDialogBounds(Component awtComponent) {
    if (this.toggleFullScreenAction.prevNormalBounds != null) {
      return this.toggleFullScreenAction.prevNormalBounds;
    } else {
      return awtComponent.getBounds();
    }
  }

  private boolean isControlPanelDesired = true;

  public boolean isControlPanelDesired() {
    return this.isControlPanelDesired;
  }

  public void setControlPanelDesired(boolean isControlPanelDesired) {
    this.isControlPanelDesired = isControlPanelDesired;
  }

  public SProgram getAbstraction() {
    return this.abstraction;
  }

  public OnscreenRenderTarget<?> getOnscreenRenderTarget() {
    return this.onscreenRenderTarget;
  }

  public abstract Animator getAnimator();

  public double getSimulationSpeedFactor() {
    return this.simulationSpeedFactor;
  }

  public void setSimulationSpeedFactor(double simulationSpeedFactor) {
    this.simulationSpeedFactor = simulationSpeedFactor;
  }

  private final AutomaticDisplayListener automaticDisplayListener = new AutomaticDisplayListener() {
    @Override
    public void automaticDisplayCompleted(AutomaticDisplayEvent e) {
      if (isAnimatorStarted) {
        ProgramImp.this.getAnimator().update();
      }
    }
  };

  public void startAnimator() {
    RenderFactory renderFactory = this.getOnscreenRenderTarget().getRenderFactory();
    renderFactory.addAutomaticDisplayListener(this.automaticDisplayListener);
    renderFactory.incrementAutomaticDisplayCount();
    this.isAnimatorStarted = true;
  }

  public void stopAnimator() {
    if (this.isAnimatorStarted) {
      isAnimatorStarted = false;
      this.getAnimator().completeAll(null);
      RenderFactory renderFactory = this.getOnscreenRenderTarget().getRenderFactory();
      renderFactory.decrementAutomaticDisplayCount();
      renderFactory.removeAutomaticDisplayListener(this.automaticDisplayListener);
    } else {
      Logger.severe(this.isAnimatorStarted);
    }
  }

  private void addComponents(AwtContainerInitializer awtContainerInitializer) {
    Component awtLgComponent = this.getOnscreenRenderTarget().getAwtComponent();
    synchronized (awtLgComponent.getTreeLock()) {
      JPanel controlPanel;
      if (this.isControlPanelDesired()) {
        controlPanel = new ProgramControlPanel(this);
      } else {
        controlPanel = null;
      }
      awtContainerInitializer.addComponents(onscreenRenderTarget, controlPanel);
    }
  }

  private void requestFocusInWindow() {
    this.getOnscreenRenderTarget().getAwtComponent().requestFocusInWindow();
  }

  public static interface AwtContainerInitializer {
    public void addComponents(OnscreenRenderTarget<?> onscreenRenderTarget, JPanel controlPanel);
  }

  private static class DefaultAwtContainerInitializer implements AwtContainerInitializer {
    private final Container awtContainer;

    public DefaultAwtContainerInitializer(Container awtContainer) {
      this.awtContainer = awtContainer;
    }

    @Override
    public void addComponents(OnscreenRenderTarget<?> onscreenRenderTarget, JPanel controlPanel) {
      this.awtContainer.add(onscreenRenderTarget.getAwtComponent());
      if (controlPanel != null) {
        this.awtContainer.add(controlPanel, BorderLayout.PAGE_START);
      }
      if (this.awtContainer instanceof JComponent) {
        ((JComponent) this.awtContainer).revalidate();
      }
    }
  }

  public void initializeInAwtContainer(AwtContainerInitializer awtContainerInitializer) {
    this.addComponents(awtContainerInitializer);
    this.startAnimator();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        requestFocusInWindow();
      }
    });
  }

  public void initializeInAwtContainer(Container awtContainer) {
    this.initializeInAwtContainer(new DefaultAwtContainerInitializer(awtContainer));
  }

  public void initializeInFrame(final JFrame frame, final Runnable runnable) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        ProgramImp.this.addComponents(new DefaultAwtContainerInitializer(frame.getContentPane()));
        frame.setVisible(true);
        runnable.run();
        requestFocusInWindow();
      }
    });
  }

  public void initializeInFrame(JFrame frame) {
    final CyclicBarrier barrier = new CyclicBarrier(2);
    this.initializeInFrame(frame, new Runnable() {
      @Override
      public void run() {
        try {
          barrier.await();
        } catch (InterruptedException ie) {
          throw new RuntimeException(ie);
        } catch (BrokenBarrierException bbe) {
          throw new RuntimeException(bbe);
        }
      }
    });
    try {
      barrier.await();
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    } catch (BrokenBarrierException bbe) {
      throw new RuntimeException(bbe);
    }
    this.startAnimator();
  }

  public void initializeInApplet(JApplet applet) {
    this.addComponents(new DefaultAwtContainerInitializer(applet.getContentPane()));
    this.startAnimator();
  }

  public void shutDown() {
    this.onscreenRenderTarget.release();
    this.stopAnimator();
    this.isProgramClosedExceptionDesired = true;
  }

  /* package-private */void perform(Animation animation, AnimationObserver animationObserver) {
    if (this.isProgramClosedExceptionDesired) {
      if (this.isAnimatorStarted) {
        this.stopAnimator();
      }
      throw new ProgramClosedException();
    }
    this.getAnimator().invokeAndWait_ThrowRuntimeExceptionsIfNecessary(animation, animationObserver);
  }

  private final SProgram abstraction;
  private final OnscreenRenderTarget<?> onscreenRenderTarget;
  private double simulationSpeedFactor = 1.0;
  private String speedFormat;
  private Action restartAction;
  private boolean isAnimatorStarted = false;
  private boolean isProgramClosedExceptionDesired = false;
  private final ToggleFullScreenAction toggleFullScreenAction = new ToggleFullScreenAction();
}
