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
package org.lgna.ik.poser.scene;

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.java.awt.RobotUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector4;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.PicturePlaneUtils;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Dennis Cosgrove
 */
public abstract class PicturePlaneInteraction {
	private static enum Mode {
		PLANE,
		RAY
	}

	private final OnscreenRenderTarget onscreenRenderTarget;
	private final AbstractCamera sgCamera;
	private Transformable sgTransformable;

	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked( MouseEvent e ) {
		}

		@Override
		public void mousePressed( MouseEvent e ) {
			handleMousePressed( e );
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
			handleMouseReleased( e );
		}

		@Override
		public void mouseEntered( MouseEvent e ) {
		}

		@Override
		public void mouseExited( MouseEvent e ) {
		}
	};
	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged( MouseEvent e ) {
			handleMouseDragged( e );
		}

		@Override
		public void mouseMoved( MouseEvent e ) {
		}
	};

	public PicturePlaneInteraction( OnscreenRenderTarget onscreenRenderTarget, AbstractCamera camera ) {
		this.onscreenRenderTarget = onscreenRenderTarget;
		//		this.sgCamera = this.onscreenLookingGlass.getCameraAt( 0 ); //todo
		this.sgCamera = camera;
	}

	public OnscreenRenderTarget getOnscreenPicturePlane() {
		return this.onscreenRenderTarget;
	}

	public AbstractCamera getSgCamera() {
		return this.sgCamera;
	}

	public Transformable getSgTransformable() {
		return this.sgTransformable;
	}

	public void startUp() {
		Component awtComponent = this.onscreenRenderTarget.getAwtComponent();
		awtComponent.addMouseListener( this.mouseListener );
		awtComponent.addMouseMotionListener( this.mouseMotionListener );
	}

	public void shutDown() {
		Component awtComponent = this.onscreenRenderTarget.getAwtComponent();
		awtComponent.removeMouseMotionListener( this.mouseMotionListener );
		awtComponent.removeMouseListener( this.mouseListener );
	}

	private Mode getMode() {
		if( Double.isNaN( this.planeZ0 ) == false ) {
			return Mode.PLANE;
		} else if( this.ray != null ) {
			return Mode.RAY;
		} else {
			return null;
		}
	}

	private double planeZ0 = Double.NaN;

	private void startPlaneDrag( MouseEvent e ) {
		Point3 p = this.sgTransformable.getTranslation( this.sgCamera );
		Vector4 xyzwInCameraSpace = new Vector4( p.x, p.y, p.z, 1.0 );
		Vector4 xyzwInViewportSpace = PicturePlaneUtils.transformFromCameraToViewport_New( xyzwInCameraSpace, this.onscreenRenderTarget, this.sgCamera );
		this.planeZ0 = xyzwInViewportSpace.z / xyzwInViewportSpace.w;
	}

	private void planeDrag( MouseEvent e ) {
		//todo: account for viewport
		int x = e.getX();
		int y = this.onscreenRenderTarget.getSurfaceHeight() - e.getY();

		Vector4 xyzwInViewportSpace = new Vector4( x, y, this.planeZ0, 1.0 );

		Vector4 xyzwInCameraSpace = PicturePlaneUtils.transformFromViewportToCamera_New( xyzwInViewportSpace, this.onscreenRenderTarget, this.sgCamera );

		Point3 p = new Point3( xyzwInCameraSpace.x / xyzwInCameraSpace.w, xyzwInCameraSpace.y / xyzwInCameraSpace.w, xyzwInCameraSpace.z / xyzwInCameraSpace.w );
		this.sgTransformable.setTranslationOnly( p, this.sgCamera );
	}

	private void stopPlaneDrag( MouseEvent e ) {
		this.planeZ0 = Double.NaN;
	}

	private static final double Y_PIXELS_TO_RAY_T_FACTOR = 0.025;
	private Ray ray = null;
	private double rayT0 = Double.NaN;
	private double rayPixelY0 = Double.NaN;

	private void startRayDrag( MouseEvent e ) {
		this.ray = this.onscreenRenderTarget.getRayAtPixel( e.getX(), e.getY() );
		this.rayPixelY0 = e.getY();
		Point3 p = this.sgTransformable.getTranslation( this.sgCamera );
		this.rayT0 = this.ray.getProjectedPointT( p );
		CursorUtilities.pushAndSet( this.onscreenRenderTarget.getAwtComponent(), CursorUtilities.NULL_CURSOR );
	}

	private void rayDrag( MouseEvent e ) {
		double deltaY = e.getY() - this.rayPixelY0;
		double rayT = this.rayT0 + ( deltaY * Y_PIXELS_TO_RAY_T_FACTOR );
		Point3 p = this.ray.getPointAlong( rayT );
		this.sgTransformable.setTranslationOnly( p, this.sgCamera );
	}

	// note: this seems to be unnecessary (on jdk7/linux at least) as mouseDragged does not seem to be invoked on cursor warps
	private boolean isInTheMidstOfACursorWarp = false;

	private void stopRayDrag( MouseEvent e ) {
		Point3 p = this.sgTransformable.getTranslation( this.sgCamera );
		Point xyInPixels = PicturePlaneUtils.transformFromCameraToAWT_New( p, this.onscreenRenderTarget, this.sgCamera );

		this.isInTheMidstOfACursorWarp = true;
		try {
			RobotUtilities.mouseMove( this.onscreenRenderTarget.getAwtComponent(), xyInPixels );
		} finally {
			this.isInTheMidstOfACursorWarp = false;
		}

		CursorUtilities.popAndSet( this.onscreenRenderTarget.getAwtComponent() );
		this.ray = null;
		this.rayT0 = Double.NaN;
		this.rayPixelY0 = Double.NaN;
	}

	protected abstract Transformable pick( MouseEvent e );

	protected void handleMousePressed( MouseEvent e ) {
		this.sgTransformable = this.pick( e );
		if( this.sgTransformable != null ) {
			if( e.isShiftDown() ) {
				this.startRayDrag( e );
			} else {
				this.startPlaneDrag( e );
			}
		}
	}

	protected void handleMouseReleased( MouseEvent e ) {
		if( this.sgTransformable != null ) {
			Mode mode = this.getMode();
			if( mode == Mode.PLANE ) {
				this.stopPlaneDrag( e );
			} else if( mode == Mode.RAY ) {
				this.stopRayDrag( e );
			}
		}
	}

	protected void handleMouseDragged( MouseEvent e ) {
		if( this.sgTransformable != null ) {
			if( this.isInTheMidstOfACursorWarp ) {
				Logger.outln( "skip warped cursor", e );
			} else {
				Mode mode = this.getMode();
				if( e.isShiftDown() ) {
					if( mode == Mode.PLANE ) {
						this.stopPlaneDrag( e );
						this.startRayDrag( e );
					}
				} else {
					if( mode == Mode.RAY ) {
						this.stopRayDrag( e );
						this.startPlaneDrag( e );
					}
				}
				mode = this.getMode();
				if( mode == Mode.PLANE ) {
					this.planeDrag( e );
				} else if( mode == Mode.RAY ) {
					this.rayDrag( e );
				}
			}
		}
	}
}
