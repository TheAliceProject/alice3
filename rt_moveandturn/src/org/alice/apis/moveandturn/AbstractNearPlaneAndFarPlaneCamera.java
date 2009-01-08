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
public abstract class AbstractNearPlaneAndFarPlaneCamera extends AbstractCamera {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > NEAR_CLIPPING_PLANE_DISTANCE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( AbstractNearPlaneAndFarPlaneCamera.class, "NearClippingPlaneDistance" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > FAR_CLIPPING_PLANE_DISTANCE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( AbstractNearPlaneAndFarPlaneCamera.class, "FarClippingPlaneDistance" );
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
    @Override
    public final edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
    	return getSGNearPlaneAndFarPlaneCamera();
    }
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
    public abstract edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera getSGNearPlaneAndFarPlaneCamera();
    
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
    public Double getNearClippingPlaneDistance() {
    	return getSGNearPlaneAndFarPlaneCamera().nearClippingPlaneDistance.getValue();
    }
    public void setNearClippingPlaneDistance( Number nearClippingPlaneDistance ) {
    	getSGNearPlaneAndFarPlaneCamera().nearClippingPlaneDistance.setValue( nearClippingPlaneDistance.doubleValue() );
    }
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
    public Double getFarClippingPlaneDistance() {
    	return getSGNearPlaneAndFarPlaneCamera().farClippingPlaneDistance.getValue();
    }
    public void setFarClippingPlaneDistance( Number nearClippingPlaneDistance ) {
    	getSGNearPlaneAndFarPlaneCamera().farClippingPlaneDistance.setValue( nearClippingPlaneDistance.doubleValue() );
    }
}
