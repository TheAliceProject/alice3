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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public class PointLightAdapter< E extends edu.cmu.cs.dennisc.scenegraph.PointLight > extends LightAdapter< E > {
	private float m_constant;
	private float m_linear;
	private float m_quadratic;

	@Override
	protected float[] getPosition( float[] rv ) {
		java.nio.DoubleBuffer db = accessAbsoluteTransformationAsBuffer();
		rv[ 0 ] = (float)db.get( 12 );
		rv[ 1 ] = (float)db.get( 13 );
		rv[ 2 ] = (float)db.get( 14 );
		rv[ 3 ] = 1.0f;
		return rv;
	}

	@Override
	protected float getConstantAttenuation() {
		return m_constant;
	}
	@Override
	protected float getLinearAttenuation() {
		return m_linear;
	}
	@Override
	protected float getQuadraticAttenuation() {
		return m_quadratic;
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.constantAttenuation ) {
			m_constant = m_element.constantAttenuation.getValue().floatValue();
		} else if( property == m_element.linearAttenuation ) {
			m_linear = m_element.linearAttenuation.getValue().floatValue();
		} else if( property == m_element.quadraticAttenuation ) {
			m_quadratic = m_element.quadraticAttenuation.getValue().floatValue();
		} else {
			super.propertyChanged( property );
		}
	}
}
