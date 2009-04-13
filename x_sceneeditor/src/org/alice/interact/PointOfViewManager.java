	package org.alice.interact;

import java.util.Vector;

import edu.cmu.cs.dennisc.alice.ide.editors.scene.AbstractSceneEditor;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class PointOfViewManager {

	private static final String POINT_OF_VIEW_KEY_SUFFIX = ".POINT_OF_VIEW";
	private static final String POINT_OF_VIEW_COUNT_KEY = PointOfViewManager.class.getName() + ".POINT_OF_VIEW_COUNT";
	
	private OnscreenLookingGlass onscreenLookingGlass = null;
	private AbstractCamera camera = null;
	private Transformable pointOfViewSource;
	private Vector<PointOfView> pointsOfView = new Vector<PointOfView>();
	
	public void initFromProject(edu.cmu.cs.dennisc.alice.Project project)
	{
		edu.cmu.cs.dennisc.alice.Project.Properties properties = project.getProperties();
		this.pointsOfView.clear();
		int povCount = properties.getInteger( POINT_OF_VIEW_COUNT_KEY, 0 );
		for (int i=0; i<povCount; i++)
		{
			String prefixKey = this.getTransformable().getName() + "."+i + POINT_OF_VIEW_KEY_SUFFIX;
			PointOfView currentPointOfView = new PointOfView();
			currentPointOfView.initFromProjectProperties( properties, prefixKey, "" );
			currentPointOfView.setReferenceFrame( AsSeenBy.SCENE );
			this.pointsOfView.add( currentPointOfView );
		}
		
	}
	
	public void writeToProject(edu.cmu.cs.dennisc.alice.Project project)
	{
		edu.cmu.cs.dennisc.alice.Project.Properties properties = project.getProperties();
		properties.putInteger( POINT_OF_VIEW_COUNT_KEY, this.pointsOfView.size() );
		for (int i=0; i<this.pointsOfView.size(); i++)
		{
			PointOfView pov = this.pointsOfView.get( i );
			String prefixKey = this.getTransformable().getName() + "."+i + POINT_OF_VIEW_KEY_SUFFIX;
			pov.writeToProjectProperties( properties, prefixKey, "" );
		}
	}
		
	public OnscreenLookingGlass getOnscreenLookingGlass() {
		return onscreenLookingGlass;
	}

	public void setOnscreenLookingGlass(OnscreenLookingGlass onscreenLookingGlass) {
		this.onscreenLookingGlass = onscreenLookingGlass;
		if (this.onscreenLookingGlass != null)
		{
			this.setCamera( this.onscreenLookingGlass.getCameraAt(0) );
		}
	}

	public int getPointOfViewCount()
	{
		return this.pointsOfView.size();
	}
	
	public PointOfView getPointOfViewAt(int index)
	{
		return this.pointsOfView.get( index );
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
		if (this.camera != null)
		{
			this.pointOfViewSource = (Transformable)this.camera.getParent();
		}
	}

	public Transformable getTransformable()
	{
		return this.pointOfViewSource;
	}
}
