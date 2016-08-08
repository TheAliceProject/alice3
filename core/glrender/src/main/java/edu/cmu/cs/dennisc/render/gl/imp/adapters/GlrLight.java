/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_CONSTANT_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LINEAR_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_QUADRATIC_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPOT_CUTOFF;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPOT_DIRECTION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPOT_EXPONENT;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrLight<T extends edu.cmu.cs.dennisc.scenegraph.Light> extends GlrAffector<T> {
	private static java.nio.FloatBuffer s_ambientBlackBuffer = null;

	private static float[] s_position = new float[ 4 ];
	private static java.nio.FloatBuffer s_positionBuffer = java.nio.FloatBuffer.wrap( s_position );
	private static float[] s_spotDirection = new float[ 3 ];
	private static java.nio.FloatBuffer s_spotDirectionBuffer = java.nio.FloatBuffer.wrap( s_spotDirection );

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
		rc.gl.glLightfv( id, GL_AMBIENT, s_ambientBlackBuffer );

		//todo: should lights' diffuse and specular colors be separated in the scenegraph?
		rc.setLightColor( id, this.color, this.brightness );

		//        rc.gl.glLightfv( id, GL_DIFFUSE, this.colorTimesBrightnessBuffer );
		//        rc.gl.glLightfv( id, GL_SPECULAR, this.colorTimesBrightnessBuffer );

		synchronized( s_position ) {
			getPosition( s_position );
			rc.gl.glLightfv( id, GL_POSITION, s_positionBuffer );
		}
		synchronized( s_spotDirection ) {
			getSpotDirection( s_spotDirection );
			rc.gl.glLightfv( id, GL_SPOT_DIRECTION, s_spotDirectionBuffer );
		}
		rc.gl.glLightf( id, GL_SPOT_EXPONENT, getSpotExponent() );
		rc.gl.glLightf( id, GL_SPOT_CUTOFF, getSpotCutoff() );
		rc.gl.glLightf( id, GL_CONSTANT_ATTENUATION, getConstantAttenuation() );
		rc.gl.glLightf( id, GL_LINEAR_ATTENUATION, getLinearAttenuation() );
		rc.gl.glLightf( id, GL_QUADRATIC_ATTENUATION, getQuadraticAttenuation() );
	}

	@Override
	public void setupAffectors( RenderContext rc ) {
		if( this instanceof GlrAmbientLight ) {
			rc.addAmbient( this.color, this.brightness );
		} else {
			int id = rc.getNextLightID();
			setup( rc, id );
		}
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.color ) {
			owner.color.getValue().getAsArray( this.color );
		} else if( property == owner.brightness ) {
			this.brightness = owner.brightness.getValue();
		} else {
			super.propertyChanged( property );
		}
	}

	private final float[] color = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private float brightness = Float.NaN;
}
