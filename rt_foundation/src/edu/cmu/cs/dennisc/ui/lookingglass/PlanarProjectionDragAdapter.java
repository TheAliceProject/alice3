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

/**
 * @author Dennis Cosgrove
 */
public abstract class PlanarProjectionDragAdapter extends edu.cmu.cs.dennisc.ui.lookingglass.OnscreenLookingGlassDragAdapter {
	private edu.cmu.cs.dennisc.math.Plane m_bufferPlaneInAbsolute = new edu.cmu.cs.dennisc.math.Plane();

	public PlanarProjectionDragAdapter() {
		setModifierMask( java.awt.event.MouseEvent.BUTTON1_MASK );
	}
	protected abstract edu.cmu.cs.dennisc.math.Plane getPlaneInAbsolute( edu.cmu.cs.dennisc.math.Plane rv );
	protected abstract void handlePointInAbsolute( edu.cmu.cs.dennisc.math.Point3 xyzInAbsolute );
	
	private edu.cmu.cs.dennisc.math.Point3 getPointInAbsolutePlane( java.awt.Point p ) {
		edu.cmu.cs.dennisc.math.Point3 rv;
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getOnscreenLookingGlass().getCameraAtPixel( p.x, p.y );
		if( sgCamera != null ) {
			edu.cmu.cs.dennisc.math.Ray ray = getOnscreenLookingGlass().getRayAtPixel( p.x, p.y, sgCamera );
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgCamera.getAbsoluteTransformation();
			ray.transform( m );
			getPlaneInAbsolute( m_bufferPlaneInAbsolute );
			double t = m_bufferPlaneInAbsolute.intersect( ray );
			rv = ray.getPointAlong( t );
		} else {
			rv = null;
		}
		return rv;
	}

	@Override
	protected void handleMousePress( java.awt.Point current, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		if( isOriginalAsOpposedToStyleChange ) {
		} else {
		}
	}
	@Override
	protected void handleMouseDrag( java.awt.Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, edu.cmu.cs.dennisc.ui.DragStyle dragStyle ) {
		edu.cmu.cs.dennisc.math.Point3 xyzInAbsolute = getPointInAbsolutePlane( current );
		if( xyzInAbsolute != null ) {
			handlePointInAbsolute( xyzInAbsolute );
		}
	}
	@Override
	protected java.awt.Point handleMouseRelease( java.awt.Point rv, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		return rv;
	}
}
