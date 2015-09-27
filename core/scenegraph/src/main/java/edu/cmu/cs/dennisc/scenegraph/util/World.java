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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.scenegraph.AmbientLight;
import edu.cmu.cs.dennisc.scenegraph.Background;
import edu.cmu.cs.dennisc.scenegraph.DirectionalLight;
import edu.cmu.cs.dennisc.scenegraph.Scene;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author Dennis Cosgrove
 */
public class World extends Scene {
	public World() {
		this.sgBackground.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.5f, 0.5f, 1, 1 ) );
		background.setValue( this.sgBackground );

		this.sgAmbientLight.setParent( this );
		this.sgAmbientLight.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.2f, 0.2f, 0.2f, 1 ) );

		this.sgSunVehicle.setParent( this );
		this.sgSunVehicle.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -Math.PI / 2 ) ) );

		this.sgSunLight.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 1, 1, 1, 1 ) );
		this.sgSunLight.setParent( this.sgSunVehicle );

		this.sgCameraVehicle.setParent( this );

		this.sgCameraVehicle.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( 0, 0, 32 ) );

		this.sgCamera.farClippingPlaneDistance.setValue( 1000.0 );
		this.sgCamera.setParent( this.sgCameraVehicle );
	}

	public Background getSGBackground() {
		return this.sgBackground;
	}

	public AmbientLight getSGAmbientLight() {
		return this.sgAmbientLight;
	}

	public Transformable getSGSunVehicle() {
		return this.sgSunVehicle;
	}

	public DirectionalLight getSGSunLight() {
		return this.sgSunLight;
	}

	public Transformable getSGCameraVehicle() {
		return this.sgCameraVehicle;
	}

	public SymmetricPerspectiveCamera getSGCamera() {
		return this.sgCamera;
	}

	@Override
	public void setName( String name ) {
		super.setName( name );
		this.sgBackground.setName( name + ".sgBackground" );
		this.sgAmbientLight.setName( name + ".sgAmbientLight" );
		this.sgSunVehicle.setName( name + ".sgSunVehicle" );
		this.sgSunLight.setName( name + ".sgSunLight" );
		this.sgCameraVehicle.setName( name + ".sgCameraVehicle" );
		this.sgCamera.setName( name + ".sgCamera" );
	}

	private final Background sgBackground = new Background();
	private final AmbientLight sgAmbientLight = new AmbientLight();
	private final Transformable sgSunVehicle = new Transformable();
	private final DirectionalLight sgSunLight = new DirectionalLight();
	private final Transformable sgCameraVehicle = new Transformable();
	private final SymmetricPerspectiveCamera sgCamera = new SymmetricPerspectiveCamera();
}
