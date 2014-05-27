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
package edu.cmu.cs.dennisc.renderer.gl;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlRenderTarget implements edu.cmu.cs.dennisc.renderer.RenderTarget {
	private javax.media.opengl.GLEventListener glEventListener = new javax.media.opengl.GLEventListener() {
		public void init( javax.media.opengl.GLAutoDrawable drawable ) {
		}

		public void reshape( javax.media.opengl.GLAutoDrawable drawable, int x, int y, int width, int height ) {
		}

		public void display( javax.media.opengl.GLAutoDrawable drawable ) {
			handleDisplay( drawable );
		}

		public void dispose( javax.media.opengl.GLAutoDrawable drawable ) {
		}
	};

	private final edu.cmu.cs.dennisc.renderer.gl.adapters.RenderContext rc = new edu.cmu.cs.dennisc.renderer.gl.adapters.RenderContext();

	private void handleDisplay( javax.media.opengl.GLAutoDrawable glDrawable ) {
		javax.media.opengl.GL gl = glDrawable.getGL();
		javax.media.opengl.GL2 gl2 = gl.getGL2();
		rc.gl = gl2;
		for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera : this.sgCameras ) {
			edu.cmu.cs.dennisc.renderer.gl.adapters.AdapterFactory.getAdapterFor( sgCamera ).performClearAndRenderOffscreen( rc, glDrawable.getWidth(), glDrawable.getHeight() );
		}
	}

	protected abstract javax.media.opengl.GLAutoDrawable getGlAutoDrawable();

	public int getWidth() {
		return this.getGlAutoDrawable().getWidth();
	}

	public int getHeight() {
		return this.getGlAutoDrawable().getHeight();
	}

	public void addSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		this.sgCameras.add( sgCamera );
		if( this.sgCameras.size() == 1 ) {
			this.getGlAutoDrawable().addGLEventListener( this.glEventListener );
		}
	}

	public void removeSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( ( this.sgCameras.size() == 1 ) && this.sgCameras.contains( sgCamera ) ) {
			this.getGlAutoDrawable().removeGLEventListener( this.glEventListener );
		}
		this.sgCameras.remove( sgCamera );
	}

	public void clearSgCameras() {
		if( this.sgCameras.size() > 0 ) {
			this.getGlAutoDrawable().removeGLEventListener( this.glEventListener );
		}
		this.sgCameras.clear();
	}

	public int getSgCameraCount() {
		return this.sgCameras.size();
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSgCameraAt( int index ) {
		return this.sgCameras.get( index );
	}

	public java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getSgCameras() {
		return java.util.Collections.unmodifiableList( this.sgCameras );
	}

	public void addRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.listeners.remove( listener );
	}

	public void removeRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.listeners.add( listener );
	}

	public java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> getRenderTargetListeners() {
		return java.util.Collections.unmodifiableList( this.listeners );
	}

	public void captureColorBuffer( edu.cmu.cs.dennisc.renderer.ColorBuffer colorBuffer, edu.cmu.cs.dennisc.renderer.Observer<edu.cmu.cs.dennisc.renderer.ColorBuffer> observer ) {
		throw new RuntimeException( "todo" );
	}

	public void captureColorBufferWithTransparencyBasedOnDepthBuffer( edu.cmu.cs.dennisc.renderer.ColorAndDepthBuffers colorAndDepthBuffers, edu.cmu.cs.dennisc.renderer.Observer<edu.cmu.cs.dennisc.renderer.ColorAndDepthBuffers> observer ) {
		throw new RuntimeException( "todo" );
	}

	public java.awt.Graphics2D createGraphics() {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera sgPerspectiveCamera ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		throw new RuntimeException( "todo" );
	}

	public java.awt.Rectangle getActualViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		throw new RuntimeException( "todo" );
	}

	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel ) {
		throw new RuntimeException( "todo" );
	}

	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		throw new RuntimeException( "todo" );
	}

	public java.awt.Rectangle getViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.mapSgCameraToViewport.get( sgCamera );
	}

	public void setViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle viewport ) {
		if( viewport != null ) {
			this.mapSgCameraToViewport.put( sgCamera, viewport );
		} else {
			if( this.mapSgCameraToViewport.containsKey( sgCamera ) ) {
				this.mapSgCameraToViewport.remove( sgCamera );
			}
		}
	}

	public void pickFrontMost( int xPixel, int yPixel, edu.cmu.cs.dennisc.renderer.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.renderer.VisualInclusionCriterion criterion, edu.cmu.cs.dennisc.renderer.PickFrontMostObserver observer ) {
		throw new RuntimeException( "todo" );
	}

	public void pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.renderer.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.renderer.VisualInclusionCriterion criterion, edu.cmu.cs.dennisc.renderer.PickAllObserver observer ) {
		throw new RuntimeException( "todo" );
	}

	public boolean isRenderingEnabled() {
		return this.isRenderingEnabled;
	}

	protected abstract void repaintIfAppropriate();

	public void setRenderingEnabled( boolean isRenderingEnabled ) {
		if( this.isRenderingEnabled != isRenderingEnabled ) {
			this.isRenderingEnabled = isRenderingEnabled;
			this.repaintIfAppropriate();
			//			//todo
			//			if( m_isRenderingEnabled ) {
			//				if( m_glEventAdapter.isListening() ) {
			//					//pass
			//				} else {
			//					m_glEventAdapter.startListening( getGLAutoDrawable() );
			//				}
			//			} else {
			//				if( m_glEventAdapter.isListening() ) {
			//					m_glEventAdapter.stopListening( getGLAutoDrawable() );
			//				} else {
			//					//pass
			//				}
			//			}
		}
	}

	private final java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> listeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> sgCameras = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.Map<edu.cmu.cs.dennisc.scenegraph.AbstractCamera, java.awt.Rectangle> mapSgCameraToViewport = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private boolean isRenderingEnabled = true;
}
