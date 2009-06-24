/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.sceneeditor.viewmanager;

import javax.swing.JPanel;

import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

/**
 * @author David Culyba
 */
public class SceneViewManagerPanel extends JPanel {
	
	private boolean isActive = false;
	
	private PointOfViewManager pointOfViewManager;
	private PointsOfViewPanel pointsOfViewPanel;
	
	public SceneViewManagerPanel()
	{
		super();
		this.setOpaque( false );
		this.pointOfViewManager = new PointOfViewManager();
		this.pointsOfViewPanel = new PointsOfViewPanel(this.pointOfViewManager);
		this.add( this.pointsOfViewPanel );
	}
	
	public void setProject(edu.cmu.cs.dennisc.alice.Project project)
	{
		this.pointOfViewManager.initFromProject( project );
		this.pointsOfViewPanel.setPOVManager( this.pointOfViewManager );
	}
	
	public void saveToProject()
	{
		this.pointOfViewManager.writeToProject();
	}
	
	public void setCamera(AbstractCamera camera)
	{
		this.pointOfViewManager.setCamera( camera );
	}
	
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

}
