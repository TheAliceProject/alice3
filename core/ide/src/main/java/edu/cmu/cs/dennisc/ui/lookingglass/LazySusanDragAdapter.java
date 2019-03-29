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

import edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.EulerAngles;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.ui.DragAdapter;
import edu.cmu.cs.dennisc.ui.DragStyle;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public class LazySusanDragAdapter extends DragAdapter {
	private Transformable m_lazySusan;
	private double m_radiansPerPixel;

	private static final boolean USE_EULER_ANGLES = true;
	private EulerAngles m_ea0 = null;
	private double m_yaw0 = Double.NaN;

	public LazySusanDragAdapter( Transformable lazySusan, double radiansPerPixel ) {
		m_lazySusan = lazySusan;
		m_radiansPerPixel = radiansPerPixel;
	}

	@Override
	protected boolean isAcceptable( MouseEvent e ) {
		return MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e );
	}

	//todo: add get/set m_lazySusan
	//todo: add get/set m_radiansPerPixel

	@Override
	protected void handleMousePress( Point current, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		EulerAngles ea = new EulerAngles( m_lazySusan.getLocalTransformation().orientation );
		if( USE_EULER_ANGLES ) {
			m_ea0 = ea;
		} else {
			m_yaw0 = ea.yaw.getAsRadians();
		}
	}

	@Override
	protected void handleMouseDrag( Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, DragStyle dragStyle ) {
		double deltaYaw = xDeltaSince0 * m_radiansPerPixel;
		OrthogonalMatrix3x3 m;
		if( USE_EULER_ANGLES ) {
			EulerAngles ea = new EulerAngles( m_ea0 );
			double yawInRadians = ea.yaw.getAsRadians();

			boolean isUpsideDownAndBackwardsSoToSpeak = false;
			final double EPSILON = 0.01;
			if( EpsilonUtilities.isWithinEpsilon( ea.pitch.getAsRadians(), Math.PI, EPSILON ) ) {
				if( EpsilonUtilities.isWithinEpsilon( ea.roll.getAsRadians(), Math.PI, EPSILON ) ) {
					isUpsideDownAndBackwardsSoToSpeak = true;
				}
			}

			if( isUpsideDownAndBackwardsSoToSpeak ) {
				yawInRadians -= deltaYaw;
			} else {
				yawInRadians += deltaYaw;
			}

			ea.yaw.setAsRadians( yawInRadians );
			m = new OrthogonalMatrix3x3( ea );
		} else {
			m = OrthogonalMatrix3x3.createFromRotationAboutYAxis( new AngleInRadians( m_yaw0 + deltaYaw ) );
		}
		m_lazySusan.setAxesOnly( m, AsSeenBy.PARENT );
	}

	@Override
	protected Point handleMouseRelease( Point rvCurrent, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange ) {
		if( USE_EULER_ANGLES ) {
			m_ea0 = null;
		} else {
			m_yaw0 = Double.NaN;
		}
		return rvCurrent;
	}
}
