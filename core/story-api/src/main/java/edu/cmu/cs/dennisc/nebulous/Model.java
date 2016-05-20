/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

import org.lgna.story.resourceutilities.StorytellingResources;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends edu.cmu.cs.dennisc.scenegraph.Geometry {

	static {
		if( SystemUtilities.getBooleanProperty( "org.alice.ide.disableDefaultNebulousLoading", false ) ) {
			//Don't load nebulous resources if the default loading is disabled
			//Disabling should only happen under controlled circumstances like running the model batch process
		} else {
			StorytellingResources.INSTANCE.loadSimsBundles();
		}
		//		Manager.setDebugDraw( true );
	}

	public Model() throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		Manager.initializeIfNecessary();
	}

	private native void render( javax.media.opengl.GL gl, float globalBrightness, boolean renderAlpha, boolean renderOpaque );

	public void synchronizedRender( javax.media.opengl.GL gl, float globalBrightness, boolean renderAlpha, boolean renderOpaque ) {
		synchronized( renderLock ) {
			try {
				this.render( gl, globalBrightness, renderAlpha, renderOpaque );
			} catch( RuntimeException re ) {
				System.err.println( this );
				throw re;
			}
		}
	}

	private native void pick();

	public void synchronizedPick() {
		synchronized( renderLock ) {
			pick();
		}
	}

	private native boolean isAlphaBlended();

	public boolean synchronizedIsAlphaBlended() {
		synchronized( renderLock ) {
			return isAlphaBlended();
		}
	}

	private native boolean hasOpaque();

	public boolean synchronizedHasOpaque() {
		synchronized( renderLock ) {
			return hasOpaque();
		}
	}

	private native void getAxisAlignedBoundingBoxForJoint( org.lgna.story.resources.JointId name, org.lgna.story.resources.JointId parent, double[] bboxData );

	private native void updateAxisAlignedBoundingBox( double[] bboxData );

	private native void getOriginalTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name, org.lgna.story.resources.JointId parent );

	private native void getLocalTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name, org.lgna.story.resources.JointId parent );

	private native void setLocalTransformationForPartNamed( org.lgna.story.resources.JointId name, org.lgna.story.resources.JointId parent, double[] transformIn );

	private native void getAbsoluteTransformationForPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );

	public void setVisual( edu.cmu.cs.dennisc.scenegraph.Visual visual ) {
		this.sgAssociatedVisual = visual;
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
			getOriginalTransformationForPartNamed( buffer, joint, joint.getParent() );
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
			getLocalTransformationForPartNamed( buffer, joint, joint.getParent() );
		} catch( RuntimeException re ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( joint );
			throw re;
		}
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 affineMatrix = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createFromColumnMajorArray12( buffer );
		return affineMatrix;
	}

	public void setLocalTransformationForJoint( org.lgna.story.resources.JointId joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTrans ) {
		synchronized( renderLock ) {
			setLocalTransformationForPartNamed( joint, joint.getParent(), localTrans.getAsColumnMajorArray12() );
		}
	}

	public boolean hasJoint( org.lgna.story.resources.JointId joint ) {
		//There's no specific "hasJoint" native call, so this uses the getLocalTransformationForPartNamed and catches the error if the joint isn't found
		//TODO: implement a simpler "hasJoint" in the native code
		double[] buffer = new double[ 12 ];
		try {
			getLocalTransformationForPartNamed( buffer, joint, joint.getParent() );
		} catch( RuntimeException re ) {
			return false;
		}
		return true;
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
		getAxisAlignedBoundingBoxForJoint( joint, joint.getParent(), bboxData );
		AxisAlignedBox bbox = new AxisAlignedBox( bboxData[ 0 ], bboxData[ 1 ], bboxData[ 2 ], bboxData[ 3 ], bboxData[ 4 ], bboxData[ 5 ] );
		bbox.scale( this.sgAssociatedVisual.scale.getValue() );
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

	protected edu.cmu.cs.dennisc.scenegraph.Composite sgParent;
	protected edu.cmu.cs.dennisc.scenegraph.Visual sgAssociatedVisual;

	protected final Object renderLock = new Object();
}
