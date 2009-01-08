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
public class Sphere extends Model {
	private edu.cmu.cs.dennisc.scenegraph.Sphere m_sgSphere = new edu.cmu.cs.dennisc.scenegraph.Sphere();
	@Override
	protected void createSGGeometryIfNecessary() {
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return m_sgSphere;
	}
	public Double getRadius() {
		return m_sgSphere.radius.getValue();
	}
	public void setRadius( Number radius, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getRadius(), radius.doubleValue() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgSphere.radius.setValue( d );
			}
		} );
	}
	public void setRadius( Number radius, Number duration ) {
		setRadius( radius, duration, DEFAULT_STYLE );
	}
	public void setRadius( Number radius ) {
		setRadius( radius, DEFAULT_DURATION );
	}
}
