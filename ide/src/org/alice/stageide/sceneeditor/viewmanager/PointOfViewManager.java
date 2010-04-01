/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
	package org.alice.stageide.sceneeditor.viewmanager;

import java.util.Vector;

import javax.swing.ListModel;

import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public class PointOfViewManager {

	private static final String POINT_OF_VIEW_KEY_SUFFIX = ".POINT_OF_VIEW";
	private static final String OPENING_POINT_OF_VIEW_KEY_SUFFIX = ".OPENING_POINT_OF_VIEW";
	private static final String POINT_OF_VIEW_COUNT_KEY = PointOfViewManager.class.getName() + ".POINT_OF_VIEW_COUNT";
	
	private static final String BASE_VIEW_NAME = "cameraView";
	private static final String STARTING_VIEW_NAME = "startingCameraView";
	
	private OnscreenLookingGlass onscreenLookingGlass = null;
	private AbstractCamera camera = null;
	private Transformable pointOfViewSource;
	private PointOfViewListModel pointsOfView = new PointOfViewListModel();
	private PointOfView openingPointOfView = new PointOfView(STARTING_VIEW_NAME);
	private Project project = null;
	
	private boolean hasViewName(String newName)
	{
		if (openingPointOfView.getName().equals( newName ))
		{
			return true;
		}
		for (int i=0; i<this.pointsOfView.getSize(); i++)
		{
			PointOfView pov = (PointOfView)this.pointsOfView.getElementAt( i );
			if (pov.getName().equals( newName ))
			{
				return true;
			}
		}
		return false;
	}
	
	private String makeViewName(int count)
	{
		return BASE_VIEW_NAME + Integer.toString( count );
	}
	
	protected String getNewViewName()
	{
		int viewCount = this.pointsOfView.getSize();
		String viewName = makeViewName(viewCount);
		while (hasViewName(viewName))
		{
			viewCount++;
			viewName = makeViewName(viewCount);
		}
		return viewName;
	}
	
	public boolean isValidViewName(String name)
	{
		if (hasViewName(name))
		{
			return false;
		}
		//TODO: check the name more thoroughly
		return true;
	}
	
	public void initFromProject(edu.cmu.cs.dennisc.alice.Project project)
	{
		this.project = project;
		edu.cmu.cs.dennisc.alice.Project.Properties properties = this.project.getProperties();
		this.pointsOfView.clear();
		int povCount = properties.getInteger( POINT_OF_VIEW_COUNT_KEY, 0 );
		for (int i=0; i<povCount; i++)
		{
			String prefixKey = this.getTransformable().getName() + "."+i + POINT_OF_VIEW_KEY_SUFFIX;
			//TODO: save and recover the name of the view
			String viewName = getNewViewName();
			PointOfView currentPointOfView = new PointOfView(viewName);
			currentPointOfView.initFromProjectProperties( properties, prefixKey, "" );
			this.pointsOfView.addElement( currentPointOfView );
		}
		String prefixKey = this.getTransformable().getName() + OPENING_POINT_OF_VIEW_KEY_SUFFIX;
		this.openingPointOfView.initFromProjectProperties( properties, prefixKey, "" );
		
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
			String prefixKey = this.getTransformable().getName() + OPENING_POINT_OF_VIEW_KEY_SUFFIX;
			this.openingPointOfView.writeToProjectProperties( properties, prefixKey, "" );
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
	
	public PointOfView getOpeningPointOfView()
	{
		return this.openingPointOfView;
	}
	
	public void setOpeningPointOfView( PointOfView pov )
	{
		this.openingPointOfView = pov;
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
		Composite parent = pointOfViewToGet.getParent();
		Composite root = pointOfViewToGet.getRoot();
		if (pointOfViewToGet != null)
		{
			PointOfView pov = new PointOfView(getNewViewName(), pointOfViewToGet.getAbsoluteTransformation(), root);
			return pov;
		}
		return null;
	}
	
	public void setPointOfView(PointOfView pointOfView)
	{
		Transformable pointOfViewToSet = this.getTransformable();
		if (pointOfViewToSet != null)
		{
			pointOfViewToSet.setTransformation(pointOfView.getTransform(), AsSeenBy.SCENE);
		}
	}
	
	public void setCamera(AbstractCamera camera) {
		this.camera = camera;
		if (this.camera != null)
		{
			this.pointOfViewSource = (Transformable)this.camera.getParent();
			Composite root = this.camera.getRoot();
			for (int i=0; i<this.pointsOfView.getSize(); i++)
			{
				PointOfView pov = (PointOfView)this.pointsOfView.getElementAt( i );
				pov.setParent( root );
			}
			this.openingPointOfView.setParent( root );
		}
	}

	public Transformable getTransformable()
	{
		return this.pointOfViewSource;
	}
}
