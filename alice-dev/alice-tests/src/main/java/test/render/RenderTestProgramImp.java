/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package test.render;

/**
 * @author Dennis Cosgrove
 */
public class RenderTestProgramImp extends org.lgna.story.implementation.ProgramImp {
  private final edu.cmu.cs.dennisc.animation.ClockBasedAnimator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();

  public RenderTestProgramImp(org.lgna.story.SProgram abstraction) {
    super(abstraction, edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().createHeavyweightOnscreenRenderTarget(new edu.cmu.cs.dennisc.render.RenderCapabilities.Builder().build()));
    java.awt.Component awtComponent = this.getOnscreenRenderTarget().getAwtComponent();
    awtComponent.addMouseListener(new java.awt.event.MouseListener() {

      @Override
      public void mouseClicked(java.awt.event.MouseEvent e) {
      }

      @Override
      public void mousePressed(java.awt.event.MouseEvent e) {
        handleMousePressed(e);
      }

      @Override
      public void mouseReleased(java.awt.event.MouseEvent e) {
      }

      @Override
      public void mouseEntered(java.awt.event.MouseEvent e) {
      }

      @Override
      public void mouseExited(java.awt.event.MouseEvent e) {
      }

    });
  }

  @Override
  public edu.cmu.cs.dennisc.animation.ClockBasedAnimator getAnimator() {
    return this.animator;
  }

  @Override
  public boolean isControlPanelDesired() {
    return false;
  }

  private void handleMousePressed(java.awt.event.MouseEvent e) {
    java.awt.Component awtComponent = e.getComponent();
    try {
      edu.cmu.cs.dennisc.render.PickResult pickResult = this.getOnscreenRenderTarget().getSynchronousPicker().pickFrontMost(e.getX(), e.getY(), edu.cmu.cs.dennisc.render.PickSubElementPolicy.NOT_REQUIRED);
      edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
      edu.cmu.cs.dennisc.scenegraph.Composite sgComposite;
      if (sgVisual != null) {
        sgComposite = sgVisual.getParent();
      } else {
        sgComposite = null;
      }
      org.lgna.story.SThing thing = org.lgna.story.implementation.EntityImp.getAbstractionFromSgElement(sgComposite);
      if (thing != null) {
        javax.swing.JOptionPane.showMessageDialog(awtComponent, "success");
      } else {
        javax.swing.JOptionPane.showMessageDialog(awtComponent, "did you click on the sphere?");
      }
    } catch (com.jogamp.opengl.GLException gle) {
      javax.swing.JOptionPane.showMessageDialog(awtComponent, gle);
    } catch (Throwable t) {
      javax.swing.JOptionPane.showMessageDialog(awtComponent, t);
    }
  }
}
