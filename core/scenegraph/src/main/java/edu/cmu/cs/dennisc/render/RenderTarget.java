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
package edu.cmu.cs.dennisc.render;

/**
 * @author Dennis Cosgrove
 */
public interface RenderTarget {
	RenderFactory getRenderFactory();

	RenderCapabilities getRequestedCapabilities();

	RenderCapabilities getActualCapabilities();

	int getSurfaceWidth();

	int getSurfaceHeight();

	java.awt.Dimension getSurfaceSize();

	int getDrawableWidth();

	int getDrawableHeight();

	java.awt.Dimension getDrawableSize();

	String getDescription();

	void setDescription( String description );

	void addSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	void removeSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	void clearSgCameras();

	java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getSgCameras();

	edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSgCameraAt( int index );

	int getSgCameraCount();

	void addRenderTargetListener( edu.cmu.cs.dennisc.render.event.RenderTargetListener listener );

	void removeRenderTargetListener( edu.cmu.cs.dennisc.render.event.RenderTargetListener listener );

	java.util.List<edu.cmu.cs.dennisc.render.event.RenderTargetListener> getRenderTargetListeners();

	edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera );

	edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera sgFrustumPerspectiveCamera );

	edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera );

	edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera );

	edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel );

	edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel );

	boolean isLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	void setLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, boolean isLetterboxedAsOpposedToDistorted );

	boolean isRenderingEnabled();

	void setRenderingEnabled( boolean isRenderingEnabled );

	SynchronousPicker getSynchronousPicker();

	AsynchronousPicker getAsynchronousPicker();

	SynchronousImageCapturer getSynchronousImageCapturer();

	AsynchronousImageCapturer getAsynchronousImageCapturer();

	void forgetAllCachedItems();

	void clearUnusedTextures();

	//todo: remove?
	edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	//todo: remove?
	void release();

	edu.cmu.cs.dennisc.math.immutable.MRectangleI getActualViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	edu.cmu.cs.dennisc.math.immutable.MRectangleI getSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	void setSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, edu.cmu.cs.dennisc.math.immutable.MRectangleI viewport );

	@Deprecated
	java.awt.Rectangle getActualViewportAsAwtRectangle( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	@Deprecated
	java.awt.Rectangle getSpecifiedViewportAsAwtRectangle( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	@Deprecated
	void setSpecifiedViewportAsAwtRectangle( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle viewport );

	//todo: remove?
	@Deprecated
	java.awt.Rectangle getActualViewportAsAwtRectangle( java.awt.Rectangle rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
}
