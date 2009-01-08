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
import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public class ProjectionCamera extends AbstractCamera {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< edu.cmu.cs.dennisc.math.Matrix4x4 > PROJECTION_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< edu.cmu.cs.dennisc.math.Matrix4x4 >( ProjectionCamera.class, "Projection" );
	private edu.cmu.cs.dennisc.scenegraph.ProjectionCamera m_sgCamera = null;
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		return getSGProjectionCamera();
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
    public edu.cmu.cs.dennisc.scenegraph.ProjectionCamera getSGProjectionCamera() {
    	if( m_sgCamera != null ) {
    		//pass
    	} else {
    		m_sgCamera = new edu.cmu.cs.dennisc.scenegraph.ProjectionCamera();
    		m_sgCamera.setParent( getSGTransformable() );
    		putElement( m_sgCamera );
    	}
    	return m_sgCamera;
    }
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
    public edu.cmu.cs.dennisc.math.Matrix4x4 getProjection() {
    	return m_sgCamera.projection.getValue();
    }
    public void setProjection( edu.cmu.cs.dennisc.math.Matrix4x4 projection ) {
    	m_sgCamera.projection.setValue( projection );
    }
}
