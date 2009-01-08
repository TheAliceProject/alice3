/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
	private edu.cmu.cs.dennisc.math.Plane m_planeInAbsolute = new edu.cmu.cs.dennisc.math.Plane();
	private edu.cmu.cs.dennisc.math.Point3 m_xyzInAbsoluteAtPress = null;
	private edu.cmu.cs.dennisc.math.Point3 m_xyzInDragAcceptorAtPress = null;
	private edu.cmu.cs.dennisc.math.Vector3 m_offset = null;

	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_undoPOV;
	
	public ModelManipulationDragAdapter() {
		setModifierMask( java.awt.event.MouseEvent.BUTTON1_MASK );
	}

	protected edu.cmu.cs.dennisc.lookingglass.PickObserver getPickObserver() {
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
	
	private edu.cmu.cs.dennisc.math.Point3 getPointInAbsolutePlane( int xPixel, int yPixel ) {
		edu.cmu.cs.dennisc.math.Ray ray = getOnscreenLookingGlass().getRayAtPixel( xPixel, yPixel, m_sgCamera );
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = m_sgCamera.getAbsoluteTransformation();
		ray.transform( m );
		double t = m_planeInAbsolute.intersect( ray );
		return ray.getPointAlong( t );
	}

	protected edu.cmu.cs.dennisc.math.Plane setPlaneInAbsolute( edu.cmu.cs.dennisc.math.Plane rv, edu.cmu.cs.dennisc.math.Point3 xyzInAbsolute ) {
		rv.set( xyzInAbsolute, edu.cmu.cs.dennisc.math.Vector3.accessPositiveYAxis() );
		return rv;
	}
	
	
	@Override
	protected void handleMousePress( java.awt.Point current, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		if( isOriginalAsOpposedToStyleChange ) {
			edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = getOnscreenLookingGlass().pickFrontMost( current.x, current.y, edu.cmu.cs.dennisc.lookingglass.LookingGlass.SUB_ELEMENT_IS_NOT_REQUIRED, getPickObserver() );
			m_sgCamera = (edu.cmu.cs.dennisc.scenegraph.AbstractCamera)pickResult.getSource();
			edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
			if( sgVisual != null ) {
				m_xyzInAbsoluteAtPress = edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformToAbsolute_New( pickResult.getPositionInSource(), m_sgCamera );
				setPlaneInAbsolute( m_planeInAbsolute, m_xyzInAbsoluteAtPress );
				m_sgDragAcceptor = lookupDragAcceptor( sgVisual );
			} else {
				m_sgDragAcceptor = null;
				m_planeInAbsolute.setNaN();
				m_xyzInAbsoluteAtPress = new edu.cmu.cs.dennisc.math.Point3();
				m_xyzInAbsoluteAtPress.setNaN();
			}
			if( m_sgDragAcceptor != null ) {
				m_undoPOV = m_sgDragAcceptor.getTransformation( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
			}
		} else {
			if( m_sgDragAcceptor != null ) {
				edu.cmu.cs.dennisc.math.Ray ray = getOnscreenLookingGlass().getRayAtPixel( current.x, current.y, m_sgCamera );
				ray.transform( m_sgCamera.getAbsoluteTransformation() );
				double t = m_planeInAbsolute.intersect( ray );
				m_xyzInAbsoluteAtPress = ray.getPointAlong( t );
			}
		}
		if( m_sgDragAcceptor != null ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = m_sgDragAcceptor.getAbsoluteTransformation();
			//m_offset = new edu.cmu.cs.dennisc.math.VectorD3( m_xyzInAbsoluteAtPress.x-m.translation.x, m_xyzInAbsoluteAtPress.y-m.translation.y, m_xyzInAbsoluteAtPress.z-m.translation.z );
			m_offset = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( m_xyzInAbsoluteAtPress, m.translation );
			
			m_xyzInDragAcceptorAtPress = m_sgDragAcceptor.transformTo_New( m_xyzInAbsoluteAtPress, m_sgDragAcceptor.getRoot()/*todo: edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE*/ );
//			java.awt.Point p = m_sgDragAcceptor.transformToAWT_New( m_xyzInDragAcceptorAtPress, getOnscreenLookingGlass(), m_sgCamera );
//			if( dragStyle.isControlDown() ) {
//				hideCursor();
//			}
		} else {
			m_xyzInDragAcceptorAtPress = null;
			//edu.cmu.cs.dennisc.math.LinearAlgebra.setNaN( m_xyzInDragAcceptorAtPress );
		}
	}
	@Override
	protected void handleMouseDrag( java.awt.Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, edu.cmu.cs.dennisc.ui.DragStyle dragStyle ) {
		if( m_sgDragAcceptor == null || m_planeInAbsolute.isNaN() || m_xyzInDragAcceptorAtPress.isNaN() ) {
			//pass
		} else {
			final edu.cmu.cs.dennisc.math.Point3 xyzInAbsolutePlane = getPointInAbsolutePlane( current.x, current.y );
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
				xyzInAbsolutePlane.subtract( m_offset );
				getOnscreenLookingGlass().getLookingGlassFactory().invokeLater( new Runnable() {
					public void run() {
						updateTranslation( m_sgDragAcceptor, xyzInAbsolutePlane, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
					}
				} );
			}
		}
	}
	@Override
	protected java.awt.Point handleMouseRelease( java.awt.Point rv, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		if( m_sgCamera != null && m_sgDragAcceptor != null ) {
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
			m_planeInAbsolute.setNaN();
		}
		return rv;
	}
}
