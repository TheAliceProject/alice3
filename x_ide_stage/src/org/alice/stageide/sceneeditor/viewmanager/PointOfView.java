package org.alice.stageide.sceneeditor.viewmanager;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

public class PointOfView {

	private static final String ORIENTATION = ".ORIENTATION";
	private static final String RIGHT = ".RIGHT";
	private static final String UP = ".UP";
	private static final String BACKWARD = ".BACKWARD";
	private static final String POSITION = ".POSITION";
	private static final String X_VALUE = ".X";
	private static final String Y_VALUE = ".Y";
	private static final String Z_VALUE = ".Z";
	
	private AffineMatrix4x4 transform = new AffineMatrix4x4();
	private ReferenceFrame referenceFrame;
	
	public PointOfView()
	{
		
	}
	
	public PointOfView(AffineMatrix4x4 transform, ReferenceFrame referenceFrame)
	{
		this.transform.set(transform);
		this.referenceFrame = referenceFrame;
	}
	
	public void initFromProjectProperties(edu.cmu.cs.dennisc.alice.Project.Properties properties, String keyPrefix, String keySuffix)
	{
		double rightX = properties.getDouble( keyPrefix + ORIENTATION+RIGHT+X_VALUE + keySuffix, 1.0d );
		double rightY = properties.getDouble( keyPrefix + ORIENTATION+RIGHT+Y_VALUE + keySuffix, 0.0d );
		double rightZ = properties.getDouble( keyPrefix + ORIENTATION+RIGHT+Z_VALUE + keySuffix, 0.0d );
		Vector3 right = new Vector3(rightX, rightY, rightZ);
		
		double upX = properties.getDouble( keyPrefix + ORIENTATION+UP+X_VALUE + keySuffix, 0.0d );
		double upY = properties.getDouble( keyPrefix + ORIENTATION+UP+Y_VALUE + keySuffix, 1.0d );
		double upZ = properties.getDouble( keyPrefix + ORIENTATION+UP+Z_VALUE + keySuffix, 0.0d );
		Vector3 up = new Vector3(upX, upY, upZ);
		
		double backwardX = properties.getDouble( keyPrefix + ORIENTATION+BACKWARD+X_VALUE + keySuffix, 0.0d );
		double backwardY = properties.getDouble( keyPrefix + ORIENTATION+BACKWARD+Y_VALUE + keySuffix, 0.0d );
		double backwardZ = properties.getDouble( keyPrefix + ORIENTATION+BACKWARD+Z_VALUE + keySuffix, 1.0d );
		Vector3 backward = new Vector3(backwardX, backwardY, backwardZ);
		OrthogonalMatrix3x3 orientation = new OrthogonalMatrix3x3( right, up, backward );
		
		double positionX = properties.getDouble( keyPrefix + POSITION+X_VALUE + keySuffix, 0.0d );
		double positionY = properties.getDouble( keyPrefix + POSITION+Y_VALUE + keySuffix, 0.0d );
		double positionZ = properties.getDouble( keyPrefix + POSITION+Z_VALUE + keySuffix, 0.0d );
		Point3 position = new Point3(positionX, positionY, positionZ);
		
		this.transform = new AffineMatrix4x4(orientation, position);
	}
	
	public void writeToProjectProperties(edu.cmu.cs.dennisc.alice.Project.Properties properties, String keyPrefix, String keySuffix)
	{
		if (this.transform != null)
		{
			properties.putDouble( keyPrefix + ORIENTATION+RIGHT+X_VALUE + keySuffix, this.transform.orientation.right.x);
			properties.putDouble( keyPrefix + ORIENTATION+RIGHT+Y_VALUE + keySuffix, this.transform.orientation.right.y);
			properties.putDouble( keyPrefix + ORIENTATION+RIGHT+Z_VALUE + keySuffix, this.transform.orientation.right.z );
			
			properties.putDouble( keyPrefix + ORIENTATION+UP+X_VALUE + keySuffix, this.transform.orientation.up.x);
			properties.putDouble( keyPrefix + ORIENTATION+UP+Y_VALUE + keySuffix, this.transform.orientation.up.y);
			properties.putDouble( keyPrefix + ORIENTATION+UP+Z_VALUE + keySuffix, this.transform.orientation.up.z );
			
			properties.putDouble( keyPrefix + ORIENTATION+BACKWARD+X_VALUE + keySuffix, this.transform.orientation.backward.x);
			properties.putDouble( keyPrefix + ORIENTATION+BACKWARD+Y_VALUE + keySuffix, this.transform.orientation.backward.y);
			properties.putDouble( keyPrefix + ORIENTATION+BACKWARD+Z_VALUE + keySuffix, this.transform.orientation.backward.z );
			
			properties.putDouble( keyPrefix + POSITION+X_VALUE + keySuffix, this.transform.translation.x );
			properties.putDouble( keyPrefix + POSITION+Y_VALUE + keySuffix, this.transform.translation.y );
			properties.putDouble( keyPrefix + POSITION+Z_VALUE + keySuffix, this.transform.translation.z );
		}
	}
	
	public AffineMatrix4x4 getTransform() {
		return transform;
	}

	public void setTransform(AffineMatrix4x4 transform) {
		this.transform.set(transform);
	}

	public ReferenceFrame getReferenceFrame() {
		return referenceFrame;
	}

	public void setReferenceFrame(ReferenceFrame referenceFrame) {
		this.referenceFrame = referenceFrame;
	}
	
	
}
