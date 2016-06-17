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

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;

/**
 * @author Dennis Cosgrove
 */
public class FieldIcon extends edu.cmu.cs.dennisc.javax.swing.AsynchronousIcon {
	public FieldIcon( org.lgna.project.ast.UserField field, javax.swing.Icon fallbackIcon ) {
		this.field = field;
		this.fallbackIcon = fallbackIcon;
	}

	public void markDirty() {
		this.imageIcon = null;
		this.isStarted = false;
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

			java.awt.image.BufferedImage newIconImage = new java.awt.image.BufferedImage( this.getIconWidthFallback(), this.getIconHeightFallback(), java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
			java.awt.Image iconImage = GraphicsUtilities.getImageForIcon( this.fallbackIcon );
			GraphicsUtilities.drawCenteredScaledToFitImage( iconImage, newIconImage );
			this.imageIcon = new javax.swing.ImageIcon( newIconImage );

			//			if( this.isStarted ) {
			//				//pass
			//			} else {
			//				this.isStarted = true;
			//
			//				java.awt.Rectangle viewport = new java.awt.Rectangle( 0, 0, this.getIconWidth(), this.getIconHeight() );
			//
			//				edu.cmu.cs.dennisc.color.Color4f backgroundColor = true ? null : edu.cmu.cs.dennisc.color.Color4f.WHITE;
			//				final edu.cmu.cs.dennisc.render.ImageBuffer rImageBuffer = edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().createImageBuffer( backgroundColor );
			//				org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor();
			//
			//				org.lgna.story.implementation.AbstractTransformableImp fieldImp = sceneEditor.getImplementation( field );
			//
			//				final edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable = fieldImp.getSgComposite();
			//				final edu.cmu.cs.dennisc.math.AffineMatrix4x4 absoluteTransform = sgTransformable.getAbsoluteTransformation();
			//				final edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = fieldImp.getAxisAlignedMinimumBoundingBox();
			//				edu.cmu.cs.dennisc.math.Point3 center = bbox.getCenter();
			//				absoluteTransform.transform( center );
			//				final edu.cmu.cs.dennisc.math.Point3 p = center;
			//
			//				org.lgna.story.implementation.SceneImp sceneImp = sceneEditor.getActiveSceneImplementation();
			//				final edu.cmu.cs.dennisc.scenegraph.Scene sgScene = sceneImp.getSgComposite();
			//
			//				final edu.cmu.cs.dennisc.scenegraph.Visual sgVisual;
			//				if( fieldImp instanceof org.lgna.story.implementation.SingleVisualModelImp ) {
			//					org.lgna.story.implementation.SingleVisualModelImp singleVisualModelImp = (org.lgna.story.implementation.SingleVisualModelImp)fieldImp;
			//					sgVisual = singleVisualModelImp.getSgVisuals()[ 0 ];
			//				} else {
			//					edu.cmu.cs.dennisc.scenegraph.Visual sgFoundVisual = null;
			//					for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : sgTransformable.getComponents() ) {
			//						if( sgComponent instanceof edu.cmu.cs.dennisc.scenegraph.Visual ) {
			//							sgFoundVisual = (edu.cmu.cs.dennisc.scenegraph.Visual)sgComponent;
			//							break;
			//						}
			//					}
			//					sgVisual = sgFoundVisual;
			//				}
			//
			//				edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> renderTarget = sceneEditor.getOnscreenRenderTarget();
			//
			//				final edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = renderTarget.getSgCameraAt( 0 );
			//				renderTarget.getAsynchronousImageCapturer().captureImageBuffer( new edu.cmu.cs.dennisc.render.RenderTask() {
			//					@Override
			//					public void render( Object context ) {
			//						edu.cmu.cs.dennisc.render.gl.imp.GlrRenderContext glrRenderContext = (edu.cmu.cs.dennisc.render.gl.imp.GlrRenderContext)context;
			//						com.jogamp.opengl.GL2 gl = glrRenderContext.getDrawable().getGL().getGL2();
			//						java.awt.Rectangle viewport = glrRenderContext.getViewport();
			//						edu.cmu.cs.dennisc.color.Color4f backgroundColor = rImageBuffer.getBackgroundColor();
			//						final boolean IS_ALPHA = backgroundColor == null;
			//						int clearMask;
			//						if( IS_ALPHA ) {
			//							clearMask = com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
			//						} else {
			//							gl.glClearColor( backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha );
			//							clearMask = com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT | com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
			//						}
			//						gl.glEnable( com.jogamp.opengl.GL.GL_SCISSOR_TEST );
			//						gl.glScissor( viewport.x, viewport.y, viewport.width, viewport.height );
			//						gl.glClear( clearMask );
			//						gl.glDisable( com.jogamp.opengl.GL.GL_SCISSOR_TEST );
			//
			//						gl.glViewport( viewport.x, viewport.y, viewport.width, viewport.height );
			//
			//						com.jogamp.opengl.glu.GLU glu = new com.jogamp.opengl.glu.GLU();
			//						gl.glMatrixMode( com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION );
			//						gl.glLoadIdentity();
			//
			//						edu.cmu.cs.dennisc.math.Angle verticalViewingAngle = new edu.cmu.cs.dennisc.math.AngleInRadians( 0.5 );
			//						double aspectRatio = viewport.width / (double)viewport.height;
			//						glu.gluPerspective( verticalViewingAngle.getAsDegrees(), aspectRatio, 0.1, 100.0 );
			//
			//						edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractTransformable<?> transformableAdapter = edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.getAdapterFor( sgTransformable );
			//						edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc = new edu.cmu.cs.dennisc.render.gl.imp.RenderContext();
			//						rc.setGL( gl );
			//						rc.initialize();
			//
			//						gl.glMatrixMode( com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW );
			//
			//						double distance;
			//						if( sgVisual != null ) {
			//							distance = edu.cmu.cs.dennisc.scenegraph.util.GoodLookAtUtils.calculateGoodLookAtDistance( sgVisual, verticalViewingAngle, aspectRatio, sgCamera );
			//						} else {
			//							distance = edu.cmu.cs.dennisc.scenegraph.util.GoodLookAtUtils.calculateGoodLookAtDistance( bbox, absoluteTransform, verticalViewingAngle, aspectRatio, sgCamera );
			//						}
			//						//m = null;
			//						if( Double.isNaN( distance ) == false ) {
			//							//									double[] array = new double[ 16 ];
			//							//									java.nio.DoubleBuffer buffer = java.nio.DoubleBuffer.wrap( array );
			//							//									m.getAsColumnMajorArray16( array );
			//							//									gl.glLoadMatrixd( buffer );
			//
			//							edu.cmu.cs.dennisc.math.AffineMatrix4x4 cameraAbsolute = sgCamera.getAbsoluteTransformation();
			//
			//							edu.cmu.cs.dennisc.math.Vector3 v = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( cameraAbsolute.translation, p );
			//							v.normalize();
			//							v.multiply( distance );
			//							v.add( p );
			//
			//							gl.glLoadIdentity();
			//							glu.gluLookAt( v.x, v.y, v.z, p.x, p.y, p.z, cameraAbsolute.orientation.up.x, cameraAbsolute.orientation.up.y, cameraAbsolute.orientation.up.z );
			//						} else {
			//							gl.glLoadIdentity();
			//							glu.gluLookAt( p.x + 8, p.y + 8, p.z - 8, p.x, p.y, p.z, 0, 1, 0 );
			//						}
			//
			//						edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene sceneAdapter = edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.getAdapterFor( sgScene );
			//						sceneAdapter.setupAffectors( rc );
			//						gl.glEnable( com.jogamp.opengl.GL.GL_DEPTH_TEST );
			//
			//						//todo: support alpha
			//						//							gl.glDisable( com.jogamp.opengl.GL.GL_BLEND );
			//						transformableAdapter.EPIC_HACK_FOR_ICON_CAPTURE_renderOpaque( rc );
			//						//							gl.glEnable( com.jogamp.opengl.GL.GL_BLEND );
			//						//							gl.glBlendFunc( com.jogamp.opengl.GL.GL_SRC_ALPHA, com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA );
			//						//							transformableAdapter.renderAlphaBlended( rc );
			//						//							gl.glDisable( com.jogamp.opengl.GL.GL_BLEND );
			//						gl.glDisable( com.jogamp.opengl.GL.GL_DEPTH_TEST );
			//					}
			//				}, viewport, rImageBuffer, edu.cmu.cs.dennisc.render.ImageOrientationRequirement.RIGHT_SIDE_UP_REQUIRED, new edu.cmu.cs.dennisc.render.ImageCaptureObserver() {
			//					@Override
			//					public void done( edu.cmu.cs.dennisc.render.ImageBuffer result ) {
			//						imageIcon = new javax.swing.ImageIcon( result.getImage() );
			//						repaintComponentsIfNecessary();
			//						//edu.cmu.cs.dennisc.java.util.logging.Logger.severe( result );
			//					}
			//				} );
			//			}
		}
		return this.imageIcon;
	}

	private final org.lgna.project.ast.UserField field;
	private final javax.swing.Icon fallbackIcon;
	private javax.swing.ImageIcon imageIcon;
	private boolean isStarted;
}
