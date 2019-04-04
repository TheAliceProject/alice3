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
package test.iconrender;

/**
 * @author Dennis Cosgrove
 */
public class IconRenderTest {
  public static void main(final String[] args) {
    org.lgna.story.SSphere sphere = new org.lgna.story.SSphere();
    org.lgna.story.SCone cone = new org.lgna.story.SCone();

    cone.move(org.lgna.story.MoveDirection.LEFT, 2.0);

    sphere.setPaint(org.lgna.story.Color.YELLOW);
    cone.setPaint(org.lgna.story.Color.GREEN);

    final test.story.TestScene scene = new test.story.TestScene(sphere, cone);

    final org.lgna.story.SProgram program = new org.lgna.story.SProgram();
    program.initializeInFrame(args);

    org.lgna.story.implementation.SphereImp sphereImp = org.lgna.story.EmployeesOnly.getImplementation(sphere);
    org.lgna.story.implementation.ConeImp coneImp = org.lgna.story.EmployeesOnly.getImplementation(cone);
    final edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = sphereImp.getSgComposite();
    org.lgna.story.implementation.SceneImp sceneImp = org.lgna.story.EmployeesOnly.getImplementation(scene);
    final edu.cmu.cs.dennisc.scenegraph.Scene sgScene = sceneImp.getSgComposite();

    sceneImp.setGlobalLightBrightnessAnimationDesired(false);

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {

        program.setActiveScene(scene);

        edu.cmu.cs.dennisc.render.RenderFactory renderFactory = edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory();

        final edu.cmu.cs.dennisc.render.RenderTarget renderTarget;
        final boolean IS_USING_OFFSCREEN_RENDER_TARGET = false;

        if (IS_USING_OFFSCREEN_RENDER_TARGET) {
          java.awt.Dimension size = new java.awt.Dimension(512, 512);
          renderTarget = renderFactory.createOffscreenRenderTarget(size.width, size.height, null, new edu.cmu.cs.dennisc.render.RenderCapabilities.Builder().build());
          org.lgna.story.implementation.CameraImp<?> impCamera = org.lgna.story.EmployeesOnly.getImplementation(scene.getCamera());
          renderTarget.addSgCamera(impCamera.getSgCamera());
        } else {
          org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.EmployeesOnly.getImplementation(program);
          renderTarget = programImp.getOnscreenRenderTarget();
        }

        final javax.swing.JFrame frame = new javax.swing.JFrame();
        final javax.swing.JLabel label = new javax.swing.JLabel();

        label.setOpaque(true);
        label.setBackground(java.awt.Color.RED);

        final boolean IS_ASYNC = true;
        javax.swing.Icon icon;
        if (IS_ASYNC) {
          java.awt.Dimension surfaceSize = renderTarget.getSurfaceSize();
          surfaceSize.width /= 2;
          surfaceSize.height /= 2;
          icon = org.alice.stageide.icons.TorusIconFactory.getInstance().getIcon(surfaceSize);
          label.setIcon(icon);

          edu.cmu.cs.dennisc.color.Color4f backgroundColor = true ? null : edu.cmu.cs.dennisc.color.Color4f.WHITE;
          final edu.cmu.cs.dennisc.render.ImageBuffer rImageBuffer = edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().createImageBuffer(backgroundColor);
          renderTarget.getAsynchronousImageCapturer().captureImageBuffer(new edu.cmu.cs.dennisc.render.RenderTask() {
            @Override
            public void render(Object context) {
              edu.cmu.cs.dennisc.render.gl.imp.GlrRenderContext glrRenderContext = (edu.cmu.cs.dennisc.render.gl.imp.GlrRenderContext) context;
              com.jogamp.opengl.GL2 gl = glrRenderContext.getDrawable().getGL().getGL2();
              java.awt.Rectangle viewport = glrRenderContext.getViewport();
              edu.cmu.cs.dennisc.color.Color4f backgroundColor = rImageBuffer.getBackgroundColor();
              final boolean IS_ALPHA = backgroundColor == null;
              int clearMask;
              if (IS_ALPHA) {
                clearMask = com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
              } else {
                gl.glClearColor(backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha);
                clearMask = com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT | com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
              }
              gl.glEnable(com.jogamp.opengl.GL.GL_SCISSOR_TEST);
              gl.glScissor(viewport.x, viewport.y, viewport.width, viewport.height);
              gl.glClear(clearMask);
              gl.glDisable(com.jogamp.opengl.GL.GL_SCISSOR_TEST);

              gl.glViewport(viewport.x, viewport.y, viewport.width, viewport.height);

              com.jogamp.opengl.glu.GLU glu = new com.jogamp.opengl.glu.GLU();
              gl.glMatrixMode(com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION);
              gl.glLoadIdentity();

              glu.gluPerspective(45.0, viewport.width / (double) viewport.height, 0.1, 100.0);

              edu.cmu.cs.dennisc.math.Point3 p = sgTransformable.getAbsoluteTransformation().translation;

              edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrTransformable<?> transformableAdapter = edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.getAdapterFor(sgTransformable);
              edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc = new edu.cmu.cs.dennisc.render.gl.imp.RenderContext();
              rc.setGL(gl);
              rc.initialize();

              gl.glMatrixMode(com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW);
              gl.glLoadIdentity();
              glu.gluLookAt(p.x + 2, p.y + 2, p.z - 2, p.x, p.y, p.z, 0, 1, 0);

              edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene sceneAdapter = edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.getAdapterFor(sgScene);
              sceneAdapter.setupAffectors(rc);
              gl.glEnable(com.jogamp.opengl.GL.GL_DEPTH_TEST);
              //              gl.glDisable( com.jogamp.opengl.GL.GL_BLEND );
              transformableAdapter.renderOpaque(rc);
              //              gl.glEnable( com.jogamp.opengl.GL.GL_BLEND );
              //              gl.glBlendFunc( com.jogamp.opengl.GL.GL_SRC_ALPHA, com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA );
              //              transformableAdapter.renderAlphaBlended( rc );
              //              gl.glDisable( com.jogamp.opengl.GL.GL_BLEND );
              gl.glDisable(com.jogamp.opengl.GL.GL_DEPTH_TEST);
            }
          }, new java.awt.Rectangle(surfaceSize.width, surfaceSize.height, surfaceSize.width, surfaceSize.height), rImageBuffer, edu.cmu.cs.dennisc.render.ImageOrientationRequirement.RIGHT_SIDE_UP_REQUIRED, new edu.cmu.cs.dennisc.render.ImageCaptureObserver() {
            @Override
            public void done(edu.cmu.cs.dennisc.render.ImageBuffer result) {
              //edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 2000 );
              label.setIcon(new javax.swing.ImageIcon(result.getImage()));
              frame.pack();
            }
          });
        } else {
          java.awt.image.BufferedImage image = renderTarget.getSynchronousImageCapturer().getColorBufferWithTransparencyBasedOnDepthBuffer();
          icon = new javax.swing.ImageIcon(image);
          label.setIcon(icon);
        }

        frame.getContentPane().add(label);
        frame.setLocation(1000, 0);
        frame.pack();
        frame.setVisible(true);
      }
    });
  }
}
