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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class Cone extends Model {
	private static java.util.Map< edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis, BaseToTip > s_map = new java.util.HashMap< edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis, BaseToTip >();
	public enum BaseToTip {
		LEFT_TO_RIGHT( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_X ),
		RIGHT_TO_LEFT( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_X ),
		BOTTOM_TO_TOP( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Y ),
		TOP_TO_BOTTOM( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Y ),
		FRONT_TO_BACK( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Z ),
		BACK_TO_FRONT( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Z );
		private edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis m_bottomToTopAxis;
		BaseToTip( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis bottomToTopAxis ) {
			m_bottomToTopAxis = bottomToTopAxis;
			s_map.put( m_bottomToTopAxis, this );
		}
		private edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis getBottomToTopAxis() {
			return m_bottomToTopAxis;
		}
	}
	private edu.cmu.cs.dennisc.scenegraph.Cylinder m_sgCylinder;
	public Cone() {
		m_sgCylinder = new edu.cmu.cs.dennisc.scenegraph.Cylinder();
		m_sgCylinder.topRadius.setValue( 0.0 );
		m_sgCylinder.hasTopCap.setValue( false );		
	}
	@Override
	protected void createSGGeometryIfNecessary() {
	}

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return m_sgCylinder;
	}

	public Double getLength() {
		createSGGeometryIfNecessary();
		return m_sgCylinder.length.getValue();
	}
	public void setLength( Number length, Number duration, Style style ) {
		createSGGeometryIfNecessary();
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getLength(), length.doubleValue() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgCylinder.length.setValue( d );
			}
		} );
	}
	public final void setLength( Number radius, Number duration ) {
		setLength( radius, duration, DEFAULT_STYLE );
	}
	public final void setLength( Number height ) {
		setLength( height, DEFAULT_DURATION );
	}

	public Double getBaseRadius() {
		createSGGeometryIfNecessary();
		return m_sgCylinder.bottomRadius.getValue();
	}
	public void setBaseRadius( Number baseRadius, Number duration, Style style ) {
		createSGGeometryIfNecessary();
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getBaseRadius(), baseRadius.doubleValue() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgCylinder.bottomRadius.setValue( d );
			}
		} );
	}
	public final void setBaseRadius( Number baseRadius, Number duration ) {
		setBaseRadius( baseRadius, duration, DEFAULT_STYLE );
	}
	public final void setBaseRadius( Number baseRadius ) {
		setBaseRadius( baseRadius, DEFAULT_DURATION );
	}

	public BaseToTip getBaseToTip() {
		createSGGeometryIfNecessary();
		return s_map.get( m_sgCylinder.bottomToTopAxis.getValue() );
	}
	public void setBaseToTip( BaseToTip baseToTip ) {
		createSGGeometryIfNecessary();
		m_sgCylinder.bottomToTopAxis.setValue( baseToTip.getBottomToTopAxis() );
	}
}
