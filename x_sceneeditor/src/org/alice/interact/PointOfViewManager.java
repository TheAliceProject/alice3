package org.alice.interact;

import java.util.Vector;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class PointOfViewManager {

	private OnscreenLookingGlass onscreenLookingGlass = null;
	private AbstractCamera camera = null;
	private Vector<PointOfView> pointsOfView = new Vector<PointOfView>();
	
	public OnscreenLookingGlass getOnscreenLookingGlass() {
		return onscreenLookingGlass;
	}

	public void setOnscreenLookingGlass(OnscreenLookingGlass onscreenLookingGlass) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}

	public PointOfView capturePointOfView()
	{
		Transformable pointOfViewToGet = this.getTransformable();
		if (pointOfViewToGet != null)
		{
			PointOfView pov = new PointOfView(pointOfViewToGet.getAbsoluteTransformation(), AsSeenBy.SCENE);
			this.pointsOfView.add(pov);
			return pov;
		}
		return null;
	}
	
	public void setPointOfView(PointOfView pointOfView)
	{
		Transformable pointOfViewToSet = this.getTransformable();
		if (pointOfViewToSet != null)
		{
			pointOfViewToSet.setTransformation(pointOfView.getTransform(), pointOfView.getReferenceFrame());
		}
	}
	
	public void setCamera(AbstractCamera camera) {
		this.camera = camera;
	}

	public Transformable getTransformable()
	{
		if (this.onscreenLookingGlass != null)
		{
			return (Transformable)this.onscreenLookingGlass.getCameraAt(0).getParent();
		}
		else if (this.camera != null)
		{
			return (Transformable)this.camera.getParent();
		}
		return null;
	}
}
