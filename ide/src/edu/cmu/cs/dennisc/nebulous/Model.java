/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends edu.cmu.cs.dennisc.scenegraph.Geometry {
	
    public Model() throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
        Manager.initializeIfNecessary();
    }
    
    public native void render();
    public native void pick();
	
	@Override
	public void transform( edu.cmu.cs.dennisc.math.AbstractMatrix4x4 trans ) {
		throw new RuntimeException( "todo" );
	}
	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		boundingBox.setNaN();
	}
	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		boundingSphere.setNaN();
	}
	@Override
	protected void updatePlane( edu.cmu.cs.dennisc.math.Vector3 forward, edu.cmu.cs.dennisc.math.Vector3 upGuide, edu.cmu.cs.dennisc.math.Point3 translation ) {
		throw new RuntimeException( "todo" );
	}
}
