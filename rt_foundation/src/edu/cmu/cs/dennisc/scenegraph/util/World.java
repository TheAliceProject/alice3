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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.scenegraph.*;

/**
 * @author Dennis Cosgrove
 */
public class World extends Scene {
	private Background m_sgBackground = new Background();
	private AmbientLight m_sgAmbientLight = new AmbientLight();
	private Transformable m_sgSunVehicle = new Transformable();
	private DirectionalLight m_sgSunLight = new DirectionalLight();
	private Transformable m_sgCameraVehicle = new Transformable();
	private SymmetricPerspectiveCamera m_sgCamera = new SymmetricPerspectiveCamera();

    public World() {
	    m_sgBackground.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.5f, 0.5f, 1, 1 ) );
        background.setValue( m_sgBackground );

        m_sgAmbientLight.setParent( this );
        m_sgAmbientLight.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.2f, 0.2f, 0.2f, 1 ) );

        m_sgSunVehicle.setParent( this );
        m_sgSunVehicle.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -Math.PI/2 ) ) );

        m_sgSunLight.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 1, 1, 1, 1 ) );
        m_sgSunLight.setParent( m_sgSunVehicle );
        
        m_sgCameraVehicle.setParent( this );

		m_sgCameraVehicle.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( 0, 0, 32 ) );

		m_sgCamera.farClippingPlaneDistance.setValue( 1000.0 );
		m_sgCamera.setParent( m_sgCameraVehicle );
    }
	public Background getSGBackground() {
		return m_sgBackground;
	}
	public AmbientLight getSGAmbientLight() {
		return m_sgAmbientLight;
	}
	public Transformable getSGSunVehicle() {
		return m_sgSunVehicle;
	}
	public DirectionalLight getSGSunLight() {
		return m_sgSunLight;
	}
	public Transformable getSGCameraVehicle() {
		return m_sgCameraVehicle;
	}
	public SymmetricPerspectiveCamera getSGCamera() {
		return m_sgCamera;
	}
	@Override
	public void setName( String name ) {
		super.setName( name );
	    m_sgBackground.setName( "m_sgBackground" );
        m_sgAmbientLight.setName( "m_sgAmbientLight" );
        m_sgSunVehicle.setName( "m_sgSunVehicle" );
        m_sgSunLight.setName( "m_sgSunLight" );
        m_sgCameraVehicle.setName( "m_sgCameraVehicle" );
		m_sgCamera.setName( "m_sgCamera" );
	}
	
}
