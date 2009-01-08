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
public class LazySusanDragAdapter extends edu.cmu.cs.dennisc.ui.DragAdapter {
	private edu.cmu.cs.dennisc.scenegraph.Transformable m_lazySusan;
	private double m_radiansPerPixel;

	private static final boolean USE_EULER_ANGLES = true; 
	private edu.cmu.cs.dennisc.math.EulerAngles m_ea0 = null;
	private double m_yaw0 = Double.NaN;

	public LazySusanDragAdapter( edu.cmu.cs.dennisc.scenegraph.Transformable lazySusan, double radiansPerPixel ) {
		m_lazySusan = lazySusan;
		m_radiansPerPixel = radiansPerPixel;
		setModifierMask( java.awt.event.MouseEvent.BUTTON1_MASK );
	}

	//todo: add get/set m_lazySusan
	//todo: add get/set m_radiansPerPixel
	
	@Override
	protected void handleMousePress( java.awt.Point current, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		edu.cmu.cs.dennisc.math.EulerAngles ea = new edu.cmu.cs.dennisc.math.EulerAngles( m_lazySusan.getLocalTransformation().orientation );
		if( USE_EULER_ANGLES ) {
			m_ea0 = ea;
		} else {
			m_yaw0 = ea.yaw.getAsRadians();
		}
	}

	@Override
	protected void handleMouseDrag( java.awt.Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, edu.cmu.cs.dennisc.ui.DragStyle dragStyle ) {
		double deltaYaw = xDeltaSince0 * m_radiansPerPixel;
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m;
		if( USE_EULER_ANGLES ) {
			edu.cmu.cs.dennisc.math.EulerAngles ea = new edu.cmu.cs.dennisc.math.EulerAngles( m_ea0 );
			double yawInRadians = ea.yaw.getAsRadians();
			
			boolean isUpsideDownAndBackwardsSoToSpeak = false;
			final double EPSILON = 0.01;
			if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( ea.pitch.getAsRadians(), Math.PI, EPSILON ) ) {
				if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilon( ea.roll.getAsRadians(), Math.PI, EPSILON ) ) {
					isUpsideDownAndBackwardsSoToSpeak = true;
				}
			}

			if( isUpsideDownAndBackwardsSoToSpeak ) {
				yawInRadians -= deltaYaw;
			} else {
				yawInRadians += deltaYaw;
			}
			
			ea.yaw.setAsRadians( yawInRadians );
			m = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3( ea );
		} else {
			m = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( m_yaw0 + deltaYaw ) );
		}
		m_lazySusan.setAxesOnly( m, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT );
	}

	@Override
	protected java.awt.Point handleMouseRelease( java.awt.Point rvCurrent, edu.cmu.cs.dennisc.ui.DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		if( USE_EULER_ANGLES ) {
			m_ea0 = null;
		} else {
			m_yaw0 = Double.NaN;
		}
		return rvCurrent;
	}
}
