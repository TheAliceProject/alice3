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

import java.awt.BorderLayout;
//import java.awt.Font;
//import java.util.HashMap;
//import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.alice.interact.AbstractDragAdapter;

/**
 * @author David Culyba
 */
public class ManipulationHandleControlPanel extends JPanel {
	private ManipulationHandleSelectionOperation handleSelectionOperation;
	private AbstractDragAdapter dragAdapter;
	
	public ManipulationHandleControlPanel()
	{
		super();
		this.dragAdapter = null;
		this.handleSelectionOperation = new ManipulationHandleSelectionOperation(dragAdapter);
		this.setOpaque( false );
		this.setLayout( new BorderLayout() );
		javax.swing.JLabel title = edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabelWithScaledFont( "Handle Style", 1.5f, edu.cmu.cs.dennisc.zoot.font.ZTextWeight.BOLD);
		this.add( title, BorderLayout.NORTH );
		this.add(edu.cmu.cs.dennisc.zoot.ZManager.createRadioButtons( this.handleSelectionOperation ), BorderLayout.CENTER);
		this.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
	}
	
	public void setDragAdapter(AbstractDragAdapter dragAdapter)
	{
		if (this.dragAdapter != null)
		{
			this.dragAdapter.getOnscreenLookingGlass().getAWTComponent().removeKeyListener( edu.cmu.cs.dennisc.zoot.ZManager.createKeyListener( this.handleSelectionOperation ) );
		}
		this.dragAdapter = dragAdapter;
		this.handleSelectionOperation.setDragAdapter( this.dragAdapter );
		if (this.dragAdapter != null)
		{
			this.dragAdapter.getOnscreenLookingGlass().getAWTComponent().addKeyListener( edu.cmu.cs.dennisc.zoot.ZManager.createKeyListener( this.handleSelectionOperation ) );
		}
	}
}
