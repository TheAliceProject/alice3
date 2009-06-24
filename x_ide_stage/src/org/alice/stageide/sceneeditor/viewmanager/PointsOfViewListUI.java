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

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author David Culyba
 */
public class PointsOfViewListUI extends JPanel implements ListDataListener{

	ListModel listModel;
	PointOfViewManager pointOfViewManager;
	
	public PointsOfViewListUI(PointOfViewManager listManager)
	{
		super();
		this.pointOfViewManager = listManager;
		this.setListManager( listManager );
		this.setLayout(new FlowLayout());
		this.setOpaque( false );
	}
	
	public void setListManager(PointOfViewManager listManager)
	{
		if (this.listModel != listManager.getPointOfViewListModel())
		{
			if (this.listModel != null)
			{
				this.listModel.removeListDataListener( this );
			}
			this.listModel = listManager.getPointOfViewListModel();
			this.listModel.addListDataListener( this );
		}
	}

	private void addPointOfView(PointOfView pov, int index)
	{
		PointOfViewControl control = new PointOfViewControl(pov, this.pointOfViewManager);
		this.add( control, index );
	}
	
	private void removePointOfView(int index)
	{
		//PointOfViewControl pov = (PointOfViewControl)this.getComponent( index );
		this.remove( index );
	}
	
	public void contentsChanged( ListDataEvent e ) {
		System.out.println("Changed: "+e);
		
	}

	public void intervalAdded( ListDataEvent e ) {
		for (int i=e.getIndex0(); i<=e.getIndex1(); i++)
		{
			PointOfView pov = (PointOfView)this.listModel.getElementAt( i );
			this.addPointOfView( pov, i );
		}
		this.revalidate();
		this.repaint();
	}

	public void intervalRemoved( ListDataEvent e ) {
		for (int i=e.getIndex0(); i<=e.getIndex1(); i++)
		{
			this.removePointOfView( e.getIndex0() ); //after each "remove" the next item to remove will be at the lower index
		}
		this.revalidate();
		this.repaint();
		
	}
	
}
