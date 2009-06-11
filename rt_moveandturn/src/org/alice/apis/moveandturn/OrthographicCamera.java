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

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public class OrthographicCamera extends AbstractNearPlaneAndFarPlaneCamera {
	private edu.cmu.cs.dennisc.scenegraph.OrthographicCamera m_sgCamera = null;
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera getSGNearPlaneAndFarPlaneCamera() {
		return getSGOrthographicCamera();
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
    public edu.cmu.cs.dennisc.scenegraph.OrthographicCamera getSGOrthographicCamera() {
    	if( m_sgCamera != null ) {
    		//pass
    	} else {
    		m_sgCamera = new edu.cmu.cs.dennisc.scenegraph.OrthographicCamera();
    		m_sgCamera.farClippingPlaneDistance.setValue( 1000.0 );
    		m_sgCamera.setParent( getSGTransformable() );
    		putElement( m_sgCamera );
    	}
    	return m_sgCamera;
    }
    public edu.cmu.cs.dennisc.math.ClippedZPlane getPicturePlane() {
    	return m_sgCamera.picturePlane.getValue();
    }
    public void setPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane picturePlane ) {
    	m_sgCamera.picturePlane.setValue( picturePlane );
    }
    public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane() {
    	edu.cmu.cs.dennisc.lookingglass.LookingGlass lg = getLookingGlass();
    	assert lg != null;
    	return lg.getActualPicturePlane( m_sgCamera );
    }
}
