/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.ui.lookingglass;

import edu.cmu.cs.dennisc.ui.scenegraph.SetPointOfViewAction;

//todo: allow specification of a reference frame other than absolute
/**
 * @author Dennis Cosgrove
 */
public class ModelManipulationDragAdapter extends edu.cmu.cs.dennisc.ui.lookingglass.OnscreenLookingGlassDragAdapter {
	private edu.cmu.cs.dennisc.scenegraph.AbstractCamera m_sgCamera = null;
	private edu.cmu.cs.dennisc.scenegraph.Transformable m_sgDragAcceptor = null;
	private edu.cmu.cs.dennisc.math.Plane m_planeInAbsolute = edu.cmu.cs.dennisc.math.Plane.NaN;
	private edu.cmu.cs.dennisc.math.Point3 m_xyzInAbsoluteAtPress = null;
	private edu.cmu.cs.dennisc.math.Point3 m_xyzInDragAcceptorAtPress = null;
	private edu.cmu.cs.dennisc.math.Vector3 m_offset = null;

	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_undoPOV;

	@Override
	protected boolean isAcceptable( java.awt.event.MouseEvent e ) {
		return edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e );
	}

	protected edu.cmu.cs.dennisc.render.PickObserver getPickObserver() {
		return null;
	}

	protected void updateTranslation( edu.cmu.cs.dennisc.scenegraph.Transformable sgDragAcceptor, edu.cmu.cs.dennisc.math.Tuple3 xyz, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		if( sgDragAcceptor != null ) {
			sgDragAcceptor.setTranslationOnly( xyz, asSeenBy );
		}
	}

	protected edu.cmu.cs.dennisc.scenegraph.Transformable lookupDragAcceptor( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual ) {
		edu.cmu.cs.dennisc.scenegraph.Composite sgParent = sgVisual.getParent();
		if( sgParent instanceof edu.cmu.cs.dennisc.scenegraph.Transformable ) {
			return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgParent;
		} else {
			return null;
		}
	}

	protected edu.cmu.cs.dennisc.scenegraph.Transformable getDragAcceptor() {
		return m_sgDragAcceptor;
	}

	private edu.cmu.cs.dennisc.math.Point3 getPointInPlane( edu.cmu.cs.dennisc.math.Plane plane, int xPixel, int yPixel ) {
		edu.cmu.cs.dennisc.math.Ray ray = getOnscreenRenderTarget().getRayAtPixel( xPixel, yPixel, m_sgCamera );
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = m_sgCamera.getAbsoluteTransformation();
		ray.transform( m );
		double t = plane.intersect( ray );
		return ray.getPointAlong( t );
	}

	private double yDelta = 0.0;

	@Override
	protected void handleMousePress( java.awt.Point current, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		if( isOriginalAsOpposedToStyleChange ) {
			edu.cmu.cs.dennisc.render.PickResult pickResult = getOnscreenRenderTarget().getSynchronousPicker().pickFrontMost( current.x, current.y, edu.cmu.cs.dennisc.render.PickSubElementPolicy.NOT_REQUIRED, getPickObserver() );
			m_sgCamera = (edu.cmu.cs.dennisc.scenegraph.AbstractCamera)pickResult.getSource();
			edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
			if( sgVisual != null ) {
				m_sgDragAcceptor = lookupDragAcceptor( sgVisual );
				if( m_sgDragAcceptor != null ) {
					m_undoPOV = m_sgDragAcceptor.getTransformation( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
					m_xyzInAbsoluteAtPress = edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformToAbsolute_New( pickResult.getPositionInSource(), m_sgCamera );
				}
			}
			this.yDelta = 0.0;
		} else {
			if( m_sgDragAcceptor != null ) {
				edu.cmu.cs.dennisc.math.Ray ray = getOnscreenRenderTarget().getRayAtPixel( current.x, current.y, m_sgCamera );
				ray.transform( m_sgCamera.getAbsoluteTransformation() );
				double t = m_planeInAbsolute.intersect( ray );
				m_xyzInAbsoluteAtPress = ray.getPointAlong( t );
				//m_xyzInAbsoluteAtPress.y += this.yDelta;
			}
		}

		if( m_sgDragAcceptor != null ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = m_sgDragAcceptor.getAbsoluteTransformation();
			m_offset = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( m_xyzInAbsoluteAtPress, m.translation );
			m_xyzInDragAcceptorAtPress = m_sgDragAcceptor.transformTo_New( m_xyzInAbsoluteAtPress, m_sgDragAcceptor.getRoot()/* todo: edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE */);
			if( dragStyle.isShiftDown() ) {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 cameraAbsolute = m_sgCamera.getAbsoluteTransformation();
				edu.cmu.cs.dennisc.math.Vector3 axis = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( cameraAbsolute.translation, m_xyzInAbsoluteAtPress );
				axis.normalize();
				m_planeInAbsolute = edu.cmu.cs.dennisc.math.Plane.createInstance( m_xyzInAbsoluteAtPress, axis );
			} else {
				m_planeInAbsolute = edu.cmu.cs.dennisc.math.Plane.createInstance( m_xyzInAbsoluteAtPress, edu.cmu.cs.dennisc.math.Vector3.accessPositiveYAxis() );
			}
		} else {
			m_planeInAbsolute = edu.cmu.cs.dennisc.math.Plane.NaN;
			m_xyzInAbsoluteAtPress = edu.cmu.cs.dennisc.math.Point3.createNaN();
			m_xyzInDragAcceptorAtPress = null;
		}
	}

	@Override
	protected void handleMouseDrag( java.awt.Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, edu.cmu.cs.dennisc.ui.DragStyle dragStyle ) {
		if( ( m_sgDragAcceptor == null ) || m_planeInAbsolute.isNaN() || m_xyzInDragAcceptorAtPress.isNaN() ) {
			//pass
		} else {
			if( dragStyle.isControlDown() ) {
				//				if( false ) {
				//					final int THRESHOLD = 25;
				//					if( (yDeltaSince0 * yDeltaSince0 + xDeltaSince0 * xDeltaSince0) > THRESHOLD ) {
				//						edu.cmu.cs.dennisc.math.Vector3d dir = edu.cmu.cs.dennisc.math.LinearAlgebra.subtract( xyzInAbsolutePlane, m_xyzInAbsoluteAtPress );
				//						double yaw = Math.atan2( dir.x, dir.z );
				//						//y += Math.PI / 2;
				//						m_sgDragAcceptor.setAxesOnly( edu.cmu.cs.dennisc.math.LinearAlgebra.newRotationAboutYAxisMatrix3d( yaw, edu.cmu.cs.dennisc.math.UnitOfAngle.RADIANS ), edu.cmu.cs.dennisc.scenegraph.ReferenceFrame.AsSeenBy.SCENE );
				//					} else {
				//						//						System.out.println( "too close" );
				//					}
				//				} else {
				//double yaw0 = Math.atan2( m_undoPOV.right.z, m_undoPOV.backward.z );
				m_sgDragAcceptor.applyRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( xDeltaSincePrevious * 0.01 ) );
				//				}
			} else {
				if( dragStyle.isShiftDown() ) {
					if( m_sgDragAcceptor != null ) {
						edu.cmu.cs.dennisc.math.Point3 t = m_sgDragAcceptor.getAbsoluteTransformation().translation;
						final edu.cmu.cs.dennisc.math.Point3 xyzInAbsolutePlane = getPointInPlane( m_planeInAbsolute, current.x, current.y );

						this.yDelta += xyzInAbsolutePlane.y - t.y;

						xyzInAbsolutePlane.subtract( m_offset );
						xyzInAbsolutePlane.x = t.x;
						xyzInAbsolutePlane.z = t.z;

						getOnscreenRenderTarget().getRenderFactory().invokeLater( new Runnable() {
							@Override
							public void run() {
								updateTranslation( m_sgDragAcceptor, xyzInAbsolutePlane, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
							}
						} );
					}
				} else {
					final edu.cmu.cs.dennisc.math.Point3 xyzInAbsolutePlane = getPointInPlane( m_planeInAbsolute, current.x, current.y );
					xyzInAbsolutePlane.subtract( m_offset );
					getOnscreenRenderTarget().getRenderFactory().invokeLater( new Runnable() {
						@Override
						public void run() {
							updateTranslation( m_sgDragAcceptor, xyzInAbsolutePlane, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
						}
					} );
				}
			}
		}
	}

	@Override
	protected java.awt.Point handleMouseRelease( java.awt.Point rv, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		if( ( m_sgCamera != null ) && ( m_sgDragAcceptor != null ) ) {
			//			if( dragStyle.isControlDown() ) {
			//				java.awt.Point p = m_sgDragAcceptor.transformToAWT_New( m_xyzInDragAcceptorAtPress, getOnscreenLookingGlass(), m_sgCamera );
			//				warpCursor( p );
			//				showCursor();
			//				rv.setLocation( p );
			//			}
		}
		if( isOriginalAsOpposedToStyleChange ) {
			if( m_sgDragAcceptor != null ) {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 redoPOV = m_sgDragAcceptor.getTransformation( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
				if( getUndoRedoManager() != null ) {
					getUndoRedoManager().pushAlreadyRunActionOntoUndoStack( new SetPointOfViewAction( getAnimator(), m_sgDragAcceptor, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE, m_undoPOV, redoPOV ) );
				}
			}
			m_sgDragAcceptor = null;
			m_planeInAbsolute = edu.cmu.cs.dennisc.math.Plane.NaN;
		}
		return rv;
	}
}
