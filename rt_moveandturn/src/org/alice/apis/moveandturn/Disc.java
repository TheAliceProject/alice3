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
public class Disc extends Model {
	private edu.cmu.cs.dennisc.scenegraph.Disc m_sgDisc = new edu.cmu.cs.dennisc.scenegraph.Disc();
	@Override
	protected void createSGGeometryIfNecessary() {
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return m_sgDisc;
	}
	public Double getInnerRadius() {
		return m_sgDisc.outerRadius.getValue();
	}
	public void setInnerRadius( Number innerRadius, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getInnerRadius(), innerRadius.doubleValue() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgDisc.innerRadius.setValue( d );
			}
		} );
	}
	public void setInnerRadius( Number innerRadius, Number duration ) {
		setInnerRadius( innerRadius, duration, DEFAULT_STYLE );
	}
	public void setInnerRadius( Number innerRadius ) {
		setInnerRadius( innerRadius, DEFAULT_DURATION );
	}
	
	public Double getOuterRadius() {
		return m_sgDisc.outerRadius.getValue();
	}
	public void setOuterRadius( Number outerRadius, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getOuterRadius(), outerRadius.doubleValue() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgDisc.outerRadius.setValue( d );
			}
		} );
	}
	public void setOuterRadius( Number outerRadius, Number duration ) {
		setOuterRadius( outerRadius, duration, DEFAULT_STYLE );
	}
	public void setOuterRadius( Number outerRadius ) {
		setOuterRadius( outerRadius, DEFAULT_DURATION );
	}
}
