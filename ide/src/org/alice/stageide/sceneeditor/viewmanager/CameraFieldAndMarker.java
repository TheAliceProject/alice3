package org.alice.stageide.sceneeditor.viewmanager;

import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.OrthographicCameraMarker;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;

public class CameraFieldAndMarker 
{
	public AbstractField field;
	public CameraMarker marker;

	public CameraFieldAndMarker( AbstractField field, CameraMarker marker)
	{
		this.field = field;
		this.marker = marker;
	}
	
	public boolean isOrthographic()
	{
		if (marker != null)
		{
			return (marker instanceof OrthographicCameraMarker);
		}
		return false;
	}
	
	public boolean isPerspective()
	{
		return !isOrthographic();
	}
	
	@Override
	public String toString() {
		if (field != null)
		{
			return field.getName();
		}
		else if (marker != null)
		{
			return marker.getName();
		}
		return "NO_NAME";
	}
}
