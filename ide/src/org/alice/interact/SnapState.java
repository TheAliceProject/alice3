package org.alice.interact;

public class SnapState 
{
	private boolean isSnapEnabled = true;
	private boolean isSnapToGroundEnabled = true;
	private boolean isSnapToGridEnabled = true;
	private double gridSpacing = .5d;
	
	
	public SnapState()
	{
		
	}
	
	public void setShouldSnapToGroundEnabled(boolean shouldSnapToGround)
	{
		this.isSnapToGroundEnabled = shouldSnapToGround;
	}
	
	public boolean shouldSnapToGround()
	{
		return this.isSnapEnabled && this.isSnapToGroundEnabled;
	}
	
	public void setShouldSnapToGridEnabled(boolean shouldSnapToGround)
	{
		this.isSnapToGridEnabled = shouldSnapToGround;
	}
	
	public boolean shouldSnapToGrid()
	{
		return this.isSnapEnabled && this.isSnapToGridEnabled;
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
	
}
