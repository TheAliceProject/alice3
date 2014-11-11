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
package org.alice.stageide.iconfactory;

/**
 * @author Dennis Cosgrove
 */
public class FieldIcon extends edu.cmu.cs.dennisc.javax.swing.AsynchronousIcon {
	public FieldIcon( org.lgna.project.ast.UserField field, javax.swing.Icon fallbackIcon ) {
		this.field = field;
		this.fallbackIcon = fallbackIcon;
	}

	@Override
	protected int getIconWidthFallback() {
		return this.fallbackIcon.getIconWidth();
	}

	@Override
	protected int getIconHeightFallback() {
		return this.fallbackIcon.getIconHeight();
	}

	@Override
	protected void paintIconFallback( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		this.fallbackIcon.paintIcon( c, g, x, y );
	}

	@Override
	protected synchronized javax.swing.Icon getResult( boolean isPaint ) {
		if( this.imageIcon != null ) {
			//pass
		} else {
			if( this.isStarted ) {
				//pass
			} else {
				this.isStarted = true;
				java.awt.Rectangle viewport = new java.awt.Rectangle( 0, 0, this.getIconWidth(), this.getIconHeight() );

				edu.cmu.cs.dennisc.color.Color4f backgroundColor = true ? null : edu.cmu.cs.dennisc.color.Color4f.WHITE;
				final edu.cmu.cs.dennisc.render.ImageBuffer rImageBuffer = edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().createImageBuffer( backgroundColor );
				org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor();

				org.lgna.story.implementation.AbstractTransformableImp fieldImp = sceneEditor.getImplementation( field );

				final edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable = fieldImp.getSgComposite();
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgTransformable.getAbsoluteTransformation();
				edu.cmu.cs.dennisc.math.Point3 center = fieldImp.getAxisAlignedMinimumBoundingBox().getCenter();
				m.transform( center );
				final edu.cmu.cs.dennisc.math.Point3 p = center;

				org.lgna.story.implementation.SceneImp sceneImp = sceneEditor.getActiveSceneImplementation();
				final edu.cmu.cs.dennisc.scenegraph.Scene sgScene = sceneImp.getSgComposite();

				edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> renderTarget = sceneEditor.getOnscreenRenderTarget();
				renderTarget.getAsynchronousImageCapturer().captureImageBuffer(
						new edu.cmu.cs.dennisc.render.RenderTask() {
							@Override
							public void render( Object context ) {
								edu.cmu.cs.dennisc.render.gl.imp.GlrRenderContext glrRenderContext = (edu.cmu.cs.dennisc.render.gl.imp.GlrRenderContext)context;
								javax.media.opengl.GL2 gl = glrRenderContext.getDrawable().getGL().getGL2();
								java.awt.Rectangle viewport = glrRenderContext.getViewport();
								edu.cmu.cs.dennisc.color.Color4f backgroundColor = rImageBuffer.getBackgroundColor();
								final boolean IS_ALPHA = backgroundColor == null;
								int clearMask;
								if( IS_ALPHA ) {
									clearMask = javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
								} else {
									gl.glClearColor( backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha );
									clearMask = javax.media.opengl.GL.GL_COLOR_BUFFER_BIT | javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
								}
								gl.glEnable( javax.media.opengl.GL.GL_SCISSOR_TEST );
								gl.glScissor( viewport.x, viewport.y, viewport.width, viewport.height );
								gl.glClear( clearMask );
								gl.glDisable( javax.media.opengl.GL.GL_SCISSOR_TEST );

								gl.glViewport( viewport.x, viewport.y, viewport.width, viewport.height );

								javax.media.opengl.glu.GLU glu = new javax.media.opengl.glu.GLU();
								gl.glMatrixMode( javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION );
								gl.glLoadIdentity();

								glu.gluPerspective( 45.0, viewport.width / (double)viewport.height, 0.1, 100.0 );

								edu.cmu.cs.dennisc.render.gl.imp.adapters.AbstractTransformableAdapter<?> transformableAdapter = edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory.getAdapterFor( sgTransformable );
								edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc = new edu.cmu.cs.dennisc.render.gl.imp.RenderContext();
								rc.setGL( gl );
								rc.initialize();

								gl.glMatrixMode( javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW );
								gl.glLoadIdentity();
								glu.gluLookAt( p.x + 1, p.y + 1, p.z - 1, p.x, p.y, p.z, 0, 1, 0 );

								edu.cmu.cs.dennisc.render.gl.imp.adapters.SceneAdapter sceneAdapter = edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory.getAdapterFor( sgScene );
								sceneAdapter.setup( rc );
								gl.glEnable( javax.media.opengl.GL.GL_DEPTH_TEST );

								//todo: support alpha
								//							gl.glDisable( javax.media.opengl.GL.GL_BLEND );
								transformableAdapter.renderOpaque( rc );
								//							gl.glEnable( javax.media.opengl.GL.GL_BLEND );
								//							gl.glBlendFunc( javax.media.opengl.GL.GL_SRC_ALPHA, javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA );
								//							transformableAdapter.renderAlphaBlended( rc );
								//							gl.glDisable( javax.media.opengl.GL.GL_BLEND );
								gl.glDisable( javax.media.opengl.GL.GL_DEPTH_TEST );
							}
						},
						viewport,
						rImageBuffer,
						edu.cmu.cs.dennisc.render.ImageOrientationRequirement.RIGHT_SIDE_UP_REQUIRED,
						new edu.cmu.cs.dennisc.render.ImageCaptureObserver() {
							@Override
							public void done( edu.cmu.cs.dennisc.render.ImageBuffer result ) {
								imageIcon = new javax.swing.ImageIcon( result.getImage() );
								repaintComponentsIfNecessary();
								//edu.cmu.cs.dennisc.java.util.logging.Logger.severe( result );
							}
						} );
			}
		}
		return this.imageIcon;
	}

	private final org.lgna.project.ast.UserField field;
	private final javax.swing.Icon fallbackIcon;
	private javax.swing.ImageIcon imageIcon;
	private boolean isStarted;
}
