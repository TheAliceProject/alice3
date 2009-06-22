	package org.alice.stageide.sceneeditor.viewmanager;

import java.util.Vector;

import javax.swing.ListModel;

import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public class PointOfViewManager {

	private static final String POINT_OF_VIEW_KEY_SUFFIX = ".POINT_OF_VIEW";
	private static final String POINT_OF_VIEW_COUNT_KEY = PointOfViewManager.class.getName() + ".POINT_OF_VIEW_COUNT";
	
	private OnscreenLookingGlass onscreenLookingGlass = null;
	private AbstractCamera camera = null;
	private Transformable pointOfViewSource;
	private PointOfViewListModel pointsOfView = new PointOfViewListModel();
	private Project project = null;
	
	public void initFromProject(edu.cmu.cs.dennisc.alice.Project project)
	{
		this.project = project;
		edu.cmu.cs.dennisc.alice.Project.Properties properties = this.project.getProperties();
		this.pointsOfView.clear();
		int povCount = properties.getInteger( POINT_OF_VIEW_COUNT_KEY, 0 );
		for (int i=0; i<povCount; i++)
		{
			String prefixKey = this.getTransformable().getName() + "."+i + POINT_OF_VIEW_KEY_SUFFIX;
			PointOfView currentPointOfView = new PointOfView();
			currentPointOfView.initFromProjectProperties( properties, prefixKey, "" );
			currentPointOfView.setReferenceFrame( AsSeenBy.SCENE );
			this.pointsOfView.addElement( currentPointOfView );
		}
		
	}
	
	public void writeToProject()
	{
		this.writeToProject( null );
	}
	
	public void writeToProject(edu.cmu.cs.dennisc.alice.Project toWriteTo)
	{
		if (toWriteTo == null)
		{
			toWriteTo = this.project;
		}
		if (toWriteTo != null)
		{
			edu.cmu.cs.dennisc.alice.Project.Properties properties = toWriteTo.getProperties();
			properties.putInteger( POINT_OF_VIEW_COUNT_KEY, this.pointsOfView.getSize() );
			for (int i=0; i<this.pointsOfView.getSize(); i++)
			{
				PointOfView pov = (PointOfView)this.pointsOfView.getElementAt( i );
				String prefixKey = this.getTransformable().getName() + "."+i + POINT_OF_VIEW_KEY_SUFFIX;
				pov.writeToProjectProperties( properties, prefixKey, "" );
			}
		}
	}
	
	public ListModel getPointOfViewListModel()
	{
		return this.pointsOfView;
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
		return this.pointsOfView.getSize();
	}
	
	public PointOfView getPointOfViewAt(int index)
	{
		return (PointOfView)this.pointsOfView.getElementAt( index );
	}
	
	public void removePointOfView(PointOfView pov)
	{
		this.pointsOfView.removeElement( pov );
	}
	
	public PointOfView capturePointOfView()
	{
		
		PointOfView pov = this.getCurrentPointOfView();
		if (pov != null)
		{
			this.pointsOfView.addElement(pov);
			return pov;
		}
		return null;
	}
	
	public void addTransformationListener(AbsoluteTransformationListener listener)
	{
		Transformable pointOfViewToListenTo = this.getTransformable();
		if (pointOfViewToListenTo != null)
		{
			pointOfViewToListenTo.addAbsoluteTransformationListener( listener );
		}
	}
	
	public void removeTransformationListener(AbsoluteTransformationListener listener)
	{
		Transformable pointOfViewToListenTo = this.getTransformable();
		if (pointOfViewToListenTo != null)
		{
			pointOfViewToListenTo.removeAbsoluteTransformationListener( listener );
		}
	}	
	
	public PointOfView getCurrentPointOfView()
	{
		Transformable pointOfViewToGet = this.getTransformable();
		if (pointOfViewToGet != null)
		{
			PointOfView pov = new PointOfView(pointOfViewToGet.getAbsoluteTransformation(), AsSeenBy.SCENE);
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
