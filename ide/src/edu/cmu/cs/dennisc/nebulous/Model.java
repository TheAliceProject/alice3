/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

import org.lgna.story.resourceutilities.StorytellingResources;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends edu.cmu.cs.dennisc.scenegraph.Geometry {

	static {
		StorytellingResources.getInstance().loadSimsBundles();
	}

	protected edu.cmu.cs.dennisc.scenegraph.Composite sgParent;
	protected edu.cmu.cs.dennisc.scenegraph.Visual associatedVisual;

	public Model() throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		Manager.initializeIfNecessary();
	}

	public native void render( javax.media.opengl.GL gl, float globalBrightness );

	public native void pick();

	//    public native boolean isAlphaBlended();
	private native void getAxisAlignedBoundingBoxForJoint( org.lgna.story.resources.JointId name, double[] bboxData );

	private native void updateAxisAlignedBoundingBox( double[] bboxData );

	public native void getOriginalTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );

	public native void getLocalTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );

	public native void setLocalTransformationForPartNamed( org.lgna.story.resources.JointId name, double[] transformIn );

	public native void getAbsoluteTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );

	public void setVisual( edu.cmu.cs.dennisc.scenegraph.Visual visual ) {
		this.associatedVisual = visual;
	}

	public void setSGParent( edu.cmu.cs.dennisc.scenegraph.Composite parent ) {
		this.sgParent = parent;
	}

	public edu.cmu.cs.dennisc.scenegraph.Composite getSGParent() {
		return this.sgParent;
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalTransformationForJoint( org.lgna.story.resources.JointId joint ) {
		double[] buffer = new double[ 12 ];
		try {
			getOriginalTransformationForPartNamed( buffer, joint );
		} catch( RuntimeException re ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( joint );
			throw re;
		}
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 affineMatrix = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createFromColumnMajorArray12( buffer );
		return affineMatrix;
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForJoint( org.lgna.story.resources.JointId joint ) {
		double[] buffer = new double[ 12 ];
		try {
			getLocalTransformationForPartNamed( buffer, joint );
		} catch( RuntimeException re ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( joint );
			throw re;
		}
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 affineMatrix = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createFromColumnMajorArray12( buffer );
		return affineMatrix;
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

	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedBoundingBoxForJoint( org.lgna.story.resources.JointId joint ) {
		double[] bboxData = new double[ 6 ];
		getAxisAlignedBoundingBoxForJoint( joint, bboxData );
		AxisAlignedBox bbox = new AxisAlignedBox( bboxData[ 0 ], bboxData[ 1 ], bboxData[ 2 ], bboxData[ 3 ], bboxData[ 4 ], bboxData[ 5 ] );
		bbox.scale( this.associatedVisual.scale.getValue() );
		return bbox;
	}

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		//the bounding boxes come in the form (double[6])
		double[] bboxData = new double[ 6 ];
		updateAxisAlignedBoundingBox( bboxData );
		boundingBox.setMinimum( bboxData[ 0 ], bboxData[ 1 ], bboxData[ 2 ] );
		boundingBox.setMaximum( bboxData[ 3 ], bboxData[ 4 ], bboxData[ 5 ] );
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
