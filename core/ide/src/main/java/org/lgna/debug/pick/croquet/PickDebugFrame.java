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
package org.lgna.debug.pick.croquet;

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.PickFrontMostObserver;
import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.Operation;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.debug.pick.croquet.views.PickDebugFrameView;

import java.awt.Component;
import java.awt.Cursor;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author Dennis Cosgrove
 */
public final class PickDebugFrame extends FrameComposite<PickDebugFrameView> {
  public PickDebugFrame() {
    super(UUID.fromString("946cd0cd-8b61-4b57-b398-c926e8c6a343"));
    this.refreshOperation.setName("refresh");
  }

  public Operation getRefreshOperation() {
    return this.refreshOperation;
  }

  //  @Override
  //  public void handlePreActivation() {
  //    super.handlePreActivation();
  //    this.refreshOperation.fire();
  //  }

  @Override
  protected PickDebugFrameView createView() {
    return new PickDebugFrameView(this);
  }

  private final Operation refreshOperation = this.createActionOperation("refreshOperation", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      Component awtComponent = getView().getAwtComponent(); //org.alice.stageide.StageIDE.getActiveInstance().getDocumentFrame().getFrame().getContentPane().getAwtComponent();
      CursorUtilities.pushAndSet(awtComponent, Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      try {
        final OnscreenRenderTarget onscreenRenderTarget = StageIDE.getActiveInstance().getSceneEditor().getOnscreenRenderTarget();
        final int PIXELS_PER_PICK = 10;
        final PickResult[][] pickResults = new PickResult[onscreenRenderTarget.getSurfaceHeight() / PIXELS_PER_PICK][onscreenRenderTarget.getSurfaceWidth() / PIXELS_PER_PICK];
        final boolean IS_ASYCH = false;
        if (IS_ASYCH) {
          final CountDownLatch latch = new CountDownLatch(pickResults.length * pickResults[0].length);
          for (int y = 0; y < pickResults.length; y++) {
            int yPixel = y * PIXELS_PER_PICK;
            final int _y = y;
            for (int x = 0; x < pickResults[y].length; x++) {
              int xPixel = x * PIXELS_PER_PICK;
              //pickResults[ y ][ x ] = onscreenRenderTarget.getSynchronousPicker().pickFrontMost( xPixel, yPixel, edu.cmu.cs.dennisc.render.PickSubElementPolicy.NOT_REQUIRED );
              final int _x = x;
              onscreenRenderTarget.getAsynchronousPicker().pickFrontMost(xPixel, yPixel, PickSubElementPolicy.NOT_REQUIRED, null, new PickFrontMostObserver() {
                @Override
                public void done(PickResult result) {
                  pickResults[_y][_x] = result;
                  latch.countDown();
                }
              });
            }
          }
          try {
            latch.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {
          for (int y = 0; y < pickResults.length; y++) {
            int yPixel = y * PIXELS_PER_PICK;
            for (int x = 0; x < pickResults[y].length; x++) {
              int xPixel = x * PIXELS_PER_PICK;
              pickResults[y][x] = onscreenRenderTarget.getSynchronousPicker().pickFrontMost(xPixel, yPixel, PickSubElementPolicy.NOT_REQUIRED);
            }
          }
        }
        getView().setPickResults(pickResults);
      } finally {
        CursorUtilities.popAndSet(awtComponent);
      }
      return null;
    }
  });
}
