package org.alice.interact;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Orientation;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

public class QuaternionAndTranslation {

	protected UnitQuaternion quaternion;
	protected Point3 translation;
	
	public QuaternionAndTranslation()
	{
		this.quaternion = UnitQuaternion.createIdentity();
		this.translation = new Point3();
	}
	
	public QuaternionAndTranslation( UnitQuaternion quaternion, Point3 translation)
	{
		this.quaternion = new UnitQuaternion(quaternion);
		this.translation = new Point3(translation);
	}
	
	public QuaternionAndTranslation( QuaternionAndTranslation other )
	{
		this.quaternion = new UnitQuaternion(other.quaternion);
		this.translation = new Point3(other.translation);
	}
	
	public QuaternionAndTranslation(AffineMatrix4x4 matrix)
	{
		this.translation = new Point3(matrix.translation);
		this.quaternion = matrix.orientation.createUnitQuaternion();
	}
	
	public void setToInterpolation(QuaternionAndTranslation a, QuaternionAndTranslation b, double portion)
	{
		this.quaternion.setToInterpolation(a.quaternion, b.quaternion, portion);
		this.translation.setToInterpolation(a.translation, b.translation, portion);
	}
	
	public AffineMatrix4x4 getAffineMatrix()
	{
		AffineMatrix4x4 toReturn = new AffineMatrix4x4();
		toReturn.translation.set(this.translation);
		toReturn.orientation.setValue( this.quaternion );
		toReturn.orientation.normalizeColumns();
		return toReturn;
	}
	
	public UnitQuaternion getQuaternion()
	{
		return this.quaternion;
	}
	
	public Point3 getTranslation()
	{
		return this.translation;
	}
	
}
