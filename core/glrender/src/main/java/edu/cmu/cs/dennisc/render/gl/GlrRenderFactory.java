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

package edu.cmu.cs.dennisc.render.gl;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Queues;
import edu.cmu.cs.dennisc.pattern.Releasable;
import edu.cmu.cs.dennisc.pattern.event.ReleaseEvent;
import edu.cmu.cs.dennisc.pattern.event.ReleaseListener;
import edu.cmu.cs.dennisc.render.ImageBuffer;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.OffscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderCapabilities;
import edu.cmu.cs.dennisc.render.RenderFactory;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;
import edu.cmu.cs.dennisc.render.gl.imp.GlrImageBuffer;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.ChangeHandler;

import java.awt.Component;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * @author Dennis Cosgrove
 */
public class GlrRenderFactory implements RenderFactory {
  static {
    RendererNativeLibraryLoader.initializeIfNecessary();
  }

  private static class SingletonHolder {
    private static final GlrRenderFactory instance = new GlrRenderFactory();
  }

  public static GlrRenderFactory getInstance() {
    return SingletonHolder.instance;
  }

  private GlrRenderFactory() {
  }

  @Override
  public ImageBuffer createImageBuffer(Color4f backgroundColor) {
    return new GlrImageBuffer(backgroundColor);
  }

  //todo: just force start and stop? or rename methods
  private int automaticDisplayCount = 0;

  private static boolean isDisplayDesired(GlrOnscreenRenderTarget lg) {
    if (lg.isRenderingEnabled()) {
      Component component = lg.getAwtComponent();
      return component.isVisible() && component.isValid()
          && component.getWidth() > 0 && component.getHeight() > 0
          && lg.getSgCameraCount() > 0;
    }
    return false;
  }

  Animator.ThreadDeferenceAction step() {
    Animator.ThreadDeferenceAction rv = Animator.ThreadDeferenceAction.SLEEP;
    synchronized (this.toBeReleased) {
      for (Releasable releasable : this.toBeReleased) {
        if (releasable instanceof GlrOnscreenRenderTarget) {
          GlrOnscreenRenderTarget onscreenLookingGlass = (GlrOnscreenRenderTarget) releasable;
          this.onscreenLookingGlasses.remove(onscreenLookingGlass);
          //this.animator.remove(onscreenLookingGlass.getGLAutoDrawable() );
        } else if (releasable instanceof GlrOffscreenRenderTarget) {
          this.offscreenLookingGlasses.remove(releasable);
        } else {
          assert false;
        }
      }
      this.toBeReleased.clear();
    }

    if (this.automaticDisplayCount > 0) {
      acquireRenderingLock();
      try {
        ChangeHandler.pushRenderingMode();
        try {
          if (ChangeHandler.getEventCountSinceLastReset() > 0 /* || isJustCreatedOnscreenLookingGlassAccountedFor == false */) {
            ChangeHandler.resetEventCount();
            //isJustCreatedOnscreenLookingGlassAccountedFor = true;
            for (GlrOnscreenRenderTarget lg : this.onscreenLookingGlasses) {
              if (isDisplayDesired(lg)) {
                //lg.getGLAutoDrawable().display();
                lg.repaint();
                //edu.cmu.cs.dennisc.print.PrintUtilities.println( lg );
                rv = Animator.ThreadDeferenceAction.YIELD;
              }
            }
          }
        } finally {
          ChangeHandler.popRenderingMode();
        }
      } finally {
        releaseRenderingLock();
      }
    // } else {
      //edu.cmu.cs.dennisc.print.PrintUtilities.println( "this.automaticDisplayCount", this.automaticDisplayCount );
    }
    GlrRenderFactory.this.handleDisplayed();
    return rv;
  }

  @Override
  public void acquireRenderingLock() {
    try {
      this.renderingLock.acquire();
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }

  @Override
  public void releaseRenderingLock() {
    this.renderingLock.release();
  }

  @Override
  public synchronized void incrementAutomaticDisplayCount() {
    this.automaticDisplayCount++;
    if (this.animator == null) {
      this.animator = new Animator() {
        @Override
        protected ThreadDeferenceAction step() {
          return GlrRenderFactory.this.step();
        }
      };
      this.animator.start();
    }
  }

  @Override
  public synchronized void decrementAutomaticDisplayCount() {
    this.automaticDisplayCount--;
  }

  @Override
  public OnscreenRenderTarget createOnscreenRenderTarget(RenderCapabilities requestedCapabilities) {
    GlrOnscreenRenderTarget lolg = new GlrOnscreenRenderTarget(this, requestedCapabilities);
    lolg.addReleaseListener(this.releaseListener);
    this.onscreenLookingGlasses.add(lolg);
    return lolg;
  }

  @Override
  public OffscreenRenderTarget createOffscreenRenderTarget(int width, int height, RenderTarget renderTargetToShareContextWith, RenderCapabilities requestedCapabilities) {
    assert (renderTargetToShareContextWith == null) || (renderTargetToShareContextWith instanceof GlrRenderTarget);
    GlrOffscreenRenderTarget olg = new GlrOffscreenRenderTarget(this, width, height, (GlrRenderTarget) renderTargetToShareContextWith, requestedCapabilities);
    olg.addReleaseListener(this.releaseListener);
    this.offscreenLookingGlasses.add(olg);
    return olg;
  }

  @Override
  public void addAutomaticDisplayListener(AutomaticDisplayListener automaticDisplayListener) {
    this.automaticDisplayListeners.add(automaticDisplayListener);
  }

  @Override
  public void removeAutomaticDisplayListener(AutomaticDisplayListener automaticDisplayListener) {
    this.automaticDisplayListeners.remove(automaticDisplayListener);
  }

  private void handleDisplayed() {
    for (AutomaticDisplayListener automaticDisplayListener : this.automaticDisplayListeners) {
      automaticDisplayListener.automaticDisplayCompleted(reusableAutomaticDisplayEvent);
    }
    while (!this.runnables.isEmpty()) {
      Runnable runnable = this.runnables.remove();
      runnable.run();
    }
  }

  @Override
  public void invokeLater(Runnable runnable) {
    this.runnables.add(runnable);
  }

  private final ReleaseListener releaseListener = new ReleaseListener() {
    @Override
    public void releasing(ReleaseEvent releaseEvent) {
    }

    @Override
    public void released(ReleaseEvent releaseEvent) {
      synchronized (toBeReleased) {
        toBeReleased.add(releaseEvent.getTypedSource());
      }
    }
  };

  private final List<GlrOnscreenRenderTarget> onscreenLookingGlasses = Lists.newCopyOnWriteArrayList();
  private final List<GlrOffscreenRenderTarget> offscreenLookingGlasses = Lists.newCopyOnWriteArrayList();

  private final List<Releasable> toBeReleased = Lists.newLinkedList();
  private final Queue<Runnable> runnables = Queues.newConcurrentLinkedQueue();

  private final Semaphore renderingLock = new Semaphore(1);

  private static class ReusableAutomaticDisplayEvent extends AutomaticDisplayEvent {
    public ReusableAutomaticDisplayEvent(GlrRenderFactory lookingGlassFactory) {
      super(lookingGlassFactory);
    }

    @Override
    public boolean isReservedForReuse() {
      return true;
    }
  }

    private final ReusableAutomaticDisplayEvent reusableAutomaticDisplayEvent = new ReusableAutomaticDisplayEvent(this);
  private final List<AutomaticDisplayListener> automaticDisplayListeners = Lists.newCopyOnWriteArrayList();

  private Animator animator = null;
}
