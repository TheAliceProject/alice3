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
package org.alice.ide.icons;

import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import edu.cmu.cs.dennisc.worker.Worker;

import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * @author Dennis Cosgrove
 */
public class UrlAsynchronousIcon extends AsynchronousIcon {
  protected final URL url;

  private class IconWorker extends Worker<Icon> {
    @Override
    protected Icon do_onBackgroundThread() throws Exception {
      return UrlAsynchronousIcon.this.do_onBackgroundThread();
    }

    @Override
    protected void handleDone_onEventDispatchThread(Icon value) {
      UrlAsynchronousIcon.this.handleDone_onEventDispatchThread(value);
    }
  }

  public UrlAsynchronousIcon(int iconWidthFallback, int iconHeightFallback, URL url) {
    this.iconWidthFallback = iconWidthFallback;
    this.iconHeightFallback = iconHeightFallback;
    this.url = url;
  }

  @Override
  protected int getIconWidthFallback() {
    return this.iconWidthFallback;
  }

  @Override
  protected int getIconHeightFallback() {
    return this.iconHeightFallback;
  }

  private Icon getIconFromDoneWorker() {
    try {
      return this.worker.get_obviouslyLockingCurrentThreadUntilDone();
    } catch (InterruptedException ie) {
      throw new Error(ie);
    } catch (ExecutionException ee) {
      throw new Error(ee);
    }
  }

  @Override
  protected Icon getResult(boolean isPaint) {
    if (this.worker != null) {
      if (this.worker.isDone()) {
        return this.getIconFromDoneWorker();
      } else {
        return null;
      }
    } else {
      if (isPaint) {
        this.worker = new IconWorker();
        this.worker.execute();
        if (this.worker.isDone()) {
          return this.getIconFromDoneWorker();
        }
      }
      return null;
    }
  }

  @Override
  protected void paintIconFallback(Component c, Graphics g, int x, int y) {
    if (c.isOpaque()) {
      g.setColor(c.getBackground());
      g.fillRect(x, y, this.getIconWidthFallback(), this.getIconHeightFallback());
    }
  }

  protected Icon do_onBackgroundThread() throws Exception {
    return IconUtilities.createImageIcon(this.url);
  }

  private void handleDone_onEventDispatchThread(Icon value) {
    this.repaintComponentsIfNecessary();
  }

  private final int iconWidthFallback;
  private final int iconHeightFallback;
  private IconWorker worker;
}
