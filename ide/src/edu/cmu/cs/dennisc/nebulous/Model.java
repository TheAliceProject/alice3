/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

import org.lgna.story.resourceutilities.StorytellingResources;

/**
 * @author Dennis Cosgrove
 */
public class Model extends edu.cmu.cs.dennisc.scenegraph.Geometry {
	
	static {
		StorytellingResources.getInstance().loadSimsBundles();
		edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory.register( Model.class, ModelAdapter.class );
	}
	
	
    public Model() throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
        Manager.initializeIfNecessary();
    }
    
    public Model(Object o) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
        this();
        initialize(o);
    }
    
    public Model(Object o, String textureName) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
        this();
        initializeWithTexture(o, textureName);
    }
    
    public native void render();
    public native void pick();
    private native void initialize( Object o );
    private native void initializeWithTexture( Object o, String textureName );
    public native void setTexture( String textureName );
    public native void getLocalTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );
	public native void setLocalTransformationForPartNamed( org.lgna.story.resources.JointId name, double[] transformIn );
	public native void getAbsoluteTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForJoint( org.lgna.story.resources.JointId joint ) {
		double[] buffer = new double[ 12 ];
		getLocalTransformationForPartNamed( buffer, joint );
		return edu.cmu.cs.dennisc.math.AffineMatrix4x4.createFromColumnMajorArray12( buffer );
	}

	public void setLocalTransformationForJoint( org.lgna.story.resources.JointId joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTrans ) {
		setLocalTransformationForPartNamed( joint, localTrans.getAsColumnMajorArray12() );
	}
	
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformationForJoint( org.lgna.story.resources.JointId joint ) {
		double[] buffer = new double[ 12 ];
		getAbsoluteTransformationForPartNamed( buffer, joint );
		return edu.cmu.cs.dennisc.math.AffineMatrix4x4.createFromColumnMajorArray12( buffer );
	}
	
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
