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
package edu.cmu.cs.dennisc.render.nil;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/abstract class NrRenderTarget implements edu.cmu.cs.dennisc.render.RenderTarget {
	public NrRenderTarget( edu.cmu.cs.dennisc.render.RenderCapabilities requestedCapabilities ) {
		this.requestedCapabilities = requestedCapabilities;
		this.actualCapabilities = new edu.cmu.cs.dennisc.render.RenderCapabilities.Builder().build();
	}

	@Override
	public edu.cmu.cs.dennisc.render.RenderCapabilities getRequestedCapabilities() {
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.render.RenderCapabilities getActualCapabilities() {
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.render.RenderFactory getRenderFactory() {
		return NilRenderFactory.INSTANCE;
	}

	@Override
	public int getSurfaceWidth() {
		return this.getSurfaceSize().width;
	}

	@Override
	public int getSurfaceHeight() {
		return this.getSurfaceSize().height;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription( String description ) {
		this.description = description;
	}

	@Override
	public void addSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		this.sgCameras.add( sgCamera );
	}

	@Override
	public void removeSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		this.sgCameras.remove( sgCamera );
	}

	@Override
	public void clearSgCameras() {
		this.sgCameras.clear();
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getSgCameras() {
		return java.util.Collections.unmodifiableList( this.sgCameras );
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSgCameraAt( int index ) {
		return this.sgCameras.get( index );
	}

	@Override
	public int getSgCameraCount() {
		return this.sgCameras.size();
	}

	@Override
	public void addRenderTargetListener( edu.cmu.cs.dennisc.render.event.RenderTargetListener listener ) {
		this.renderTargetListeners.add( listener );
	}

	@Override
	public void removeRenderTargetListener( edu.cmu.cs.dennisc.render.event.RenderTargetListener listener ) {
		this.renderTargetListeners.remove( listener );
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.render.event.RenderTargetListener> getRenderTargetListeners() {
		return java.util.Collections.unmodifiableList( this.renderTargetListeners );
	}

	@Override
	public boolean isRenderingEnabled() {
		return this.isRenderingEnabled;
	}

	@Override
	public void setRenderingEnabled( boolean isRenderingEnabled ) {
		this.isRenderingEnabled = isRenderingEnabled;
	}

	@Override
	public edu.cmu.cs.dennisc.render.SynchronousPicker getSynchronousPicker() {
		return this.synchronousPicker;
	}

	@Override
	public edu.cmu.cs.dennisc.render.AsynchronousPicker getAsynchronousPicker() {
		return this.asynchronousPicker;
	}

	@Override
	public edu.cmu.cs.dennisc.render.SynchronousImageCapturer getSynchronousImageCapturer() {
		return this.synchronousImageCapturer;
	}

	@Override
	public edu.cmu.cs.dennisc.render.AsynchronousImageCapturer getAsynchronousImageCapturer() {
		return this.asynchronousImageCapturer;
	}

	@Override
	public edu.cmu.cs.dennisc.math.immutable.MRectangleI getActualViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.toMRectangleI( this.getActualViewportAsAwtRectangle( sgCamera ) );
	}

	@Override
	public edu.cmu.cs.dennisc.math.immutable.MRectangleI getSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.toMRectangleI( this.getSpecifiedViewportAsAwtRectangle( sgCamera ) );
	}

	@Override
	public void setSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, edu.cmu.cs.dennisc.math.immutable.MRectangleI viewport ) {
		this.setSpecifiedViewportAsAwtRectangle( sgCamera, edu.cmu.cs.dennisc.java.awt.RectangleUtilities.toAwtRectangle( viewport ) );
	}

	@Override
	public java.awt.Rectangle getActualViewportAsAwtRectangle( java.awt.Rectangle rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		java.awt.Rectangle viewport = this.getSpecifiedViewportAsAwtRectangle( sgCamera );
		if( viewport != null ) {
			rv.setBounds( viewport );
		} else {
			java.awt.Dimension surfaceSize = this.getSurfaceSize();
			//todo: isLetterboxedAsOpposedToDistorted?
			rv.setBounds( 0, 0, surfaceSize.width, surfaceSize.height );
		}
		return rv;
	}

	@Override
	public final java.awt.Rectangle getActualViewportAsAwtRectangle( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.getActualViewportAsAwtRectangle( new java.awt.Rectangle(), sgCamera );
	}

	@Override
	public java.awt.Rectangle getSpecifiedViewportAsAwtRectangle( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.mapCameraToViewport.get( sgCamera );
	}

	@Override
	public void setSpecifiedViewportAsAwtRectangle( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle viewport ) {
		this.mapCameraToViewport.put( sgCamera, viewport );
	}

	@Override
	public boolean isLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		Boolean rv = this.mapCameraToIsLetterboxed.get( sgCamera );
		if( rv != null ) {
			return rv;
		} else {
			return true;
		}
	}

	@Override
	public void setLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, boolean isLetterboxedAsOpposedToDistorted ) {
		this.mapCameraToIsLetterboxed.put( sgCamera, isLetterboxedAsOpposedToDistorted );
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel ) {
		//todo
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		//todo
		return null;
	}

	@Override
	public final edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel ) {
		return this.getRayAtPixel( xPixel, yPixel, this.getCameraAtPixel( xPixel, yPixel ) );
	}

	@Override
	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera ) {
		//todo
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera sgFrustumPerspectiveCamera ) {
		//todo
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		//todo
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		//todo
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		//todo
		return rv;
	}

	@Override
	public final edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4.createNaN(), sgCamera );
	}

	@Override
	public void forgetAllCachedItems() {
	}

	@Override
	public void clearUnusedTextures() {
	}

	@Override
	public void release() {
	}

	private final edu.cmu.cs.dennisc.render.RenderCapabilities requestedCapabilities;
	private final edu.cmu.cs.dennisc.render.RenderCapabilities actualCapabilities;

	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> sgCameras = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.render.event.RenderTargetListener> renderTargetListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private final java.util.Map<edu.cmu.cs.dennisc.scenegraph.AbstractCamera, Boolean> mapCameraToIsLetterboxed = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<edu.cmu.cs.dennisc.scenegraph.AbstractCamera, java.awt.Rectangle> mapCameraToViewport = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private final NrSynchronousPicker synchronousPicker = new NrSynchronousPicker();
	private final NrAsynchronousPicker asynchronousPicker = new NrAsynchronousPicker();
	private final NrSynchronousImageCapturer synchronousImageCapturer = new NrSynchronousImageCapturer();
	private final NrAsynchronousImageCapturer asynchronousImageCapturer = new NrAsynchronousImageCapturer();
	private String description;
	private boolean isRenderingEnabled = true;
}
