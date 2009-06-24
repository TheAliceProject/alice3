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

import javax.media.opengl.GL;

/**
 * @author Dennis Cosgrove
 */
public abstract class LightAdapter< E extends edu.cmu.cs.dennisc.scenegraph.Light > extends AffectorAdapter< E > {
	private static java.nio.FloatBuffer s_ambientBlackBuffer = null;

	private static float[] s_position = new float[ 4 ];
	private static java.nio.FloatBuffer s_positionBuffer = java.nio.FloatBuffer.wrap( s_position );
	private static float[] s_spotDirection = new float[ 3 ];
	private static java.nio.FloatBuffer s_spotDirectionBuffer = java.nio.FloatBuffer.wrap( s_spotDirection );

	private float[] m_color = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private float m_brightness = Float.NaN;
	
	protected float[] getPosition( float[] rv ) {
		rv[ 0 ] = 0;
		rv[ 1 ] = 0;
		rv[ 2 ] = 1;
		rv[ 3 ] = 0;
		return rv;
	}
	protected float[] getSpotDirection( float[] rv ) {
		rv[ 0 ] = 0;
		rv[ 1 ] = 0;
		rv[ 2 ] = -1;
		return rv;
	}
	protected float getSpotExponent() {
		return 0;
	}
	protected float getSpotCutoff() {
		return 180;
	}
	protected float getConstantAttenuation() {
		return 1;
	}
	protected float getLinearAttenuation() {
		return 0;
	}
	protected float getQuadraticAttenuation() {
		return 0;
	}

	protected void setup( RenderContext rc, int id ) {
		rc.gl.glEnable( id );

		//todo: investigate
		if( s_ambientBlackBuffer == null ) {
			s_ambientBlackBuffer = java.nio.FloatBuffer.allocate( 4 );
			s_ambientBlackBuffer.put( 0 );
			s_ambientBlackBuffer.put( 0 );
			s_ambientBlackBuffer.put( 0 );
			s_ambientBlackBuffer.put( 1 );
			s_ambientBlackBuffer.rewind();
		}
		rc.gl.glLightfv( id, GL.GL_AMBIENT, s_ambientBlackBuffer );
		

		//todo: should lights' diffuse and specular colors be separated in the scenegraph?
		rc.setLightColor( id, m_color, m_brightness );

		//        rc.gl.glLightfv( id, GL.GL_DIFFUSE, m_colorTimesBrightnessBuffer );
		//        rc.gl.glLightfv( id, GL.GL_SPECULAR, m_colorTimesBrightnessBuffer );

		synchronized( s_position ) {
			getPosition( s_position );
			rc.gl.glLightfv( id, GL.GL_POSITION, s_positionBuffer );
		}
		synchronized( s_spotDirection ) {
			getSpotDirection( s_spotDirection );
			rc.gl.glLightfv( id, GL.GL_SPOT_DIRECTION, s_spotDirectionBuffer );
		}
		rc.gl.glLightf( id, GL.GL_SPOT_EXPONENT, getSpotExponent() );
		rc.gl.glLightf( id, GL.GL_SPOT_CUTOFF, getSpotCutoff() );
		rc.gl.glLightf( id, GL.GL_CONSTANT_ATTENUATION, getConstantAttenuation() );
		rc.gl.glLightf( id, GL.GL_LINEAR_ATTENUATION, getLinearAttenuation() );
		rc.gl.glLightf( id, GL.GL_QUADRATIC_ATTENUATION, getQuadraticAttenuation() );
	}
	@Override
	public void setup( RenderContext rc ) {
		if( this instanceof AmbientLightAdapter ) {
			rc.addAmbient( m_color, m_brightness );
		} else {
			int id = rc.getNextLightID();
			setup( rc, id );
		}
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.color ) {
			m_element.color.getValue().getAsArray( m_color );
		} else if( property == m_element.brightness ) {
			m_brightness = m_element.brightness.getValue();
		} else {
			super.propertyChanged( property );
		}
	}
}
