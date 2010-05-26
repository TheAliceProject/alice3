package org.alice.interact;

import edu.cmu.cs.dennisc.math.AngleInDegrees;

import edu.cmu.cs.dennisc.math.Angle;

public class SnapState 
{
	public static Integer[] ANGLE_SNAP_OPTIONS = {new Integer(15), new Integer(30), new Integer(45), new Integer(60), new Integer(90), new Integer(120), new Integer(180)};
	
	
	
	private boolean isSnapEnabled = false;
	private boolean isSnapToGroundEnabled = true;
	private boolean isSnapToGridEnabled = true;
	private boolean isRotationSnapEnabled = true;
	private double gridSpacing = .5d;
	private boolean showSnapGrid = true;
	private Angle rotationSnapAngle = new AngleInDegrees(45); //In degrees
	
	
	public SnapState()
	{
		
	}
	
	public static Integer getAngleOptionForAngle(int angle)
	{
		for (Integer angleOption : ANGLE_SNAP_OPTIONS)
		{
			if (angleOption.equals(angle))
			{
				return angleOption;
			}
		}
		return null;
	}
	
	public void setShouldSnapToGroundEnabled(boolean shouldSnapToGround)
	{
		this.isSnapToGroundEnabled = shouldSnapToGround;
	}
	
	public boolean shouldSnapToGround()
	{
		return this.isSnapEnabled && this.isSnapToGroundEnabled;
	}
	
	public boolean isSnapToGroundEnabled()
	{
		return this.isSnapToGroundEnabled;
	}
	
	public void setShouldSnapToGridEnabled(boolean shouldSnapToGround)
	{
		this.isSnapToGridEnabled = shouldSnapToGround;
	}
	
	public boolean shouldSnapToGrid()
	{
		return this.isSnapEnabled && this.isSnapToGridEnabled;
	}
	
	public boolean isSnapToGridEnabled()
	{
		return this.isSnapToGridEnabled;
	}
	
	public void setGridSpacing(double gridSpacing)
	{
		this.gridSpacing = gridSpacing;
	}
	
	public double getGridSpacing()
	{
		return this.gridSpacing;
	}
	
	public void setSnapEnabled(boolean snapEnabled)
	{
		this.isSnapEnabled = snapEnabled;
	}
	
	public boolean isSnapEnabled()
	{
		return this.isSnapEnabled;
	}
	
	public void setRotationSnapEnabled(boolean rotationSnapEnabled)
	{
		this.isRotationSnapEnabled = rotationSnapEnabled;
	}
	
	public boolean shouldSnapToRotation()
	{
		return this.isSnapEnabled && this.isRotationSnapEnabled;
	}
	
	public boolean isRotationSnapEnabled()
	{
		return this.isRotationSnapEnabled;
	}
	
	public void setRotationSnapAngleInDegrees(double degrees)
	{
		this.rotationSnapAngle.setAsDegrees(degrees);
	}
	
	public void setRotationSnapAngle(Angle snapAngle)
	{
		this.rotationSnapAngle = new AngleInDegrees(snapAngle);
	}
	
	public Angle getRotationSnapAngle()
	{
		return this.rotationSnapAngle;
	}
	
	public boolean shouldShowSnapGrid()
	{
		return this.isSnapEnabled && this.showSnapGrid;
	}
	
	public boolean isShowSnapGridEnabled()
	{
		return this.showSnapGrid;
	}
	
	public void setShowSnapGrid( boolean showSnapGrid )
	{
		this.showSnapGrid = showSnapGrid;
	}
	
}
